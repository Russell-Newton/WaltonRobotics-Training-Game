package utilities;

import static utilities.metadata.StaticUtilities.PRIMARY_SCREEN_BOUNDS;
import static utilities.metadata.StaticUtilities.scene;
import static utilities.metadata.StaticUtilities.sceneController;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Russell Newton
 */
public class Window extends Application {


  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    addScreens();
    sceneController.activate("test");

    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.setTitle("Walton Robotics Training Game");

    primaryStage.setX(PRIMARY_SCREEN_BOUNDS.getMinX());
    primaryStage.setY(PRIMARY_SCREEN_BOUNDS.getMinY());
    primaryStage.setWidth(PRIMARY_SCREEN_BOUNDS.getWidth());
    primaryStage.setHeight(PRIMARY_SCREEN_BOUNDS.getHeight());

    primaryStage.show();
  }

  /**
   * Add new {@code sceneController.addScreen()} calls here if you want to use multiple screens.
   *
   * @throws IOException if the .fxml file cannot be found
   */
  private void addScreens() throws IOException {
    sceneController.addScreen("main", "/assets/BasicGameController.fxml");
    sceneController.addScreen("test", "/assets/TestGameController.fxml");
  }
}
