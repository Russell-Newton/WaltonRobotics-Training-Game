package utilities;

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

  private HashMap<String, String> filePathMap = new HashMap<>();
  private HashMap<String, AnchorPane> screenMap = new HashMap<>();
  private HashMap<String, GameController> controllerMap = new HashMap<>();
  private Scene main;
  private GameController currentController;

  /**
   * Set up a {@code SceneController} for a {@code Scene} in order to manage the different
   * controllers and .fxml setups.
   *
   * @param main the {@code Scene} that the {@code SceneController} manages.
   */
  public SceneController(Scene main) {
    this.main = main;
  }

  /**
   * Add a screen setup under a designated name from a .fxml file.
   *
   * @param name what the screen setup will be called.
   * @param fxmlPath path to GameController .fxml.
   * @throws IOException if it cannot load the .fxml file at the given path.
   */
  public void addScreen(String name, String fxmlPath) throws IOException {
    if (!filePathMap.containsKey(name)) {
      filePathMap.put(name, fxmlPath);
    }
    FXMLLoader fxmlLoader = getLoaderFromPath(filePathMap.get(name));

    //Add layout to screenMap
    screenMap.put(name, fxmlLoader.load());
    //Add controller to controllerMap
    GameController controller = fxmlLoader.getController();
    controller.pauseExecutionTimeline();
    controllerMap.put(name, controller);
  }

  /**
   * Remove a screen setup from the list of possible setups.
   *
   * @param name the name of the screen setup to be removed.
   */
  public void removeScreen(String name) {
    screenMap.remove(name);
  }

  /**
   * Activate one of the loaded screen setups.
   *
   * @param name the name of the screen setup to be loaded.
   */
  public void activate(String name) {
    pauseCurrentControllerTimeline();
    main.setRoot(screenMap.get(name));
    currentController = controllerMap.get(name);
    resumeCurrentControllerTimeline();
  }

  /**
   * Load a .fxml file into an {@code FXMLLoader}.
   *
   * @param filePath the path to the .fxml file.
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

  /**
   * Pauses the current controller's execution timeline.
   */
  private void pauseCurrentControllerTimeline() {
    if (currentController != null) {
      currentController.pauseExecutionTimeline();
    }
  }

  /**
   * Resumes the current controller's execution timeline.
   */
  private void resumeCurrentControllerTimeline() {
    if (currentController != null) {
      currentController.resumeExecutionTimeline();
    }
  }

  /**
   * Resets the controller of the given name by replacing the current instance in the {@code
   * controllerMap} with a new instance. It will need to be reactivated in order to see changes.
   *
   * @param name the name of the controller to reset.
   * @throws IOException if it cannot reload the .fxml file at the previously given path.
   */
  private void resetController(String name) throws IOException {
    screenMap.remove(name);
    controllerMap.remove(name);
    addScreen(name, filePathMap.get(name));
  }

}
