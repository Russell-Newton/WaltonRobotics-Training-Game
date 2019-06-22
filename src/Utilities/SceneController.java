package Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

/**
 * @author Russell Newton
 */
public class SceneController {

  private HashMap<String, AnchorPane> screenMap = new HashMap<>();
  private HashMap<String, GameController> controllerMap = new HashMap<>();
  private Scene main;
  private GameController currentController;

  /**
   * Set up a {@code SceneController} for a {@code Scene} in order to manage the different
   * controllers and .fxml setups.
   *
   * @param main - the {@code Scene} that the {@code SceneController} manages.
   */
  public SceneController(Scene main) {
    this.main = main;
  }

  /**
   * Add a screen setup under a designated name from a .fxml file.
   *
   * @param name - what the screen setup will be called.
   * @param fxmlPath - path to GameController .fxml.
   */
  public void addScreen(String name, String fxmlPath) throws IOException {
    FXMLLoader fxmlLoader = getLoaderFromPath(fxmlPath);

    //Add layout to screenMap
    screenMap.put(name, fxmlLoader.load());
    //Add controller to controllerMap
    controllerMap.put(name, fxmlLoader.getController());
  }

  /**
   * Remove a screen setup from the list of possible setups.
   *
   * @param name - the name of the screen setup to be removed.
   */
  public void removeScreen(String name) {
    screenMap.remove(name);
  }

  /**
   * Activate one of the loaded screen setups.
   *
   * @param name - the name of the screen setup to be loaded.
   */
  public void activate(String name) {
    main.setRoot(screenMap.get(name));
    currentController = controllerMap.get(name);
  }

  /**
   * Load a .fxml file into an {@code FXMLLoader}.
   *
   * @param filePath - the path to the .fxml file.
   * @return an {@code FXMLLoader} of the specified .fxml file.
   */
  private FXMLLoader getLoaderFromPath(String filePath) {
    URL location = getClass().getResource(filePath);
    return new FXMLLoader(location);
  }

  /**
   * @return the controller currently activated in the main {@code Scene}.
   */
  public GameController getCurrentController() {
    return currentController;
  }
}
