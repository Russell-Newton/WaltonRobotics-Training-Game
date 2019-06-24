package utilities;

import static utilities.metadata.StaticUtilities.DEFAULT_PLAYER_ANGLE;
import static utilities.metadata.StaticUtilities.DEFAULT_PLAYER_FILL;
import static utilities.metadata.StaticUtilities.DEFAULT_PLAYER_FRICTION;
import static utilities.metadata.StaticUtilities.DEFAULT_PLAYER_HEIGHT;
import static utilities.metadata.StaticUtilities.DEFAULT_PLAYER_MASS;
import static utilities.metadata.StaticUtilities.DEFAULT_PLAYER_RESTITUTION;
import static utilities.metadata.StaticUtilities.DEFAULT_PLAYER_START_X;
import static utilities.metadata.StaticUtilities.DEFAULT_PLAYER_START_Y;
import static utilities.metadata.StaticUtilities.DEFAULT_PLAYER_WIDTH;
import static utilities.metadata.StaticUtilities.IMAGE_SPRITE_SCALE;
import static utilities.metadata.StaticUtilities.JUMP_COUNT;
import static utilities.metadata.StaticUtilities.JUMP_VECTOR;
import static utilities.metadata.StaticUtilities.RUN_VECTOR;
import static utilities.metadata.StaticUtilities.WALK_VECTOR;
import static utilities.metadata.StaticUtilities.scene;

import utilities.metadata.ContactOperation;
import utilities.metadata.KeyBind;
import utilities.metadata.KeyBind.KeyBindHandler;
import utilities.metadata.UserData;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * @author Russell Newton
 **/
public class Player extends Obstacle {

  private int jumpCount = 0;
  private boolean isRunning = false;
  private HashMap<String, KeyBind> keyBinds = new HashMap<>();

  /**
   * Creates a player with an {@code Image} as a sprite.
   *
   * @param controller the controller this {@code Player} belongs to.
   * @param sprite the {@code Image} to be used as a sprite.
   * @param startX the starting x coordinate of the corresponding physics body.
   * @param startY the starting y coordinate of the corresponding physics body.
   */
  public Player(GameController controller, Image sprite, float startX, float startY) {
    super(controller, startX, startY, (float) sprite.getWidth() * IMAGE_SPRITE_SCALE,
        (float) sprite.getHeight() * IMAGE_SPRITE_SCALE, DEFAULT_PLAYER_ANGLE,
        new ImagePattern(sprite), BodyType.DYNAMIC);
  }

  /**
   * Creates a player with an {@code Image} as a sprite at the default starting location.
   *
   * @param controller the controller this {@code Player} belongs to.
   * @param sprite the {@code Image} to be used as a sprite.
   */
  public Player(GameController controller, Image sprite) {
    this(controller, sprite, DEFAULT_PLAYER_START_X, DEFAULT_PLAYER_START_Y);
  }

  /**
   * Creates a player with a sprite from a file path.
   *
   * @param controller the controller this {@code Player} belongs to.
   * @param spritePath the path to the desired sprite image.
   * @param startX the starting x coordinate of the corresponding physics body.
   * @param startY the starting y coordinate of the corresponding physics body.
   */
  public Player(GameController controller, String spritePath, float startX, float startY) {
    this(controller, new Image(spritePath), startX, startY);
  }

  /**
   * Creates a player with a sprite from a file path at the default starting location.
   *
   * @param controller the controller this {@code Player} belongs to.
   * @param spritePath the path to the desired sprite image.
   */
  public Player(GameController controller, String spritePath) {
    this(controller, new Image(spritePath));
  }

  /**
   * Creates a player with the default fill.
   *
   * @param controller the controller this {@code Player} belongs to.
   * @param startX the starting x coordinate of the corresponding physics body.
   * @param startY the starting y coordinate of the corresponding physics body.
   */
  public Player(GameController controller, float startX, float startY) {
    super(controller, startX, startY, DEFAULT_PLAYER_WIDTH, DEFAULT_PLAYER_HEIGHT,
        DEFAULT_PLAYER_ANGLE,
        DEFAULT_PLAYER_FILL, BodyType.DYNAMIC);
  }

