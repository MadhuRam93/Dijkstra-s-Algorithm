# Dijkstra-s-Algorithm
Single source shortest paths algorithm

Dijkstra's algorithm uses a greedy approach to find the shortest path from a given source vertex to all the other vertices in a directed graph. 
Hence called Single-source shortest paths algorithm.
Each node in the graph/network can be modelled as a vertex & the links leading to / coming from the vertices can be modelled as edges.
Each of these edges have corresponding weight. 
The algorithm starts from the given source vertex, incrementally finds a path to all other vertices in the graph. The goal is to minimise 
the sum of weights in each path. (In this program, weight correspond to transmission time). It yields a locally optimum solution. 

Files:
	graph.java --> To evaluate shortest paths
	Vertex.java --> Represents a vertex in the graph
	Edge.java --> Represents an edge in the graph

Run graph.java

Input file: txt file that contains initial state of the graph; Each link representing two directed edges is listed on a line, and is specifed by the names of its
two vertices followed by the transmission time. Vertices are simply strings (vertex names with no spaces) and the transmission times are foating point numbers. 
: "vertexA vertexB weight", 1 in each line
Use commands:
  addedge tail_vertex head_vertex transmit_time : to add a new edge
  deleteedge tail_vertex head_vertex : to add an edge
  edgedown tail_vertex head_vertex : corresponding edge is "down" and hence, unavailable for use
  edgeup tail_vertex head_vertex : corresponding edge is "up" and hence, available for use
  vertexdown vertex : corresponding vertex is "down" and hence, unavailable for use
  vertexup vertex : corresponding vertex is "up" and hence, available for use
  path from_vertex to_vertex : query to find the shortest path from from_vertex to to_vertex
  print : to print the content of the graph 
  reachable: prints all vertices reachable from a given vertex
  quit
  
