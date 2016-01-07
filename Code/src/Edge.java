import java.util.Vector;

public class Edge {

	public Node parent;
	public Node child;
	public Vector<Integer> offPeriods;

	
	public Edge(Node a, Node b,Vector<Integer> pipeOffPeriods){
		this.parent=a;
		this.child=b;
		this.offPeriods=pipeOffPeriods;
		
	}
	
}