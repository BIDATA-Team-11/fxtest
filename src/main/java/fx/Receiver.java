package fx;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Receiver extends Task<Void> {
  private final FXMLController c;
  private final ConcurrentLinkedQueue<Coordinates> clq; 
  // private final ConcurrentLinkedQueue<Integer> r; 

  // public Receiver(FXMLController c, ConcurrentLinkedQueue<Coordinates> clq, ConcurrentLinkedQueue<Integer> r) { 
  public Receiver(FXMLController c, ConcurrentLinkedQueue<Coordinates> clq) { 
    this.c = c;
    this.clq = clq;
    // this.r = r;
  }

  @Override 
  protected Void call() throws Exception {
    while (true) {
      if (isCancelled()) { break; }

      Coordinates c = this.clq.poll();
      this.c.mapping(c);
      
      // Integer i = this.r.peek();
      // System.out.println(i.intValue());
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
