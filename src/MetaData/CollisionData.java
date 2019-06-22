package MetaData;

import org.waltonrobotics.util.Vector;

/**
 * @author Russell Newton
 **/
public class CollisionData {

  private final EnhancedBoolean hasCollided;
  private final Vector collisionOverlap;
  private final double surfaceAngle;

  public CollisionData(EnhancedBoolean hasCollided, Vector collisionOverlap, double surfaceAngle) {
    this.hasCollided = hasCollided;
    this.collisionOverlap = collisionOverlap;
    this.surfaceAngle = surfaceAngle;
  }

  public EnhancedBoolean getHasCollided() {
    return hasCollided;
  }

  public Vector getCollisionOverlap() {
    return collisionOverlap;
  }

  public double getSurfaceAngle() {
    return surfaceAngle;
  }
}
