import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class GameBoard {
	
	public Node root;
	public LinkedList<Node> allNodes;
	public int totalLayers;
	
	/**
	 * This class object represents a single space on a game board.
	 * Each space is characterized mainly by its neighbors.
	 * There are three axes on a hexagonal game board, and we will name these the H,P, and N axes.
	 * H - Horizontal
	 * P - Positive
	 * N - Negative
	 * These axes relate to the slope each axis would have if the board was superImposed over Cartesian coordinates.
	 * @author Jacob Noble
	 *
	 * @param <T>
	 */
	public class Node{
		
		private Node nextH;
		private Node nextP;
		private Node nextN;
		private Node previousH;
		private Node previousP;
		private Node previousN;
		private Integer nodeId;
		private boolean empty;
		
		/**
		 * This constructor initializes a Node object with the given ID.
		 * The next and previous nodes along all axes are initialized to null;
		 * The node is set to empty by default.
		 * @param id
		 */
		private Node (int id){
			this.nextH = null;
			this.nextP = null;
			this.nextN = null;
			this.previousH = null;
			this.previousP = null;
			this.previousN = null;
			this.setNodeId(id);
			this.setEmpty(false);
		}
		
		public void setNeighbors(Node nh, Node np, Node nn, Node ph, Node pp, Node pn){
			this.setHorizontalNeighbors(nh, ph);
			this.setNegativeNeighbors(nn, pn);
			this.setPositiveNeighbors(np, pp);
		}
		
		public void setHorizontalNeighbors(Node nh, Node ph){
			this.nextH = nh;
			if(nh != null){
				nh.previousH = this;
			}
			this.previousH = ph;
			if(ph != null){
				ph.nextH = this;
			}
		}
		
		public void setPositiveNeighbors(Node np, Node pp){
			this.nextP = np;
			if(np != null){
				np.previousP = this;
			}
			this.previousP = pp;
			if(pp != null){
				pp.nextP = this;
			}
		}
		
		public void setNegativeNeighbors(Node nn, Node pn){
			this.nextN = nn;
			if(nn != null){
				nn.previousN = this;
			}
			this.previousN = pn;
			if(pn != null){
				pn.nextN = this;
			}
		}
		
		public ArrayList<Node> getNeighbors(){
			ArrayList<Node> neighbors = new ArrayList<>();
			
			if(this.nextH != null){neighbors.add(this.nextH);};
			if(this.nextP != null){neighbors.add(this.nextP);};
			if(this.nextN != null){neighbors.add(this.nextN);};
			if(this.previousH != null){neighbors.add(this.previousH);};
			if(this.previousP != null){neighbors.add(this.previousP);};
			if(this.previousN != null){neighbors.add(this.previousN);};
			return neighbors;
		}

		public int getNodeId() {
			return nodeId;
		}

		public void setNodeId(int nodeId) {
			this.nodeId = nodeId;
		}

		public boolean isEmpty() {
			return empty;
		}

		public void setEmpty(boolean empty) {
			this.empty = empty;
		}
		
		public String toString(){
			if(!this.isEmpty()){
				return "("+this.nodeId+")";
			}
			return " " + this.nodeId.toString() + " ";
		}
		
		/**
		 * This method returns an array of ID numbers of valid move locations from a given node.
		 * @param node this is the node from which we want to move.
		 * @return array of ID numbers of valid move locations.
		 */
		public ArrayList<Integer> getValidMoves(){
			
			ArrayList<Integer> locationIds = new ArrayList<>();
			if(this.nextH != null && !this.nextH.isEmpty()){if(this.nextH.nextH != null){locationIds.add(this.nextH.nextH.getNodeId());}};
			if(this.nextP != null && !this.nextP.isEmpty()){if(this.nextP.nextP != null){locationIds.add(this.nextP.nextP.getNodeId());}};
			if(this.nextN != null && !this.nextN.isEmpty()){if(this.nextN.nextN != null){locationIds.add(this.nextN.nextN.getNodeId());}};
			if(this.previousH != null && !this.previousH.isEmpty()){if(this.previousH.previousH != null){locationIds.add(this.previousH.previousH.getNodeId());}};
			if(this.previousP != null && !this.previousP.isEmpty()){if(this.previousP.previousP != null){locationIds.add(this.previousP.previousP.getNodeId());}};
			if(this.previousN != null && !this.previousN.isEmpty()){if(this.previousN.previousN != null){locationIds.add(this.previousN.previousN.getNodeId());}};
			
			return locationIds;
		}
	}
	
	/**
	 * This method creates a game board, layer by layer.
	 * As
	 * @param numberOfLayers
	 */
	public GameBoard(int numberOfLayers){
		this.totalLayers = numberOfLayers;
		// Initialize all nodes
		this.allNodes = new LinkedList<>();
		int numberOfNodes = UsefulMethods.factorial(numberOfLayers);
		for(int i=1; i<=numberOfNodes; i++){
			this.newNode(i);
		}
		this.root = getNode(1);
		// Iterate through nodes, setting neighbors
		for(int layer=0; layer <= numberOfLayers; layer++){
			
			for(int j=1; j<=layer; j++){
				int nodeId = UsefulMethods.factorial(layer-1) + j;
				
				Node np = this.getNode(1 + nodeId - layer);
				Node nh = this.getNode(1 + nodeId);
				Node nn = this.getNode(1 + nodeId + layer);
				Node pp = this.getNode(nodeId + layer);
				Node ph = this.getNode(nodeId - 1);
				Node pn = this.getNode(nodeId - layer);
				// Corrections for Nodes on sides and bottom
				if(j == 1){
					ph = null;
					pn = null;
				}
				if(nodeId == (UsefulMethods.factorial(layer))){
					np = null;
					nh = null;
				}
				if(nodeId > UsefulMethods.factorial(numberOfLayers-1)){
					pp = null;
					nn = null;
				}
				
				this.getNode(nodeId).setNeighbors(nh, np, nn, ph, pp, pn);
			}
		}
	}
	
	public Node newNode(int id){
		Node n = new Node(id);
		allNodes.add(n);
		return n;
	}
	
	public Node getNode(int id){
		for(Node n : this.allNodes){
			if(n.getNodeId() == id){
				return n;
			}
		}
		return null;
	}
	
	/**
	 * This method only works because the GameBoard specifically adds nodes in order to the LinkedList allNodes.
	 * Be Warned.
	 */
	public String toString(){ // TODO: Fix toString such that is prints out game board as an equilateral triangle
		String s = "";
		int counter = 1;
		int expectedNumInRow = 1;
		for (Node n : this.allNodes){
			s += n.toString();
			s += " ";
			if(counter == expectedNumInRow){
				s += "\n";
				counter = 0;
				expectedNumInRow++;
			}
			counter++;
		}
		String [] parts = s.split("\n");
		String strung = "";
		int largestWidth = parts[parts.length-1].length();
		for(int i=0; i<parts.length; i++){
			String space = "";
			for(int j=0; j<(largestWidth - parts[i].length())/2; j++){
				space += " ";
			}
			parts[i] = space + parts[i] + space;
		}
		strung = String.join("\n", parts);
		return strung;
	}

	/**
	 * This method moves a peg from one location to an empty location.
	 * @param emptySpace this is the empty space node
	 * @param peg this is the peg node
	 */
	public boolean movePiece(Node emptySpace, Node peg){
		// Make sure empty node is actually empty, and that the peg has an actual peg
		// Also check that peg is from a valid node
		// TODO: Exception handling for numbers larger than what's on the board e.g. input = 65 when there are only 15 nodes 
		if(!emptySpace.isEmpty() || peg.isEmpty()  || !emptySpace.getValidMoves().contains(peg.nodeId)){
			System.err.println("Invalid Selection!");
			return false;
		}
		// Remove peg that neighbors both nodes. this is the peg between them
		for(Node n : emptySpace.getNeighbors()){
			if(peg.getNeighbors().contains(n)){
				n.setEmpty(true);
				break;
			}
		}
		emptySpace.setEmpty(false);
		peg.setEmpty(true);
		//debug
		System.out.println(this.toString());
		return true;
		
	}

}
