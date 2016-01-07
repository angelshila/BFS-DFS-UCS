import java.io.*;
import java.util.*;

public class Graph 
{
	public ArrayList<Node> nodes=new ArrayList<Node>();
	public ArrayList<Edge> edges=new ArrayList<Edge>();
	public int[][] adjacencyMatrix;//Edges will be represented as adjacency Matrix
	public int[][] adjacencyMatrixUCS;//Edges will be represented as adjacency Matrix
	int sizeofMatrix;
	public Node rootNode;
	
	public void setRootNode(Node n)
	{
		this.rootNode=n;
	}
	
	public Node getRootNode()
	{
		return this.rootNode;
	}
	
	public void addNode(Node n)
	{
		nodes.add(n);
	}
	
	public Node getNode(String string) {
		
		Iterator<Node> it=nodes.iterator();
		
		while(it.hasNext()){
			Node thiselement= it.next();
			String thisnode=thiselement.label;
			if(thisnode.equals(string)){
				return thiselement;
			}		
		}
		return null;	
	}
	
	//Sorting nodes alphabetically
	public void sortNodes(){
		Collections.sort(nodes);
	}
	
	public void addEdgeUCS(Edge e)
	{
		edges.add(e);
	}
	
	private Vector<Integer> getEdgeoffTimes(Node p, Node c) {
		
		Iterator<Edge> it=edges.iterator();
		
		while(it.hasNext()){
			Edge thiselement= it.next();
			Node parent=thiselement.parent;
			Node child=thiselement.child;
			if(parent==p && child ==c){
				Vector <Integer> Periods=thiselement.offPeriods;
				return Periods;
			}		
		}
		return null;
	}
	
public void calculateOffPeriodsOfEdges(String n1, String n2, String periods[]) {
		
		Vector<Integer> offPeriods = new Vector<Integer>();
		Node parent=getNode(n1);
		Node child=getNode(n2);
		
		for(int i=0;i<periods.length;i++){
			int offStart=Integer.parseInt(periods[i].split("-")[0]);
			int offStop=Integer.parseInt(periods[i].split("-")[1]);
			
			for(int j=offStart;j<=offStop;j++){
				
				if(!offPeriods.contains(j))
					offPeriods.addElement(j);
			
			}
				
		}
		addEdgeUCS(new Edge(parent,child,offPeriods));
	
	}
	
	//Connect nodes using asymmetric adjacency matrix
	public void connectNode(Node start,Node end)
	{
		if(adjacencyMatrix==null)
		{
			sizeofMatrix=nodes.size();
			adjacencyMatrix=new int[sizeofMatrix][sizeofMatrix];
		}

		int startIndex=nodes.indexOf(start);
		int endIndex=nodes.indexOf(end);
		adjacencyMatrix[startIndex][endIndex]=1;
//		adjMatrix[endIndex][startIndex]=1;
	}
	
	//Connect nodes using asymmetric adjacency matrix using pipeLength
	public void connectNodeUCS(Node start,Node end,int pipeLength)
	{
		if(adjacencyMatrixUCS==null)
		{
			sizeofMatrix=nodes.size();
			adjacencyMatrixUCS=new int[sizeofMatrix][sizeofMatrix];
		}

		int startIndex=nodes.indexOf(start);
		int endIndex=nodes.indexOf(end);
		adjacencyMatrixUCS[startIndex][endIndex]=pipeLength;
//		adjMatrix[endIndex][startIndex]=1;
	}
	
	private Node getUnvisitedChildNode(Node n)
	{
		
		int index=nodes.indexOf(n);
		int j=0;
		while(j<sizeofMatrix)
		{
			if(adjacencyMatrix[index][j]==1 && ((Node)nodes.get(j)).visited==false)
			{
				return (Node)nodes.get(j);
			}
			j++;
		}
		return null;
	}
	
	private ArrayList<Node> getUnvisitedChildNodeUCS(Node n,int currentTime)
	{
		ArrayList<Node> children=new ArrayList<Node>();
		
		//Getting indices of the nodes
		int j=0;
		int index=nodes.indexOf(n);

		//Finding valid nodes
		while(j<sizeofMatrix)
		{
			if(adjacencyMatrixUCS[index][j]!=0)
			{
				int currentNodecost=n.cost+adjacencyMatrixUCS[index][j];
				//Either New Node or old node with greater Cost
				if( (((Node)nodes.get(j)).visited==true && nodes.get(j).cost>currentNodecost) || (((Node)nodes.get(j)).visited==false) ){
					
					//Getting all the offTimes
					Vector<Integer> offTimes=getEdgeoffTimes(n,nodes.get(j));
					
					//Offtimes present
					if(offTimes!=null){	
						if(!(offTimes.contains(currentTime%24))){
							nodes.get(j).cost=currentNodecost;
							children.add((Node)nodes.get(j));
						}
					}
					
					else{
						nodes.get(j).cost=currentNodecost;
						children.add((Node)nodes.get(j));
					}
							
				}
				
			}
			j++;
		}
		return children;
	}
	
