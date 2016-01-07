package backup;


import java.io.*;
import java.util.Arrays;

public class waterFlow {

	public static void main(String[] args) throws IOException {
		
	    long startTime = System.currentTimeMillis();

//		File inFile = null;
//		if (0 < args.length) {
//		   inFile = new File(args[1]);
//		} else {
//		   System.err.println("Invalid arguments count:" + args.length);
//		   System.exit(0);
//		}
		
		File dir = new File("/Users/Mumu/Dropbox/Fall 2015/AI/Homework");
		File fin = new File(dir.getCanonicalPath() + File.separator + "sampleInput.txt");
		 
//		readFile(inFile);
		
		readFile(fin);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);


	}

	private static void readFile(File fin) throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(fin));
		//Reading Number of Test cases
		String firstLine=br.readLine();
		int testcases=Integer.parseInt(firstLine);
//		System.out.println(testcases);
		
		for(int i=0;i<testcases;i++){
			
			//Repeat certain things
				
			String task=br.readLine();
			if(task.isEmpty())
				task=br.readLine();
			
			String source=br.readLine();
			Graph n = new Graph();
			Node nSource = new Node(source,Integer.MAX_VALUE);
			n.addNode(nSource);
			n.setRootNode(nSource);
			
			String destination[]=br.readLine().split(" ");
			Arrays.sort(destination);
			for(String destinationsNodes:destination)
				n.addNode(new Node(destinationsNodes,Integer.MAX_VALUE));
			
			String restOfGraph=br.readLine();
			
			if(!restOfGraph.isEmpty()){
				String middlenodes[]=restOfGraph.split(" ");
				Arrays.sort(middlenodes);
				for(String middleNodes:middlenodes)
					if(n.getNode(middleNodes) == null)
						n.addNode(new Node(middleNodes,Integer.MAX_VALUE));
			}
			
			n.sortNodes();
			
			int pipes=Integer.parseInt(br.readLine());
			
			for(int j=0;j<pipes;j++){
				String paths[]=br.readLine().split(" ");
//				System.out.println(Arrays.toString(paths));

				
				//Pipe Length and Off Periods for UCS
				if(task.equals("UCS")){
					int pipeLength=Integer.parseInt(paths[2]);
					int offperiodCount=Integer.parseInt(paths[3]);
					if(offperiodCount!=0){
						String periods[]=new String[offperiodCount];
						System.arraycopy(paths,4, periods, 0, (paths.length-4));
//						System.out.println(Arrays.toString(periods));
						n.calculateOffPeriodsOfEdges(paths[0],paths[1],periods);
					}
							
					//Edges
					Node a,b;
					a=n.getNode(paths[0]);
					b=n.getNode(paths[1]);
					n.connectNodeUCS(a,b,pipeLength);
					
		
				}

				else{
					//Edges
					Node a,b;
					a=n.getNode(paths[0]);
					b=n.getNode(paths[1]);
					
					n.connectNode(a,b);
				}
				
			}
			int startTime=Integer.parseInt(br.readLine());
			
			if(task.equals("BFS"))
				n.bfs(destination,startTime);
			else if(task.equals("DFS"))
				n.dfs(destination,startTime);
			else if(task.equals("UCS"))
				n.ucs(destination,startTime);
			
//			System.out.println("next iter");
		}
			

		
	}

}
