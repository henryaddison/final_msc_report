public class LaneChangeEdge extends LaneEdge {
  //constant for number of edges to lay down for a lane change branch
	final private static int NODES_AHEAD_TO_JOIN_LANE = 4;
	//keep track of where this edge is aiming for
	private Lane destinationLane; 
	//need to keep track of which proper edges are currently being occupied while a boat traverses this lane
	//fortunately can only have one boat on an edge like this at a time so 
	private LaneEdge destinationLaneEdge, startLaneEdge;
	
	
	public static LaneChangeEdge createLaneChangeBranch(NdPoint startingFrom, LaneEdge startingLaneEdge, boolean upstream, Lane destinationLane) {
		
		LaneChangeEdge return_value = null;
		
		//find the nearest edge in destination lane
		LaneEdge destinationLaneEdge = destinationLane.edgeNearest(startingFrom);
		
		//place startNode of first edge in lane changing branch
		LaneNode startNode = new TemporaryLaneNode(startingFrom, destinationLane);
		destinationLane.getContext().add(startNode);
		
		
		LaneNode endNode; //end point of next edge in lane changing branch
		LaneNode sLNode, dLNode; //node in starting lane and destination lane that the next edge will sit between
		
		
		//make sure there's sufficient space head to fit the branch in for both lanes
		try {
			destinationLane.getNthNodeAhead(destinationLane.nodeNearest(startingFrom), upstream, NODES_AHEAD_TO_JOIN_LANE+1);
			startingLaneEdge.getNextNode(upstream).getLane().getNthNodeAhead(startingLaneEdge.getNextNode(upstream), upstream, NODES_AHEAD_TO_JOIN_LANE+1);
		} catch (NoNextNode e) {
			throw e;
		}
		
		//place the next edges and nodes one-by-one
		for(int i = 1; i <= NODES_AHEAD_TO_JOIN_LANE; i++) {
			sLNode = startingLaneEdge.getNextNode(upstream);
			dLNode = destinationLaneEdge.getNextNode(upstream);
			
			if(i < NODES_AHEAD_TO_JOIN_LANE) { //if we haven't put enough edges down
			  //then place a temporary node midway between the nodes in the start and destination nodes
			  //this point gradually shifts towards the destination lane as we iterate
				double x = sLNode.getLocation().getX()*((NODES_AHEAD_TO_JOIN_LANE-i)/((double)NODES_AHEAD_TO_JOIN_LANE)) + dLNode.getLocation().getX()*(i/((double)NODES_AHEAD_TO_JOIN_LANE));
				double y = sLNode.getLocation().getY()*((NODES_AHEAD_TO_JOIN_LANE-i)/((double)NODES_AHEAD_TO_JOIN_LANE)) + dLNode.getLocation().getY()*(i/((double)NODES_AHEAD_TO_JOIN_LANE));
				
				endNode = new TemporaryLaneNode(x,y,destinationLane);
				destinationLane.getContext().add(endNode);
			} else {
			  //otherwise have placed enough edges so should join the next edge to the destination lane
				endNode = dLNode;
			}
			
			//set the source and destination nodes appriately for direction of graph compared to direction of travel
			LaneNode source = startNode;
			LaneNode destination = endNode;
			if(upstream) {
				source = endNode;
				destination = startNode;
			}
			
			//place the next edge based on the node just calculated and the one placed before
			//LaneChangeEdge is sandwiched between the two LaneEdges provided
			LaneChangeEdge next_edge = new LaneChangeEdge(source, destination, startingLaneEdge, destinationLaneEdge);
			destinationLane.getNet().addEdge(next_edge);
			
			//record for later the first edge placed
			if(return_value == null) {
				return_value = next_edge;
			}
			
			
			//setup for the next iteration
			destinationLaneEdge = destinationLane.getNextEdge(dLNode, upstream);
			startingLaneEdge = sLNode.getLane().getNextEdge(sLNode, upstream);
			
			startNode = endNode;
		}
		
		//return the first edge placed so the cox creating this temporary boat knows where to have the boat move to next
		return return_value;
	}
}