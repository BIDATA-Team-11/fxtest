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
import lejos.remote.ev3.RMISampleProvider;
import java.rmi.RemoteException;
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
 */
public class Color extends Service<Void> {
  private RMISampleProvider sampleProvider;
  private String port;

  /**
   * Constructor that requires and sets the EV3 and the port the sensor is
   * connected to, as well as creating a sample provider for the given port, with
   * a sensor name and mode name.
   *
   * @param ev3  The ev3 that's being input
   * @param port The port the sensor is connected to
   */
  public Color(RemoteEV3 ev3, String port) {
    this.port = port;
    this.sampleProvider = ev3.createSampleProvider(this.port, "lejos.hardware.sensor.EV3ColorSensor", "RGB");
  }

  /**
   * Method that fetches and returns a sample in the form of a float array from
   * the sensor
   *
   * @return A float array sample
   * @throws RemoteException Throws a RemoteException if an error occurs
   */
  public float[] getSample() throws RemoteException {
    float[] sample = null;
    sample = this.sampleProvider.fetchSample();

    return sample;
  }

  /**
   * Method that returns the first sample in the float array provided by
   * {@link Farge#getSample()}
   *
   * @return The first element in the array given by {@link Farge#getSample()}
   * @throws RemoteException Throws a RemoteException if an error occurs
   */
  public float getColor() throws RemoteException {
    return getSample()[0];
  }

  /**
   * Override close method to close the sample provider
   *
   * @throws IOException Throws an IOException if an error occurs
   */
  public void close() throws RemoteException {
    this.sampleProvider.close();
  }

  protected Task<Void> createTask() {
    return new Task<Void>() {
      protected Void call() throws Exception {
        while (!isCancelled()) {
          if (Thread.interrupted()) {
            close();
            break;
          }
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
