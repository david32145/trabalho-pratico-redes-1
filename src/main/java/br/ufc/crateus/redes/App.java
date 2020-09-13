package br.ufc.crateus.redes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import br.ufc.crateus.redes.dijkstra.AlgorithmDijkstra;
import br.ufc.crateus.redes.models.Device;
import br.ufc.crateus.redes.models.Link;
import br.ufc.crateus.redes.models.RoutingTableDetails;
import br.ufc.crateus.redes.reader.NetworkFileReader;

public class App {
	public static void main(String[] args) throws FileNotFoundException {
		String networkFileName = "networkInput.txt";
		InputStream inputStream = new FileInputStream(new File(networkFileName));
		NetworkFileReader networkFileReader = new NetworkFileReader(inputStream);
		networkFileReader.read();

		List<Device> devices = networkFileReader.getDevices();

//        System.out.println();

		List<Link> links = networkFileReader.getLinks();

//        System.out.println();

//        networkFileReader.getRoutes()
//    	.forEach(route -> System.out.printf("(%s, %s)\n", route.getSource(), route.getTarget()));

//        System.out.println();

		AtomicInteger i = new AtomicInteger(0);
		AlgorithmDijkstra algorithmDijkstra = new AlgorithmDijkstra(devices, links);
		algorithmDijkstra.makeRoutes().forEach(routingTable -> {
			System.out.println("Int. " + i.getAndIncrement());
			devices.forEach(d -> System.out.printf("|%s:\t", d));
			System.out.println();
			for(int j = 0; j < routingTable.size(); j++) {
				for(int k = 0; k < devices.size(); k++) {
					RoutingTableDetails detail = routingTable.getByDeviceByIndex(devices.get(k), j);
					System.out.printf("|%s\t", detail);
				}
				System.out.println();
			}
			System.out.println();
//			devices.forEach(device -> {
//				System.out.printf(device.getLabel() + " ");
//				routingTable.getByDevice(device)
//					.forEach(routeDetais -> {
//						System.out.println(routeDetais);
//					});
//			});
		});
		;
//        int vertex[] = {0, 1, 2, 3, 4};
//        
//        double edge[][] = new double[5][5];
//        set(edge, 0, 1, 1);
//        set(edge, 0, 3, 3);
//        set(edge, 0, 4, 10);
//        set(edge, 1, 2, 5);
//        set(edge, 2, 4, 1);
//        set(edge, 3, 2, 2);
//        set(edge, 3, 4, 6);
//        
//        AlgorithmDijkstra algorithmDijkstra = new AlgorithmDijkstra(vertex, edge);
//        algorithmDijkstra.run(0);
//        
//        for(double i : algorithmDijkstra.getDistance()) {
//        	System.out.printf("%f ", i);
//        }
//        
//        for(int i : algorithmDijkstra.getP()) {
//        	System.out.printf("%d -> %d\n", 0, i);
//        }
	}

	public static void set(double edge[][], int i, int j, double v) {
		edge[i][j] = v;
	}
}
