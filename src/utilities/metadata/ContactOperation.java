package utilities.metadata;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * {@code ContactOperations} run when added to a {@code WorldContactListener}. Use this to
 * perform operations based around collisions or sensors.
 * @author Russell Newton
 * @see utilities.WorldContactListener
 **/
public interface ContactOperation {

  /**
   * This operation runs when a contact is first made anywhere in the physics engine. Use it to
   * determine handling of two objects based on their {@code UserData}.
   * @param contact passed by the {@code WorldContactListener}. Perform checks on {@code contact
   * .getFixtureA()} and {@code contact.getFixtureB()}.
   */
  void beginContact(Contact contact);

  /**
   * This operation runs when a contact is ended anywhere in the physics engine. Use it to
   * determine handling of two objects based on their {@code UserData}.
   * @param contact passed by the {@code WorldContactListener}. Perform checks on {@code contact
   * .getFixtureA()} and {@code contact.getFixtureB()}.
   */
  void endContact(Contact contact);

  /**
   * This operation runs when a two objects are overlapping, before the engine solves the overlap.
   * Use it to determine handling of two objects during collision, based on their {@code UserData}.
   * This can be used for one-way obstacles.
   * @param contact passed by {@code WorldContactListener}. Perform checks on {@code contact
   * .getFixtureA()} and {@code contact.getFixtureB()}.
   * @param manifold gives information about the overlap, supplied by the (@code
   * WorldContactListener}.
   */
  void preSolve(Contact contact, Manifold manifold);

  /**
   * This operation runs after the engine solves the collision. Use this to determine handling of
   * two objects after they collide, based on their {@code UserData}. This can be used for sticky
   * projectiles.
   * @param contact passed by the @code WorldContactListener}. Perform checks on {@code contact
   * .getFixtureA()} and {@code contact.getFixtureB()}.
   * @param contactImpulse gives information about the impulse given to solve the collision. A
   * very large impulse means the two {@code Fixtures} had a large overlap.
   */
  void postSolve(Contact contact, ContactImpulse contactImpulse);
}
