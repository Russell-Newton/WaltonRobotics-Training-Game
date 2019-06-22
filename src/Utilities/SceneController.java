package Utilities;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class SceneController {

  private HashMap<String, AnchorPane> screenMap = new HashMap<>();
  private HashMap<String, GameController> controllerMap = new HashMap<>();
  private Scene main;
  private GameController currentController;

  public SceneController(Scene main) {
    this.main = main;
  }


  /**
   * @param name - key
   * @param fxmlPath - path to GameController .fxml
   */
  public void addScreen(String name, String fxmlPath) throws IOException {
    FXMLLoader fxmlLoader = getLoaderFromPath(fxmlPath);
    screenMap.put(name, fxmlLoader.load());
    controllerMap.put(name, fxmlLoader.getController());
  }

  public void removeScreen(String name) {
    screenMap.remove(name);
  }

  public void activate(String name) {
    main.setRoot(screenMap.get(name));
    currentController = controllerMap.get(name);
  }

  public FXMLLoader getLoaderFromPath(String filePath) {
    URL location = getClass().getResource(filePath);
    return new FXMLLoader(location);
  }

  public GameController getCurrentController() {
    return currentController;
  }
}
