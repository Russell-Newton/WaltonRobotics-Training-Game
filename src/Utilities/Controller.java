package Utilities;

import java.util.LinkedList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * @author Russell Newton
 **/
public abstract class Controller {

  private int frameInterval = 10;

  @FXML
  protected AnchorPane root;

  public Controller() {
    Timeline executionTimeline = new Timeline(new KeyFrame(Duration.millis(frameInterval),
        (event) -> {
          execute();
          if(isFinished()) {
            end();
          }
        }));
  }

  @FXML
  private void initialize() {
    init();
  }

  protected abstract void init();

  protected abstract void execute();

  protected abstract boolean isFinished();

  protected abstract void end();

  protected void addToScreen(Node object) {
    root.getChildren().addAll(object);
  }

  protected void addToScreen(LinkedList<Node> objects) {
    root.getChildren().addAll(objects);
  }

}
