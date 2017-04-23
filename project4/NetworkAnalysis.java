import java.io.*;
import java.util.Scanner;


public class NetworkAnalysis
{
	public static EdgeWeightedDigraph network;
	public static EdgeWeightedGraph undirectNetwork;
	public static FlowNetwork flowNetwork;
	public static double minBandwith;
	public static double maxFlow;
	public static int[] count;
	
	public static void main(String[] args) throws IOException
	{
		//create Graph object, call addEdge(new DirectedEdge())
		String fileName = args[0];
		processFile(fileName);		//build graph
		System.out.println(network);
		Scanner scan = new Scanner(System.in);
		int choice;
		
		do
		{
			System.out.println("Menu Options:\n");
			System.out.println("1) Find the lowest latency path between two switches:");
			System.out.println("2) Determine whether the network is copper-only connected:");
			System.out.println("3) Find the maximum data that can be transmitted between two switches");
			System.out.println("4) Find the lowest average latency spanning tree for the network");
			System.out.println("5) Determine whether the network would remain connected if any two switches were to fail");
			System.out.println("6) Quit");

			System.out.print("Select an option: ");
			choice = scan.nextInt();

			if(choice == 1)		//Shortest latency path using Dijkstra's
			{
				int v, w;
				minBandwith = 0;
				System.out.println("\nEnter the first vertex: ");
				v = scan.nextInt();
				System.out.println("Enter the second vertex: ");
				w = scan.nextInt();

				DijkstraAllPairsSP sp = new DijkstraAllPairsSP(network);
				if(sp.hasPath(v, w))
				{
					String path = sp.path(v, w).toString();
					String [] p = path.split(".00 ");
					System.out.println("\nSHORTEST TIME PATH: ");
					for(int i = 0; i < p.length; i++)
						System.out.println(p[i] + ".00");
					System.out.println("\nMIN. BANDWITH FOR THIS PATH: [" + minBandwith + " megabits per second]");
					System.out.println("/////////////////////////////////////////////////////////////////////\n");
				}
			}
			else if(choice ==2)		//Copper-only connectivity tester using DFS
			{
				boolean copperOnly = true;
				DepthFirstSearch df = new DepthFirstSearch(network, 0);
				for (int v = 0; v < network.V(); v++) {
					if (df.marked(v))
						for(DirectedEdge e : network.adj(v))
						{
							if(e.cableType().equals("optical"))
							{
								System.out.println("\nThis graph is [NOT] copper-only connected.");
								System.out.println("/////////////////////////////////////////////////////////////////////\n");
								copperOnly = false;
								break;
							}
							if(!copperOnly) break;
						}
					if(!copperOnly) break;
				}
				if(copperOnly)
				{
					System.out.println("\nThis graph [IS] copper-only connected.");
					System.out.println("/////////////////////////////////////////////////////////////////////\n");
				}
				
			}
			else if(choice==3)		//Max. data flow using Ford Fulkerson's
			{
				makeFlowNetwork(fileName);
				System.out.println(flowNetwork);
				int v, w;
				maxFlow = 0;
				System.out.println("\nEnter the first vertex: ");
				v = scan.nextInt();
				System.out.println("Enter the second vertex: ");
				w = scan.nextInt();
				
				FordFulkerson ff = new FordFulkerson(flowNetwork, v, w);
				System.out.println("\nThe maximum amount of data is: [" + maxFlow + "]");
				System.out.println("/////////////////////////////////////////////////////////////////////\n");
			}
			else if(choice==4)		//min. latency spanning tree using Kruskal's MST
			{
				makeUndirectedGraph(fileName);
				System.out.println(undirectNetwork);
				int v, w;
				
				System.out.println("Min. latency spanning tree: \n");
				KruskalMST minSpanning = new KruskalMST(undirectNetwork);
				System.out.println("/////////////////////////////////////////////////////////////////////\n");
				
			}
			else if(choice==5)		//vertices failure tester using DFS
			{
				count = new int[network.V()];
				DepthFirstSearch df = new DepthFirstSearch(network, 0);
				
				//get edge count at each vertex
				for (int v = 0; v < network.V(); v++) {
					if (df.marked(v))
					{
						for(DirectedEdge e : network.adj(v))
						{
							count[v]++;
						}
					}
					
				}
				
				int failCount = 0;
				//if any two vertices have <=2 edges, our graph fails!
				for(int v = 0; v < network.V(); v++)
				{
					if(count[v] <= 2)
						failCount++;
				}
				if(failCount >= 2)
					System.out.println("\nThis graph will [NOT SURVIVE] if ANY two vertices fail.\n/////////////////////////////////////////////////////////////////////\n");
				else
					System.out.println("\nThis graph will [SURVIVE] if ANY two vertices fail.\n/////////////////////////////////////////////////////////////////////\n");
			}
			else
			{
				System.exit(0);
			}
			
		}while (choice > 0 && choice < 6);

/*		int v = 2;
		int w = 7;
		DijkstraAllPairsSP mst = new DijkstraAllPairsSP(network);
		if(mst.hasPath(v, w))
		{
			System.out.println("reached");
			Iterable<DirectedEdge> sp = mst.path(v, w);

		}*/
	}

	private static void processFile(String file) throws IOException
	{
		FileReader fr = new FileReader(file);	
		BufferedReader br = new BufferedReader(fr);
		String numVertices = br.readLine();
		network = new EdgeWeightedDigraph(Integer.parseInt(numVertices));
		String currLine;
		while((currLine = br.readLine()) != null)
		{
			String[] data = currLine.split(" ");
			network.addEdge(new DirectedEdge(data));
			//need to add edge going from W->V in addition to V->W added above
			//only need to swap vertex values, edge will be the same in 
			String temp = data[0];
			data[0] = data[1];
			data[1] = temp;
			network.addEdge(new DirectedEdge(data));
			
		}
		fr.close();
		System.out.println("\nGraph created succesfully");
		
	}
	private static void makeFlowNetwork(String file) throws IOException
	{
		FileReader fr = new FileReader(file);	
		BufferedReader br = new BufferedReader(fr);
		String numVertices = br.readLine();
		flowNetwork = new FlowNetwork(Integer.parseInt(numVertices));
		String currLine;
		while((currLine = br.readLine()) != null)
		{
			String[] data = currLine.split(" ");
			flowNetwork.addEdge(new FlowEdge(data));
			//need to add edge going from W->V in addition to V->W added above
			//only need to swap vertex values, edge will be the same in 
			String temp = data[0];
			data[0] = data[1];
			data[1] = temp;
			flowNetwork.addEdge(new FlowEdge(data));
			
		}
		fr.close();
		System.out.println("\nGraph created succesfully");
	}
	private static void makeUndirectedGraph(String file) throws IOException
	{
		FileReader fr = new FileReader(file);	
		BufferedReader br = new BufferedReader(fr);
		String numVertices = br.readLine();
		undirectNetwork = new EdgeWeightedGraph(Integer.parseInt(numVertices));
		String currLine;
		while((currLine = br.readLine()) != null)
		{
			String[] data = currLine.split(" ");
			undirectNetwork.addEdge(new Edge(data));
			//need to add edge going from W->V in addition to V->W added above
			//only need to swap vertex values, edge will be the same in 
			/* String temp = data[0];
			data[0] = data[1];
			data[1] = temp;
			network.addEdge(new Edge(data)); */
			
		}
		fr.close();
		System.out.println("\nNEW UNDIRECTED Graph created succesfully");
	}
}
