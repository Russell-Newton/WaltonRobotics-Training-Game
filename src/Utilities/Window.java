package Utilities;

import static Utilities.StaticUtilities.primaryScreenBounds;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Russell Newton
 */
public class Window extends Application {

  public static Scene scene = new Scene(new AnchorPane());
  public static SceneController sceneController = new SceneController(scene);

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    addScreens();
    sceneController.activate("main");

    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.setTitle("Walton Robotics Training Game");

    primaryStage.setX(primaryScreenBounds.getMinX());
    primaryStage.setY(primaryScreenBounds.getMinY());
    primaryStage.setWidth(primaryScreenBounds.getWidth());
    primaryStage.setHeight(primaryScreenBounds.getHeight());

    primaryStage.show();
  }

  /**
   * Add new {@code sceneController.addScreen()} calls here if you want to use multiple screens.
   *
   * @throws IOException if the .fxml file cannot be found
   */
  private void addScreens() throws IOException {
    sceneController.addScreen("main", "/Assets/BasicGameController.fxml");
  }
}
