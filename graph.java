/**
 *
 * @author madhu ramachandra
 */

import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * 
 * Used to signal violations of preconditions for various shortest path algorithms
 */
class GraphException extends RuntimeException
{
    public GraphException( String name )
    {
        super( name );
    }
}

/**
 * 
 * Graph class: evaluate shortest paths.
 */
public class graph {

    public static final int INFINITY = Integer.MAX_VALUE;
    private static final Map<String,Vertex> vertexMap = new LinkedHashMap<>( );
    private ArrayList<Vertex> q = null;
    private int n;
    private Map<String,Vertex> s = null;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Return weight of current edge between src & dest vertices
     * @param src
     * @param dest
     * @return 
     */
    private static double getWeight(Vertex src, Vertex dest){
        for(Edge w : src.edges){
            if((src.equals(w.getSource())) && dest.equals(w.getDest())){
                return w.getWeight();
            }
        }
        return 0;
    }

    /**
     * Set the weight of current edge between src & dest vertices
     * @param src
     * @param dest
     * @param weight 
     */
    private void setWeight(Vertex src, Vertex dest, Double weight){
        for(Edge w : src.edges){
            if((src.equals(w.getSource())) && dest.equals(w.getDest())){
                w.setWeight(weight);
            }
        }
    }

    /**
     * Delete current edge between src & dest vertices
     * @param source
     * @param destn 
     */
    private static void deleteEdge(String source, String destn){
        Vertex src = vertexMap.get(source);
        Vertex dest = vertexMap.get(destn);

        for(Edge w : src.edges){
            if((src.equals(w.getSource())) && dest.equals(w.getDest())){
                src.edges.remove(w);
                break;
            }
        }
    }

    /**
     * Change the status of current edge from up to down or from down to up
     * @param source
     * @param destn
     * @param newStatus
     */
    private static void ChangeEdgeStatus(String source, String destn, String newStatus){
        Vertex src = vertexMap.get(source);
        Vertex dest = vertexMap.get(destn);

        for(Edge w : src.edges){
            if((src.equals(w.getSource())) && dest.equals(w.getDest())){
                w.setStatus(newStatus);
                break;
            }
        }
    }

    /**
     * Change the status of current vertex from up to down or from down to up
     * @param source
     * @param newStatus
     */
    private static void ChangeVertexStatus(String source, String newStatus){
        Vertex src = vertexMap.get(source);
        src.setStatus(newStatus);
    }

    /**
     * Print total distance & all vertices along shortest path from source to destination
     * @param destName 
     */
    private void printPath( String destName ){
        Vertex w = vertexMap.get( destName );

        if( w == null )
            throw new NoSuchElementException( "Destination vertex not found" );
        else if( w.dist == INFINITY )
            System.out.println( destName + " is unreachable" );
        else
        {
            printPath( w );
            System.out.print(" " + df.format(w.dist));
            System.out.println( );
        }
    }

    /**
     * recursively prints vertices along shortest path from source to destination
     * @param dest 
     */
    private void printPath( Vertex dest ){
        if( dest.prev != null ){
            printPath( dest.prev );
            System.out.print( " " );
        }
        System.out.print( dest.name );
    }

    /**
     * Initializes the vertex output info prior to running any shortest path algorithm.
     */
    private void clearAll( ){
        for( Vertex v : vertexMap.values( ) )
            v.reset( );
    }
    
    /**
     * Routine that restores min_heap condition when the distance parameter of a vertex is decreased
     * (ie., updated in dijkstra's algorithm)
     * @param v 
     */
    private void heap_decrease_key(Vertex v){        
        int i = q.indexOf(v);

        while(i>0 && v.dist < q.get((i-1)/2).dist) {
            Vertex parent = q.get((i-1)/2);
            Vertex current = q.get(i);
            
            q.set((i-1)/2, current);
            q.set(i, parent);
 
            i = (i-1)/2;
        }        
        q.set(i, v);
    }

    /**
     * Returns the root vertex of the min heap, ie., the vertex with least distance &
     * Calls min_heapify() routine to form a min heap again
     * @return 
     */
    private Vertex extract_min(){
        if(n < 0){
            System.out.println("Heap underflow");
        }

        Vertex min = q.get(0);
        Vertex last = q.get(n-1);

        q.set(0, last);
        q.remove(n-1);
        n = n - 1;

        min_heapify(0);

        return min;
    }

