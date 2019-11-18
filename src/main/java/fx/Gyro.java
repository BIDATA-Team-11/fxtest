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
public class Gyro extends Service<Void>  {
  private RMISampleProvider sampleProvider;
  private RemoteEV3 ev3;
  private String port;

  /**
   * Constructor that requires and sets the EV3 and the port the sensor is connected to,
   * as well as creating a sample provider for the given port, with a sensor name and mode name.
   * @param ev3 The ev3 that's being input
   * @param port The port the sensor is connected to
   */
  public Gyro(RemoteEV3 ev3, String port) {
    this.port = port;
    this.ev3 = ev3;
    this.sampleProvider = ev3.createSampleProvider(this.port, "lejos.hardware.sensor.EV3GyroSensor", "Angle");
  }

  /**
   * Method that fetches and returns a sample from the sensor
   * @return sample angle from sensor
   * @throws RemoteException Throws an exception if an error occurs
   */
  public float[] getSample() throws RemoteException {
    float[] sample = null; 
    sample = this.sampleProvider.fetchSample();

    return sample;
  }

  /**
   * Method that executes {@link Gyro#getSample()} and returns the first element in the array
   * @return The first element in the array given by {@link Gyro#getSample}
   * @throws RemoteException Throws a RemoteException if an error occurs
   */
  public float getAngle() throws RemoteException { return getSample()[0]; }

  /**
   * Override close method to close the sample provider
   * @throws IOException Throws an IOException if an error occurs
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
