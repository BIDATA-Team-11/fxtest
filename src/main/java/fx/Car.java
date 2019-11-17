package fx;

import javafx.concurrent.Task;
import javafx.concurrent.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Car extends Service<Void> {
  private final ConcurrentLinkedQueue<Kordinater> clq; 

  public Car(ConcurrentLinkedQueue<Kordinater> clq) { 
    this.clq = clq;
  }

  protected Task<Void> createTask() {
    return new Task<Void>() {
      protected Void call() throws Exception {
        int i;
        for (i = 10; i < 30; i++) {
          if (isCancelled()) { break; }

          final int n = i;

          Kordinater k = new Kordinater(n, (n+2));
          clq.add(k);
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
