import ch10.graphs.WeightedGraph;

public class Dijkstra {
 
	public static void main (String[] args) {
		WeightedGraph<String> myGraph = new WeightedGraph<String>();
		
//		myGraph.addVertex("A");
//		myGraph.addVertex("B");
//		myGraph.addVertex("C");
//		myGraph.addVertex("D");
//		myGraph.addVertex("E");
//		
//		myGraph.addEdge("A", "B", 6);
//		myGraph.addEdge("B", "C", 5);
//		myGraph.addEdge("A", "D", 1);
//		myGraph.addEdge("D", "B", 2);
//		myGraph.addEdge("D", "E", 1);
//		myGraph.addEdge("B", "E", 2);
//		myGraph.addEdge("E", "C", 5);
//		
//		myGraph.getShortestDistance("A", "D");
		
		myGraph.addVertex("A");
		myGraph.addVertex("B");
		myGraph.addVertex("C");
		myGraph.addVertex("D");
		myGraph.addVertex("E");
		myGraph.addVertex("F");
		
		myGraph.addEdge("A", "B", 10);
		myGraph.addEdge("A", "C", 15);
		myGraph.addEdge("B", "D", 12);
		myGraph.addEdge("B", "F", 15);
		myGraph.addEdge("D", "F", 1);
		myGraph.addEdge("F", "E", 5);
		myGraph.addEdge("D", "E", 2);
		myGraph.addEdge("C", "E", 10);
		
		myGraph.getShortestDistance("A", "D");
		

	}
  
}
