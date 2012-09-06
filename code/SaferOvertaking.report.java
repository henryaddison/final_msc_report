public class SaferOvertaking extends Overtaking {
  private static final double OVERTAKING_SPEED_DIFFERENCE = 1.0;
  private static final int AHEAD_CLEAR_BOUNDARY = 4;
  private static final int BEHIND_CLEAR_BOUNDARY = 2;

  //relevant attributes and methods inherited from Overtaking superclass:
  //protected boolean overtaking = false;
  //protected Class<? extends Action> overtakingActionChoice()
  //protected Class<? extends Action> continueInLaneChoice()

  @Override
  protected Class<? extends Action> typeSpecificActionChoice() {
    if(latestObservations.atRiversEnd()) {
      overtaking = false;
      return Spin.class;
    }
    if(overtaking) {
      return overtakingActionChoice();
    }
    if(latestObservations.aboveDesiredSpeed()) {
      return SlowDown.class;
    }
    if(slowBoatInfront() && laneToLeftIsClear()) {
      overtaking = true;
      return MoveToLaneOnLeft.class;
    }
    if(nearbyBoatInfront()) {
      return SlowDown.class;
    }
    if(true) {
      return continueInLaneChoice();
    }
  }

  private boolean nearbyBoatInfront() {
    Blockage blockage = latestObservations.aheadCurrentLaneLook();
    return (blockage.getEdgesAway() < AHEAD_CLEAR_BOUNDARY) && (blockage.getBlockedEdge() != null);
  }

  protected boolean slowBoatInfront() {
    Blockage blockage = latestObservations.aheadCurrentLaneLook();
    return nearbyBoatInfront() && blockage.getMaxRelativeSpeed() > OVERTAKING_SPEED_DIFFERENCE;
  }

  protected boolean laneToLeftIsClear() {
    Blockage aheadLeftblockage = latestObservations.aheadLeftLaneLook();
    Blockage behindLeftblockage = latestObservations.behindLeftLaneLook();
    return (aheadLeftblockage.getEdgesAway() >= AHEAD_CLEAR_BOUNDARY) && (behindLeftblockage.getEdgesAway() >= BEHIND_CLEAR_BOUNDARY); 
  }

  protected boolean laneToRightIsClear() {
    Blockage aheadRightblockage = latestObservations.aheadRightLaneLook();
    Blockage behindRightblockage = latestObservations.behindRightLaneLook();
    return (aheadRightblockage.getEdgesAway() >= AHEAD_CLEAR_BOUNDARY) && (behindRightblockage.getEdgesAway() >= BEHIND_CLEAR_BOUNDARY);
  }
}
