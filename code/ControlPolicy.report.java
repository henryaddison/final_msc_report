public abstract class ControlPolicy {
  CoxObservations latestObservations;

  public void updateObservations(CoxObservations obs) {
    latestObservations = obs;
  }

  public Class<? extends Action> chooseAction() {
    if(latestObservations.outingComplete()) {
      return Land.class;
    }
    if(true) {
      return typeSpecificActionChoice();
    }
  }

  protected abstract Class<? extends Action> typeSpecificActionChoice();
}
