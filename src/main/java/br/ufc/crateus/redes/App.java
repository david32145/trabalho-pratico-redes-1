package br.ufc.crateus.redes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.ufc.crateus.redes.dijkstra.AlgorithmDijkstra;
import br.ufc.crateus.redes.reader.NetworkFileReader;

public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
        String networkFileName = "networkInput.txt";
        InputStream inputStream = new FileInputStream(new File(networkFileName));
        NetworkFileReader networkFileReader = new NetworkFileReader(inputStream);
        networkFileReader.read();
        
        networkFileReader
        	.getDevices()
        	.forEach(device -> System.out.printf("%s ", device));
        
        System.out.println();
        
        networkFileReader.getLinks()
        	.forEach(link -> System.out.printf("(%s, %s, %d)\n", link.getSource(), link.getTarget(), (int)link.getWeight()));
        
        System.out.println();
        
        networkFileReader.getRoutes()
    	.forEach(route -> System.out.printf("(%s, %s)\n", route.getSource(), route.getTarget()));
    
        System.out.println();
        
        int vertex[] = {0, 1, 2, 3, 4};
        
        double edge[][] = new double[5][5];
        set(edge, 0, 1, 1);
        set(edge, 0, 3, 3);
        set(edge, 0, 4, 10);
        set(edge, 1, 2, 5);
        set(edge, 2, 4, 1);
        set(edge, 3, 2, 2);
        set(edge, 3, 4, 6);
        
        AlgorithmDijkstra algorithmDijkstra = new AlgorithmDijkstra(vertex, edge);
        algorithmDijkstra.run(0);
        
        for(double i : algorithmDijkstra.getDistance()) {
        	System.out.printf("%f ", i);
        }
    }
    
    public static void set(double edge[][], int i, int j, double v) {
    	edge[i][j] = v;
    }
}
