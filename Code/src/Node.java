public class Node implements Comparable<Node>
{
	public String label;
	public int cost;
	public boolean visited=false;
	public Node(String l, int nodeCost)
	{
		this.label=l;
		this.cost=nodeCost;
	}
	
	 @Override
	    public int compareTo(Node n) {
	       
		 String compareLabel=((Node)n).label;
	        /* For Ascending order*/
	        return this.label.compareTo(compareLabel);
	 }


}

