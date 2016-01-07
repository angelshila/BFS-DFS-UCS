package backup;


import java.io.*;

import java.util.*;

public class Graph 
{
	public Node rootNode;
	public ArrayList<Node> nodes=new ArrayList<Node>();
	public ArrayList<Edge> edges=new ArrayList<Edge>();
	public int[][] adjMatrix;//Edges will be represented as adjacency Matrix
	public int[][] adjMatrixUCS;//Edges will be represented as adjacency Matrix
	int offStart[];
	int offStop[];
	int size;
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
	
	public void addEdgeUCS(Edge e)
	{
		edges.add(e);
	}
	
	//This method will be called to make connect two nodes
	public void connectNode(Node start,Node end)
	{
		if(adjMatrix==null)
		{
			size=nodes.size();
			adjMatrix=new int[size][size];
		}

		int startIndex=nodes.indexOf(start);
		int endIndex=nodes.indexOf(end);
		adjMatrix[startIndex][endIndex]=1;
//		adjMatrix[endIndex][startIndex]=1;
	}
	
	public void connectNodeUCS(Node start,Node end,int pipeLength)
	{
		if(adjMatrixUCS==null)
		{
			size=nodes.size();
			adjMatrixUCS=new int[size][size];
		}

		int startIndex=nodes.indexOf(start);
		int endIndex=nodes.indexOf(end);
		adjMatrixUCS[startIndex][endIndex]=pipeLength;
//		adjMatrix[endIndex][startIndex]=1;
	}
	
	private Node getUnvisitedChildNode(Node n)
	{
		
		int index=nodes.indexOf(n);
		int j=0;
		while(j<size)
		{
			if(adjMatrix[index][j]==1 && ((Node)nodes.get(j)).visited==false)
			{
				return (Node)nodes.get(j);
			}
			j++;
		}
		return null;
	}
	
	private ArrayList<Node> getUnvisitedChildNodeUCS(Node n)
	{
		ArrayList<Node> children=new ArrayList<Node>();
		int index=nodes.indexOf(n);
		int j=0;
		while(j<size)
		{
			if(adjMatrixUCS[index][j]!=0 /*&& ((Node)nodes.get(j)).visited==false*/)
			{
				int currentNodecost=n.cost+adjMatrixUCS[index][j];
				if( (((Node)nodes.get(j)).visited==true && nodes.get(j).cost>currentNodecost) || (((Node)nodes.get(j)).visited==false) ){
					nodes.get(j).cost=currentNodecost;
					int[][] offTimes=getEdgeoffTimes(n,nodes.get(j));
					if(offTimes!=null){
						System.out.println(offTimes[0].toString());
						
						int [] offTimeStartChild = offTimes[0];
						int [] offTimeStopChild = offTimes[1];
						boolean flag=false;
						for(int k=0;k<offTimeStopChild.length;k++){
							
							flag=false;
							
							if(!(nodes.get(j).cost>=offTimeStartChild[k] && nodes.get(j).cost<=offTimeStopChild[k])){
								
								flag=true;
								
							}
						}
						
						if(flag == true){
							children.add((Node)nodes.get(j));
						}
						
					}
					
					else{
						
						children.add((Node)nodes.get(j));
					}
					

				}
				
				
			}
			j++;
		}
		return children;
	}
	

	private int[][] getEdgeoffTimes(Node p, Node c) {
			
		Iterator<Edge> it=edges.iterator();
		
		while(it.hasNext()){
			Edge thiselement= it.next();
			Node parent=thiselement.parent;
			Node child=thiselement.child;
			if(parent==p && child ==c){
				int Periodsencap[][]= {thiselement.offStart ,thiselement.offStop};
				return Periodsencap;
			}		


		}
		return null;

	}
	
	

	//BFS traversal of a tree is performed by the bfs() function
		public void ucs(String destination[],int startTime)
		{
//			PriorityQueue pq= new PriorityQueue();
			
			PriorityQueue<Node> pq = new PriorityQueue<Node>(
			        new Comparator<Node>( ) {

						@Override
						public int compare(Node o1, Node o2) {
							return o1.cost - o2.cost;
						}
				
			});
			                                    
			
			ArrayList<Node>children = new ArrayList<Node>();
			boolean flag=false;
			pq.add(this.rootNode);
//			printNode(this.rootNode);
			rootNode.visited=true;
			rootNode.cost=0;
			
			while(!pq.isEmpty()){
				
				Node parent=(Node)pq.remove();
				for(String s:destination){
					if(parent.label.equals(s)){
						flag=true;
						break;
					}
						
				}
				if (flag==true){
					
					int timeReached=parent.cost+startTime;
					String data=parent.label+" "+timeReached;

					printNode(data);
//					System.out.println(parent.cost);
					break;
				}
				children=getUnvisitedChildNodeUCS(parent);
				for(int y=0;y<children.size();y++){
					children.get(y).visited=true;
					if( !(pq.contains(children.get(y))) ){
						pq.add(children.get(y));
					}
				}

			}
			
			if(flag==false){
				printNodeNotFound();
			}

			//Clear visited property of nodes
			clearNodes();
		}
	
