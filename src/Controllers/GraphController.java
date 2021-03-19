package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class GraphController implements Initializable
{
    @FXML
    private NumberAxis burnDateAxis;
    @FXML
    private NumberAxis intakeDateAxis;
    @FXML
    private NumberAxis spentDateAxis;
    @FXML
    private LineChart<Number, Number> intakeChart;
    @FXML
    private LineChart<Number, Number> burnChart;
    @FXML
    private LineChart<Number, Number> spentChart;

    public GraphController(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/Graph.fxml"));
        loader.setController(this);
        try {
            loader.load();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + "Error loading Graph.fxml");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        intakeDateAxis.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number number)
            {
                //https://stackoverflow.com/questions/11882926/how-to-subtract-x-day-from-a-date-object-in-java
                return LocalDate.now().minusDays(7 - number.intValue()).toString();
            }

            //https://stackoverflow.com/questions/13037654/subtract-two-dates-in-java
            @Override
            public Number fromString(String s) {
                LocalDate d1 = LocalDate.now();
                LocalDate d2 = LocalDate.parse(s);
                Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());

                return diff.toDays();
            }
        });

        burnDateAxis.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number number)
            {
                //https://stackoverflow.com/questions/11882926/how-to-subtract-x-day-from-a-date-object-in-java
                return LocalDate.now().minusDays(7 - number.intValue()).toString();
            }

            //https://stackoverflow.com/questions/13037654/subtract-two-dates-in-java
            @Override
            public Number fromString(String s) {
                LocalDate d1 = LocalDate.now();
                LocalDate d2 = LocalDate.parse(s);
                Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());

                return diff.toDays();
            }
        });

        spentDateAxis.setTickLabelFormatter(new StringConverter<>() {
            @Override
            public String toString(Number number)
            {
                //https://stackoverflow.com/questions/11882926/how-to-subtract-x-day-from-a-date-object-in-java
                return LocalDate.now().minusDays(7 - number.intValue()).toString();
            }

            //https://stackoverflow.com/questions/13037654/subtract-two-dates-in-java
            @Override
            public Number fromString(String s) {
                LocalDate d1 = LocalDate.now();
                LocalDate d2 = LocalDate.parse(s);
                Duration diff = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());

                return diff.toDays();
            }
        });

        XYChart.Series<Number, Number> intakeSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> burnSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> spentSeries = new XYChart.Series<>();

        intakeSeries.setName("Caloric Intake");
        burnSeries.setName("Calories Burned via Exercise");
        spentSeries.setName("Minutes Spent");

        for (int i = 1; i < 8; i++)
        {
            intakeSeries.getData().add(new XYChart.Data<>(i, i));
            burnSeries.getData().add(new XYChart.Data<>(i, i));
            spentSeries.getData().add(new XYChart.Data<>(i, i));
        }

        intakeChart.getData().add(intakeSeries);
        burnChart.getData().add(burnSeries);
        spentChart.getData().add(spentSeries);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/Graph.fxml"));
        loader.setController(this);
        try {
            loader.load();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage() + "Error loading Graph.fxml");
        }
    }

}
