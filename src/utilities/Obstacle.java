package utilities;

import static utilities.metadata.StaticUtilities.DEFAULT_OBSTACLE_FILL;
import static utilities.metadata.StaticUtilities.DEFAULT_OBSTACLE_FRICTION;
import static utilities.metadata.StaticUtilities.DEFAULT_OBSTACLE_MASS;
import static utilities.metadata.StaticUtilities.DEFAULT_OBSTACLE_RESTITUTION;
import static utilities.metadata.StaticUtilities.FLIP_HORIZONTAL;
import static utilities.metadata.StaticUtilities.SIDE_SENSOR_OFFSET;
import static utilities.metadata.StaticUtilities.getFillFromString;
import static utilities.metadata.StaticUtilities.toJB2DAngle;
import static utilities.metadata.StaticUtilities.toJFXAngle;
import static utilities.metadata.StaticUtilities.toPixelHeight;
import static utilities.metadata.StaticUtilities.toPixelPosX;
import static utilities.metadata.StaticUtilities.toPixelPosY;
import static utilities.metadata.StaticUtilities.toPixelWidth;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import utilities.metadata.StaticUtilities;
import utilities.metadata.UserData;

/**
 * {@code Obstacles} are the non-{@code Player} bodies in the application.
 *
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
  protected Vec2 centerOfMass;
  protected Paint fill;
  Rectangle screenMask;
  private BodyType bodyType;

  /**
   * Create a new {@code obstacle}.
   *
   * @param controller the controller this {@code Obstacle} belongs to.
   * @param startX the starting x coordinate of the corresponding physics body.
   * @param startY the starting y coordinate of the corresponding physics body.
   * @param width the width of the corresponding physics body.
   * @param height the height of the corresponding physics body.
   * @param angle the starting angle of the corresponding physics body. Because it's not working
   * right, this is overridden to 0.
   * @param fill the fill of the corresponding screen mask.
   * @param bodyType the {@code BodyType} of the corresponding physics body.
   */
  public Obstacle(GameController controller, float startX, float startY, float width,
      float height, float angle, Paint fill, BodyType bodyType) {
    this.startX = startX;
    this.startY = startY;
    this.width = width;
    this.height = height;
    this.angle = /*angle*/ 0; //TODO

    this.controller = controller;

    this.bodyType = bodyType;

    this.centerOfMass = new Vec2(width / 2, height / 2);

    this.fill = fill;
  }

  /**
   * Create a new {@code Obstacle} with the recommended {@code KINEMATIC BodyType}.
   */
  public Obstacle(GameController controller, float startX, float startY, float width,
      float height, float angle, Paint fill) {
    this(controller, startX, startY, width, height, angle, fill, BodyType.KINEMATIC);
  }

  /**
   * Create a new {@code Obstacle} with the recommended {@code KINEMATIC BodyType} and default
   * fill.
   */
  public Obstacle(GameController controller, float startX, float startY, float width,
      float height, float angle) {
    this(controller, startX, startY, width, height, angle, DEFAULT_OBSTACLE_FILL,
        BodyType.KINEMATIC);
  }

  /**
   * Create a new {@code Object} from a {@code String}. It should be formatted as follows:<br><br>
   * "X:nnn,Y:nnn,W:nnn,H:nnn,A:nnn,P:nnn", <br><br> where each capital letter denotes {@code
   * Obstacle} parameters:<br> X: = startX<br> Y: = startY<br> W: = width<br> H: = height<br> A: =
   * angle (degrees)  --  This is not working like it should right now and will be overridden to be
   * 0.<br> P: = file or web location of the sprite (If an error in this step occurs, the color will
   * be the default Color).
   *
   * @param controller the controller this {@code Obstacle} belongs.
   * @param paramString a string containing the above format to create the {@code Obstacle} from.
   * @param doPrint whether or not to print out the debug string.
   */
  public static Obstacle fromString(GameController controller, String paramString,
      boolean doPrint) {
    if (doPrint) {
      System.out.println("Creating obstacle from: " + paramString);
    }
    String[] parameters = paramString.split(",?[XYWHAP]:");
    try {
      float startX = Float.parseFloat(parameters[1]);
      float startY = Float.parseFloat(parameters[2]);
      float width = Float.parseFloat(parameters[3]);
      float height = Float.parseFloat(parameters[4]);
      float angle = /*Float.parseFloat(parameters[5])*/ 0;
      Paint paint = DEFAULT_OBSTACLE_FILL;
      if (parameters.length > 6) {
        paint = getFillFromString(parameters[6]);
      }
      return new Obstacle(controller, startX, startY, width, height, angle, paint);
    } catch (NumberFormatException | IndexOutOfBoundsException | NullPointerException e) {
      System.out.println("The input string was not in the correct format.");
      e.printStackTrace();
      return null;
    }
  }

  public static Obstacle fromString(GameController controller, String paramString) {
    return fromString(controller, paramString, true);
  }

  public void initialize() {
    initBody();
    createWorldContactOperations();

    screenMask = new Rectangle(toPixelWidth(width), toPixelHeight(height), fill);
    screenMask.setUserData(body);
    updateScreenMask();
    controller.addToScreen(screenMask);
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
    body.setUserData(new UserData().addUserData("obstacle", this));
    setFixtureData();

    //TODO this still doesn't work right
    //Apply starting rotation
    float rotation = toJB2DAngle(angle);
    while (Math.abs(body.getAngle() - rotation) >= StaticUtilities.ANGLE_ROTATION_TOLERANCE) {

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

    boolean replaceFlipHorizontal = false;
    if (screenMask.getTransforms().contains(FLIP_HORIZONTAL)) {
      replaceFlipHorizontal = true;
    }

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

    if (replaceFlipHorizontal) {
      screenMask.getTransforms().add(FLIP_HORIZONTAL);
      screenMask.getTransforms().add(Transform.translate(-screenMask.getWidth(), 0));
    }

//    System.out.println(toString());
  }

  /**
   * Calculates the required screenMask shift to sync up JavaFX and JBox2D rotations.
   *
   * @param angle the rotation angle of the screenMask, in radians.
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

  /**
   * This method is run as a part of the {@code initBody()} method. Place {@code Fixture} field
   * parameters and any new {@code Fixtures} in here. Override this method in any subclasses. The
   * default method code is as follows: <br><br> {@code Fixture primaryFixture =
   * body.getFixtureList();} <br> {@code primaryFixture.setUserData(new
   * UserData().addUserData("obstacleType", "default"));}<br> {@code primaryFixture.setDensity(DEFAULT_OBSTACLE_MASS
   * / (width * height));}
   * <br> {@code primaryFixture.setFriction(DEFAULT_OBSTACLE_FRICTION);} <br> {@code
   * primaryFixture.setRestitution(DEFAULT_OBSTACLE_RESTITUTION);} <br> {@code
   * body.resetMassData();} <br><br> The {@code body .getFixtureList()} retrieves the first {@code
   * Fixture} out of those bound to this {@code Obstacle's} physics body. Every time this method is
   * overrode, the {@code addUserData()} method should be modified to set the correct obstacle
   * fixture type.To manipulate the next one in the list, use the method {@code
   * primaryFixture.getNext()}.<br> Density is obvious. It is used to calculate the body's
   * weight.<br> Friction should be a float between 0 and 1. During a collision, the lower of the
   * two bodies' friction values is used. The default for {@code Obstacles} and {@code Players} is
   * 10, to keep {@code Players} them from sliding when they stop moving. <br> Restitution is the
   * "bounciness" of the body. During a collision, the higher of the two bodies' restitution values
   * is used. <br> The method {@code body.resetMassData()} must be called every time the density is
   * changed.
   */
  protected void setFixtureData() {
    Fixture primaryFixture = body.getFixtureList();
    primaryFixture.setUserData(new UserData().addUserData("obstacleType", "default"));
    primaryFixture.setDensity(DEFAULT_OBSTACLE_MASS / (width * height));
    primaryFixture.setFriction(DEFAULT_OBSTACLE_FRICTION);
    primaryFixture.setRestitution(DEFAULT_OBSTACLE_RESTITUTION);
    body.resetMassData();
  }

  /**
   * Use this method to create and implement and {@code ContactOperations} that will operate in the
   * {@code GameController's WorldContactListener}. Pass the {@code ContactOperations} into the
   * {@code controller.contactListener.addContactOperation} method. Override this method in any
   * subclasses.
   */
  protected void createWorldContactOperations() {

  }

  /**
   * Set the sprite of this {@code Obstacle}.
   *
   * @param sprite the sprite {@code Paint}.
   */
  public void setSprite(Paint sprite) {
    screenMask.setFill(sprite);
  }

  /**
   * This method creates sensors on all four sides of the {@code Obstacle's} physics body. Run this
   * at the end of the {@code setFixtureData()} method.
   *
   * @param sensorKey the key to refer to the sensors as in {@code UserData}
   */
  protected void createSideSensors(String sensorKey) {
    //Create the bottom sensor
    PolygonShape bottomSensorShape = new PolygonShape();
    bottomSensorShape.set(new Vec2[]{
        new Vec2(SIDE_SENSOR_OFFSET, -height + SIDE_SENSOR_OFFSET),
        new Vec2(width - SIDE_SENSOR_OFFSET, -height + SIDE_SENSOR_OFFSET),
        new Vec2(width - SIDE_SENSOR_OFFSET, -height - SIDE_SENSOR_OFFSET),
        new Vec2(SIDE_SENSOR_OFFSET, -height - SIDE_SENSOR_OFFSET)
    }, 4);
    FixtureDef bottomSensor = new FixtureDef();
    bottomSensor.shape = bottomSensorShape;
    bottomSensor.isSensor = true;
    bottomSensor.userData = new UserData().addUserData(sensorKey, "bottom");
    body.createFixture(bottomSensor);

    //Create the left sensor
    PolygonShape leftSensorShape = new PolygonShape();
    leftSensorShape.set(new Vec2[]{
        new Vec2(-SIDE_SENSOR_OFFSET, -height + SIDE_SENSOR_OFFSET),
        new Vec2(SIDE_SENSOR_OFFSET, -height + SIDE_SENSOR_OFFSET),
        new Vec2(SIDE_SENSOR_OFFSET, -SIDE_SENSOR_OFFSET),
        new Vec2(-SIDE_SENSOR_OFFSET, -SIDE_SENSOR_OFFSET)
    }, 4);
    FixtureDef leftSensor = new FixtureDef();
    leftSensor.shape = leftSensorShape;
    leftSensor.isSensor = true;
    leftSensor.userData = new UserData().addUserData(sensorKey, "left");
    body.createFixture(leftSensor);

    //Create the right sensor
    PolygonShape rightSensorShape = new PolygonShape();
    rightSensorShape.set(new Vec2[]{
        new Vec2(width - SIDE_SENSOR_OFFSET, -height + SIDE_SENSOR_OFFSET),
        new Vec2(width + SIDE_SENSOR_OFFSET, -height + SIDE_SENSOR_OFFSET),
        new Vec2(width + SIDE_SENSOR_OFFSET, -SIDE_SENSOR_OFFSET),
        new Vec2(width - SIDE_SENSOR_OFFSET, -SIDE_SENSOR_OFFSET)
    }, 4);
    FixtureDef rightSensor = new FixtureDef();
    rightSensor.shape = rightSensorShape;
    rightSensor.isSensor = true;
    rightSensor.userData = new UserData().addUserData(sensorKey, "right");
    body.createFixture(rightSensor);

    //Create the top sensor
    PolygonShape topSensorShape = new PolygonShape();
    topSensorShape.set(new Vec2[]{
        new Vec2(SIDE_SENSOR_OFFSET, SIDE_SENSOR_OFFSET),
        new Vec2(width - SIDE_SENSOR_OFFSET, SIDE_SENSOR_OFFSET),
        new Vec2(width - SIDE_SENSOR_OFFSET, -SIDE_SENSOR_OFFSET),
        new Vec2(SIDE_SENSOR_OFFSET, -SIDE_SENSOR_OFFSET)
    }, 4);
    FixtureDef topSensor = new FixtureDef();
    topSensor.shape = topSensorShape;
    topSensor.isSensor = true;
    topSensor.userData = new UserData().addUserData(sensorKey, "top");
    body.createFixture(topSensor);
  }

  public void deleteObstacle() {
    controller.world.destroyBody(body);
  }

  /**
   * Updates the {@code Obstacle}. Call this in a {@code GameController's execute()} method.
   */
  public void update() {
    updateScreenMask();
  }


  public void flipScreenMask() {
    if (screenMask.getTransforms().contains(FLIP_HORIZONTAL)) {
      screenMask.getTransforms().remove(FLIP_HORIZONTAL);
    } else {
      screenMask.getTransforms().add(FLIP_HORIZONTAL);
    }
    updateScreenMask();
  }

  @Override
  public String toString() {
    StringBuilder toString = new StringBuilder(String.format("Obstacle:[%n  ScreenMask:[x:%f, "
            + "y:%f, width:%f, height:%f],%n  WorldBody:[x:%f, y:%f, angle:%f, Fixtures:[%n",
        screenMask.getLayoutX(), screenMask.getLayoutY(), screenMask.getWidth(),
        screenMask.getHeight(), body.getPosition().x, body.getPosition().y, body.getAngle()));
    toString.append(fixturesToString());
    toString.append("  ]\n]");
    return toString.toString();
  }

  /**
   * Used in the {@code toString()} method.
   *
   * @return a string containing all the {@code Fixture} vertices, formatted for the {@code
   * toString()} method.
   */
  protected String fixturesToString() {
    StringBuilder toString = new StringBuilder();
    for (Fixture fixture = body.getFixtureList(); fixture != null; fixture = fixture.getNext()) {
      toString.append("    Fixture:[\n");
      for (Vec2 vertex : ((PolygonShape) body.getFixtureList().m_shape).m_vertices) {
        toString.append(String.format("      Vertex:[%f, %f]%n", vertex.x, vertex.y));
      }
      toString.append("    ]\n");
    }
    return toString.toString();
  }
}
