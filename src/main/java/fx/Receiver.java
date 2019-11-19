package fx;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.stage.Stage;
import javafx.application.Platform;

public class Receiver extends Task<Void> {
  private final FXMLController c;
  private final ConcurrentLinkedQueue<Coordinates> clq; 
  private final Stage primaryStage;

  // public Receiver(FXMLController c, ConcurrentLinkedQueue<Coordinates> clq) { 
  public Receiver(FXMLController c, ConcurrentLinkedQueue<Coordinates> clq, Stage primaryStage) { 
    this.c = c;
    this.clq = clq;
    this.primaryStage = primaryStage;
  }

  @Override 
  protected Void call() throws Exception {
    // while (!isCancelled()) {
      System.out.println("I am triggered");
      Coordinates c = this.clq.poll();

      if (c != null) {
        System.out.println(c.x);
        this.c.mapping(c);
      }

      // Platform.runLater(new Runnable() {
      //   @Override 
      //   public void run() {
      //     primaryStage.show();
      //   }
      // });
    // }

    return null;
  }
  
  @Override 
  protected void cancelled() {
    super.cancelled();
    updateMessage("Cancelled!");
  }

  @Override 
  protected void failed() {
    super.failed();
    updateMessage("Failed!");
  }
}
