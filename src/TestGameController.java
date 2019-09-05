import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.SHIFT;
import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.KeyCode.W;
import static utilities.metadata.StaticUtilities.getFillFromString;

import javafx.scene.paint.Paint;
import utilities.GameController;
import utilities.Player;
import utilities.Player.MotionState;

/**
 * @author Russell Newton
 **/
public class TestGameController extends GameController {

  private Paint runSprite = getFillFromString("mario3.png", true);
  private Paint walkSprite = getFillFromString("luigi3.png", true);
  private Paint jumpSprite = getFillFromString("toadette3.png", true);

  @Override
  protected void init() {
    createObstacles("/assets/obstacles/Obstacles.json");
    setBackground("background8");

    player.setSprite(walkSprite);
    player.setJumpKey(SPACE);
    player.setWalkLeftKey(A);
    player.setWalkRightKey(D);
    player.setRunKey(SHIFT);   // What to hold down to run
  }

  @Override
  protected void execute() {
    if (player.isRunning()) {
      player.setSprite(runSprite);
    } else if (MotionState.JUMPING.getValue()) {
      player.setSprite(jumpSprite);
    } else {
      player.setSprite(walkSprite);
    }
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {

  }
}
