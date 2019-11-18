package fx;

import lejos.remote.ev3.RemoteEV3;
import lejos.remote.ev3.RMISampleProvider;
import java.rmi.RemoteException;
import java.lang.InterruptedException;

import javafx.concurrent.Task;
import javafx.concurrent.Service;

/**
 * NXTUltrasonicSensor implementation of the DistanceMeasure interface.
 *
 * @author Stian Selvåg
 * @author Herman Aagaard
 * @author Henrik Hafsø
 * @author Joakim Skogø Langvand
 * @author Erling Sletta
 * @author Torbjørn Øverås
 * @author Gruppe 11, dataingeniør NTNU, første semester.
 * @version 1.0.0
 */
public class Ultrasonic extends Service<Void> {
  private RMISampleProvider sampleProvider;
  private RemoteEV3 ev3;
  private String port;

  /**
   * Construct new ultrasonic object using default EV3 brick.
   *
   * @param port Physical port where the sensor is connected.
   */
  public Ultrasonic(RemoteEV3 ev3, String port) {
    this.port = port;
    this.ev3 = ev3;
    this.sampleProvider = ev3.createSampleProvider(this.port, "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");
  }

  /**
   * Method for sampling distance with the ultrasonic sensor.
   *
   * @return distance measured with the ultrasonic sensor.
   * @throws RemoteException - Exception is thrown if an error occurs.
   */
  public float[] getSample() throws RemoteException {
    float[] sample = null; 
    sample = this.sampleProvider.fetchSample();

    return sample;
  }

  /**
   * Method for getting the distance measured by the ultrasonic sensor.
   *
   * @return the first value in the sample array, which measures distance.
   * @throws RemoteException - Exception is thrown if an error occurs.
   */
  public float getDistance() throws RemoteException { return getSample()[0]; }

  /**
   * Method for closing the ultrasonicsensor port.
   *
   * @throws IOException - Exception is thrown if an error occurs.
   */
  public void close() throws Exception { this.sampleProvider.close(); }

  protected Task<Void> createTask() {
    return new Task<Void>() {
      protected Void call() throws Exception {

        try {
          while (true) {
            if (isCancelled()) { close(); break; }     
            if (Thread.interrupted()) { close(); break; }
          }
        } catch (Exception e) {
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
