package fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;

import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * TODO
 */
public class FXMLController implements Initializable {

  private XYChart.Series<String, Number> series;

  @FXML
  private ScatterChart<String, Number> chart;

  @FXML
  private CategoryAxis xaxis;

  @FXML
  private NumberAxis yaxis;

  /**
   * Initialise resource. TODO
   *
   * @param url TODO
   * @param rb  TODO
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.xaxis.setLabel("Bredde");
    this.yaxis.setLabel("Høyde");
    this.chart.setAnimated(false);

    this.series = new XYChart.Series<String, Number>();
  }

  /**
   * TODO
   *
   * @param c Coordinates instance
   */
  public void mapping(Coordinates c) {
    this.series.getData().add(new XYChart.Data<String, Number>(String.valueOf(c.x), c.y));
    this.chart.getData().setAll(this.series);
  }
}
