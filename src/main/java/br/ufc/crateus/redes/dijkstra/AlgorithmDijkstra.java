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
	private int[] nextHops;
	private boolean[] deviceClosed;
	
	private List<RoutingTable> routingTables;
	private Device device;
	private int interaction;
	
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
			this.init(device);
			this.run(device);
		}
		return this.routingTables;
	}
	
	private RoutingTable getCurrentRoutingTable(int index) {
		if(index > this.routingTables.size() - 1) {
			if(this.interaction == 0) {
				this.routingTables.add(new RoutingTable());
			} else {
				int lastInteraction = this.interaction - 1;
				RoutingTable lastRoutingTable = this.routingTables.get(lastInteraction);
				this.routingTables.add(lastRoutingTable.clone());
			}
		}
		return this.routingTables.get(index);
	}
	
	private void init(Device device) {
		this.device = device;
		this.interaction = 0;
		this.parentDevice = new int[devices.size()];
		this.distance = new double[devices.size()];
		this.nextHops = new int[devices.size()];
		this.deviceClosed = new boolean[devices.size()];
		for(int i = 0; i < devices.size(); i++) {
			this.distance[i] = Double.MAX_VALUE;
			this.deviceClosed[i] = false;
			this.parentDevice[i] = -1;
			this.nextHops[i] = -1;
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
		Device currentDevice = this.devices.get(index);
		List<Link> adjacentsLinks = this.getAdjacentsLinks(currentDevice);
		RoutingTable routingTable = this.getCurrentRoutingTable(this.interaction);
		for(Link link : adjacentsLinks) {
			Device adjacentDevice = link.getOther(currentDevice);
			int adjacentDeviceIndex = this.devices.indexOf(adjacentDevice);
			if(distance[adjacentDeviceIndex] > this.distance[index] + link.getWeight()) {
				distance[adjacentDeviceIndex] = this.distance[index] + link.getWeight();
				this.parentDevice[adjacentDeviceIndex] = index;
			}
			Link l = new Link(adjacentDevice, currentDevice, distance[adjacentDeviceIndex]);
			routingTable.put(adjacentDevice, new RoutingTableDetails(device, l));
		}
		this.interaction++;
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
				.filter(link -> {
					if(!link.containDevice(device)) {
						return false;
					}
					int indexOfOther = this.devices.indexOf(link.getOther(device));
					if(this.deviceClosed[indexOfOther]) {
						return false;
					}
					return true;
				 })
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
