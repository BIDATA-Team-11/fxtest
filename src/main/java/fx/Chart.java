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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lejos.remote.ev3.RemoteEV3;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.util.Duration;

/**
 * TODO
 *
 * @author Stian Selvåg
 * @author Herman Aagaard
 * @author Henrik Hafsø
 * @author Joakim Skogø Langvand
 * @author Erling Sletta
 * @author Torbjørn Øverås
 * @author Gruppe 11, dataingeniør NTNU, første semester.
 */
public class Chart extends Application {
  private RemoteEV3 connectEV3() throws Exception {
    RemoteEV3 ev3 = new RemoteEV3("10.0.1.1");
    ev3.setDefault();

    return ev3;
  }

  /**
   * TODO: javadoc
   *
   * @param primaryStage TODO
   * @throws Exception TODO
   */
  @Override
  public void start(final Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/chart.fxml"));
    primaryStage.setTitle("OpenJFX Threaded Send and Receive");
    primaryStage.setScene(new Scene((Parent) loader.load()));
    primaryStage.show();

    final FXMLController controller = loader.<FXMLController>getController();

    final ConcurrentLinkedQueue<Coordinates> coordinates = new ConcurrentLinkedQueue<Coordinates>();
    final ConcurrentLinkedQueue<Radar> radar = new ConcurrentLinkedQueue<Radar>();
    final ConcurrentLinkedQueue<Integer> rotation = new ConcurrentLinkedQueue<Integer>();

    final Receiver receiver = new Receiver(controller, coordinates);
    receiver.setPeriod(Duration.seconds(2));
    receiver.start();

    final RemoteEV3 ev3 = connectEV3();

    // final Sender sender = new Sender(coordinates);
    final MediumMotor rotator = new MediumMotor(ev3, "B", rotation);
    final Ultrasonic sonic = new Ultrasonic(ev3, "S1", radar, rotation);
    final LargeMotor car = new LargeMotor(ev3, "A", "C", coordinates, radar);
    // final Gyro gyro = new Gyro(ev3, "S2");
    // final Color color = new Color(ev3, "S3");

    try {
      // sender.start();
      rotator.start();
      sonic.start();
      car.start();
      // // gyro.start();
      // // color.start();
    } catch (Exception e) {
      // sender.cancel();
      rotator.cancel();
      sonic.cancel();
      car.cancel();
      // // gyro.cancel();
      // // color.cancel();
    }
  }
}
