package fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;


import java.util.concurrent.ConcurrentLinkedQueue;

public class Chart extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("view/chart.fxml"));
    primaryStage.setTitle("OpenJFX Threaded Send and Receive");
    primaryStage.setScene(new Scene((Parent) loader.load()));
    primaryStage.show();

    FXMLController controller = loader.<FXMLController>getController();
    
    ConcurrentLinkedQueue<Kordinater> clq = new ConcurrentLinkedQueue<Kordinater>();

    // Platform.runLater(new Car(controller));
    Platform.runLater(new Receiver(controller, clq));

    Sender sender = new Sender(clq);
    sender.start();
    // Thread s = new Thread(sender);
    // s.setDaemon(true);
    // s.start();

    Car car = new Car(clq);
    car.start();

  }

  // public static void main(String[] args) {
  //   Application.launch(args);
  // }
}