	//BFS traversal of a tree is performed by the bfs() function
	public void bfs(String destination[],int startTime) throws IOException
	{
		
		//BFS uses Queue data structure
		Queue<Node> q=new LinkedList<Node>();
		Queue<Integer> depthQueue = new LinkedList<Integer>();
		q.add(this.rootNode);
//		printNode(this.rootNode);
		depthQueue.add(0);
		rootNode.visited=true;
//		int timeReached=0;
		boolean flag=false;
		String data;
//		boolean nodeFound=false;
		while(!q.isEmpty())
		{
			Node n=(Node)q.remove();
		    Object depth = depthQueue.peek();
		    depthQueue.remove();
			for(String s:destination){
				if(n.label.equals(s)){
					flag=true;
					break;
				}
					
			}
			if (flag==true){
				
				int timeReached=(int)depth+startTime;
				data=n.label+" "+timeReached;
				
				printNode(data);
//				System.out.println((int)depth+startTime);
				break;
			}
			
			Node child=null;
			while((child=getUnvisitedChildNode(n))!=null)
			{
				depthQueue.add((int)depth+1);;
				child.visited=true;
//				timeReached++;
//				printNode(child);
				q.add(child);
			}
		}
		
		if(flag==false){
			printNodeNotFound();
		}
		//Clear visited property of nodes
		clearNodes();
	}
	
	private void printNodeNotFound() {

		try{
    		
    		File file =new File("output.txt");

    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    			FileWriter fileWritter = new FileWriter(file.getName(),true);
    	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    	        bufferWritter.write("none");
    	        bufferWritter.close();
    			
    		}
    		else{
    			FileWriter fileWritter = new FileWriter(file.getName(),true);
    			 BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	    	        bufferWritter.newLine();
	    	        bufferWritter.write("none");
	    	        bufferWritter.close();

    		}
    		
		}catch(IOException e){
    		e.printStackTrace();
		}		
		
	}

	//DFS traversal of a tree is performed by the dfs() function
	public void dfs(String[] destination, int startTime)
	{
		//DFS uses Stack data structure
		Stack<Node> s=new Stack<Node>();
		Stack<Integer> depthStack=new Stack<Integer>();
		s.push(this.rootNode);
		depthStack.push(0);
		boolean flag=false;
		rootNode.visited=true;
//		printNode(rootNode);
		while(!s.isEmpty())
		{
			Node n=(Node)s.peek();
			Object depth = depthStack.peek();
			Node child=getUnvisitedChildNode(n);
			
			for(String graphNodes:destination){
				if(n.label.equals(graphNodes)){
					flag=true;
					break;
				}
			}
			
			if (flag==true){
				
				int timeReached=(int)depthStack.peek()+startTime;
				String data=n.label+" "+timeReached;
				
				printNode(data);
				
//				System.out.println((int)depthStack.peek()+startTime);
				break;
			}
			
			if(child!=null)
			{
				depthStack.add((int)depth+1);;
				child.visited=true;
//				printNode(child);
				s.push(child);
			}
			
			
			else
			{
				depthStack.pop();
				s.pop();
			}
		}
		
		if(flag == false){
			printNodeNotFound();
		}
		//Clear visited property of nodes
		clearNodes();
	}
	
	
	//Utility methods for clearing visited property of node
	private void clearNodes()
	{
		int i=0;
		while(i<size)
		{
			Node n=(Node)nodes.get(i);
			n.visited=false;
			i++;
		}
	}
	
	//Utility methods for printing the node's label
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

	public Node getNode(String string) {
		
		Iterator<Node> it=nodes.iterator();
		
		while(it.hasNext()){
//			System.out.println(it.next().label);
			Node thiselement= it.next();
			String thisnode=thiselement.label;
			if(thisnode.equals(string)){
//				System.out.println("Bhoot");
				return thiselement;
			}		

//			}
		}
		return null;
		
	}
	
	public void sortNodes(){
		Collections.sort(nodes);
	}
	
	public void calculateOffPeriodsOfEdges(String n1, String n2, String periods[]) {
		
		offStart=new int[periods.length];
		offStop = new int[periods.length];

		Node parent=getNode(n1);
		Node child=getNode(n2);
		
		for(int i=0;i<periods.length;i++){
			offStart[i]=Integer.parseInt(periods[i].split("-")[0]);
			offStop[i]=Integer.parseInt(periods[i].split("-")[1]);
			
		}
		System.out.println(offStart.toString());
		System.out.println(offStop.toString());
		
		addEdgeUCS(new Edge(parent,child,offStart,offStop));
		
	}


}
