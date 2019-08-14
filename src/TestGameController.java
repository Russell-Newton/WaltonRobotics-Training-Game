import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.SHIFT;
import static javafx.scene.input.KeyCode.W;

import org.jbox2d.dynamics.Body;
import utilities.GameController;
import utilities.Player;

/**
 * @author Russell Newton
 **/
public class TestGameController extends GameController {

  @Override
  protected void init() {
    createObstacles("/assets/obstacles/Obstacles.json");

    player = new Player(this/*,
        "https://www.seekpng.com/png/detail/223-2238252_image-result-for-mario-sprite-video-game-font.png"*/);
    player.setJumpKey(W);
    player.setWalkLeftKey(A);
    player.setWalkRightKey(D);
    player.setRunKey(SHIFT);   // What to hold down to run
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
