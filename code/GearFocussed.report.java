public class GearFocussed extends ControlPolicy {
  protected Class<? extends Action> typeSpecificActionChoice() {
    if(latestObservations.atRiversEnd()) {
      return Spin.class;
    }
    if(latestObservations.belowDesiredSpeed()) {
      return SpeedUp.class;
    }
    if(true) {
      return LetBoatRun.class;
    }
  }
}
