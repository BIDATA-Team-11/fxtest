package fx;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.application.Platform;

import javafx.concurrent.ScheduledService;

public class ReceiverService extends ScheduledService<Void> {
  private final FXMLController c;
  private final ConcurrentLinkedQueue<Coordinates> clq; 

  public ReceiverService(FXMLController c, ConcurrentLinkedQueue<Coordinates> clq) { 
    this.c = c;
    this.clq = clq;
  }

  protected Task<Void> createTask() {
    return new Task<Void>() {
      protected Void call() throws Exception {
        Platform.runLater(new Runnable() {
          @Override 
          public void run() {
            Coordinates k = clq.poll();
            if (k != null) {
              c.mapping(k);
            }
          }
        });

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
    };
  }
}
