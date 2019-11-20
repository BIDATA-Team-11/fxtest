// Copyright (c) 2019 Several authors, see javadoc comment
//
// GNU GENERAL PUBLIC LICENSE
//    Version 3, 29 June 2007
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;

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
