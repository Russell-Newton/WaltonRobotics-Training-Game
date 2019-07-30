package utilities;

import static utilities.metadata.StaticUtilities.FRAME_INTERVAL;
import static utilities.metadata.StaticUtilities.GRAVITY_ACCELERATION;
import static utilities.metadata.StaticUtilities.POSITION_ITERATIONS;
import static utilities.metadata.StaticUtilities.VELOCITY_ITERATIONS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
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
import javafx.util.Pair;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Russell Newton
 **/
public abstract class GameController {

  protected Player player;
  @FXML
  AnchorPane root;
  World world;
  WorldContactListener contactListener;
  private Timeline executionTimeline;
  private LinkedList<Pair<String, Obstacle>> obstacles = new LinkedList<>();
  private Paint backgroundPaint = Color.LIGHTBLUE;

  /**
   * Creates a new {@code GameController}.
   */
  public GameController() {
    //Set up physics engine
    Vec2 gravity = new Vec2(0f, GRAVITY_ACCELERATION);
    world = new World(gravity);
    contactListener = new WorldContactListener();
    world.setContactListener(contactListener);

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
   * Run when the {@code GameController} is loaded. Requires the {@code @FXML} tag.
   */
  @FXML
  protected void initialize() {
    setBackground(backgroundPaint);
    createObstacles("/assets/Boundaries.json");

    init();
    executionTimeline.play();
  }

  /**
   * Adds obstacles to the controller.
   * @param obstacleJSONFiles an array of {@code Obstacle} JSON files' relative locations.
   */
  protected void createObstacles(String... obstacleJSONFiles) {
//    createObstaclesFromJSON("/assets/Boundaries.json");
//    createObstaclesFromJSON("/assets/Obstacles.json");
    for(String location : obstacleJSONFiles) {
      createObstaclesFromJSON(location);
    }
  }

//  protected void createObstaclesFromFile(String filePath) {
//    try {
//      String absolutePath = new File("").getAbsolutePath();
//      BufferedReader br = new BufferedReader(new FileReader(absolutePath +
//          "\\src" + filePath));
//      String line;
//      while ((line = br.readLine()) != null) {
//        if (!line.startsWith("*") && !line.isEmpty()) {
//          obstacles.add(Obstacle.fromString(this, line));
//        }
//      }
//    } catch (IOException e) {
//      System.out.println("Obstacle file at " + filePath + " cannot be opened.");
//      e.printStackTrace();
//    }
//  }

  private String getObstacleStringFromJSONMap(Map obstacle) {
    float x = Float.parseFloat("" + obstacle.get("x"));
    float y = Float.parseFloat("" + obstacle.get("y"));
    float width = Float.parseFloat("" + obstacle.get("width"));
    float height = Float.parseFloat("" + obstacle.get("height"));
    float angle = Float.parseFloat("" + obstacle.get("angle"));
    String sprite = (String) obstacle.get("sprite");
    if (sprite == null) {
      sprite = "";
    }

    return String.format("X:%f,Y:%f,W:%f,H:%f,A:%f,P:%s",
        x, y, width, height, angle, sprite);
  }

  protected void createObstaclesFromJSON(String filePath) {
    try {
      String absolutePath = new File("").getAbsolutePath();
      JSONObject json = (JSONObject) (new JSONParser().parse(
          new FileReader(absolutePath + "\\src" + filePath)));
      
      Map staticObstacles = (Map) json.get("obstacles");
      Iterator<Entry> iterator = staticObstacles.entrySet().iterator();

      while(iterator.hasNext()) {
        Entry obstacleEntry = iterator.next();
        Map obstacle = (Map) obstacleEntry.getValue();
        String type = (String) obstacle.get("type");
        if(type == null) type = "";
        switch(type) {
          case "kinematic":
          case "static":
          default:
            this.obstacles.add(new Pair<>((String) obstacleEntry.getKey(),
                Obstacle.fromString(this, getObstacleStringFromJSONMap(obstacle))));
            break;
        }
      }
    } catch (Exception e) {
      System.out.println("Obstacle file at " + filePath + " cannot be opened.");
      e.printStackTrace();
    }
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
   * @param object the object to add to the {@code Scene}.
   */
  protected void addToScreen(Node object) {
    root.getChildren().addAll(object);
  }

  /**
   * Adds a {@code LinkedList} of {@code Nodes} to the {@code Scene}.
   *
   * @param objects the objects to add to the {@code Scene}.
   */
  protected void addToScreen(LinkedList<? extends Node> objects) {
    root.getChildren().addAll(objects);
  }

  /**
   * Adds a {@code Stream} of {@code Nodes} to the {@code Scene}.
   *
   * @param objects the objects to add to the {@code Scene}.
   */
  protected void addToScreen(Stream<? extends Node> objects) {
    root.getChildren().addAll(objects.collect(Collectors.toCollection(LinkedList::new)));
  }

  protected void removeFromScreen(Node node) {
    root.getChildren().remove(node);
  }

  /**
   * Sets the background fill.
   *
   * @param paint the {@code Paint} to fill the background with.
   */
  protected void setBackground(Paint paint) {
    root.setBackground(new Background(new BackgroundFill(paint, null, null)));
  }

  /**
   * @return the {@code Obstacles} currently in the screen.
   */
  public LinkedList<Pair<String, Obstacle>> getObstacles() {
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
   * @param name the {@code Obstacle's} name.
   * @param obstacle the {@code Obstacle} to add.
   */
  protected void addObstacle(String name, Obstacle obstacle) {
    obstacles.add(new Pair<>(name, obstacle));
    addToScreen(obstacle.getScreenMask());
  }

  /**
   * Removes the first {@code Obstacle} with the given name.
   * @param name the {@code Obstacle's} name.
   */
  protected void removeObstacle(String name) {
    for(Pair<String, Obstacle> obstacle : obstacles) {
      if(obstacle.getKey().equals(name)) {
        obstacles.remove(obstacle);
        removeFromScreen(obstacle.getValue().getScreenMask());
        break;
      }
    }
  }

  /**
   * Resumes the timeline's execution.
   */
  public void resumeExecutionTimeline() {
    executionTimeline.play();
  }

  /**
   * Pauses the timeline's execution.
   */
  public void pauseExecutionTimeline() {
    executionTimeline.pause();
  }

}
