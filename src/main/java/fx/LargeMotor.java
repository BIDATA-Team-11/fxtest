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

import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RMIRegulatedMotor;
import java.rmi.RemoteException;
import javafx.concurrent.Task;
import javafx.concurrent.Service;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Class for implementing NXTRegulatedMotor.
 *
 * @author Stian Selvåg
 * @author Herman Aagaard
 * @author Henrik Hafsø
 * @author Joakim Skogø Langvand
 * @author Erling Sletta
 * @author Torbjørn Øverås
 * @author Gruppe 11, dataingeniør NTNU, første semester.
 */
public class LargeMotor extends Service<Void> {
  private RemoteEV3 ev3;
  private RMIRegulatedMotor leftMotor;
  private RMIRegulatedMotor rightMotor;

  private final ConcurrentLinkedQueue<Coordinates> coordinates;
  private final ConcurrentLinkedQueue<Radar> radar;

  /**
   * Constructer for setting value of ev3. It also pairs left and right motor with
   * ports.
   *
   * @param ev3 Remote connection to EV3 computer
   */
  public LargeMotor(RemoteEV3 ev3, String port1, String port2, ConcurrentLinkedQueue<Coordinates> coordinates,
      ConcurrentLinkedQueue<Radar> radar) {
    this.ev3 = ev3;
    this.leftMotor = this.ev3.createRegulatedMotor(port1, 'L');
    this.rightMotor = this.ev3.createRegulatedMotor(port2, 'L');
    this.coordinates = coordinates;
    this.radar = radar;
  }

  /**
   * Method for closing port A and C.
   *
   * @throws RemoteException - Exception is thrown if an error occurs.
   */
  public void close() throws RemoteException {
    try {
      this.leftMotor.close();
      this.rightMotor.close();

      cancel();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Method for stopping both motors.
   *
   * @throws RemoteException - Exception is thrown if an error occurs.
   */
  public void stop() throws RemoteException {
    this.leftMotor.stop(true);
    this.rightMotor.stop(true);
  }

  /**
   * Method got making the car turn left.
   *
   * @throws RemoteException - Exception is thrown if an error occurs.
   */
  public void left() throws RemoteException {
    this.rightMotor.backward();
    this.leftMotor.forward();
  }

  /**
   * Method for making the car turn right.
   *
   * @throws RemoteException - Exception is thrown if an error occurs.
   */
  public void right() throws RemoteException {
    this.leftMotor.backward();
    this.rightMotor.forward();
  }

  /**
   * Method for making the motors drive backwards.
   *
   * @throws RemoteException - Exception is thrown if an error occurs.
   */
  public void backward() throws RemoteException {
    this.leftMotor.forward();
    this.rightMotor.forward();
  }

  /**
   * Method for making the motors drive forwards.
   *
   * @throws RemoteException - Exception is thrown if an error occurs.
   */
  public void forward() throws RemoteException {
    this.leftMotor.backward();
    this.rightMotor.backward();
  }

  /**
   * Satisfies the Service class
   *
   * @return null
   * @see Service
   */
  protected Task<Void> createTask() {
    return new Task<Void>() {
      int testX = 0;
      int testY = 0;

      final float minimumDistanceToObstacle = 0.2f;
      final float maximumDistanceToObstacle = 0.4f;

      boolean obstacleInFront = false;

      /**
       *
       *
       * @return
       * @throws Exception Exception is thrown if an error occurs
       */
      protected Void call() throws Exception {
        try {
          while (!isCancelled()) {
            if (Thread.interrupted()) {
              close();
              break;
            }

            if (!radar.isEmpty()) {
              Radar r = radar.poll();

              if (r.angle <= 0) {
                if (r.distance < minimumDistanceToObstacle) {
                  right();
                  obstacleInFront = true;
                } else {
                  forward();
                  obstacleInFront = false;
                }

              } else if (r.angle >= 90 && !obstacleInFront) {
                if (r.distance > maximumDistanceToObstacle) {
                  left();
                } else if (r.distance < minimumDistanceToObstacle) {
                  right();
                } else {
                  forward();
                }
              } else {
                forward();
              }

              coordinates.add(new Coordinates(++testX, (int) r.distance));
            }

          }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          close();
        }

        return null;
      }
    };
  }
}
