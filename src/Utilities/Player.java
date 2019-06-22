package Utilities;

import static Utilities.StaticUtilities.defaultPlayerAngle;
import static Utilities.StaticUtilities.defaultPlayerFill;
import static Utilities.StaticUtilities.defaultPlayerHeight;
import static Utilities.StaticUtilities.defaultPlayerStartX;
import static Utilities.StaticUtilities.defaultPlayerStartY;
import static Utilities.StaticUtilities.defaultPlayerWidth;
import static Utilities.StaticUtilities.imageSpriteScale;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;

/**
 * @author Russell Newton
 **/
public class Player extends Obstacle {

  /**
   * Creates a player with an {@code Image} as a sprite.
   *
   * @param controller - the controller this {@code Player} belongs to.
   * @param sprite - the {@code Image} to be used as a sprite.
   * @param startX - the starting x coordinate of the corresponding physics body.
   * @param startY - the starting y coordinate of the corresponding physics body.
   */
  public Player(GameController controller, Image sprite, float startX, float startY) {
    super(controller, startX, startY, (float) sprite.getWidth() * imageSpriteScale,
        (float) sprite.getHeight() * imageSpriteScale, defaultPlayerAngle,
        new ImagePattern(sprite), BodyType.DYNAMIC);
  }

  /**
   * Creates a player with an {@code Image} as a sprite at the default starting location.
   *
   * @param controller - the controller this {@code Player} belongs to.
   * @param sprite - the {@code Image} to be used as a sprite.
   */
  public Player(GameController controller, Image sprite) {
    this(controller, sprite, defaultPlayerStartX, defaultPlayerStartY);
  }

  /**
   * Creates a player with a sprite from a file path.
   *
   * @param controller - the controller this {@code Player} belongs to.
   * @param spritePath - the path to the desired sprite image.
   * @param startX - the starting x coordinate of the corresponding physics body.
   * @param startY - the starting y coordinate of the corresponding physics body.
   */
  public Player(GameController controller, String spritePath, float startX, float startY) {
    this(controller, new Image(spritePath), startX, startY);
  }

  /**
   * Creates a player with a sprite from a file path at the default starting location.
   *
   * @param controller - the controller this {@code Player} belongs to.
   * @param spritePath - the path to the desired sprite image.
   */
  public Player(GameController controller, String spritePath) {
    this(controller, new Image(spritePath));
  }

  /**
   * Creates a player with the default fill.
   *
   * @param controller - the controller this {@code Player} belongs to.
   * @param startX - the starting x coordinate of the corresponding physics body.
   * @param startY - the starting y coordinate of the corresponding physics body.
   */
  public Player(GameController controller, float startX, float startY) {
    super(controller, startX, startY, defaultPlayerWidth, defaultPlayerHeight, defaultPlayerAngle,
        defaultPlayerFill, BodyType.DYNAMIC);
  }

  /**
   * Creates a player with the default fill at the default location.
   *
   * @param controller - the controller this {@code Player} belongs to.
   */
  public Player(GameController controller) {
    this(controller, defaultPlayerStartX, defaultPlayerStartY);
  }

  /**
   * Updates this {@code Player's} screenMask to its physics body. This is run every execution
   * cycle.
   */
  public void update() {
    updateScreenMask();
  }

  @Override
  public String toString() {
    StringBuilder toString = new StringBuilder(String.format("Player:[%n  ScreenMask:[x:%f, y:%f, "
            + "width:%f, height:%f],%n  WorldBody:[x:%f, y:%f, angle:%f, Shape:[%n",
        screenMask.getLayoutX(), screenMask.getLayoutY(), screenMask.getWidth(),
        screenMask.getHeight(), body.getPosition().x, body.getPosition().y, body.getAngle()));
    for (Vec2 vertex : ((PolygonShape) body.getFixtureList().m_shape).m_vertices) {
      toString.append(String.format("    Vertex:[%f, %f]%n", vertex.x, vertex.y));
    }
    toString.append("  ]\n]");
    return toString.toString();
  }
}
