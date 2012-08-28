public class SafetyFocussed extends ControlPolicy {
  public Class<? extends Action> typeSpecificActionChoice() {
    if(latestObservations.atRiversEnd()) {
      return Spin.class;
    }
    if(nearbyBoatInfront()) {
      return SlowDown.class;
    }
    if(latestObservations.belowDesiredSpeed()) {
      return SpeedUp.class;
    }
    if(true) {
      return LetBoatRun.class;
    }
  }

  private static final int CLEAR_BOUNDARY = 3; //how big a space to leave before the next boat
  
  private boolean nearbyBoatInfront() {
    Blockage blockage = latestObservations.aheadCurrentLaneLook();
    return (blockage.getEdgesAway() < CLEAR_BOUNDARY) && (blockage.getBlockedEdge() != null);
  }
}
