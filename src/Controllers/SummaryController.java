package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * A class to control the summary viewing page of the ProActive App. Handles the adding of data to the graphs and the
 * formatting of x axes from number to date format.
 *
 * @author Samuel Scarfe 100048633
 * @version 1.0 - Simple graphing with a loop for simple dummy data and axis formatting.
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
     * Initializes the graphs with formatted axes and dummy data.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     * @see javafx.fxml.Initializable
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        //format each axis from showing numeric tick marks to showing the past 7 days
        formatAxis(intakeDateAxis);
        formatAxis(burnDateAxis);
        formatAxis(spentDateAxis);

        //Create a data series for each graph and set names
        XYChart.Series<Number, Number> intakeSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> burnSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> spentSeries = new XYChart.Series<>();

        intakeSeries.setName("Caloric Intake");
        burnSeries.setName("Calories Burned via Exercise");
        spentSeries.setName("Minutes Spent");

        //Create a datapoint for each day.
        for (int i = 1; i < 8; i++)
        {
            //Add data points to series.
            intakeSeries.getData().add(new XYChart.Data<>(i, i));
            burnSeries.getData().add(new XYChart.Data<>(i, i));
            spentSeries.getData().add(new XYChart.Data<>(i, i));
        }

        //Add series to graphs.
        intakeChart.getData().add(intakeSeries);
        burnChart.getData().add(burnSeries);
        spentChart.getData().add(spentSeries);

        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/Summary.fxml"));
        loader.setController(this);
        try
        {
            loader.load();
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage() + "Error loading Summary.fxml");
        }

         */
    }

    /**
     * Private method for formatting a NumberAxis to show dates for the past week instead of ordinals.
     * Centers today's date at ordinal 7.
     *
     * @param DateAxis A NumberAxis object for a JavaFX Chart
     */
    private void formatAxis(NumberAxis DateAxis)
    {
        //Set tick labels by calling the formatter method and passing an anonymous StringConverter.
        DateAxis.setTickLabelFormatter(new StringConverter<>()
        {
            /**
             * Converts a Number to a String representing a date.
             * @param number The numeric tick mark.
             * @return A string representation of a date with 7 equal to today's date.
             */
            @Override
            public String toString(Number number)
            {
                //https://stackoverflow.com/questions/11882926/how-to-subtract-x-day-from-a-date-object-in-java
                return LocalDate.now().minusDays(7 - number.intValue()).toString();
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
                LocalDate d1 = LocalDate.now();
                LocalDate d2 = LocalDate.parse(s);
                Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());

                return diff.toDays();
            }
        });
    }

}
