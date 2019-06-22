import Utilities.GameController;
import Utilities.Player;

/**
 * This is a simple GameController to get you started.
 **/
public class BasicGameController extends GameController {

  @Override
  protected void init() {
    player = new Player(this);
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
