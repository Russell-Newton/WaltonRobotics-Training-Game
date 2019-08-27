import utilities.GameController;
import utilities.Player;

/**
 * This is a simple GameController to get you started.
 **/
public class BasicGameController extends GameController {

  @Override
  protected void init() {
//    Right now the sprite is Mario
    player = new Player(this,
        "https://www.seekpng.com/png/detail/223-2238252_image-result-for-mario-sprite-video-game-font.png");
//    Here place in your player.set methods:
//    player.setJumpKey();
//    player.setRunKey();
//    player.setWalkLeftKey();
//    player.setWalkRightKey();
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
