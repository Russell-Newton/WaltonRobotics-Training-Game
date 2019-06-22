package Utilities;

import static Utilities.StaticUtilities.defaultObstacleFill;
import static Utilities.StaticUtilities.toJB2DAngle;
import static Utilities.StaticUtilities.toJFXAngle;
import static Utilities.StaticUtilities.toPixelHeight;
import static Utilities.StaticUtilities.toPixelPosX;
import static Utilities.StaticUtilities.toPixelPosY;
import static Utilities.StaticUtilities.toPixelWidth;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

/**
 * @author Russell Newton
 **/
public class Obstacle {

  protected final GameController controller;
  public float startX;
  public float startY;
  public float width;
  public float height;
  public float angle;
  protected Body body;
  Rectangle screenMask;
  private BodyType bodyType;

  /**
   * Create a new {@code obstacle}.
   *
   * @param controller - the controller this {@code Obstacle} belongs to.
   * @param startX - the starting x coordinate of the corresponding physics body.
   * @param startY - the starting y coordinate of the corresponding physics body.
   * @param width - the width of the corresponding physics body.
   * @param height - the height of the corresponding physics body.
   * @param angle - the starting angle of the corresponding physics body..
   * @param fill - the fill of the corresponding screen mask.
   * @param bodyType - the {@code BodyType} of the corresponding physics body.
   */
  public Obstacle(GameController controller, float startX, float startY, float width,
      float height, float angle, Paint fill, BodyType bodyType) {
    this.startX = startX;
    this.startY = startY;
    this.width = width;
    this.height = height;
    this.angle = angle;

    this.controller = controller;

    this.bodyType = bodyType;

    initBody();

    screenMask = new Rectangle(toPixelWidth(width), toPixelHeight(height), fill);
    screenMask.setUserData(body);
//    screenMask.setStroke(Color.BLACK);
    updateScreenMask();
    controller.addToScreen(screenMask);
  }

  /**
   * Create a new {@code obstacle} with the a {@code KINEMATIC BodyType}.
   *
   * @param controller - the controller this {@code Obstacle} belongs to.
   * @param startX - the starting x coordinate of the corresponding physics body.
   * @param startY - the starting y coordinate of the corresponding physics body.
   * @param width - the width of the corresponding physics body.
   * @param height - the height of the corresponding physics body.
   * @param angle - the starting angle of the corresponding physics body..
   * @param fill - the fill of the corresponding screenMask.
   */
  public Obstacle(GameController controller, float startX, float startY, float width,
      float height, float angle, Paint fill) {
    this(controller, startX, startY, width, height, angle, fill, BodyType.KINEMATIC);
  }

  /**
   * Create a new {@code obstacle} with the a {@code KINEMATIC BodyType} and default fill.
   *
   * @param controller - the controller this {@code Obstacle} belongs to.
   * @param startX - the starting x coordinate of the corresponding physics body.
   * @param startY - the starting y coordinate of the corresponding physics body.
   * @param width - the width of the corresponding physics body.
   * @param height - the height of the corresponding physics body.
   * @param angle - the starting angle of the corresponding physics body..
   */
  public Obstacle(GameController controller, float startX, float startY, float width,
      float height, float angle) {
    this(controller, startX, startY, width, height, angle, defaultObstacleFill, BodyType.KINEMATIC);
  }

  /**
   * @return this {@code Obstacle's} screenMask.
   */
  public Rectangle getScreenMask() {
    return screenMask;
  }

  /**
   * Initializes this {@code Obstacle's} corresponding physics body.
   */
  protected void initBody() {
    BodyDef bd = new BodyDef();
    bd.position.set(startX, startY);
    bd.type = bodyType;

    PolygonShape ps = new PolygonShape();
    ps.set(new Vec2[]{
        new Vec2(0, 0), new Vec2(width, 0),
        new Vec2(width, -height), new Vec2(0, -height)
    }, 4);

    FixtureDef fd = new FixtureDef();
    fd.shape = ps;

    body = controller.world.createBody(bd);
    body.createFixture(fd);
    float rotation = toJB2DAngle(angle);
    while (Math.abs(body.getAngle() - rotation) >= StaticUtilities.angleRotationTolerance) {

      float c = 1; //speed of rotation
      float q = rotation - body.getAngle();
      body.setAngularVelocity(c * q);
      controller.stepWorld();
    }
    body.setAngularVelocity(0);
  }

  /**
   * Updates this {@code Obstacle's} screenMask to match its physics body.
   */
  protected void updateScreenMask() {

    //Remove previous rotation
    screenMask.getTransforms().clear();

    //Set layout box translation and rotation
    screenMask.getTransforms().add(new Rotate(toJFXAngle(body.getAngle()), 0,
        screenMask.getHeight()));
    screenMask.setLayoutX(toPixelPosX(body.getPosition().x));
    screenMask.setLayoutY(toPixelPosY(body.getPosition().y));

    //Set translation fields to keep rotation based on bottom left corner
    Vec2 dCorner = originPointShift(body.getAngle());
    screenMask.setTranslateX(-dCorner.x);
    screenMask.setTranslateY(-dCorner.y);

    System.out.println(toString());
  }

  /**
   * Calculates the required screenMask shift to sync up JavaFX and JBox2D rotations.
   *
   * @param angle - the rotation angle of the screenMask, in radians.
   * @return a {@code Vec2} corresponding to the required shift.
   */
  private Vec2 originPointShift(float angle) {
    //Find location of bottom left corner after rotation
    double xPrime = -screenMask.getHeight() * Math.sin(angle);
    double yPrime = screenMask.getHeight() * Math.cos(angle);

    float dX = (float) xPrime;
    float dY = (float) (yPrime - screenMask.getHeight());

    return new Vec2(dX, dY);
  }

  @Override
  public String toString() {
    StringBuilder toString = new StringBuilder(String.format("Obstacle:[%n  ScreenMask:[x:%f, "
            + "y:%f, width:%f, height:%f],%n  WorldBody:[x:%f, y:%f, angle:%f, Shape:[%n",
        screenMask.getLayoutX(), screenMask.getLayoutY(), screenMask.getWidth(),
        screenMask.getHeight(), body.getPosition().x, body.getPosition().y, body.getAngle()));
    for (Vec2 vertex : ((PolygonShape) body.getFixtureList().m_shape).m_vertices) {
      toString.append(String.format("    Vertex:[%f, %f]%n", vertex.x, vertex.y));
    }
    toString.append("  ]\n]");
    return toString.toString();
  }
}
