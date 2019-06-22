package Utilities;

import static Utilities.StaticUtilities.FRAME_INTERVAL;
import static Utilities.StaticUtilities.GRAVITY_ACCELERATION;
import static Utilities.StaticUtilities.POSITION_ITERATIONS;
import static Utilities.StaticUtilities.VELOCITY_ITERATIONS;

import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

/**
 * @author Russell Newton
 **/
public abstract class GameController {

  protected Player player;
  @FXML
  protected AnchorPane root;
  World world;
  private Timeline executionTimeline;
  private LinkedList<Obstacle> obstacles = new LinkedList<>();
  private Paint backgroundPaint = Color.LIGHTBLUE;

  /**
   * Creates a new {@code GameController}
   */
  public GameController() {
    //Set up physics engine
    Vec2 gravity = new Vec2(0f, GRAVITY_ACCELERATION);
    world = new World(gravity);

    //Set up executionTimeline
    executionTimeline = new Timeline(
        new KeyFrame(Duration.millis(FRAME_INTERVAL),
            (event) -> {
              //Update the engine and player
              stepWorld();
              player.update();

              //Run periodic controller methods
              execute();
              if (isFinished()) {
                end();
                executionTimeline.stop();
              }
            }));
    executionTimeline.setCycleCount(Animation.INDEFINITE);
  }

  /**
   * Run when the {@code GameController} is loaded. Requires the @FXML tag.
   */
  @FXML
  protected void initialize() {
    setBackground(backgroundPaint);
    createObstacles();

    init();
    executionTimeline.play();
  }

  /**
   * Adds obstacles to the controller on startup.
   */
  private void createObstacles() {
    obstacles.add(new Obstacle(this, 30, 40, 50, 10, 0));
  }

  /**
   * This method runs once upon startup.
   */
  protected abstract void init();

  /**
   * This method runs periodically after startup. Anything you want to have run every time the
   * application updates should go in here.
   */
  protected abstract void execute();

  /**
   * This method runs after the {@code execute()} method is run. If it returns true, the {@code
   * end()} method is run and the {@code executionTimeline} is stopped. Any logic to determine when
   * to stop the execution of the application should go in here.
   *
   * @return - whether or not to finish the execution of the application.
   */
  protected abstract boolean isFinished();

  /**
   * This method runs after the {@code isFinished()} method returns true. Anything you want to run
   * when the execution of the application stops should go in here.
   */
  protected abstract void end();

  /**
   * Adds a {@code Node} to the {@code Scene}.
   *
   * @param object - the object to add to the {@code Scene}.
   */
  protected void addToScreen(Node object) {
    root.getChildren().addAll(object);
  }

  /**
   * Adds a {@code LinkedList} of {@code Nodes} to the {@code Scene}.
   *
   * @param objects - the objects to add to the {@code Scene}.
   */
  protected void addToScreen(LinkedList<? extends Node> objects) {
    root.getChildren().addAll(objects);
  }

  /**
   * Adds a {@code Stream} of {@code Nodes} to the {@code Scene}.
   *
   * @param objects - the objects to add to the {@code Scene}.
   */
  protected void addToScreen(Stream<? extends Node> objects) {
    root.getChildren().addAll(objects.collect(Collectors.toCollection(LinkedList::new)));
  }

  /**
   * Sets the background fill.
   *
   * @param paint - the {@code Paint} to fill the background with.
   */
  protected void setBackground(Paint paint) {
    root.setBackground(new Background(new BackgroundFill(paint, null, null)));
  }

  /**
   * @return the {@code Obstacles} currently in the screen.
   */
  public LinkedList<Obstacle> getObstacles() {
    return obstacles;
  }

  /**
   * Steps the physics simulation. This is run whenever the {@code execute()} method is run.
   */
  void stepWorld() {
    world.step((float) FRAME_INTERVAL / 1000.0f, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
  }

  /**
   * Adds an {@code Obstacle} to the list of active {@code Obstacles}.
   *
   * @param obstacle - the {@code Obstacle} to add.
   */
  protected void addObstacle(Obstacle obstacle) {
    obstacles.add(obstacle);
  }
}