  /**
   * Creates a player with the default fill at the default location.
   *
   * @param controller the controller this {@code Player} belongs to.
   */
  public Player(GameController controller) {
    this(controller, DEFAULT_PLAYER_START_X, DEFAULT_PLAYER_START_Y);
  }

  /**
   * Updates the {@code Player} every execution cycle.
   */
  void update() {
    updateScreenMask();
    for (KeyBind keyBind : keyBinds.values()) {
      keyBind.handle();
    }
  }

  /**
   * Sets the {@code KeyBind} for jumping.
   * @param keyCode the {@code KeyCode} of the key to use for jumping.
   */
  public void setJumpKey(KeyCode keyCode) {
    implementKeyBind("Jump", new KeyBind(keyCode, new KeyBindHandler() {
      @Override
      public void handleRisingEdge() {
        jump();
      }

      @Override
      public void handlePeriodic() {

      }

      @Override
      public void handleFallingEdge() {

      }

      @Override
      public void handleDisable() {

      }
    }));
  }

  /**
   * Sets the {@code KeyBind} for walking right.
   * @param keyCode the {@code KeyCode} of the key to use for walking right.
   */
  public void setWalkRightKey(KeyCode keyCode) {
    implementKeyBind("Walk Right", new KeyBind(keyCode, new KeyBindHandler() {
      @Override
      public void handleRisingEdge() {
        body.applyLinearImpulse(WALK_VECTOR, body.getWorldPoint(centerOfMass));
      }

      @Override
      public void handlePeriodic() {
        walk(true);
      }

      @Override
      public void handleFallingEdge() {
      }

      @Override
      public void handleDisable() {

      }
    }));
  }

  /**
   * Sets the {@code KeyBind} for walking left.
   * @param keyCode the {@code KeyCode} of the key to use for walking left.
   */
  public void setWalkLeftKey(KeyCode keyCode) {
    implementKeyBind("Walk Left", new KeyBind(keyCode, new KeyBindHandler() {
      @Override
      public void handleRisingEdge() {
        body.applyLinearImpulse(WALK_VECTOR.negate(), body.getWorldPoint(centerOfMass));
      }

      @Override
      public void handlePeriodic() {
        walk(false);
      }

      @Override
      public void handleFallingEdge() {
      }

      @Override
      public void handleDisable() {

      }
    }));
  }

  /**
   * Sets the {@code KeyBind} for sprinting.
   * @param keyCode the {@code KeyCode} of the key to use for sprinting.
   */
  public void setRunKey(KeyCode keyCode) {
    implementKeyBind("Run", new KeyBind(keyCode, new KeyBindHandler() {
      @Override
      public void handleRisingEdge() {
        isRunning = true;
      }

      @Override
      public void handlePeriodic() {
      }

      @Override
      public void handleFallingEdge() {
        isRunning = false;
      }

      @Override
      public void handleDisable() {

      }
    }));
  }

