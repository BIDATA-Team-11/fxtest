package fx;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;

// import javafx.application.Platform;
// import javafx.stage.Stage;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Receiver extends Task<Void> {
  private final FXMLController c;
  private final ConcurrentLinkedQueue<Coordinates> clq; 

  public Receiver(FXMLController c, ConcurrentLinkedQueue<Coordinates> clq) { 
    this.c = c;
    this.clq = clq;
  }

  @Override 
  protected Void call() throws Exception {
    while (!isCancelled()) {
      Coordinates c = this.clq.poll();
      System.out.println(c.x);
      this.c.mapping(c);
    }

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
