//----------------------------------------------------------------------------
// WeightedGraph.java            by Dale/Joyce/Weems                Chapter 10
//
// Implements a directed graph with weighted edges.
// Vertices are objects of class T and can be marked as having been visited.
// Edge weights are integers.
// Equivalence of vertices is determined by the vertices' equals method.
//
// General precondition: Except for the addVertex and hasVertex methods, 
// any vertex passed as an argument to a method is in this graph.
//----------------------------------------------------------------------------

package ch10.graphs;

import ch04.queues.LinkedQueue;
import ch04.queues.QueueInterface;

public class WeightedGraph<T> implements WeightedGraphInterface<T> {
	public static final int NULL_EDGE = 0;
	private static final int DEFCAP = 50; // default capacity
	private int numVertices;
	private int maxVertices;
	private T[] vertices;
	private int[][] edges;
	private boolean[] marks; // marks[i] is mark for vertices[i]

	@SuppressWarnings("unchecked")
	public WeightedGraph()
	// Instantiates a graph with capacity DEFCAP vertices.
	{
		numVertices = 0;
		maxVertices = DEFCAP;
		vertices = (T[]) new Object[DEFCAP];
		marks = new boolean[DEFCAP];
		edges = new int[DEFCAP][DEFCAP];
	}

	public WeightedGraph(int maxV)
	// Instantiates a graph with capacity maxV.
	{
		numVertices = 0;
		maxVertices = maxV;
		vertices = (T[]) new Object[maxV];
		marks = new boolean[maxV];
		edges = new int[maxV][maxV];
	}

	public int size() {
		return numVertices;
	}

	private int[] shortestPaths(T startVertex) {
		int[] distances = new int[numVertices];
		boolean[] visited = new boolean[numVertices];

		for (int i = 0; i < numVertices; i++) {
			distances[i] = Integer.MAX_VALUE;
			visited[i] = false;
		}
		distances[indexIs(startVertex)] = 0;

		int currentVertex = minVertex(distances, visited);
		while (currentVertex != -1) {
			QueueInterface<T> adjacentVertices = getToVertices(vertices[currentVertex]);
			while (!adjacentVertices.isEmpty()) {
				T neighbor = adjacentVertices.dequeue();
				int neighborValue = indexIs(neighbor);
				int distance = weightIs(vertices[currentVertex], vertices[indexIs(neighbor)]);
				distances[neighborValue] = Math.min(distances[neighborValue], 
													distances[currentVertex] + distance);
			}
			visited[currentVertex] = true;
			currentVertex = minVertex(distances, visited);
		}
		return distances;
	}

	public int getShortestDistance(T start, T end) {
		int[] distances = shortestPaths(start);
		return distances[indexIs(end)];
	}

	public int minVertex(int[] dist, boolean[] vis) {
		int minDist = Integer.MAX_VALUE;
		int pos = -1;
		for (int i = 0; i < numVertices; i++) {
			if (!vis[i] && dist[i] < minDist) {
					minDist = dist[i];
					pos = i;
				}
		}
		//System.out.println("min vertex: " + pos);
		return pos;
	}

	public boolean isEmpty()
	// Returns true if this graph is empty; otherwise, returns false.
	{
		return false;
	}

	public boolean isFull()
	// Returns true if this graph is full; otherwise, returns false.
	{
		return false;
	}

	public void addVertex(T vertex)
	// Preconditions: This graph is not full.
	// vertex is not already in this graph.
	// vertex is not null.
	//
	// Adds vertex to this graph.
	{
		vertices[numVertices] = vertex;
		for (int index = 0; index < numVertices; index++) {
			edges[numVertices][index] = NULL_EDGE;
			edges[index][numVertices] = NULL_EDGE;
		}
		numVertices++;
	}

	public boolean hasVertex(T vertex)
	// Returns true if this graph contains vertex; otherwise, returns false.
	{
		if (indexIs(vertex) >= 0)
			return true;
		return false;
	}

	private int indexIs(T vertex)
	// Returns the index of vertex in vertices.
	{
		// System.out.println("Looking for " + vertex);
		for (int index = 0; index < numVertices; index++) {

			if (vertex.equals(vertices[index])) {
				// System.out.println("found");
				return index;
			}
		}
		return -1;
	}

	public void addEdge(T fromVertex, T toVertex, int weight)
	// Adds an edge with the specified weight from fromVertex to toVertex.
	{
		int row;
		int column;

		row = indexIs(fromVertex);
		column = indexIs(toVertex);
		edges[row][column] = weight;
	}

	public int weightIs(T fromVertex, T toVertex)
	// If edge from fromVertex to toVertex exists, returns the weight of edge;
	// otherwise, returns a special “null-edge” value.
	{
		int row;
		int column;

		row = indexIs(fromVertex);
		column = indexIs(toVertex);
		return edges[row][column];
	}

	public QueueInterface<T> getToVertices(T vertex)
	// Returns a queue of the vertices that vertex is adjacent to.
	{
		QueueInterface<T> adjVertices = new LinkedQueue<T>();
		int fromIndex;
		int toIndex;
		fromIndex = indexIs(vertex);
		for (toIndex = 0; toIndex < numVertices; toIndex++)
			if (edges[fromIndex][toIndex] != NULL_EDGE)
				adjVertices.enqueue(vertices[toIndex]);
		return adjVertices;
	}

	public void clearMarks()
	// Unmarks all vertices.
	{
	}

	public void markVertex(T vertex)
	// Marks vertex.
	{
	}

	public boolean isMarked(T vertex)
	// Returns true if vertex is marked; otherwise, returns false.
	{
		return false;
	}

	public T getUnmarked()
	// Returns an unmarked vertex if any exist; otherwise, returns null.
	{
		return null;
	}

	public boolean edgeExists(T vertex1, T vertex2)
	// Preconditions: vertex1 and vertex2 are in the set of vertices
	//
	// Return value = (vertex1, vertex2) is in the set of edges
	{
		return (edges[indexIs(vertex1)][indexIs(vertex2)] != NULL_EDGE);
	}

	public boolean removeEdge(T vertex1, T vertex2)
	// Preconditions: vertex1 and vertex2 are in the set of vertices
	//
	// Return value = true if edge was in the graph (and has been removed)
	// = false if edge was not in the graph
	{
		boolean existed = edgeExists(vertex1, vertex2);
		edges[indexIs(vertex1)][indexIs(vertex2)] = NULL_EDGE;
		return existed;
	}

}
