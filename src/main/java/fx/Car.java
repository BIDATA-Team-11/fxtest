package fx;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;

public class Car extends Task<Void> {
  private final FXMLController f;

  public Car(FXMLController f) { this.f = f; }

  @Override 
  protected Void call() throws Exception {
    int i;
    for (i = 0; i < 10; i++) {
      if (isCancelled()) { break; }

      final int n = i;

      Kordinater k = new Kordinater(n, (n+2));
      f.mapping(k);
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
}
