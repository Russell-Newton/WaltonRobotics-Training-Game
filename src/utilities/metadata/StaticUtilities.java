package utilities.metadata;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.stage.Screen;
import org.jbox2d.common.Vec2;
import utilities.SceneController;

/**
 * A bunch of static constants and methods used in the application.
 *
 * @author Russell Newton
 **/
public class StaticUtilities {

  //Physics engine constants
  public static final float GRAVITY_ACCELERATION = -50.0f;  //-9.8 is crazy slow what heck
  public static final double FRAME_INTERVAL = 5.0;
  public static final int VELOCITY_ITERATIONS = 5;
  public static final int POSITION_ITERATIONS = 3;
  //
  //Obstacle constants
  //
  public static final float ANGLE_ROTATION_TOLERANCE = 0.0001f;
  public static final Paint DEFAULT_OBSTACLE_FILL = Color.BLUE;
  public static final float DEFAULT_OBSTACLE_MASS = 1;
  public static final float DEFAULT_OBSTACLE_FRICTION = 10;
  public static final float DEFAULT_OBSTACLE_RESTITUTION = 0;
  public static final float SIDE_SENSOR_OFFSET = 0.1f;
  public static final float DEFAULT_KINEMATIC_OBSTACLE_SPEED = 1;
  public static final Scale FLIP_HORIZONTAL = Transform.scale(-1, 1);
  //
  //Player constants
  //
  public static final float DEFAULT_PLAYER_HEIGHT = 8f;
  public static final float DEFAULT_PLAYER_WIDTH = 5f;
  public static final float DEFAULT_PLAYER_ANGLE = 0f;    //Changing this will do nothing
  public static final float DEFAULT_PLAYER_START_X = 48f;
  public static final float DEFAULT_PLAYER_START_Y = 75f;
  public static final Paint DEFAULT_PLAYER_FILL = Color.BLUE;
  public static final float DEFAULT_PLAYER_MASS = 1;
  public static final float DEFAULT_PLAYER_FRICTION = 10;
  public static final float DEFAULT_PLAYER_RESTITUTION = 0;
  public static final Vec2 JUMP_VECTOR = new Vec2(0, 30f);
  public static final Vec2 WALK_VECTOR = new Vec2(15f, 0);
  public static final Vec2 RUN_VECTOR = new Vec2(30f, 0);
  public static final int JUMP_COUNT = 4;
  public static final boolean STOP_HORIZONTAL_MOTION_ON_KEY_RELEASE = true;
  public static final float DEFAULT_PLAYER_SPRITE_SCALE = 8f;
  //
  //Screen constants
  //
  public static final Rectangle2D PRIMARY_SCREEN_BOUNDS = Screen.getPrimary().getVisualBounds();
  public static final Paint DEFAULT_BACKGROUND = Color.LIGHTBLUE;
  private static final float PRIMARY_SCREEN_BOUNDS_WIDTH = (float) PRIMARY_SCREEN_BOUNDS.getWidth();
  private static final float PRIMARY_SCREEN_BOUNDS_HEIGHT =
      (float) PRIMARY_SCREEN_BOUNDS.getHeight();
  private static final float JB_TO_JFX_FACTOR = 10.0f;   //Conversion factor from JBox2D to JavaFX
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
    float y = PRIMARY_SCREEN_BOUNDS_HEIGHT - posY * JB_TO_JFX_FACTOR;
    return y;
  }

  /**
   * Convert a JavaFX pixel y coordinate to a JBox2D y coordinate.
   *
   * @param posY JavaFX y coordinate.
   * @return JBox2D y coordinate.
   */
  public static float toPosY(float posY) {
    float y = (PRIMARY_SCREEN_BOUNDS_HEIGHT - posY) / JB_TO_JFX_FACTOR;
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

  public static float getWidthFromSprite(Paint sprite) {
    ImagePattern spriteImagePattern = (ImagePattern) sprite;
    return (float) (spriteImagePattern.getWidth() * DEFAULT_PLAYER_SPRITE_SCALE);
  }

  public static float getHeightFromSprite(Paint sprite) {
    ImagePattern spriteImagePattern = (ImagePattern) sprite;
    return (float) (spriteImagePattern.getHeight() * DEFAULT_PLAYER_SPRITE_SCALE);
  }

  /**
   * Load an {@code ImagePattern} from a {@code String}.<br>It will first try to load the image
   * directly from the passed path. Upon failure, it will look for the image in /assets/sprites.
   * Upon failure, it will look for the image with a ".png" ending in /assets/sprites. Upon failure,
   * it will return the default obstacle fill.
   *
   * @param stretch whether or not to stretch the image to fit the JavaFX {@code Node}. If this is
   * false, the image will tile. Defaults to false.
   */
  public static Paint getFillFromString(String string, boolean stretch) {
    Image image = null;
    Paint sprite;

    // Try from URL or Path
    try {
      System.out.print("Attempting to load sprite from URL or path... ");
      image = new Image(string);
      System.out.println("Success!");
    } catch (Exception e) {
      System.out.println("Failed.");
    }

    if (image == null) {
      // Try from name within sprites assets folder
      try {
        System.out.print("Attempting to load sprite from sprites assets folder... ");
        image = new Image("/assets/sprites/" + string);
        System.out.println("Success!");
      } catch (Exception e) {
        System.out.println("Failed.");
      }
    }

    if (image == null) {
      // Try from name within sprites assets folder
      try {
        System.out.print("Attempting to load sprite from sprites assets folder with \".png\"... ");
        image = new Image("/assets/sprites/" + string + ".png");
        System.out.println("Success!");
      } catch (Exception e) {
        System.out.println("Failed.");
      }
    }

    if (image == null) {
      System.out.println("Setting to default fill.");
      sprite = DEFAULT_OBSTACLE_FILL;
      return sprite;
    } else if (stretch) {
      sprite = new ImagePattern(image);
      return sprite;
    } else {
      sprite = new ImagePattern(image, 0, 0, image.getWidth(), image.getHeight(), false);
      return sprite;
    }
  }

  public static Paint getFillFromString(String string) {
    return getFillFromString(string, false);
  }

}
