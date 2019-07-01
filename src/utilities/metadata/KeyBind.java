package utilities.metadata;

import javafx.scene.input.KeyCode;

/**
 * {@code KeyBinds} should be created and passed in to the {@code Player implementKeyBind()} method
 * to function.
 *
 * @author Russell Newton
 **/
public class KeyBind {

  public final EnhancedBoolean status;
  private final KeyCode keyCode;
  private final KeyBindHandler handler;

  /**
   * Create a new {@code KeyBind} from a {@code KeyCode} and {@code KeyBindHandler}.
   *
   * @param keyCode the {@code KeyCode} that will be used to trigger the different methods in {@code
   * handler}.
   * @param handler the {@code KeyBindHandler} that will run different functions based on the {@code
   * status}.
   */
  public KeyBind(KeyCode keyCode, KeyBindHandler handler) {
    status = new EnhancedBoolean();
    this.keyCode = keyCode;
    this.handler = handler;
  }

  /**
   * This method is automatically run every execution cycle for any {@code KeyBind} that has been
   * passed to the {@code Player implementKeyBind()} method. It will run different {@code
   * KeyBindHandler} methods depending on {@code status}.
   */
  public void handle() {
    if (status.isRisingEdge()) {
      status.set(true);         //Eliminate the rising edge status
      handler.handleRisingEdge();
    } else if (status.get()) {
      handler.handlePeriodic();
    } else if (status.isFallingEdge()) {
      status.set(false);        //Eliminate the falling edge status
      handler.handleFallingEdge();
    } else if (!status.get()) {
      handler.handleDisable();
    }
  }

  /**
   * @return the {@code KeyCode} that triggers this {@code KeyBind}.
   */
  public KeyCode getKeyCode() {
    return keyCode;
  }

  /**
   * A {@code KeyBindHandler} is used to create a {@code KeyBind}. Supply this with any code you
   * want to have triggered at various points in the execution cycle of a {@code KeyBind}.
   */
  public interface KeyBindHandler {

    /**
     * This method is run right when the key is pressed.
     */
    void handleRisingEdge();

    /**
     * This method is run while the key is held.
     */
    void handlePeriodic();

    /**
     * This method is run right when the key is released.
     */
    void handleFallingEdge();

    /**
     * This method is run when the key is not being held.
     */
    void handleDisable();
  }
}
