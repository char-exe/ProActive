package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import sample.DatabaseHandler;
import sample.NutritionItem;
import sample.User;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

/**
 * A class to control the summary viewing page of the ProActive App. Handles the adding of data to the graphs and the
 * formatting of x axes from number to date format.
 *
 * @author Samuel Scarfe 100048633
 * @author Charlie Jones 100234961
 *
 * @version 1.0 - (SS) Simple graphing with a loop for simple dummy data and axis formatting.
 * @version 1.1 - (CJ) Added weight chart (still with dummy data) & reformatted fxml.
 * @version 1.2 - (SS) Added DateConverter as a nested class. Implemented functionality for pulling real data
 *                     from database.
 */
public class SummaryController implements Initializable
{
    /**
     * A chart for viewing caloric intake over 7 days.
     */
    @FXML
    private LineChart<Number, Number> intakeChart;
    /**
     * A chart for viewing caloric burn over 7 days.
     */
    @FXML
    private LineChart<Number, Number> burnChart;
    /**
     * A chart for view time spent exercising over 7 days.
     */
    @FXML
    private LineChart<Number, Number> spentChart;
    /**
     * A chart for showing weight gain/loss over the last 7 days
     */
    @FXML
    private LineChart<Number, Number> weightChart;


    @FXML private LineChart<Number, Number> nutritionChart;
    @FXML private LineChart<Number, Number> mineralsChart;
    @FXML private LineChart<Number, Number> vitaminsChart;

    /**
     * The x axis for intakeChart.
     */
    @FXML
    private NumberAxis intakeDateAxis;
    /**
     * The x axis for burnChart.
     */
    @FXML
    private NumberAxis burnDateAxis;
    /**
     * The x axis for spentChart.
     */
    @FXML
    private NumberAxis spentDateAxis;
    /**
     * The x axis for weightChart
     */
    @FXML
    private NumberAxis weightDateAxis;
    /**
     * The Welcome message for summary page
     */

    @FXML private NumberAxis nutritionDateAxis;
    @FXML private NumberAxis mineralsDateAxis;
    @FXML private NumberAxis vitaminsDateAxis;

    @FXML
    private Label welcomeBackLabel;

    @FXML private VBox activitySummarySection;
    @FXML private VBox nutritionSummarySection;

    @FXML private Button weightChartNextWeekButton;
    @FXML private Button intakeChartNextWeekButton;
    @FXML private Button burnChartNextWeekButton;
    @FXML private Button spentChartNextWeekButton;
    @FXML private Button nutritionChartNextWeekButton;
    @FXML private Button mineralsChartNextWeekButton;
    @FXML private Button vitaminsChartNextWeekButton;

    private final ToggleGroup summaryGroup = new ToggleGroup();
    @FXML private ToggleButton nutritionSummary;
    @FXML private ToggleButton activitySummary;

    private User user;
    private DatabaseHandler dh;

    private LocalDate weightChartDate = LocalDate.now();
    private LocalDate intakeChartDate = LocalDate.now();
    private LocalDate burnChartDate = LocalDate.now();
    private LocalDate spentChartDate = LocalDate.now();
    private LocalDate nutritionChartDate = LocalDate.now();
    private LocalDate mineralsChartDate = LocalDate.now();
    private LocalDate vitaminsChartDate = LocalDate.now();

    private final Tooltip nextWeekTooltip = new Tooltip("You cannot summarise the future!");

