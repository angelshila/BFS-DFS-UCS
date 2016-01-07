package backup;

public class Edge {

	public Node parent;
	public Node child;
	public int offStart[];
	public int offStop[];
	
	public Edge(Node a, Node b,int ostrt[], int ostp[]){
		this.parent=a;
		this.child=b;
		this.offStart=ostrt;
		this.offStop=ostp;
		
	}
	
}
