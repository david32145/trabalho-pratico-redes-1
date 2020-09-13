package br.ufc.crateus.redes.dijkstra;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.ufc.crateus.redes.models.Device;
import br.ufc.crateus.redes.models.Link;
import br.ufc.crateus.redes.models.Route;
import br.ufc.crateus.redes.models.RoutingTable;
import br.ufc.crateus.redes.models.RoutingTableDetails;

public class AlgorithmDijkstra {
	private int[] vertex;
	private double[][] edge;
	private int[] p;
	private double[] distanceX;
	private boolean[] closedVertex;
	
	private List<Device> devices;
	private List<Link> links;
	private int[] parentDevice;
	private double[] distance;
	private boolean[] deviceClosed;
	
	private List<RoutingTable> routingTables;
	private RoutingTable routingTableTmp;
	private Device device;
	
	public AlgorithmDijkstra(int[] vertex, double[][] edge) {
		this.vertex = vertex;
		this.edge = edge;
		
		this.p = new int[vertex.length];
		this.distanceX = new double[vertex.length];
		this.closedVertex = new boolean[vertex.length];
		
		for (int i = 0; i < this.p.length; i++) {
			this.p[i] = -1;
			this.distanceX[i] = Double.MAX_VALUE;
		}
	}
	
	public AlgorithmDijkstra(List<Device> devices, List<Link> links) {
		this.devices = devices;
		this.links = links;
		this.routingTables = new ArrayList<RoutingTable>();
	}
	
	public List<RoutingTable> makeRoutes() {
		for(int i = 0; i < devices.size(); i++) {
			Device device = devices.get(i);
			this.routingTableTmp = getOrCreate(i);
			this.init(device);
			this.run(device);
		}
		return this.routingTables;
	}
	
	private RoutingTable getOrCreate(int index) {
		if(index > this.routingTables.size() - 1) {
			this.routingTables.add(new RoutingTable());
		}
		return this.routingTables.get(index);
	}
	
	private void init(Device device) {
		this.device = device;
		this.parentDevice = new int[devices.size()];
		this.distance = new double[devices.size()];
		this.deviceClosed = new boolean[devices.size()];
		for(int i = 0; i < devices.size(); i++) {
			this.distance[i] = Double.MAX_VALUE;
			this.deviceClosed[i] = false;
			this.parentDevice[i] = -1;
		}
	}
	
	public void run(Device device) {
		int index = this.devices.indexOf(device);
		this.distance[index] = 0;
		this.process(null);
	}
	
	private void process(Void v) {
		Integer index = this.getMinorClosed(null);
		if(index == null) {
			return;
		}
		this.deviceClosed[index] = true;
		List<Link> adjacentsLinks = this.getAdjacentsLinks(this.device);
		
		for(Link link : adjacentsLinks) {
			Device adjacentDevice = link.getTarget();
			int adjacentDeviceIndex = this.devices.indexOf(adjacentDevice);
			if(distance[adjacentDeviceIndex] > this.distance[index] + link.getWeight()) {
				distance[adjacentDeviceIndex] = this.distance[index] + link.getWeight();
				this.parentDevice[adjacentDeviceIndex] = index;
			}
			this.routingTableTmp.put(device, new RoutingTableDetails(adjacentDevice, link));
		}
		process(null);
	}
	
	public void run(int sourceIndex) {
		this.distanceX[sourceIndex] = 0;
		process();
	}
	
	private void process() {
		Integer index = this.getMinorClosed();
		if(index == null) {
			return;
		}
		this.closedVertex[index] = true;
		
		List<Integer> adjacents = this.getAdjacents(index);
		
		for (Integer vertex : adjacents) {
			if(distanceX[vertex] > this.distanceX[index] + this.edge[index][vertex]) {
				this.distanceX[vertex] = this.distanceX[index] + this.edge[index][vertex];
				this.p[vertex] = index;
			}
		}
		process();
	}
	
	private List<Integer> getAdjacents(int index) {
		double [] adj = this.edge[index];
		List<Integer> adjacentsList = new ArrayList<Integer>();
		for (int i = 0; i < adj.length; i++) {
			if(adj[i] > 0) {
				adjacentsList.add(i);
			}
		}
		return adjacentsList;
	}
	
	private List<Link> getAdjacentsLinks(Device device) {
		return this.links.stream()
				.filter(link -> link.getTarget().equals(device))
				.collect(Collectors.toList());
	}
	
	private Integer getMinorClosed(Void v) {
		Integer deviceIndex = null;
		for(int i = 0; i < distance.length; i++) {
			if(!this.deviceClosed[i]) {
				if(deviceIndex == null || distance[i] < distance[deviceIndex]) {
					deviceIndex = i;
				}
			}
		}
		return deviceIndex;
	}
	
	private Integer getMinorClosed() {
		Integer vertexIndex = null; 
		for (int i = 0; i < distanceX.length; i++) {
			if(!this.closedVertex[i]) {
				if(vertexIndex == null || distanceX[i] < distanceX[vertexIndex] ) {
					vertexIndex = i;
				}
			}
		}
		return vertexIndex;
	}
	
	public double[] getDistance() {
		return distanceX;
	}
	
	public int[] getP() {
		return p;
	}
}
