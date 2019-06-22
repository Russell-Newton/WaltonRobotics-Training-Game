package Utilities;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

/**
 * @author Russell Newton
 **/
public class Constants {

  public static final Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

  //World constants
  public static final float WIDTH = (float) primaryScreenBounds.getWidth();
  public static final float HEIGHT = (float) primaryScreenBounds.getHeight();
  public static final float GRAVITY_ACCELERATION = -9.8f;

  //Player constants
  public static final double WALK_SPEED = 1.5;
  public static final double RUN_SPEED = 2.25;
  public static final double WALK_ACCELERATION = 4;
  public static final double FRICTION_COEFFICIENT = 0.4;
  public static final double MASS = 2;
  public static final double WEIGHT = MASS * GRAVITY_ACCELERATION;
  public static final double JUMP_STRENGTH = 2.25;

  public static final double FRAME_INTERVAL = 5.0;
  public static final int VELOCITY_ITERATIONS = 5;
  public static final int POSITION_ITERATIONS = 3;

  //Convert a JBox2D x coordinate to a JavaFX pixel x coordinate
  public static float toPixelPosX(float posX) {
//    float x = WIDTH * posX / 100.0f;
    float x = posX * 10.0f;
    return x;
  }

  //Convert a JavaFX pixel x coordinate to a JBox2D x coordinate
  public static float toPosX(float posX) {
    float x = (posX * 100.0f * 1.0f) / WIDTH;
    return x;
  }

  //Convert a JBox2D y coordinate to a JavaFX pixel y coordinate
  public static float toPixelPosY(float posY) {
//    float y = HEIGHT - (1.0f * HEIGHT) * posY / 100.0f;
    float y = HEIGHT - posY * 10;
    return y;
  }

  //Convert a JavaFX pixel y coordinate to a JBox2D y coordinate
  public static float toPosY(float posY) {
    float y = 100.0f - ((posY * 100 * 1.0f) / HEIGHT);
    return y;
  }

  //Convert a JBox2D width to pixel width
  public static float toPixelWidth(float width) {
//    return WIDTH * width / 100.0f;
    return width * 10.0f;
  }

  //Convert a JBox2D height to pixel height
  public static float toPixelHeight(float height) {
//    return HEIGHT * height / 100.0f;
    return height * 10.0f;
  }

  //Convert a JBox2D angle to JavaFX angle
  public static float toJFXAngle(float angle) {
    return (float) -Math.toDegrees(angle);
  }

  //Convert a JavaFX angle to JBox2D angle
  public static float toJB2DAngle(float angle) {
    return (float) -Math.toRadians(angle);
  }

}
