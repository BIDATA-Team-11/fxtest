package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import lejos.remote.ev3.RMIRemoteKey;
import lejos.remote.ev3.RemoteEV3;
import java.rmi.RemoteException;
import java.lang.InterruptedException;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;


import java.util.concurrent.ConcurrentLinkedQueue;

public class Chart extends Application {
  private RemoteEV3 connect() throws Exception {
    RemoteEV3 ev3 = new RemoteEV3("10.0.1.1");
    ev3.setDefault();

    return ev3;
  }

  @Override
  public void stop() {
    Thread.currentThread().interrupt();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/chart.fxml"));
    primaryStage.setTitle("OpenJFX Threaded Send and Receive");
    primaryStage.setScene(new Scene((Parent) loader.load()));
    primaryStage.show();

    FXMLController controller = loader.<FXMLController>getController();
    
    ConcurrentLinkedQueue<Coordinates> clq = new ConcurrentLinkedQueue<Coordinates>();

    Platform.runLater(new Receiver(controller, clq));

    RemoteEV3 ev3 = connect();
    final LargeMotor car = new LargeMotor(ev3, "A", "C");
    final Sender sender = new Sender(clq);
    // final MediumMotor rotator = new MediumMotor(ev3, "B");
    // final Gyro gyro = new Gyro(ev3, "S2");
    // final Color color = new Color(ev3, "S3");
    // final Ultrasonic sonic = new Ultrasonic(ev3, "S1");
    
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() { 
        car.cancel();
        sender.cancel();
        // rotator.cancel();
        // gyro.cancel();
        // color.cancel();
        // sonic.cancel();
      }
    });

    try {
      car.start();
      sender.start();
      // rotator.start();
      // gyro.start();
      // color.start();
      // sonic.start();
    } catch (Exception e) {
      car.cancel();
      sender.cancel();
      // rotator.cancel();
      // gyro.cancel();
      // color.cancel();
      // sonic.cancel();
    }
  }
}