		//UCS traversal
		public void ucs(String destination[],int startTime)
		{
			
			PriorityQueue<Node> pq = new PriorityQueue<Node>(
			        new Comparator<Node>( ) {

						@Override
						public int compare(Node o1, Node o2) {
							if(o1.cost == o2.cost){
								return o1.label.compareTo(o2.label);
							}
							else{
								return o1.cost - o2.cost;
							}
						}	
			});
			                                    		
			ArrayList<Node>children = new ArrayList<Node>();
			boolean flag=false;
			pq.add(this.rootNode);
			rootNode.visited=true;
			rootNode.cost=0;
			int currentTime=startTime;
			
			while(!pq.isEmpty()){
				
				Node parent=(Node)pq.remove();
				currentTime=parent.cost+startTime;
				//Check if node is destination node
				for(String s:destination){
					if(parent.label.equals(s)){
						flag=true;
						break;
					}			
				}
				//Break loop after print node data
				if (flag==true){
					
					int timeReached=currentTime%24;
					String data=parent.label+" "+timeReached;
					printNode(data);
					break;
				}
				//Get children of parent node
				children=getUnvisitedChildNodeUCS(parent,currentTime);
				for(int y=0;y<children.size();y++){
					children.get(y).visited=true;
					if( !(pq.contains(children.get(y))) ){
						pq.add(children.get(y));
					}
				}

			}
			//No path found for destination node
			if(flag==false){
				printNodeNotFound();
			}

			
		}
	
	//BFS traversal
	public void bfs(String destination[],int startTime) throws IOException
	{
		
		//BFS uses Queue data structure
		Queue<Node> q=new LinkedList<Node>();
		Queue<Integer> depthQueue = new LinkedList<Integer>();
		q.add(this.rootNode);
		depthQueue.add(0);
		rootNode.visited=true;
		boolean flag=false;
		//Node Data
		String data;
		
		while(!q.isEmpty())
		{
			Node n=(Node)q.remove();
		    Object depth = depthQueue.peek();
		    depthQueue.remove();
			//Check if node is destination node
		    for(String s:destination){
				if(n.label.equals(s)){
					flag=true;
					break;
				}
					
			}
		    //Print node details and break loop if destination node found
			if (flag==true){
				
				int timeReached=((int)depth+startTime)%24;
				data=n.label+" "+timeReached;
				printNode(data);
				break;
			}
			
			Node child=null;
			while((child=getUnvisitedChildNode(n))!=null)
			{
				depthQueue.add((int)depth+1);;
				child.visited=true;
				q.add(child);
			}
		}
	//Destination node not found	
		if(flag==false){
			printNodeNotFound();
		}
	
	}
	
	//DFS traversal
		public void dfs(String[] destination, int startTime)
		{
			//DFS uses Stack data structure
			Stack<Node> s=new Stack<Node>();
			Stack<Integer> depthStack=new Stack<Integer>();
			s.push(this.rootNode);
			depthStack.push(0);
			boolean flag=false;
			rootNode.visited=true;
			
			while(!s.isEmpty())
			{
				Node n=(Node)s.peek();
				Object depth = depthStack.peek();
				Node child=getUnvisitedChildNode(n);
				
				//Check if node is destination node
				for(String graphNodes:destination){
					if(n.label.equals(graphNodes)){
						flag=true;
						break;
					}
				}
				//Print node data and break loop if node is destination node
				if (flag==true){
					
					int timeReached=((int)depthStack.peek()+startTime)%24;
					String data=n.label+" "+timeReached;
					printNode(data);
					break;
				}
				
				if(child!=null)
				{
					depthStack.add((int)depth+1);;
					child.visited=true;
					s.push(child);
				}	
				else
				{
					depthStack.pop();
					s.pop();
				}
			}
			
			//Destination node not found
			if(flag == false){
				printNodeNotFound();
			}

		}
		
	//Print Node Data
	private void printNode(String data)
	{
//		System.out.print(n.label+" ");
		
		
		try{
    		
    		File file =new File("output.txt");
    		
    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    			FileWriter fileWritter = new FileWriter(file.getName(),true);
    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    	        bufferWritter.write(data);
    	        bufferWritter.close();
    			
    		}
    		else{
    			FileWriter fileWritter = new FileWriter(file.getName(),true);
    			 BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	    	        bufferWritter.newLine();
	    	        bufferWritter.write(data);
	    	        bufferWritter.close();

    		}
    		
		}catch(IOException e){
    		e.printStackTrace();
		}		
		
	}

	//Print node
	private void printNodeNotFound() {

		try{
    		
    		File file =new File("output.txt");

    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    			FileWriter fileWritter = new FileWriter(file.getName(),true);
    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    	        bufferWritter.write("None");
    	        bufferWritter.close();
    			
    		}
    		else{
    			FileWriter fileWritter = new FileWriter(file.getName(),true);
    			 BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	    	        bufferWritter.newLine();
	    	        bufferWritter.write("None");
	    	        bufferWritter.close();

    		}
    		
		}catch(IOException e){
    		e.printStackTrace();
		}		
		
	}

}
