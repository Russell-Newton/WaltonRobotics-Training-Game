package utilities.metadata;

/**
 * {@code EnhancedBooleans} are like a {@code Boolean Object}, but they observe rising and falling
 * edge states.
 *
 * @author Marius Juston
 */
public class EnhancedBoolean {

  private boolean value;
  private boolean previousValue;

  /**
   * @param value starting value.
   */
  public EnhancedBoolean(boolean value) {
    this.value = value;
    previousValue = value;
  }

  /**
   * Defaults to false.
   */
  public EnhancedBoolean() {
    this(false);
  }

  /**
   * @return the value of the {@code EnhancedBoolean}.
   */
  public boolean get() {
    return value;
  }

  /**
   * Set the value of the {@code EnhancedBoolean}.
   *
   * @param newValue the value to set it to.
   */
  public void set(boolean newValue) {
    previousValue = value;
    value = newValue;
  }

  /**
   * @return if the {@code EnhancedBoolean} is now true after being false.
   */
  public boolean isRisingEdge() {
    return value && !previousValue;
  }

  /**
   * @return if the {@code EnhancedBoolean} is now false after being true.
   */
  public boolean isFallingEdge() {
    return !value && previousValue;
  }

  /**
   * @return if the {@code EnhancedBoolean} is either at a rising or falling edge.
   */
  public boolean hasChanged() {
    return value != previousValue;
  }
}
