import Utilities.SceneController;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Window  extends Application {

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
    primaryStage.show();
  }

  private void addScreens() throws IOException {
    sceneController.addScreen("main", "Controller.fxml");
  }
}
