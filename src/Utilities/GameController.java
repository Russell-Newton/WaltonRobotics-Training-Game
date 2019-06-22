package Utilities;

import static Utilities.Constants.FRAME_INTERVAL;
import static Utilities.Constants.GRAVITY_ACCELERATION;
import static Utilities.Constants.POSITION_ITERATIONS;
import static Utilities.Constants.VELOCITY_ITERATIONS;

import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * @author Russell Newton
 **/
public abstract class GameController {

  public World world;
  protected Timeline executionTimeline;
  protected LinkedList<Obstacle> obstacles = new LinkedList<>();
  @FXML
  protected AnchorPane root;
  protected Player player;
  private Vec2 gravity;

  public GameController() {
    gravity = new Vec2(0f, GRAVITY_ACCELERATION);
    world = new World(gravity);
    executionTimeline = new Timeline(new KeyFrame(Duration.millis(FRAME_INTERVAL),
        (event) -> {
          execute();
          if (isFinished()) {
            end();
            executionTimeline.stop();
          }
        }));
    executionTimeline.setCycleCount(Animation.INDEFINITE);
  }

  @FXML
  protected void initialize() {
    setBackgroundColor(Color.LIGHTBLUE);
    createObstacles();

    init();
    executionTimeline.play();
  }

  private void createObstacles() {
    obstacles.add(new Obstacle(this, 30, 40, 50, 10, 0, Color.RED));

    addToScreen(obstacles.stream().map(Obstacle::getScreenMask));
  }

  protected abstract void init();

  protected void execute() {
    stepWorld();
  }

  protected abstract boolean isFinished();

  protected abstract void end();

  protected void addToScreen(Node object) {
    root.getChildren().addAll(object);
  }

  protected void addToScreen(LinkedList<? extends Node> objects) {
    root.getChildren().addAll(objects);
  }

  protected void addToScreen(Stream<? extends Node> objects) {
    root.getChildren().addAll(objects.collect(Collectors.toCollection(LinkedList::new)));
  }

  protected void setBackgroundColor(Paint paint) {
    root.setBackground(new Background(new BackgroundFill(paint, null, null)));
  }

  protected void setBackgroundImage(String imageLocation) {
    root.setBackground(new Background(new BackgroundImage(new Image(imageLocation), null, null,
        null, null)));
  }

  public LinkedList<Obstacle> getObstacles() {
    return obstacles;
  }

  public void stepWorld() {
    world.step((float) FRAME_INTERVAL / 1000.0f, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
  }
}