  /**
   * Implements a {@code KeyBind} by adding it to the {@code HashMap} of currently activated
   * {@code KeyBinds} and creating {@code EventHandlers} to enable and disable the {@code KeyBind
   * 's} status {@code EnhancedBoolean}.
   * @param name the name of the {@code KeyBind}.
   * @param keyBind the {@code KeyBind} to implement.
   */
  private void implementKeyBind(String name, KeyBind keyBind) {
    keyBinds.put(name, keyBind);
    System.out.println("Key " + keyBind.getKeyCode().toString() + " bound to " + name);
    scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
      if (key.getCode() == keyBind.getKeyCode()) {
        keyBind.status.set(true);
      }
    });
    scene.addEventHandler(KeyEvent.KEY_RELEASED, (key) -> {
      if (key.getCode() == keyBind.getKeyCode()) {
        keyBind.status.set(false);
      }
    });
  }

  /**
   * Make the player jump. Place this method in the {@code handleRisingEdge()} method of your
   * {@code KeyBind}. You can continue to jump as long as you have not exceeded {@code jumpCount}.
   */
  private void jump() {
    if (jumpCount > 0) {
      body.getLinearVelocity().y = 0;
      body.applyLinearImpulse(JUMP_VECTOR, body.getWorldPoint(centerOfMass));
      jumpCount--;
    }
  }

  /**
   * Make the player walk. Place this method in the {@code handlePeriodic()} method of your
   * {@code KeyBind}. Place a horizontal impulse command in the {@code handleRisingEdge()} method.
   * If {@code isRunning} is true, this method will make the player run.
   * @param isRight whether or not the direction of the walking is to the right.
   */
  private void walk(boolean isRight) {
    if (isRunning) {
      if (isRight) {
        body.getLinearVelocity().x = RUN_VECTOR.x;
      } else {
        body.getLinearVelocity().x = -RUN_VECTOR.x;

      }
    } else {
      if (isRight) {
        body.getLinearVelocity().x = WALK_VECTOR.x;
      } else {
        body.getLinearVelocity().x = -WALK_VECTOR.x;
      }
    }
  }

  /**
   * Resets the jump counter. Run this inside of the {@code beginContact()} method in a {@code
   * ContactOperation}.
   */
  private void resetJumpCount() {
    jumpCount = JUMP_COUNT;
  }

  @Override
  protected void createWorldContactOperations() {
    ContactOperation resetJump = new ContactOperation() {
      @Override
      public void beginContact(Contact contact) {
        //If the foot sensor is contacting the ground, reset the jump count
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() != null &&
            ((UserData) fixtureA.getUserData()).get("fixture").equals("foot")) {
          resetJumpCount();
          System.out.println("Can now jump.");
        } else if (fixtureB.getUserData() != null &&
            ((UserData) fixtureB.getUserData()).get("fixture").equals("foot")) {
          resetJumpCount();
          System.out.println("Can now jump.");
        }
      }

      @Override
      public void endContact(Contact contact) {

      }

      @Override
      public void preSolve(Contact contact, Manifold manifold) {

      }

      @Override
      public void postSolve(Contact contact, ContactImpulse contactImpulse) {

      }
    };
    controller.contactListener.addContactOperation("Reset Player Jump", resetJump);
  }

  @Override
  protected void setFixtureData() {
    body.getFixtureList().setDensity(DEFAULT_PLAYER_MASS / (width * height));
    body.getFixtureList().setFriction(DEFAULT_PLAYER_FRICTION);
    body.getFixtureList().setRestitution(DEFAULT_PLAYER_RESTITUTION);
    body.setFixedRotation(true);
    body.resetMassData();

    //Create the foot sensor
    PolygonShape footSensorShape = new PolygonShape();
    footSensorShape.set(new Vec2[]{
        new Vec2(0, -height + 0.2f), new Vec2(width, -height + 0.2f),
        new Vec2(width, -height - 0.2f), new Vec2(0, -height - 0.2f)
    }, 4);

    FixtureDef footSensor = new FixtureDef();
    footSensor.shape = footSensorShape;
    footSensor.isSensor = true;
    footSensor.userData = new UserData().addUserData("fixture", "foot");
    body.createFixture(footSensor);
  }

  @Override
  public String toString() {
    StringBuilder toString = new StringBuilder(String.format("Player:[%n  ScreenMask:[x:%f, y:%f, "
            + "width:%f, height:%f],%n  WorldBody:[x:%f, y:%f, angle:%f, Shape:[%n",
        screenMask.getLayoutX(), screenMask.getLayoutY(), screenMask.getWidth(),
        screenMask.getHeight(), body.getPosition().x, body.getPosition().y, body.getAngle()));
    toString.append(fixturesToString());
    toString.append("  ]\n]");
    return toString.toString();
  }
}
