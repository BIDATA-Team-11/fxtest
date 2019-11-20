// Copyright (c) 2019 Several authors, see javadoc comment
//
// GNU GENERAL PUBLIC LICENSE
//    Version 3, 29 June 2007
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.

package fx;

import javafx.concurrent.Task;
import java.util.concurrent.ConcurrentLinkedQueue;
import javafx.stage.Stage;

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
public class Receiver extends Task<Void> {
  private final FXMLController c;
  private final ConcurrentLinkedQueue<Coordinates> clq;

  // public Receiver(FXMLController c, ConcurrentLinkedQueue<Coordinates> clq) {
  /**
   * TODO
   */
  public Receiver(FXMLController c, ConcurrentLinkedQueue<Coordinates> clq, Stage primaryStage) {
    this.c = c;
    this.clq = clq;
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
    // @Override
    // public void run() {
    // primaryStage.show();
    // }
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