    /**
     * Initializes the graphs with formatted axes and dummy data.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     * @see javafx.fxml.Initializable
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        dh = DatabaseHandler.getInstance();
        nextWeekTooltip.setShowDelay(javafx.util.Duration.millis(0));

        summaryGroup.getToggles().addAll(activitySummary, nutritionSummary);
        summaryGroup.selectToggle(activitySummary);

        summaryGroup.selectedToggleProperty().addListener(e -> {

            if(summaryGroup.getSelectedToggle() == activitySummary){

                nutritionSummarySection.setManaged(false);
                nutritionSummarySection.setVisible(false);

                activitySummarySection.setManaged(true);
                activitySummarySection.setVisible(true);

            } else {

                activitySummarySection.setManaged(false);
                activitySummarySection.setVisible(false);

                nutritionSummarySection.setManaged(true);
                nutritionSummarySection.setVisible(true);

            }

        });

        weightChartNextWeekButton.setTooltip(nextWeekTooltip);
        spentChartNextWeekButton.setTooltip(nextWeekTooltip);
        intakeChartNextWeekButton.setTooltip(nextWeekTooltip);
        burnChartNextWeekButton.setTooltip(nextWeekTooltip);

        weightChart.setAnimated(false);
        spentChart.setAnimated(false);
        intakeChart.setAnimated(false);
        burnChart.setAnimated(false);

        nutritionChart.setAnimated(false);
        mineralsChart.setAnimated(false);
        vitaminsChart.setAnimated(false);

    }

    public void initData(User user) {
        this.user = user;

        //Set Welcome message to Users first name
        welcomeBackLabel.setText("Welcome Back " + user.getFirstname() + "!");
    }

    public void initChartData(LocalDate latest) {

        DateConverter dc = new DateConverter(latest);

        //format each axis from showing numeric tick marks to showing the past 7 days
        formatAxis(intakeDateAxis, latest);
        formatAxis(burnDateAxis, latest);
        formatAxis(spentDateAxis, latest);
        formatAxis(weightDateAxis, latest);
        formatAxis(nutritionDateAxis, latest);
        formatAxis(mineralsDateAxis, latest);
        formatAxis(vitaminsDateAxis, latest);

        // Array list to contain all nutrient chart series
        ArrayList<XYChart.Series<Number, Number>> nutrientSeries = new ArrayList<>();
        ArrayList<XYChart.Series<Number, Number>> mineralsSeries = new ArrayList<>();
        ArrayList<XYChart.Series<Number, Number>> vitaminsSeries = new ArrayList<>();

        // Hashmap to contain all macros summed for each day
        HashMap<String, HashMap<String, Double>> macros = new HashMap<>();
        HashMap<String, HashMap<String, Double>> minerals = new HashMap<>();
        HashMap<String, HashMap<String, Double>> vitamins = new HashMap<>();

        HashMap<String, ArrayList<NutritionItem>> nutritionData = dh.getNutrientEntries(user.getUsername(), latest);

        // For each date in nutrition data keyset (collection of NutritionItems)
        for(String date : nutritionData.keySet()){

            // Hashmap to contain all macros summed for each day
            HashMap<String, Double> allItemMacrosSummed = new HashMap<>();
            HashMap<String, Double> allItemMineralsSummed = new HashMap<>();
            HashMap<String, Double> allItemVitaminsSummed = new HashMap<>();

            // For each NutritionItem in NutritionItem collection for current day
            for(NutritionItem item : nutritionData.get(date)){

                // Get macro nutrients for each NutritionItem
                HashMap<String, Double> itemMacros = item.getMacroNutrients();
                HashMap<String, Double> itemMinerals = item.getMinerals();
                HashMap<String, Double> itemVitamins = item.getVitamins();

                // For each macro nutritent
                for(String macroName : itemMacros.keySet()){

                    // If macro nutrient already exists, add value to entry
                    if(allItemMacrosSummed.containsKey(macroName)){

                        allItemMacrosSummed.put(
                                macroName, allItemMacrosSummed.get(macroName) + itemMacros.get(macroName)
                        );

                    // Else, add entry for new macro nutrient
                    } else {

                        allItemMacrosSummed.put(macroName, itemMacros.get(macroName));

                    }

                }

                for(String mineralName : itemMinerals.keySet()){

                    // If macro nutrient already exists, add value to entry
                    if(allItemMineralsSummed.containsKey(mineralName)){

                        allItemMineralsSummed.put(
                                mineralName, allItemMineralsSummed.get(mineralName) + itemMinerals.get(mineralName)
                        );

                        // Else, add entry for new macro nutrient
                    } else {

                        allItemMineralsSummed.put(mineralName, itemMinerals.get(mineralName));

                    }

                }

                for(String vitaminName : itemVitamins.keySet()){

                    // If macro nutrient already exists, add value to entry
                    if(allItemVitaminsSummed.containsKey(vitaminName)){

                        allItemVitaminsSummed.put(
                                vitaminName, allItemVitaminsSummed.get(vitaminName) + itemVitamins.get(vitaminName)
                        );

                        // Else, add entry for new macro nutrient
                    } else {

                        allItemVitaminsSummed.put(vitaminName, itemVitamins.get(vitaminName));

                    }

                }

            }

            macros.put(date, allItemMacrosSummed);
            minerals.put(date, allItemMineralsSummed);
            vitamins.put(date, allItemVitaminsSummed);

        }

        // For each date in macros HashMap
        setNutrientGraph(dc, nutrientSeries, macros);

        setNutrientGraph(dc, mineralsSeries, minerals);

        setNutrientGraph(dc, vitaminsSeries, vitamins);

        // For each nutrient series, add series to chart
        for(XYChart.Series<Number, Number> series : nutrientSeries) {

            nutritionChart.getData().add(series);

            // Setting tooltip
            for (XYChart.Data<Number, Number> d : series.getData()) {

                String value = series.getName() + ": " + d.getYValue();

                if (value.endsWith(".0"))
                    value = value.substring(0, value.length() - 2);

                Tooltip t = new Tooltip(value);
                t.setShowDelay(javafx.util.Duration.millis(0));
                Tooltip.install(d.getNode(), t);

            }

        }

        for(XYChart.Series<Number, Number> series : mineralsSeries){

            mineralsChart.getData().add(series);

            // Setting tooltip
            for (XYChart.Data<Number, Number> d : series.getData()) {

                String value = series.getName() + ": " + d.getYValue().toString();

                if(value.endsWith(".0"))
                    value = value.substring(0, value.length() - 2);

                Tooltip t = new Tooltip(value);
                t.setShowDelay(javafx.util.Duration.millis(0));
                Tooltip.install(d.getNode(), t);

            }

        }

        for(XYChart.Series<Number, Number> series : vitaminsSeries){

            vitaminsChart.getData().add(series);

            // Setting tooltip
            for (XYChart.Data<Number, Number> d : series.getData()) {

                String value = series.getName() + ": " + d.getYValue().toString();

                if(value.endsWith(".0"))
                    value = value.substring(0, value.length() - 2);

                Tooltip t = new Tooltip(value);
                t.setShowDelay(javafx.util.Duration.millis(0));
                Tooltip.install(d.getNode(), t);

            }

        }

        //Create a data series for each graph and set names
        XYChart.Series<Number, Number> intakeSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> burnSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> spentSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> weightSeries = new XYChart.Series<>();

        HashMap<String, Double> intakeData = dh.getIntakeEntries(user.getUsername(), latest);
        HashMap<String, Integer> spentData = dh.getSpentEntries(user.getUsername(), latest);
        HashMap<String, Float> burnedData = dh.getBurnedEntries(user.getUsername(), latest);
        HashMap<String, Integer> weightData = dh.getWeightEntries(user.getUsername(), latest);


        //Add data to each series.

        for (String key : intakeData.keySet())
        {
            intakeSeries.getData().add(new XYChart.Data<>(dc.fromString(key), intakeData.get(key)));
        }

        for (String key : spentData.keySet())
        {
            spentSeries.getData().add(new XYChart.Data<>(dc.fromString(key), spentData.get(key)));
        }

        for (String key : burnedData.keySet())
        {
            burnSeries.getData().add(new XYChart.Data<>(dc.fromString(key), burnedData.get(key)));
        }

        for (String key : weightData.keySet())
        {
            weightSeries.getData().add(new XYChart.Data<>(dc.fromString(key), weightData.get(key)));
        }

        //Add series to graphs.
        intakeChart.getData().add(intakeSeries);
        burnChart.getData().add(burnSeries);
        spentChart.getData().add(spentSeries);
        weightChart.getData().add(weightSeries);

        for (XYChart.Data<Number, Number> d : intakeSeries.getData()) {

            String value = d.getYValue().toString();

            if(value.endsWith(".0"))
                value = value.substring(0, value.length() - 2);


            Tooltip t = new Tooltip(value);
            t.setShowDelay(javafx.util.Duration.millis(0));
            Tooltip.install(d.getNode(), t);

        }

        for (XYChart.Data<Number, Number> d : burnSeries.getData()) {

            String value = d.getYValue().toString();

            if(value.endsWith(".0"))
                value = value.substring(0, value.length() - 2);


            Tooltip t = new Tooltip(value);
            t.setShowDelay(javafx.util.Duration.millis(0));
            Tooltip.install(d.getNode(), t);

        }

        for (XYChart.Data<Number, Number> d : spentSeries.getData()) {

            String value = d.getYValue().toString();

            if(value.endsWith(".0"))
                value = value.substring(0, value.length() - 2);


            Tooltip t = new Tooltip(value);
            t.setShowDelay(javafx.util.Duration.millis(0));
            Tooltip.install(d.getNode(), t);

        }

        for (XYChart.Data<Number, Number> d : weightSeries.getData()) {

            String value = d.getYValue().toString();

            if(value.endsWith(".0"))
                value = value.substring(0, value.length() - 2);


            Tooltip t = new Tooltip(value);
            t.setShowDelay(javafx.util.Duration.millis(0));
            Tooltip.install(d.getNode(), t);

        }

        nutritionSummarySection.setVisible(false);
        nutritionSummarySection.setManaged(false);

    }

    private void setNutrientGraph(DateConverter dc, ArrayList<XYChart.Series<Number, Number>> nutrientSeries, HashMap<String, HashMap<String, Double>> macros) {
        for(String date : macros.keySet()){

            // For each macro nutrient in date
            for(String macroName : macros.get(date).keySet()){

                // Indicator to whether series list already contains a series for specific macro nutrient
                boolean containsSeries = false;

                // For each series name in nutrition collection
                for(XYChart.Series<Number, Number> series : nutrientSeries) {

                    // If series name is the same as macro name
                    if (series.getName().equals(macroName)) {

                        containsSeries = true;

                        series.getData().add(new XYChart.Data<>(dc.fromString(date), macros.get(date).get(macroName)));

                        break;

                    }

                }

                if(!containsSeries){

                    XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();

                    newSeries.getData().add(new XYChart.Data<>(dc.fromString(date), macros.get(date).get(macroName)));

                    newSeries.setName(macroName);

                    nutrientSeries.add(newSeries);

                }

            }

        }
    }

    public void setWeightChartData(){

        weightChart.getData().clear();

        //format each axis from showing numeric tick marks to showing the past 7 days
        formatAxis(weightDateAxis, weightChartDate);

        //Create a data series for each graph and set names
        XYChart.Series<Number, Number> weightSeries = new XYChart.Series<>();

        HashMap<String, Integer> weightData = dh.getWeightEntries(user.getUsername(), weightChartDate);


        //Add data to each series.
        DateConverter dc = new DateConverter(weightChartDate);

        for (String key : weightData.keySet())
        {
            weightSeries.getData().add(new XYChart.Data<>(dc.fromString(key), weightData.get(key)));
        }

        //Add series to graphs.
        weightChart.getData().add(weightSeries);

        for (XYChart.Data<Number, Number> d : weightSeries.getData()) {

            String value = d.getYValue().toString();

            if(value.endsWith(".0"))
                value = value.substring(0, value.length() - 2);


            Tooltip t = new Tooltip(value);
            t.setShowDelay(javafx.util.Duration.millis(0));
            Tooltip.install(d.getNode(), t);

        }

    }

    public void setIntakeChartData(){

        intakeChart.getData().clear();

        //format each axis from showing numeric tick marks to showing the past 7 days
        formatAxis(intakeDateAxis, intakeChartDate);

        //Create a data series for each graph and set names
        XYChart.Series<Number, Number> intakeSeries = new XYChart.Series<>();

        HashMap<String, Double> intakeData = dh.getIntakeEntries(user.getUsername(), intakeChartDate);


        //Add data to each series.
        DateConverter dc = new DateConverter(intakeChartDate);

        for (String key : intakeData.keySet())
        {
            intakeSeries.getData().add(new XYChart.Data<>(dc.fromString(key), intakeData.get(key)));
        }

        //Add series to graphs.
        intakeChart.getData().add(intakeSeries);

        for (XYChart.Data<Number, Number> d : intakeSeries.getData()) {

            String value = d.getYValue().toString();

            if(value.endsWith(".0"))
                value = value.substring(0, value.length() - 2);


            Tooltip t = new Tooltip(value);
            t.setShowDelay(javafx.util.Duration.millis(0));
            Tooltip.install(d.getNode(), t);

        }

    }

    public void setBurnChartData(){

        burnChart.getData().clear();

        //format each axis from showing numeric tick marks to showing the past 7 days
        formatAxis(burnDateAxis, burnChartDate);

        //Create a data series for each graph and set names
        XYChart.Series<Number, Number> burnSeries = new XYChart.Series<>();

        HashMap<String, Float> burnData = dh.getBurnedEntries(user.getUsername(), burnChartDate);


        //Add data to each series.
        DateConverter dc = new DateConverter(burnChartDate);

        for (String key : burnData.keySet())
        {
            burnSeries.getData().add(new XYChart.Data<>(dc.fromString(key), burnData.get(key)));
        }

        //Add series to graphs.
        burnChart.getData().add(burnSeries);

        for (XYChart.Data<Number, Number> d : burnSeries.getData()) {

            String value = d.getYValue().toString();

            if(value.endsWith(".0"))
                value = value.substring(0, value.length() - 2);


            Tooltip t = new Tooltip(value);
            t.setShowDelay(javafx.util.Duration.millis(0));
            Tooltip.install(d.getNode(), t);

        }

    }

    public void setSpentChartData(){

        spentChart.getData().clear();

        //format each axis from showing numeric tick marks to showing the past 7 days
        formatAxis(spentDateAxis, spentChartDate);

        //Create a data series for each graph and set names
        XYChart.Series<Number, Number> spentSeries = new XYChart.Series<>();

        HashMap<String, Integer> spentData = dh.getSpentEntries(user.getUsername(), spentChartDate);


        //Add data to each series.
        DateConverter dc = new DateConverter(spentChartDate);

        for (String key : spentData.keySet())
        {
            spentSeries.getData().add(new XYChart.Data<>(dc.fromString(key), spentData.get(key)));
        }

        //Add series to graphs.
        spentChart.getData().add(spentSeries);

        for (XYChart.Data<Number, Number> d : spentSeries.getData()) {

            String value = d.getYValue().toString();

            if(value.endsWith(".0"))
                value = value.substring(0, value.length() - 2);


            Tooltip t = new Tooltip(value);
            t.setShowDelay(javafx.util.Duration.millis(0));
            Tooltip.install(d.getNode(), t);

        }

    }

    public void setNutritionChart(){
        nutritionChart.getData().clear();

        DateConverter dc = new DateConverter(nutritionChartDate);

        //format each axis from showing numeric tick marks to showing the past 7 days
        formatAxis(nutritionDateAxis, nutritionChartDate);

        // Array list to contain all nutrient chart series
        ArrayList<XYChart.Series<Number, Number>> nutrientSeries = new ArrayList<>();

        // Hashmap to contain all macros summed for each day
        HashMap<String, HashMap<String, Double>> macros = new HashMap<>();

        HashMap<String, ArrayList<NutritionItem>> nutritionData =
                dh.getNutrientEntries(user.getUsername(), nutritionChartDate);

        // For each date in nutrition data keyset (collection of NutritionItems)
        for(String date : nutritionData.keySet()){

            // Hashmap to contain all macros summed for each day
            HashMap<String, Double> allItemMacrosSummed = new HashMap<>();

            // For each NutritionItem in NutritionItem collection for current day
            for(NutritionItem item : nutritionData.get(date)){

                // Get macro nutrients for each NutritionItem
                HashMap<String, Double> itemMacros = item.getMacroNutrients();

                // For each macro nutrient
                for (String macroName : itemMacros.keySet()) {

                    // If macro nutrient already exists, add value to entry
                    if (allItemMacrosSummed.containsKey(macroName)) {

                        allItemMacrosSummed.put(
                                macroName, allItemMacrosSummed.get(macroName) + itemMacros.get(macroName)
                        );

                        // Else, add entry for new macro nutrient
                    } else {

                        allItemMacrosSummed.put(macroName, itemMacros.get(macroName));

                    }

                }

            }

            macros.put(date, allItemMacrosSummed);

        }

        // For each date in macros HashMap
        setNutrientGraph(dc, nutrientSeries, macros);

        // For each nutrient series, add series to chart
        for(XYChart.Series<Number, Number> series : nutrientSeries){

            nutritionChart.getData().add(series);

            // Setting tooltip
            for (XYChart.Data<Number, Number> d : series.getData()) {

                String value = d.getYValue().toString();

                if(value.endsWith(".0"))
                    value = value.substring(0, value.length() - 2);

                Tooltip t = new Tooltip(value);
                t.setShowDelay(javafx.util.Duration.millis(0));
                Tooltip.install(d.getNode(), t);

            }

        }

    }

    public void setMineralsChart(){
        mineralsChart.getData().clear();

        DateConverter dc = new DateConverter(mineralsChartDate);

        //format each axis from showing numeric tick marks to showing the past 7 days
        formatAxis(mineralsDateAxis, mineralsChartDate);

        // Array list to contain all nutrient chart series
        ArrayList<XYChart.Series<Number, Number>> mineralsSeries = new ArrayList<>();

        // Hashmap to contain all macros summed for each day
        HashMap<String, HashMap<String, Double>> minerals = new HashMap<>();

        HashMap<String, ArrayList<NutritionItem>> nutritionData = dh.getNutrientEntries(user.getUsername(), mineralsChartDate);

        // For each date in nutrition data keyset (collection of NutritionItems)
        for(String date : nutritionData.keySet()){

            // Hashmap to contain all macros summed for each day
            HashMap<String, Double> allItemMineralsSummed = new HashMap<>();

            // For each NutritionItem in NutritionItem collection for current day
            for(NutritionItem item : nutritionData.get(date)){

                // Get macro nutrients for each NutritionItem
                HashMap<String, Double> itemMinerals = item.getMinerals();

                for(String mineralName : itemMinerals.keySet()){

                    // If macro nutrient already exists, add value to entry
                    if(allItemMineralsSummed.containsKey(mineralName)){

                        allItemMineralsSummed.put(
                                mineralName, allItemMineralsSummed.get(mineralName) + itemMinerals.get(mineralName)
                        );

                        // Else, add entry for new macro nutrient
                    } else {

                        allItemMineralsSummed.put(mineralName, itemMinerals.get(mineralName));

                    }

                }

            }

            minerals.put(date, allItemMineralsSummed);

        }

        setNutrientGraph(dc, mineralsSeries, minerals);

        for(XYChart.Series<Number, Number> series : mineralsSeries){

            mineralsChart.getData().add(series);

            // Setting tooltip
            for (XYChart.Data<Number, Number> d : series.getData()) {

                String value = d.getYValue().toString();

                if(value.endsWith(".0"))
                    value = value.substring(0, value.length() - 2);

                Tooltip t = new Tooltip(value);
                t.setShowDelay(javafx.util.Duration.millis(0));
                Tooltip.install(d.getNode(), t);

            }

        }

    }

    public void setVitaminsChart(){
        vitaminsChart.getData().clear();

        DateConverter dc = new DateConverter(vitaminsChartDate);

        //format each axis from showing numeric tick marks to showing the past 7 days
        formatAxis(vitaminsDateAxis, vitaminsChartDate);

        // Array list to contain all nutrient chart series
        ArrayList<XYChart.Series<Number, Number>> vitaminsSeries = new ArrayList<>();

        // Hashmap to contain all macros summed for each day
        HashMap<String, HashMap<String, Double>> vitamins = new HashMap<>();

        HashMap<String, ArrayList<NutritionItem>> nutritionData = dh.getNutrientEntries(user.getUsername(), vitaminsChartDate);

        // For each date in nutrition data keyset (collection of NutritionItems)
        for(String date : nutritionData.keySet()){

            // Hashmap to contain all macros summed for each day
            HashMap<String, Double> allItemVitaminsSummed = new HashMap<>();

            // For each NutritionItem in NutritionItem collection for current day
            for(NutritionItem item : nutritionData.get(date)){

                // Get macro nutrients for each NutritionItem
                HashMap<String, Double> itemVitamins = item.getVitamins();

                for(String vitaminName : itemVitamins.keySet()){

                    // If macro nutrient already exists, add value to entry
                    if(allItemVitaminsSummed.containsKey(vitaminName)){

                        allItemVitaminsSummed.put(
                                vitaminName, allItemVitaminsSummed.get(vitaminName) + itemVitamins.get(vitaminName)
                        );

                        // Else, add entry for new macro nutrient
                    } else {

                        allItemVitaminsSummed.put(vitaminName, itemVitamins.get(vitaminName));

                    }

                }

            }

            vitamins.put(date, allItemVitaminsSummed);

        }

        setNutrientGraph(dc, vitaminsSeries, vitamins);

        for(XYChart.Series<Number, Number> series : vitaminsSeries){

            vitaminsChart.getData().add(series);

            // Setting tooltip
            for (XYChart.Data<Number,Number> d : series.getData()) {

                String value = d.getYValue().toString();

                if(value.endsWith(".0"))
                    value = value.substring(0, value.length() - 2);

                Tooltip t = new Tooltip(value);
                t.setShowDelay(javafx.util.Duration.millis(0));
                Tooltip.install(d.getNode(), t);

            }
        }
    }

    public void weightPrevWeek() {
        weightChartNextWeekButton.setTooltip(null);
        weightChartDate = weightChartDate.minusDays(7);
        setWeightChartData();
    }

    public void weightNextWeek() {
        if(!weightChartDate.plusDays(7).isAfter(LocalDate.now())){
            weightChartDate = weightChartDate.plusDays(7);
            setWeightChartData();
        } else {
            weightChartNextWeekButton.setTooltip(nextWeekTooltip);
        }
    }

    public void intakePrevWeek() {
        intakeChartNextWeekButton.setTooltip(null);
        intakeChartDate = intakeChartDate.minusDays(7);
        setIntakeChartData();
    }

    public void intakeNextWeek() {
        if(!intakeChartDate.plusDays(7).isAfter(LocalDate.now())){
            intakeChartDate = intakeChartDate.plusDays(7);
            setIntakeChartData();
        } else {
            intakeChartNextWeekButton.setTooltip(nextWeekTooltip);
        }
    }

    public void burnPrevWeek() {
        burnChartNextWeekButton.setTooltip(null);
        burnChartDate = burnChartDate.minusDays(7);
        setBurnChartData();
    }

    public void burnNextWeek() {
        if(!burnChartDate.plusDays(7).isAfter(LocalDate.now())){
            burnChartDate = burnChartDate.plusDays(7);
            setBurnChartData();
        } else {
            burnChartNextWeekButton.setTooltip(nextWeekTooltip);
        }
    }

    public void spentPrevWeek() {
        spentChartNextWeekButton.setTooltip(null);
        spentChartDate = spentChartDate.minusDays(7);
        setSpentChartData();
    }

    public void spentNextWeek() {
        if(!spentChartDate.plusDays(7).isAfter(LocalDate.now())){
            spentChartDate = spentChartDate.plusDays(7);
            setSpentChartData();
        } else {
            spentChartNextWeekButton.setTooltip(nextWeekTooltip);
        }
    }

    public void nutritionPrevWeek() {
        nutritionChartNextWeekButton.setTooltip(null);
        nutritionChartDate = nutritionChartDate.minusDays(7);
        setNutritionChart();
    }

    public void nutritionNextWeek() {
        if(!nutritionChartDate.plusDays(7).isAfter(LocalDate.now())){
            nutritionChartDate = nutritionChartDate.plusDays(7);
            setNutritionChart();
        } else {
            nutritionChartNextWeekButton.setTooltip(nextWeekTooltip);
        }
    }

    public void mineralsPrevWeek() {
        mineralsChartNextWeekButton.setTooltip(null);
        mineralsChartDate = mineralsChartDate.minusDays(7);
        setMineralsChart();
    }

    public void mineralsNextWeek() {
        if(!mineralsChartDate.plusDays(7).isAfter(LocalDate.now())){
            mineralsChartDate = mineralsChartDate.plusDays(7);
            setMineralsChart();
        } else {
            mineralsChartNextWeekButton.setTooltip(nextWeekTooltip);
        }
    }

    public void vitaminsPrevWeek() {
        vitaminsChartNextWeekButton.setTooltip(null);
        vitaminsChartDate = vitaminsChartDate.minusDays(7);
        setVitaminsChart();
    }

    public void vitaminsNextWeek() {
        if(!vitaminsChartDate.plusDays(7).isAfter(LocalDate.now())){
            vitaminsChartDate = vitaminsChartDate.plusDays(7);
            setVitaminsChart();
        } else {
            vitaminsChartNextWeekButton.setTooltip(nextWeekTooltip);
        }
    }

    /**
     * Private method for formatting a NumberAxis to show dates for the past week instead of ordinals.
     * Centers today's date at ordinal 7.
     *
     * @param DateAxis A NumberAxis object for a JavaFX Chart
     */
    private void formatAxis(NumberAxis DateAxis, LocalDate latest)
    {
        //Set tick labels by calling the formatter method and passing a DateConverter.
        DateAxis.setTickLabelFormatter(new DateConverter(latest));
    }

    /**
     * Private nested utility class for converting dates represented by strings to ordinals and vice versa.
     */
    private static class DateConverter extends StringConverter<Number>
    {
        private final LocalDate latest;

        public DateConverter(LocalDate latest) {
            this.latest = latest;
        }

        /**
         * Converts a Number to a String representing a date.
         * @param number The numeric tick mark.
         * @return A string representation of a date with 7 equal to today's date.
         */
        @Override
        public String toString(Number number)
        {
            //https://stackoverflow.com/questions/11882926/how-to-subtract-x-day-from-a-date-object-in-java
            return latest.minusDays(7 - number.intValue()).toString();
        }

        /**
         * Converts a String representation of a date to an ordinal by comparing the date to today.
         *
         * @param s A string representing a date.
         * @return An ordinal representation of the date, 7 equals today.
         */
        //https://stackoverflow.com/questions/13037654/subtract-two-dates-in-java
        @Override
        public Number fromString(String s)
        {
            LocalDate d2 = LocalDate.parse(s);
            Duration diff = Duration.between(latest.atStartOfDay(), d2.atStartOfDay());

            return diff.toDays() + 7;
        }
    }
}
