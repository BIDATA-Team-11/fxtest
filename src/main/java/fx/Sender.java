package fx;

import javafx.concurrent.Task;
import javafx.concurrent.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Sender extends Service<Void> {
  private final ConcurrentLinkedQueue<Coordinates> clq; 
  public Sender(ConcurrentLinkedQueue<Coordinates> clq) { this.clq = clq; }

  protected Task<Void> createTask() {
    return new Task<Void>() {
      protected Void call() throws Exception {
        int i;
        for (i = 0; i < 10; i++) {
          if (isCancelled()) { break; }

          final int n = i;

          Coordinates c = new Coordinates(n, (n+2));
          clq.add(c);
        }

        return null;
      }

      @Override 
      protected void cancelled() {
        super.cancelled();
        updateMessage("Cancelled!");
        // TODO: Close logic
      }

      @Override 
      protected void failed() {
        super.failed();
        updateMessage("Failed!");
      }
    };
  }

}