    /**
     * Rearranges the vertices in the heap to form a binary min heap.
     * @param i 
     */
    private void min_heapify(int i){
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        int smallest;

        if(l < n && q.get(l).dist < q.get(i).dist) 
            smallest = l;
        
        else 
            smallest = i;

        if(r < n && q.get(r).dist < q.get(smallest).dist)
            smallest = r;
        
        if(smallest != i){
            
            Vertex temp = q.get(i);

            q.set(i, q.get(smallest));
            q.set(smallest, temp);

            min_heapify(smallest);
        }
    }
    
    /**
     * Implementation of Dijkstra's algorithm to find shortest path from source vertex to all reachable
     * vertices in the graph
     * 
     * Here, q is a binary heap
     * heap_decrease_key() takes O(|E| lg|V|) time, as this operation has to be performed on atmost |E| edges
     * extract_min() takes O(|V| lg|V|) time, as this operation has to be performed on |V| vertices
     * Total running time = O((|E|+|V|) lg|V|)
     * 
     * @param startName 
     */
    private void dijkstra( String startName ){
        clearAll( ); 

        Vertex start = vertexMap.get( startName );

        if( start == null )
            throw new NoSuchElementException( "Start vertex not found" );

        start.dist = 0.0;

        q = new ArrayList<>();
        q.addAll(vertexMap.values());
        
        heap_decrease_key(start);
        
        n = q.size();
        
        s = new HashMap<>( );
        while( q.size() > 0 ){

            Vertex v = extract_min();
            
            s.put(v.name, v);

            if(v.status.equals("up")){
                for(Edge e : v.edges) {
                    if(e.status.equals("up")) {
                        Vertex w = e.getDest();

                        if( w.dist > v.dist + getWeight(v, w))
                        {
                            w.dist = v.dist + getWeight(v, w);
                            w.prev = v;
                            
                            heap_decrease_key(w);
                        }
                    }
                }
            }
        }
    }

    /**
     * Prints contents of the graph in alphabetical order
     */
    private static void print(){
        Object[] keys = vertexMap.keySet().toArray();
        Arrays.sort(keys);

        for(Object k : keys){
            Vertex v = vertexMap.get(k);
            ArrayList<String> adj_v = new ArrayList<>();

            System.out.print(v.name);
            if(v.status.equals("down"))
                System.out.print("   DOWN");

            for(Edge e : v.edges){
                adj_v.add(e.getDest().name);
            }

            Collections.sort(adj_v);

            System.out.println();
            for(String s : adj_v) {
                System.out.print("    " + s + " " + df.format(getWeight(v, vertexMap.get(s))));  

                for(Edge e : v.edges){
                    if(e.getDest().equals(vertexMap.get(s))){
                        if(e.status.equals("down")){
                            System.out.print("   DOWN");
                        }
                    }
                }
                System.out.println();
            }                                
        }            
    }

    /**
     * Implementation of BFS to find all reachable vertices from every vertex in the graph
     * Queue is used to store all discovered vertices
     * HashSet is used to store all finished vertices
     *      An ArrayList takes O(n) time to check whether the list contains an item
     *      But, HashSet takes O(1) time for the same operation.
     * Initializations, enqueue & dequeue operations take O(1) time
     * for each vertex, all its adjacent vertices are scanned. This takes atmost O(|V|+|E|) time
     * Thus, BFS takes O(|V| + |E|) time for 1 source
     * As we are performing this for |V| vertices, total time taken is O(|V|*(|V|+|E|))
     * 
     * @param adjVertices
     * @param source 
     * @return  
     */
    public static HashSet<String> reachable(HashMap<String, List<String>> adjVertices, String source) {
        System.out.println(source);

        Queue<String> queue = new ArrayDeque<>();
        queue.add(source);
        HashSet<String> finished = new HashSet<>();
        
        while(!queue.isEmpty()){
            String vertex = queue.poll();
            if(!finished.contains(vertex)){
                if(vertexMap.get(vertex).status.equals("up")) {
                    queue.addAll(adjVertices.get(vertex)); 
                    finished.add(vertex);                        
                }
            }                
        }
        return finished;
    }

