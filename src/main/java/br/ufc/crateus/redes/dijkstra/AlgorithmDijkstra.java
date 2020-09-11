package br.ufc.crateus.redes.dijkstra;

import java.util.ArrayList;
import java.util.List;

public class AlgorithmDijkstra {
	private int[] vertex;
	private double[][] edge;
	private int[] p;
	private double[] distance;
	private boolean[] closedVertex;
	
	public AlgorithmDijkstra(int[] vertex, double[][] edge) {
		this.vertex = vertex;
		this.edge = edge;
		
		this.p = new int[vertex.length];
		this.distance = new double[vertex.length];
		this.closedVertex = new boolean[vertex.length];
		
		for (int i = 0; i < this.p.length; i++) {
			this.p[i] = -1;
			this.distance[i] = Double.MAX_VALUE;
		}
	}
	
	public void run(int sourceIndex) {
		this.distance[sourceIndex] = 0;
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
			if(distance[vertex] > this.distance[index] + this.edge[index][vertex]) {
				this.distance[vertex] = this.distance[index] + this.edge[index][vertex];
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
	
	private Integer getMinorClosed() {
		Integer vertexIndex = null; 
		for (int i = 0; i < distance.length; i++) {
			if(!this.closedVertex[i]) {
				if(vertexIndex == null || distance[i] < distance[vertexIndex] ) {
					vertexIndex = i;
				}
			}
		}
		return vertexIndex;
	}
	
	public double[] getDistance() {
		return distance;
	}
}
