package utilities;

import java.util.HashMap;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;
import utilities.metadata.ContactOperation;

/**
 * A {@code WorldContactListener} is created in any {@code GameController} by default. It manages
 * and runs every {@code ContactOperation}.
 *
 * @author Russell Newton
 * @see ContactOperation
 **/
public class WorldContactListener implements ContactListener {

  private HashMap<String, ContactOperation> contactOperations;

  /**
   * Create a new {@code WorldContactListener}.
   */
  public WorldContactListener() {
    contactOperations = new HashMap<>();
  }

  /**
   * This operation runs when a contact is first made anywhere in the physics engine. Use it to
   * determine handling of two objects based on their {@code UserData}.
   *
   * @param contact passed by the {@code WorldContactListener}. Perform checks on {@code contact
   * .getFixtureA()} and {@code contact.getFixtureB()}.
   */
  @Override
  public void beginContact(Contact contact) {
    for (ContactOperation contactOperation : contactOperations.values()) {
      contactOperation.beginContact(contact);
    }
  }

  /**
   * This operation runs when a contact is ended anywhere in the physics engine. Use it to determine
   * handling of two objects based on their {@code UserData}.
   *
   * @param contact passed by the {@code WorldContactListener}. Perform checks on {@code contact
   * .getFixtureA()} and {@code contact.getFixtureB()}.
   */
  @Override
  public void endContact(Contact contact) {
    for (ContactOperation contactOperation : contactOperations.values()) {
      contactOperation.endContact(contact);
    }
  }

  /**
   * This operation runs when a two objects are overlapping, before the engine solves the overlap.
   * Use it to determine handling of two objects during collision, based on their {@code UserData}.
   * This can be used for one-way obstacles.
   *
   * @param contact passed by {@code WorldContactListener}. Perform checks on {@code contact
   * .getFixtureA()} and {@code contact.getFixtureB()}.
   * @param manifold gives information about the overlap, supplied by the (@code
   * WorldContactListener}.
   */
  @Override
  public void preSolve(Contact contact, Manifold manifold) {
    for (ContactOperation contactOperation : contactOperations.values()) {
      contactOperation.preSolve(contact, manifold);
    }
  }

  /**
   * This operation runs after the engine solves the collision. Use this to determine handling of
   * two objects after they collide, based on their {@code UserData}. This can be used for sticky
   * projectiles.
   *
   * @param contact passed by the @code WorldContactListener}. Perform checks on {@code contact
   * .getFixtureA()} and {@code contact.getFixtureB()}.
   * @param contactImpulse gives information about the impulse given to solve the collision. A very
   * large impulse means the two {@code Fixtures} had a large overlap.
   */
  @Override
  public void postSolve(Contact contact, ContactImpulse contactImpulse) {
    for (ContactOperation contactOperation : contactOperations.values()) {
      contactOperation.postSolve(contact, contactImpulse);
    }
  }

  /**
   * Add a {@code ContactOperation} to the {@code HashMap} of those to run.
   *
   * @param name the name to refer to the {@code ContactOperation} by.
   * @param contactOperation the {@code ContactOperation}.
   */
  public void addContactOperation(String name, ContactOperation contactOperation) {
    contactOperations.put(name, contactOperation);
  }

  /**
   * Remove a {@code ContactOperation} from the {@code HashMap} of those to run.
   *
   * @param name the name used to refer to the {@code ContactOperation}.
   */
  public void removeContactOperation(String name) {
    contactOperations.remove(name);
  }
}
