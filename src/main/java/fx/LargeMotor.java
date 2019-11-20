package fx;

import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.robotics.RegulatedMotor;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.lang.InterruptedException;

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
   * @param ev3 RMI connection to EV3 computer
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
   * @throws RemoteException - Exception is thrown if an error occurss.
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
   * @throws RemoteException - Exception is thrown if an error occurss.
   */
  public void stop() throws RemoteException {
    this.leftMotor.stop(true);
    this.rightMotor.stop(true);
  }

  /**
   * Method got making the car turn left.
   *
   * @throws RemoteException - Exception is thrown if an error occurss.
   */
  public void left() throws RemoteException {
    this.rightMotor.backward();
    this.leftMotor.forward();
  }

  /**
   * Method for making the car turn right.
   *
   * @throws RemoteException - Exception is thrown if an error occurss.
   */
  public void right() throws RemoteException {
    this.leftMotor.backward();
    this.rightMotor.forward();
  }

  /**
   * Method for making the motors drive backwards.
   *
   * @throws RemoteException - Exception is thrown if an error occurss.
   */
  public void backward() throws RemoteException {
    this.leftMotor.forward();
    this.rightMotor.forward();
  }

  /**
   * Method for making the motors drive forwards.
   *
   * @throws RemoteException - Exception is thrown if an error occurss.
   */
  public void forward() throws RemoteException {
    this.leftMotor.backward();
    this.rightMotor.backward();
  }

  protected Task<Void> createTask() {
    return new Task<Void>() {
      int testX = 0;
      int testY = 0;

      final float minimumDistanceToObstacle = 0.2f;
      final float maximumDistanceToObstacle = 0.4f;

      boolean obstacleInFront = false;

      protected Void call() throws Exception {
        try {
          while (!isCancelled()) {
            if (Thread.interrupted()) {
              close();
              break;
            }

            if (!radar.isEmpty()) {
              Radar r = radar.poll();

              System.out.printf("Distance: %s Angle: %s\n", r.distance, r.angle);

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

              coordinates.add(new Coordinates(++testX, ++testY));
            }

          }
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          close();
        }

        return null;
      }

      @Override
      protected void cancelled() {
        super.cancelled();
        updateMessage("Cancelled!");
        try {
          close();
        } catch (Exception e) {
          System.out.println(e);
        }
      }

      @Override
      protected void failed() {
        super.failed();
        updateMessage("Failed!");
        try {
          close();
        } catch (Exception e) {
          System.out.println(e);
        }
      }
    };
  }
}
