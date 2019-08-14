package utilities;

import static utilities.metadata.StaticUtilities.DEFAULT_OBSTACLE_FILL;
import static utilities.metadata.StaticUtilities.FRAME_INTERVAL;

import java.util.ListIterator;
import javafx.scene.paint.Paint;
import org.jbox2d.common.Vec2;
import org.waltonrobotics.metadata.PathData;
import org.waltonrobotics.motion.Path;

/**
 * @author Russell Newton
 **/
public class KinematicObstacle extends Obstacle {

  private final float speed;
  private final Path path;
  private final Vec2 velocityVector = new Vec2(0, 0);
  private final Vec2 positionVector = new Vec2(0, 0);
  private double startTime;
  private ListIterator<PathData> pdIterator;
  private PathData pdPrevious;
  private PathData pdNext;

  public KinematicObstacle(GameController controller, float startX, float startY, float width,
      float height, float angle, Paint fill, float speed, Path path) {
    super(controller, startX, startY, width, height, angle, fill);
    this.speed = speed;
    this.path = path;
    startTime = controller.getExecutionTime();
    pdIterator = path.getPathData().listIterator();
  }

  public KinematicObstacle(GameController controller, float startX, float startY, float width,
      float height, float angle, float speed, Path path) {
    this(controller, startX, startY, width, height, angle, DEFAULT_OBSTACLE_FILL, speed, path);
  }

  public KinematicObstacle(Obstacle staticObstacle, float speed, Path path) {
    super(staticObstacle.controller, staticObstacle.startX, staticObstacle.startY,
        staticObstacle.width, staticObstacle.height, staticObstacle.angle,
        staticObstacle.fill);
    this.speed = speed;
    this.path = path;
    startTime = staticObstacle.controller.getExecutionTime();
    pdIterator = path.getPathData().listIterator();
  }

  /**
   * Create a new {@code KinematicObject} from a {@code String}. It should be formatted as
   * follows:<br><br> "X:nnn,Y:nnn,W:nnn,H:nnn,A:nnn,P:nnn,S:nnn", <br><br> where each capital
   * letter denotes {@code Obstacle} parameters:<br> X: = startX<br> Y: = startY<br> W: = width<br>
   * H: = height<br> A: = angle (degrees)  --  This is not working like it should right now and will
   * be overridden to be 0.<br> P: = file or web location of the sprite (If an error in this step
   * occurs, the color will be the default Color).<br> S: = speed. <br> A path needs to be passed in
   * as a parameter.
   *
   * @param controller the controller this {@code Obstacle} belongs.
   * @param paramString a string containing the above format to create the {@code Obstacle} from.
   * @param path I can't figure out how to get this to be nice from a {@code String}.
   */
  public static KinematicObstacle fromString(GameController controller, String paramString,
      float speed, Path path) {
    System.out.println("Creating KinematicObstacle from " + paramString);
    Obstacle staticObstacle = Obstacle.fromString(controller, paramString, false);
    if (staticObstacle == null) {
      return null;
    }
    return new KinematicObstacle(staticObstacle, speed, path);
  }

  @Override
  public void update() {
    super.update();
    if (pdIterator.hasNext()) {
      interpolate();
    }
    body.setLinearVelocity(velocityVector);
  }

  private void interpolate() {
    if (pdNext == null) {
      pdNext = pdIterator.next();
    }
    double currentTime = controller.getExecutionTime() - startTime;
    while (currentTime > pdNext.getTime()) {
      if (pdIterator.hasNext()) {
        pdPrevious = pdNext;
        pdNext = pdIterator.next();
      } else {
        pdPrevious = pdNext = null;
        pdIterator = path.getPathData().listIterator();
        startTime = controller.getExecutionTime();
        velocityVector.set(0, 0);
        return;
      }
    }

    double timePrevious = pdPrevious.getTime();
    double timeNext = pdNext.getTime();
    double dTime = timeNext - timePrevious;
    double rctn =
        (timeNext - currentTime) / dTime; // Ratio of the current time to the next pose time
    double rltc =
        (currentTime - timePrevious)
            / dTime; // Ratio of the previous time to the current pose
    // time

    // Current pose is made from the weighted average of the x, y, and angle values
    double x =
        (pdPrevious.getCenterPose().getX() * rctn) + (pdNext.getCenterPose().getX() * rltc);
    double y =
        (pdPrevious.getCenterPose().getY() * rctn) + (pdNext.getCenterPose().getY() * rltc);

    double dx = x - positionVector.x;
    double dy = y - positionVector.y;

    positionVector.set((float) x, (float) y);

//    System.out.println(String.format("%f %f %f", timePrevious, currentTime, timeNext));
    velocityVector
        .set((float) (dx / (FRAME_INTERVAL / 1000)), (float) (dy / (FRAME_INTERVAL / 1000)));
  }
}
