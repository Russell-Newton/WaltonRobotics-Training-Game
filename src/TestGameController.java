import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.SHIFT;
import static javafx.scene.input.KeyCode.SPACE;

import utilities.GameController;
import utilities.Player;

/**
 * @author Russell Newton
 **/
public class TestGameController extends GameController {

  @Override
  protected void init() {
    System.out.println("Running test controller");
    player = new Player(this);
    player.setJumpKey(SPACE);
    player.setWalkLeftKey(A);
    player.setWalkRightKey(D);
    player.setRunKey(SHIFT);
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
