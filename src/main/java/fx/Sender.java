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

          Coordinates c = new Coordinates(n, (n+2));
          clq.add(c);

          ++i;

          try {
            Thread.sleep(2000);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }

        return null;
      }
    };
  }
}
