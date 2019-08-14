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
import static utilities.metadata.StaticUtilities.JUMP_COUNT;
import static utilities.metadata.StaticUtilities.JUMP_VECTOR;
import static utilities.metadata.StaticUtilities.RUN_VECTOR;
import static utilities.metadata.StaticUtilities.STOP_HORIZONTAL_MOTION_ON_KEY_RELEASE;
import static utilities.metadata.StaticUtilities.WALK_VECTOR;
import static utilities.metadata.StaticUtilities.scene;

import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.ImagePattern;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.contacts.Contact;
import utilities.metadata.ContactOperation;
import utilities.metadata.KeyBind;
import utilities.metadata.KeyBind.KeyBindHandler;
import utilities.metadata.UserData;

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
    super(controller, startX, startY, DEFAULT_PLAYER_WIDTH, DEFAULT_PLAYER_HEIGHT,
        DEFAULT_PLAYER_ANGLE, new ImagePattern(sprite), BodyType.DYNAMIC);
    initialize();
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
    initialize();
  }

  /**
   * Creates a player with the default fill at the default location.
   *
   * @param controller the controller this {@code Player} belongs to.
   */
  public Player(GameController controller) {
    this(controller, DEFAULT_PLAYER_START_X, DEFAULT_PLAYER_START_Y);
  }

  @Override
  public void update() {
    super.update();
    for (KeyBind keyBind : keyBinds.values()) {
      keyBind.handle();
    }

    if (body.getLinearVelocity().x > 0) {
      MotionState.MOVING_RIGHT.set(true);
    }
    if (body.getLinearVelocity().x < 0) {
      MotionState.MOVING_LEFT.set(true);
    }
    if (body.getLinearVelocity().y > 0) {
      MotionState.JUMPING.set(true);
    }
    if (body.getLinearVelocity().y < 0) {
      MotionState.FALLING.set(true);
    }
    if (body.getLinearVelocity().x == 0 && body.getLinearVelocity().y == 0) {
      MotionState.STANDING.set(true);
    }
  }

  /**
   * Sets the {@code KeyBind} for jumping.
   *
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
   *
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
        if(STOP_HORIZONTAL_MOTION_ON_KEY_RELEASE)
        body.getLinearVelocity().x = 0;
      }

      @Override
      public void handleDisable() {

      }
    }));
  }

  /**
   * Sets the {@code KeyBind} for walking left.
   *
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
        if(STOP_HORIZONTAL_MOTION_ON_KEY_RELEASE)
          body.getLinearVelocity().x = 0;
      }

      @Override
      public void handleDisable() {

      }
    }));
  }

  /**
   * Sets the {@code KeyBind} for sprinting.
   *
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
   * Implements a {@code KeyBind} by adding it to the {@code HashMap} of currently activated {@code
   * KeyBinds} and creating {@code EventHandlers} to enable and disable the {@code KeyBind 's}
   * status {@code EnhancedBoolean}.
   *
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
   * Make the player jump. Place this method in the {@code handleRisingEdge()} method of your {@code
   * KeyBind}. You can continue to jump as long as you have not exceeded {@code jumpCount}.
   */
  private void jump() {
    if (jumpCount > 0) {
      body.getLinearVelocity().y = 0;
      body.applyLinearImpulse(JUMP_VECTOR, body.getWorldPoint(centerOfMass));
      jumpCount--;
    }
  }

  /**
   * Make the player walk. Place this method in the {@code handlePeriodic()} method of your {@code
   * KeyBind}. Place a horizontal impulse command in the {@code handleRisingEdge()} method. If
   * {@code isRunning} is true, this method will make the player run.
   *
   * @param isRight whether or not the direction of the walking is to the right.
   */
  private void walk(boolean isRight) {
    if (isRunning) {
      if (isRight && !MotionState.ON_WALL_RIGHT.get()) {
        body.getLinearVelocity().x = RUN_VECTOR.x;
      } else if (!MotionState.ON_WALL_LEFT.get()) {
        body.getLinearVelocity().x = -RUN_VECTOR.x;
      }
    } else {
      if (isRight && !MotionState.ON_WALL_RIGHT.get()) {
        body.getLinearVelocity().x = WALK_VECTOR.x;
      } else if (!MotionState.ON_WALL_LEFT.get()) {
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
    //Create jump reset operation
    ContactOperation resetJump = new ContactOperation() {
      @Override
      public void beginContact(Contact contact) {
        //If the foot sensor is contacting the ground, reset the jump count
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if (fixtureA.getUserData() != null &&
            ((UserData) fixtureA.getUserData()).get("playerSensor").equals("bottom")) {
          resetJumpCount();
        } else if (fixtureB.getUserData() != null &&
            ((UserData) fixtureB.getUserData()).get("playerSensor").equals("bottom")) {
          resetJumpCount();
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

    //TODO fix the stutter
    //Create ON_WALL_RIGHT set operation
    ContactOperation onWallRightSet = new ContactOperation() {
      @Override
      public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        //Right side is colliding with a default obstacle
        if ((((UserData) fixtureA.getUserData()).get("playerSensor").equals("right") &&
            ((UserData) fixtureB.getUserData()).get("obstacleType").equals("default")) ||
            (((UserData) fixtureB.getUserData()).get("playerSensor").equals("right") &&
                ((UserData) fixtureA.getUserData()).get("obstacleType").equals("default"))) {
          MotionState.ON_WALL_RIGHT.set(true);
        }
      }

      @Override
      public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if ((((UserData) fixtureA.getUserData()).get("playerSensor").equals("right") &&
            ((UserData) fixtureB.getUserData()).get("obstacleType").equals("default")) ||
            (((UserData) fixtureB.getUserData()).get("playerSensor").equals("right") &&
                ((UserData) fixtureA.getUserData()).get("obstacleType").equals("default"))) {
          MotionState.ON_WALL_RIGHT.set(false);
        }
      }

      @Override
      public void preSolve(Contact contact, Manifold manifold) {

      }

      @Override
      public void postSolve(Contact contact, ContactImpulse contactImpulse) {

      }
    };

    //Create ON_WALL_LEFT set operation
    ContactOperation onWallLeftSet = new ContactOperation() {
      @Override
      public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        //Left side is colliding with a default obstacle
        if ((((UserData) fixtureA.getUserData()).get("playerSensor").equals("left") &&
            ((UserData) fixtureB.getUserData()).get("obstacleType").equals("default")) ||
            (((UserData) fixtureB.getUserData()).get("playerSensor").equals("left") &&
                ((UserData) fixtureA.getUserData()).get("obstacleType").equals("default"))) {
          MotionState.ON_WALL_LEFT.set(true);
        }
      }

      @Override
      public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if ((((UserData) fixtureA.getUserData()).get("playerSensor").equals("left") &&
            ((UserData) fixtureB.getUserData()).get("obstacleType").equals("default")) ||
            (((UserData) fixtureB.getUserData()).get("playerSensor").equals("left") &&
                ((UserData) fixtureA.getUserData()).get("obstacleType").equals("default"))) {
          MotionState.ON_WALL_LEFT.set(false);
        }
      }

      @Override
      public void preSolve(Contact contact, Manifold manifold) {

      }

      @Override
      public void postSolve(Contact contact, ContactImpulse contactImpulse) {

      }
    };

    controller.contactListener.addContactOperation("resetJump", resetJump);
    controller.contactListener.addContactOperation("onWallRightSet", onWallRightSet);
    controller.contactListener.addContactOperation("onWallLeftSet", onWallLeftSet);
  }

  @Override
  protected void setFixtureData() {
    Fixture primaryFixture = body.getFixtureList();
    primaryFixture.setDensity(DEFAULT_PLAYER_MASS / (width * height));
    primaryFixture.setUserData(new UserData().addUserData("obstacleType", "default"));
    primaryFixture.setFriction(DEFAULT_PLAYER_FRICTION);
    primaryFixture.setRestitution(DEFAULT_PLAYER_RESTITUTION);
    body.resetMassData();
    body.setFixedRotation(true);

    createSideSensors("playerSensor");
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

  public enum MotionState {
    STANDING {
      @Override
      public void set(boolean value) {
        active = value;
        if (value) {
          MOVING_RIGHT.set(false);
          MOVING_LEFT.set(false);
          JUMPING.set(false);
          FALLING.set(false);
        }
      }

      @Override
      public boolean get() {
        return active;
      }
    },
    MOVING_RIGHT {
      @Override
      public void set(boolean value) {
        active = value;
        if (value) {
          MOVING_LEFT.set(false);
          STANDING.set(false);
        }
      }

      @Override
      public boolean get() {
        return active;
      }
    },
    MOVING_LEFT {
      @Override
      public void set(boolean value) {
        active = value;
        if (value) {
          MOVING_RIGHT.set(false);
          STANDING.set(false);
        }
      }

      @Override
      public boolean get() {
        return active;
      }
    },
    JUMPING {
      @Override
      public void set(boolean value) {
        active = value;
        if (value) {
          FALLING.set(false);
          STANDING.set(false);
        }
      }

      @Override
      public boolean get() {
        return active;
      }
    },
    FALLING {
      @Override
      public void set(boolean value) {
        active = value;
        if (value) {
          JUMPING.set(false);
          STANDING.set(false);
        }
      }

      @Override
      public boolean get() {
        return active;
      }
    },
    ON_WALL_RIGHT {
      @Override
      public void set(boolean value) {
        active = value;
        if (value) {
          MOVING_RIGHT.set(false);
        }
      }

      @Override
      public boolean get() {
        return active;
      }
    },
    ON_WALL_LEFT {
      @Override
      public void set(boolean value) {
        active = value;
        if (value) {
          MOVING_LEFT.set(false);
        }
      }

      @Override
      public boolean get() {
        return active;
      }
    };

    protected boolean active = false;

    public abstract void set(boolean value);

    public abstract boolean get();
  }
}
