package Utilities;

import static Utilities.Constants.toJB2DAngle;
import static Utilities.Constants.toJFXAngle;
import static Utilities.Constants.toPixelHeight;
import static Utilities.Constants.toPixelPosX;
import static Utilities.Constants.toPixelPosY;
import static Utilities.Constants.toPixelWidth;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javax.annotation.Nullable;
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
  //Scaled by screen units
  public float startX;
  public float startY;
  public float width;
  public float height;
  public float angle;
  protected Body body;
  Rectangle screenMask;
  private float angleRotationTolerance = 0.0001f;

  public Obstacle(GameController controller, float startX, float startY, float width,
      float height, float angle, @Nullable Paint fill) {
    this.startX = startX;
    this.startY = startY;
    this.width = width;
    this.height = height;
    this.angle = angle;

    this.controller = controller;

    initBody(startX, startY, width, height);

    screenMask = new Rectangle(toPixelWidth(width), toPixelHeight(height), fill);
    screenMask.setUserData(body);
//    screenMask.setStroke(Color.BLACK);
    updateScreenMask();
  }

  public Rectangle getScreenMask() {
    return screenMask;
  }

  protected void initBody(float startX, float startY, float width, float height) {
    BodyDef bd = new BodyDef();
    bd.position.set(startX, startY);
    bd.type = BodyType.KINEMATIC;

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
    while(Math.abs(body.getAngle() - rotation) >= angleRotationTolerance) {

      float c = 1; //speed of rotation
      float q = rotation - body.getAngle();
      body.setAngularVelocity(c * q);
      controller.stepWorld();
    }
    body.setAngularVelocity(0);

//    updateScreenMask();
  }

  protected void updateScreenMask() {

    screenMask.getTransforms().clear();

    screenMask.getTransforms().add(new Rotate(toJFXAngle(body.getAngle()), 0,
        screenMask.getHeight()));

    screenMask.setLayoutX(toPixelPosX(body.getPosition().x));
    screenMask.setLayoutY(toPixelPosY(body.getPosition().y));
    Vec2 dCorner = originPointShift(body.getAngle());
    screenMask.setTranslateX(-dCorner.x);
    screenMask.setTranslateY(-dCorner.y);
//    float dX = (float) (x - screenMask.getBoundsInParent().getMinX());
//    float dY = (float) (y - screenMask.getBoundsInParent().getMinY());
//    screenMask.getTransforms().add(new Translate(-toPixelPosX(dCorner.x), -toPixelPosY(dCorner.y)));
//    screenMask.setLayoutX(x + dX);
//    screenMask.setLayoutY(y + dY);

    System.out.println(toString());
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

  /**
   *
   * @param angle - the rotation angle of the screenMask, in radians.
   * @return
   */
  private Vec2 originPointShift(float angle) {
    double xPrime = -screenMask.getHeight() * Math.sin(angle);
    double yPrime = screenMask.getHeight() * Math.cos(angle);

    float dX = (float) xPrime;
    float dY = (float) (yPrime - screenMask.getHeight());

    return new Vec2(dX, dY);
  }
}
