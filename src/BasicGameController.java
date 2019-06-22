import Utilities.GameController;
import Utilities.Player;

/**
 * @author Russell Newton
 **/
public class BasicGameController extends GameController {

  @Override
  protected void init() {
    player = new Player(this, 48f, 78);
    addToScreen(player.getScreenMask());
  }

  @Override
  protected void execute() {
    super.execute();
    player.update();
  }

  @Override
  protected boolean isFinished() {
    return false;
  }

  @Override
  protected void end() {

  }
}
