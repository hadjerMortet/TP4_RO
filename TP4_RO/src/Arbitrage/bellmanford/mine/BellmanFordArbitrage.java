package arbitrage.bellmanford.mine;

import arbitrage.cache.DataCache;
import arbitrage.factory.*;
import arbitrage.model.OptimalRate;
import java.util.*;


public class BellmanFordArbitrage implements Arbitrage {
    private double[] dist;        
    private int[] p;               
    private OptimalRate adj[][];  
    private final int INF = Integer.MAX_VALUE;
    private int vertex, source, number;
    private List<Integer> cycle;
    private Set<List> cycleList;
    private String currencies[];
    

    public BellmanFordArbitrage() {
        cycleList = new LinkedHashSet();
    }
    

    @Override
    public void start(Data data) {
        currencies = data.getCurrencies();
        adj = DataCache.getAdjencyMatrix(data, currencies);
        vertex = adj.length;
        source = 0;
        callBellmanFord();
    }
    

    public void callBellmanFord() {
        initializeSingleSource();
        for (int k = 1; k < vertex; k++)  
            for (int u = 0; u < vertex; u++) 
                for (int v = 0; v < vertex; v++)  
                    relax(u, v);
            
        if (hasNegativeCycle()) checkArbitrage();
        else System.out.println("No arbitrage opportunity");
    }
    
    
    private void initializeSingleSource() {
        dist = new double[vertex];
        p = new int[vertex];
        for (int i = 0; i < vertex; i++) {
            dist[i] = INF;
            p[i] = -1;
        }
        dist[source] = 0;
    }
    

    private void relax(int u, int v) {
        double weight = adj[u][v].getValue();
        if (u == v || weight == -1) return; 
        weight = -Math.log(weight);
        if (dist[v] > dist[u] + weight) {
            dist[v] = dist[u] + weight;
            p[v] = u;
        }
    }
    
   
    private boolean hasNegativeCycle() {
        for (int u = 0; u < vertex; u++) 
            for (int v = 0; v < vertex; v++) {
                double weight = adj[u][v].getValue();
                if(u == v || weight == -1) continue;
                weight = -Math.log(weight);
                if (dist[v] > dist[u] + weight)
                    findNegativeCyclePath(v);
            }
        
        return !cycleList.isEmpty();
    }
    
    
    private void findNegativeCyclePath(int v) {
        if(v == 0) return;
        boolean visited[] = new boolean[p.length];

        Stack<Integer> path = new Stack<>();
        for (; ; v = p[v]) {
            path.push(v);
            if (v == 0 || visited[v]) break;
            else visited[v] = true;
        }

        cycle = new ArrayList();
        for (Integer i : path) {
            if(cycle.contains(i)) break;
            cycle.add(i);
        }
        
        if (cycle.size() > 1) cycle.add(cycle.get(0));
        cycleList.add(cycle);
    }
    
    
    private void checkArbitrage(){
       
        
        cycleList.forEach(v -> {
            System.out.printf("\nArbitrage %d: \n", ++number);
            double stake = 1000;
            for (int i = 0; i < v.size() - 1; i++) {
                int from = (int) v.get(i);
                int to = (int) v.get(i + 1);
                OptimalRate opt = adj[from][to];

                System.out.printf("%10.4f %s = %10.4f %s (%s)\n", stake, currencies[from], stake *= opt.getValue(), currencies[to], opt.getBankName());
            }
        });
    }
    
    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }

}

