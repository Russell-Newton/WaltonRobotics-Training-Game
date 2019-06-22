package Utilities;

import static Utilities.Constants.toJFXAngle;
import static Utilities.Constants.toPixelPosX;
import static Utilities.Constants.toPixelPosY;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 * @author Russell Newton
 **/
public class Player extends Obstacle {

  private static float defaultHeight = 5f;
  private static float defaultWidth = 15f;
  private static float defaultAngle = 0f;

  public Player(GameController controller, Image sprite, float startX, float startY) {
    super(controller, startX, startY, (float) sprite.getWidth(),
        (float) sprite.getHeight(), defaultAngle, new ImagePattern(sprite));

    initBody(startX, startY, (float) (sprite.getWidth()),
        (float) (sprite.getHeight()));
  }

  public Player(GameController controller, Image sprite) {
    this(controller, sprite, 0, 0);
  }

  public Player(GameController controller, float startX, float startY) {
    super(controller, startX, startY, defaultWidth, defaultHeight, defaultAngle, Color.BLUE);

//    initBody((float) startX, (float) startY, defaultWidth, defaultHeight);
  }

  public Player(GameController controller) {
    this(controller, 0, 0);
  }

  public Player(GameController controller, String spritePath) {
    this(controller, new Image(spritePath));
  }

  @Override
  protected void initBody(float startX, float startY, float width, float height) {
    BodyDef bd = new BodyDef();
    bd.position.set(startX, startY);
    bd.type = BodyType.DYNAMIC;

    PolygonShape ps = new PolygonShape();
    ps.set(new Vec2[]{
        new Vec2(0, 0), new Vec2(width, 0),
        new Vec2(width, -height), new Vec2(0, -height)
    }, 4);
//    ps.setAsBox(width, height);

    FixtureDef fd = new FixtureDef();
    fd.shape = ps;
    fd.friction = 0.3f;
    fd.restitution = 0f;
    fd.density = 1f;

    body = controller.world.createBody(bd);
    body.createFixture(fd);

//    updateScreenMask();
  }

  public void update() {
    updateScreenMask();
  }

//  protected void updateScreenMask() {
//    screenMask.setLayoutX(toPixelPosX(body.getPosition().x));
//    screenMask.setLayoutY(toPixelPosY(body.getPosition().y));
//    screenMask.setRotate(toJFXAngle(body.getAngle()));
//    System.out.println(toString());
//  }


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
