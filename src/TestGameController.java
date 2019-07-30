import static javafx.scene.input.KeyCode.*;

import utilities.GameController;
import utilities.Player;

/**
 * @author Russell Newton
 **/
public class TestGameController extends GameController {

  @Override
  protected void init() {
    createObstacles("/assets/Obstacles.json");

    player = new Player(this,
        "https://www.seekpng.com/png/detail/223-2238252_image-result-for-mario-sprite-video-game-font.png");
//    player.setJumpKey();
//    player.setWalkLeftKey();
//    player.setWalkRightKey();
//    player.setRunKey();   // What to hold down to run
  }

  @Override
  protected void execute() {

  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {

  }
}
