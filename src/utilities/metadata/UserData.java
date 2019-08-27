package utilities.metadata;

import java.util.HashMap;

/**
 * {@code UserData} can be stored in any JBox2D {@code Fixture}, {@code FixtureDef}, or {@code Body}
 * and JavaFX {@code Nodes}. This class provides a {@code HashMap} to neatly store data and provide
 * easy ways to add and remove from that data.
 *
 * @author Russell Newton
 **/
public class UserData {

  private HashMap<String, Object> userData;

  /**
   * Create a {@code UserData} instance from an already existing {@code HashMap}.
   */
  public UserData(HashMap<String, Object> userData) {
    this.userData = userData;
  }

  /**
   * Create an empty {@code UserData} instance.
   */
  public UserData() {
    userData = new HashMap<>();
  }

  /**
   * Add a {@code String}, {@code Object} pair to the {@code UserData}. Overrides an existing
   * value.
   *
   * @param name the {@code String} identifier of the data pair.
   * @param value the {@code Object} value of the data pair.
   * @return the {@code UserData} with the new data pair. This can be used as an alternative to the
   * constructor that takes in a {@code HashMap}.
   */
  public UserData addUserData(String name, Object value) {
    userData.put(name, value);
    return this;
  }

  /**
   * Remove a {@code String}, {@code Object} pair from the {@code UserData}.
   *
   * @param name the {@code String} identifier of the data pair.
   * @return the {@code UserData} with the data pair removed. This could be used to create new
   * {@code UserData} from another instance.
   */
  public UserData removeUserData(String name) {
    userData.remove(name);
    return this;
  }

  /**
   * @return the {@code HashMap} of all the data pairs.
   */
  public HashMap<String, Object> getUserData() {
    return userData;
  }

  /**
   * Get the {@code Object} value of a specific {@code String} identifier. This will most likely
   * require a cast to function.
   *
   * @param key the {@code String} identifier.
   * @return the {@code Object} value.
   */
  public Object get(String key) {
    Object data = userData.get(key);
    if (data != null) {
      return userData.get(key);
    } else {
      return new Object();
    }
  }
}