    /**
     * Process a request; Takes in queries from the standard input & calls appropriate routines for their execution
     * @param in
     * @param g
     * @return 
     */
    private static boolean processRequest( Scanner in, graph g ){
        try{
            String cmd = in.nextLine( );
            StringTokenizer st = new StringTokenizer( cmd );

            switch (st.countTokens( )) {
                case 1:
                    {
                        String cmd1 = st.nextToken( );
                        if(cmd1.equals("print")){
                            print();
                        }

                        if(cmd1.equals("reachable")){

                            HashMap<String, List<String>> newMap = new HashMap<>();

                            Object[] keys = vertexMap.keySet().toArray();
                            Arrays.sort(keys);

                            for(Object k : keys){
                                Vertex v = vertexMap.get(k);

                                List<String> ls = new ArrayList<>();

                                for(Edge e : v.edges){
                                    ls.add(e.getDest().name);
                                }

                                Collections.sort(ls);

                                newMap.put(v.name, ls);                                                                        
                            }

                            for(Object k : keys){
                                Vertex v = vertexMap.get(k);
                                if(v.status.equals("up")) {
                                    HashSet<String> finished = new HashSet<>();
                                    finished = reachable(newMap,v.name);
                                    
                                    List ls = new ArrayList(finished);
                                    Collections.sort(ls);

                                    for(Object s : ls){
                                        if(!s.equals(v.name)) {
                                            if(vertexMap.get(s).status.equals("up")) {
                                                 System.out.println("    " + s);
                                             }
                                         }
                                     }
                                }   
                            }                                
                        }    

                        if(cmd1.equals("quit")){
                            System.exit(0);
                        }
                        break;
                    }

                case 2:
                    {
                        String cmd1 = st.nextToken( );
                        String src = st.nextToken( );

                        if(cmd1.equals("vertexdown")){
                            ChangeVertexStatus(src, "down");
                        }

                        if(cmd1.equals("vertexup")){
                            ChangeVertexStatus(src, "up");
                        }    
                        break;
                    }

                case 3:
                    {
                        String cmd1 = st.nextToken( );
                        String src = st.nextToken( );
                        String dest = st.nextToken( );

                        if(cmd1.equals("deleteedge")){                                
                            deleteEdge(src, dest);
                        }       

                        if(cmd1.equals("edgedown")){
                            ChangeEdgeStatus(src, dest, "down");

                        }   

                        if(cmd1.equals("edgeup")){
                            ChangeEdgeStatus(src, dest, "up");

                        }   

                        if(cmd1.equals("path")){
                            g.dijkstra( src );
                            g.printPath( dest );
                        }       
                        break;
                    }
                case 4:
                    {
                        String cmd1 = st.nextToken( );
                        if(cmd1.equals("addedge")){
                            String source = st.nextToken( );
                            String dest = st.nextToken( );
                            String weight_str = st.nextToken( );
                            double w = Double.parseDouble(weight_str);

                            g.addEdge(source, dest, w);
                        }       
                        break;
                    }
                default:  System.out.println( "Invalid arguments" );
                    break;
            }
        }
        catch( NoSuchElementException e )
          { return false; }
        catch( GraphException e )
          { System.err.println( e ); }
        return true;
    }

    /**
     * Add a new edge to the graph; If the edge already exists, update its weight
     * @param sourceName
     * @param destName
     * @param weight_d 
     */
    private void addEdge( String sourceName, String destName, double weight_d ){
        Vertex v = getVertex( sourceName );
        Vertex w = getVertex( destName );

        if(v.adj.contains(w)){
            setWeight(v, w, weight_d);
        }

        else {
            Edge e1 = new Edge(v, w, weight_d);
            v.adj.add( w );
            v.edges.add(e1);
        }
    }

    /**
     * If vertexName is not present in vertexMap, add it to vertexMap.
     * In either case, return the Vertex.
     * @param vertexName
     * @return 
     */
    private Vertex getVertex( String vertexName ){
        Vertex v = vertexMap.get( vertexName );
        if( v == null ) {
            v = new Vertex( vertexName );
            vertexMap.put( vertexName, v );
        }
        return v;
    }

    /**
     * Main routine
     * 1. Reads a file containing edges (supplied as a command-line parameter);
     * 2. Forms the graph;
     * 3. Waits for user's queries, until "quit" is entered
     * @param args 
     */
    @SuppressWarnings("empty-statement")
    public static void main( String[] args ) {
        graph g = new graph( );
        try {                
            FileReader fin = new FileReader( args[0] );
            Scanner graphFile = new Scanner( fin );

            // Read the edges and insert
            String line;
            while( graphFile.hasNextLine( ) ) {
                line = graphFile.nextLine( );
                StringTokenizer st = new StringTokenizer( line );

                try{
                    if( st.countTokens( ) != 3 ){
                        System.err.println( "Skipping ill-formatted line " + line );
                        continue;
                    }

                    String source  = st.nextToken( );
                    String dest    = st.nextToken( );

                    String weight_str = st.nextToken();
                    double w = Double.parseDouble(weight_str);

                    g.addEdge(source, dest, w);
                    g.addEdge(dest, source, w);
                }
                catch( NumberFormatException e )
                  { System.err.println( "Skipping ill-formatted line " + line ); }
             }
         }
         catch( IOException e )
           { System.err.println( e ); }

         Scanner in = new Scanner( System.in );
         while( processRequest( in, g ) )
     ;
    }
}