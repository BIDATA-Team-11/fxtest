package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import lejos.remote.ev3.RemoteEV3;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Chart extends Application {
  private RemoteEV3 connect() throws Exception {
    RemoteEV3 ev3 = new RemoteEV3("10.0.1.1");
    ev3.setDefault();

    return ev3;
  }

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

    Receiver receiver = new Receiver(controller, coordinates);
    Platform.runLater(receiver);

    final RemoteEV3 ev3 = connect();

    final Sender sender = new Sender(coordinates);
    final MediumMotor rotator = new MediumMotor(ev3, "B", rotation);
    final Ultrasonic sonic = new Ultrasonic(ev3, "S1", radar, rotation);
    final LargeMotor car = new LargeMotor(ev3, "A", "C", coordinates, radar);
    // final Gyro gyro = new Gyro(ev3, "S2");
    // final Color color = new Color(ev3, "S3");

    try {
      sender.start();
      rotator.start();
      sonic.start();
      car.start();
    //   // gyro.start();
    //   // color.start();
    } catch (Exception e) {
      sender.cancel();
      rotator.cancel();
      sonic.cancel();
      car.cancel();
    //   // gyro.cancel();
    //   // color.cancel();
    }
  }
}
