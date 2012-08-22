public class BoatNavigation {	
	public void continueForward() {
	  //first setup the how far the boat can move at most this turn
		setTickDistanceRemaining(boat.getSpeed());
		//then move the boat
		moveBoat();
	}

	private void moveBoat() {
	  //work out whether the boat will reach the end of this edge during this turn
		double distance_till_next_node = getTillEdgeEnd();
		if(distance_till_next_node > tick_distance_remaining) {
		  //if boat won't reach end of edge, then just carry on along the edge as far as possible
			moveAlongEdge(tick_distance_remaining);
			setTickDistanceRemaining(0);
		} else {
		  //otherwise move the boat to the end of the edge and use up the appropriate amount of tick_distance_remaining to get there
			moveToEdgeEnd();
			setTickDistanceRemaining(getTickDistanceRemaining()-distance_till_next_node);
			
			//and then find the following edge to move onto
			LaneNode steer_from = getDestinationNode();
			Lane lane = steer_from.getLane();
			LaneEdge next_edge = lane.getNextEdge(steer_from, headingUpstream());
			
			if(next_edge != null) {
			  //if there is a next edge, move onto it and keep going as far as we can this turn
				updateEdge(next_edge);
				moveBoat(); 
			} else {
				// no next edge - assume we have hit the end of the river so stop the boat instantly
				boat.deadStop();
			}
		}
	}
}

