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
import java.util.concurrent.ConcurrentLinkedQueue;

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
public class Ultrasonic extends Service<Void> {
  private RMISampleProvider sampleProvider;
  private String port;

  private final ConcurrentLinkedQueue<Radar> radar;
  private final ConcurrentLinkedQueue<Integer> rotation;

  /**
   * Construct new ultrasonic object using default EV3 brick.
   *
   * @param port Physical port where the sensor is connected.
   */
  public Ultrasonic(RemoteEV3 ev3, String port, ConcurrentLinkedQueue<Radar> radar,
      ConcurrentLinkedQueue<Integer> rotation) {
    this.port = port;
    this.sampleProvider = ev3.createSampleProvider(this.port, "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");
    this.radar = radar;
    this.rotation = rotation;
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
  public float getDistance() throws RemoteException {
    return getSample()[0];
  }

  /**
   * Method for closing the ultrasonicsensor port.
   *
   * @throws IOException - Exception is thrown if an error occurs.
   */
  public void close() throws Exception {
    this.sampleProvider.close();
    cancel();
  }
  /**
   * Satisfy the Service class
   * @see Service
   */
  protected Task<Void> createTask() {
    return new Task<Void>() {
      Float distance;
      float d;

      protected Void call() throws Exception {
        try {
          while (!isCancelled()) {
            if (Thread.interrupted()) {
              close();
              break;
            }

            if (!rotation.isEmpty()) {
              Integer angle = rotation.poll();

              if (angle.intValue() == 90 || angle.intValue() == 0) {
                d = getDistance();

                if (!Float.isInfinite(d)) {
                  distance = Float.valueOf(d);
                } else {
                  distance = Float.valueOf(2);
                }

                radar.add(new Radar(distance, angle));
              }
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
