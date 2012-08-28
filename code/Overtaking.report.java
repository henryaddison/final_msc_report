public class Overtaking extends ControlPolicy {
  private static final double OVERTAKING_SPEED_DIFFERENCE = 1.0; //how much faster a boat should be moving before attempting to overtake another
  private static final int CLEAR_BOUNDARY = 3; //the amount of space to leave clear when changing lanes
  
  private boolean overtaking = false; //keeps track of whether policy is in overtaking mode
  
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
    if(true) {
      return continueInLaneChoice();
    }
  }
  
  private Class<? extends Action> overtakingActionChoice() {
    if(laneToRightIsClear() && !latestObservations.changingLane()) {
      overtaking = false;
      return MoveToLaneOnRight.class;
    }
    if(true) {
      return continueInLaneChoice();
    }
  }
  
  private Class<? extends Action> continueInLaneChoice() {
    if(latestObservations.belowDesiredSpeed()) {
      return SpeedUp.class;
    }
    if(true) {
      return LetBoatRun.class;
    }
  }
  
  private boolean slowBoatInfront() {
    Blockage blockage = latestObservations.aheadCurrentLaneLook();
    return blockage.getMaxRelativeSpeed() > OVERTAKING_SPEED_DIFFERENCE;
  }
  
  private boolean laneToLeftIsClear() {
    Blockage aheadLeftblockage = latestObservations.aheadLeftLaneLook();
    Blockage behindLeftblockage = latestObservations.behindLeftLaneLook();
    return (aheadLeftblockage.getEdgesAway() >= CLEAR_BOUNDARY) && (behindLeftblockage.getEdgesAway() >= CLEAR_BOUNDARY); 
  }
  
  private boolean laneToRightIsClear() {
    Blockage aheadRightblockage = latestObservations.aheadRightLaneLook();
    Blockage behindRightblockage = latestObservations.behindRightLaneLook();
    return (aheadRightblockage.getEdgesAway() >= CLEAR_BOUNDARY) && (behindRightblockage.getEdgesAway() >= CLEAR_BOUNDARY);
  }

}
