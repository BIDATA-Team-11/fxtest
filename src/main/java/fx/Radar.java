package fx;

/**
 * Class for holding radar reading data.
 *
 * @author Stian Selvåg
 * @author Herman Aagaard
 * @author Henrik Hafsø
 * @author Joakim Skogø Langvand
 * @author Erling Sletta
 * @author Torbjørn Øverås
 * @author Gruppe 11, dataingeniør NTNU, første semester.
 */
public class Radar {
  float distance;
  float angle;

  /**
   *
   * @param distance to target.
   * @param angle    relative to robot axis.
   */
  public Radar(float distance, float angle) {
    this.distance = distance;
    this.angle = angle;
  }
}
