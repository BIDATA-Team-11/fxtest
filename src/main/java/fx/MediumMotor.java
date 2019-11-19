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
 * Class that implements the simplemotor used for the NXTUltrasonicSensor.
 *
 *@author Stian Selvåg
 *@author Herman Aagaard
 *@author Henrik Hafsø
 *@author Joakim Skogø Langvand
 *@author Erling Sletta
 *@author Torbjørn Øverås
 *@author Gruppe 11, dataingeniør NTNU, første semester.
 */
public class MediumMotor extends Service<Void> {
  private RemoteEV3 ev3;
  private RMIRegulatedMotor motor;
  private String port;

  private final ConcurrentLinkedQueue<Integer> rotation;

  /**
   * Construct new simplemotor object using default Ev3 brick.
   *
   * @param port physical port where the motor is connected.
   */
  public MediumMotor(RemoteEV3 ev3, String port, final ConcurrentLinkedQueue<Integer> rotation) { 
    this.ev3 = ev3; this.port = port; 
    this.motor = this.ev3.createRegulatedMotor(this.port, 'M'); 
    this.rotation = rotation;
  }

  /**
   * Method for closing the simplemotor port.
   *
   * @throws RemoteException Exception is thrown if an error occurs.
   */
  public void close() throws RemoteException {
    try { this.motor.close(); cancel(); }
    catch (Exception e) { e.printStackTrace(); }
  }

  /**
   * Method for stopping the simplemotor.
   *
   * @throws RemoteException Exception is thrown if an error occurs.
   */
  public void stop() throws RemoteException { this.motor.stop(true); }

  /**
   * Method for rotating the simple motor by angle degrees.
   * @param angle int for the amount of degrees the simplemotor should rotate.
   * @throws RemoteException Exception is thrown if an error occurs.
   */
  public void rotate(int angle) throws RemoteException { this.motor.rotateTo(angle); }

  protected Task<Void> createTask() {
    return new Task<Void>() {
      boolean resetAngle = false;

      protected Void call() throws Exception {
        try {
          while (!isCancelled()) {
            if (Thread.interrupted()) { close(); break; }

            if (resetAngle) {
              rotate(0);
              resetAngle = false;

              rotation.add(Integer.valueOf(0));
            } else {
              rotate(-90);
              resetAngle = true;

              rotation.add(Integer.valueOf(90));
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
