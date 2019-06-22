package Utilities;

import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;

/**
 * A bunch of static constants and methods used in the application.
 *
 * @author Russell Newton
 **/
public class StaticUtilities {

  public static final Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

  //Screen constants
  public static final float WIDTH = (float) primaryScreenBounds.getWidth();
  public static final float HEIGHT = (float) primaryScreenBounds.getHeight();
  public static final float JB_TO_JFX_FACTOR = 10.0f;   //Conversion factor from JBox2D to JavaFX

  //Physics engine constants
  public static final float GRAVITY_ACCELERATION = -9.8f;
  //Obstacle constants
  public static final float angleRotationTolerance = 0.0001f;
  public static final Paint defaultObstacleFill = Color.RED;
  //Player sprite constants
  public static final float defaultPlayerHeight = 5f;
  public static final float defaultPlayerWidth = 15f;
  public static final float defaultPlayerAngle = 0f;
  public static final float imageSpriteScale = 0.1f;
  public static final float defaultPlayerStartX = 48f;
  public static final float defaultPlayerStartY = 75f;
  public static final Paint defaultPlayerFill = Color.BLUE;
  //Player motion constants
  public static final double WALK_SPEED = 1.5;
  public static final double RUN_SPEED = 2.25;
  public static final double FRICTION_COEFFICIENT = 0.4;
  public static final double MASS = 2;
  public static final double WEIGHT = MASS * GRAVITY_ACCELERATION;
  public static final double JUMP_STRENGTH = 2.25;
  static final double FRAME_INTERVAL = 5.0;
  static final int VELOCITY_ITERATIONS = 5;
  static final int POSITION_ITERATIONS = 3;

  /**
   * Convert a JBox2D x coordinate to a JavaFX pixel x coordinate.
   *
   * @param posX - JBox2D x coordinate.
   * @return JavaFX x coordinate.
   */
  public static float toPixelPosX(float posX) {
    float x = posX * JB_TO_JFX_FACTOR;
    return x;
  }

  /**
   * Convert a JavaFX pixel x coordinate to a JBox2D x coordinate.
   *
   * @param posX - JavaFX x coordinate.
   * @return JBox2D x coordinate.
   */
  public static float toPosX(float posX) {
    float x = posX / JB_TO_JFX_FACTOR;
    return x;
  }

  /**
   * Convert a JBox2D y coordinate to a JavaFX pixel y coordinate.
   *
   * @param posY - JBox2D y coordinate.
   * @return JavaFX y coordinate.
   */
  public static float toPixelPosY(float posY) {
    float y = HEIGHT - posY * JB_TO_JFX_FACTOR;
    return y;
  }

  /**
   * Convert a JavaFX pixel y coordinate to a JBox2D y coordinate.
   *
   * @param posY - JavaFX y coordinate.
   * @return JBox2D y coordinate.
   */
  public static float toPosY(float posY) {
    float y = (HEIGHT - posY) / JB_TO_JFX_FACTOR;
    return y;
  }

  /**
   * Convert a JBox2D width to pixel width.
   *
   * @param width - JBox2D width.
   * @return JavaFX width.
   */
  public static float toPixelWidth(float width) {
    return width * JB_TO_JFX_FACTOR;
  }

  /**
   * Convert a JBox2D height to pixel height.
   *
   * @param height - JBox2D height.
   * @return JavaFX height.
   */
  public static float toPixelHeight(float height) {
    return height * JB_TO_JFX_FACTOR;
  }

  /**
   * Convert a JBox2D angle to JavaFX angle.
   *
   * @param angle - JBox2D angle (radians).
   * @return JavaFX angle (-degrees).
   */
  public static float toJFXAngle(float angle) {
    return (float) -Math.toDegrees(angle);
  }

  /**
   * Convert a JavaFX angle to JBox2D angle.
   *
   * @param angle - JavaFX angle (-degrees).
   * @return JBox2D angle (radians).
   */
  public static float toJB2DAngle(float angle) {
    return (float) -Math.toRadians(angle);
  }

}
