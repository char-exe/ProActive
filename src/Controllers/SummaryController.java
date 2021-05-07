package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.DatabaseHandler;
import sample.User;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;

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
    @FXML
    private Label welcomeBackLabel;

    @FXML private Button weightChartNextWeekButton;
    @FXML private Button intakeChartNextWeekButton;
    @FXML private Button burnChartNextWeekButton;
    @FXML private Button spentChartNextWeekButton;

    private User user;
    private DatabaseHandler dh;

    private LocalDate weightChartDate = LocalDate.now();
    private LocalDate intakeChartDate = LocalDate.now();
    private LocalDate burnChartDate = LocalDate.now();
    private LocalDate spentChartDate = LocalDate.now();

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
    }

    public void initData(User user) {
        this.user = user;

        //Set Welcome message to Users first name
        welcomeBackLabel.setText("Welcome Back " + user.getFirstname() + "!");
    }

    /**
     * Sets the data for the caloric intake to the net caloric intake for the past 7 days, including today.
     */
    public void initChartData(LocalDate latest) {
        //format each axis from showing numeric tick marks to showing the past 7 days
        formatAxis(intakeDateAxis, latest);
        formatAxis(burnDateAxis, latest);
        formatAxis(spentDateAxis, latest);
        formatAxis(weightDateAxis, latest);

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
        DateConverter dc = new DateConverter(latest);

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

        weightChartNextWeekButton.setTooltip(nextWeekTooltip);
        spentChartNextWeekButton.setTooltip(nextWeekTooltip);
        intakeChartNextWeekButton.setTooltip(nextWeekTooltip);
        burnChartNextWeekButton.setTooltip(nextWeekTooltip);

        weightChart.setAnimated(false);
        spentChart.setAnimated(false);
        intakeChart.setAnimated(false);
        burnChart.setAnimated(false);

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

    public void weightPrevWeek(ActionEvent actionEvent) {
        weightChartNextWeekButton.setTooltip(null);
        weightChartDate = weightChartDate.minusDays(7);
        setWeightChartData();
    }

    public void weightNextWeek(ActionEvent actionEvent) {
        if(!weightChartDate.plusDays(7).isAfter(LocalDate.now())){
            weightChartDate = weightChartDate.plusDays(7);
            setWeightChartData();
        } else {
            weightChartNextWeekButton.setTooltip(nextWeekTooltip);
        }
    }

    public void intakePrevWeek(ActionEvent actionEvent) {
        intakeChartNextWeekButton.setTooltip(null);
        intakeChartDate = intakeChartDate.minusDays(7);
        setIntakeChartData();
    }

    public void intakeNextWeek(ActionEvent actionEvent) {
        if(!intakeChartDate.plusDays(7).isAfter(LocalDate.now())){
            intakeChartDate = intakeChartDate.plusDays(7);
            setIntakeChartData();
        } else {
            intakeChartNextWeekButton.setTooltip(nextWeekTooltip);
        }
    }

    public void burnPrevWeek(ActionEvent actionEvent) {
        burnChartNextWeekButton.setTooltip(null);
        burnChartDate = burnChartDate.minusDays(7);
        setBurnChartData();
    }

    public void burnNextWeek(ActionEvent actionEvent) {
        if(!burnChartDate.plusDays(7).isAfter(LocalDate.now())){
            burnChartDate = burnChartDate.plusDays(7);
            setBurnChartData();
        } else {
            burnChartNextWeekButton.setTooltip(nextWeekTooltip);
        }
    }

    public void spentPrevWeek(ActionEvent actionEvent) {
        spentChartNextWeekButton.setTooltip(null);
        spentChartDate = spentChartDate.minusDays(7);
        setSpentChartData();
    }

    public void spentNextWeek(ActionEvent actionEvent) {
        if(!spentChartDate.plusDays(7).isAfter(LocalDate.now())){
            spentChartDate = spentChartDate.plusDays(7);
            setSpentChartData();
        } else {
            spentChartNextWeekButton.setTooltip(nextWeekTooltip);
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
        private LocalDate latest;

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
            LocalDate d1 = latest;
            LocalDate d2 = LocalDate.parse(s);
            Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());

            return diff.toDays() + 7;
        }
    }
}
