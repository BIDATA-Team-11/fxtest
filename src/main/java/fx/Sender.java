package fx;

import javafx.concurrent.Task;
import javafx.concurrent.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

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
public class Sender extends Service<Void> {
  private final ConcurrentLinkedQueue<Coordinates> clq;

  /**
   * TODO
   *
   * @param clq TODO
   */
  public Sender(ConcurrentLinkedQueue<Coordinates> clq) {
    this.clq = clq;
  }

  protected Task<Void> createTask() {
    return new Task<Void>() {

      protected Void call() throws Exception {
        int i = 0;

        while (true) {
          // for (i = 0; i < 10; ++i) {
          if (isCancelled()) {
            break;
          }

          final int n = i;

          // System.out.printf("sending %s\n", n);

          Coordinates c = new Coordinates(n, (n + 2));
          clq.add(c);

          ++i;

          try {
            Thread.sleep(2000);
          } catch (Exception e) {
            System.out.println("EXCEPTION");
            e.printStackTrace();
          }
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
    };
  }

}
