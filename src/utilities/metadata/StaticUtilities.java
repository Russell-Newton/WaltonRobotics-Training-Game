package utilities.metadata;

import utilities.SceneController;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import org.jbox2d.common.Vec2;

/**
 * A bunch of static constants and methods used in the application.
 *
 * @author Russell Newton
 **/
public class StaticUtilities {

  public static final Rectangle2D PRIMARY_SCREEN_BOUNDS = Screen.getPrimary().getVisualBounds();
  public static final float WIDTH = (float) PRIMARY_SCREEN_BOUNDS.getWidth();
  public static final float HEIGHT = (float) PRIMARY_SCREEN_BOUNDS.getHeight();
  public static final float JB_TO_JFX_FACTOR = 10.0f;   //Conversion factor from JBox2D to JavaFX
  //Physics engine constants
  public static final float GRAVITY_ACCELERATION = -9.8f;
  public static final double FRAME_INTERVAL = 5.0;
  public static final int VELOCITY_ITERATIONS = 5;
  public static final int POSITION_ITERATIONS = 3;
  //Obstacle constants
  public static final float ANGLE_ROTATION_TOLERANCE = 0.0001f;
  public static final Paint DEFAULT_OBSTACLE_FILL = Color.RED;
  public static final float DEFAULT_OBSTACLE_MASS = 1;
  public static final float DEFAULT_OBSTACLE_FRICTION = 1;
  public static final float DEFAULT_OBSTACLE_RESTITUTION = 0;
  //Player sprite constants
  public static final float DEFAULT_PLAYER_HEIGHT = 5f;
  public static final float DEFAULT_PLAYER_WIDTH = 15f;
  public static final float DEFAULT_PLAYER_ANGLE = 0f;
  public static final float IMAGE_SPRITE_SCALE = 0.1f;
  public static final float DEFAULT_PLAYER_START_X = 48f;
  public static final float DEFAULT_PLAYER_START_Y = 75f;
  public static final Paint DEFAULT_PLAYER_FILL = Color.BLUE;
  //Player motion constants
  public static final float DEFAULT_PLAYER_MASS = 1;
  public static final float DEFAULT_PLAYER_FRICTION = 1;
  public static final float DEFAULT_PLAYER_RESTITUTION = 0;
  public static final Vec2 JUMP_VECTOR = new Vec2(0, 10f);
  public static final Vec2 WALK_VECTOR = new Vec2(5f, 0);
  public static final Vec2 RUN_VECTOR = new Vec2(10f, 0);
  public static final int JUMP_COUNT = 2;
  //Screen constants
  public static Scene scene = new Scene(new AnchorPane());
  public static SceneController sceneController = new SceneController(scene);

  /**
   * Convert a JBox2D x coordinate to a JavaFX pixel x coordinate.
   *
   * @param posX JBox2D x coordinate.
   * @return JavaFX x coordinate.
   */
  public static float toPixelPosX(float posX) {
    float x = posX * JB_TO_JFX_FACTOR;
    return x;
  }

  /**
   * Convert a JavaFX pixel x coordinate to a JBox2D x coordinate.
   *
   * @param posX JavaFX x coordinate.
   * @return JBox2D x coordinate.
   */
  public static float toPosX(float posX) {
    float x = posX / JB_TO_JFX_FACTOR;
    return x;
  }

  /**
   * Convert a JBox2D y coordinate to a JavaFX pixel y coordinate.
   *
   * @param posY JBox2D y coordinate.
   * @return JavaFX y coordinate.
   */
  public static float toPixelPosY(float posY) {
    float y = HEIGHT - posY * JB_TO_JFX_FACTOR;
    return y;
  }

  /**
   * Convert a JavaFX pixel y coordinate to a JBox2D y coordinate.
   *
   * @param posY JavaFX y coordinate.
   * @return JBox2D y coordinate.
   */
  public static float toPosY(float posY) {
    float y = (HEIGHT - posY) / JB_TO_JFX_FACTOR;
    return y;
  }

  /**
   * Convert a JBox2D width to pixel width.
   *
   * @param width JBox2D width.
   * @return JavaFX width.
   */
  public static float toPixelWidth(float width) {
    return width * JB_TO_JFX_FACTOR;
  }

  /**
   * Convert a JBox2D height to pixel height.
   *
   * @param height JBox2D height.
   * @return JavaFX height.
   */
  public static float toPixelHeight(float height) {
    return height * JB_TO_JFX_FACTOR;
  }

  /**
   * Convert a JBox2D angle to JavaFX angle.
   *
   * @param angle JBox2D angle (radians).
   * @return JavaFX angle (-degrees).
   */
  public static float toJFXAngle(float angle) {
    return (float) -Math.toDegrees(angle);
  }

  /**
   * Convert a JavaFX angle to JBox2D angle.
   *
   * @param angle JavaFX angle (-degrees).
   * @return JBox2D angle (radians).
   */
  public static float toJB2DAngle(float angle) {
    return (float) -Math.toRadians(angle);
  }

}
