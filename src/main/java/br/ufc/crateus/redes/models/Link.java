package br.ufc.crateus.redes.models;

public class Link {
	private Device source;
	private Device target;
	private double weight;
	
	public Link(Device source, Device target, double weight) {
		this.source = source;
		this.target = target;
		this.weight = weight;
	}
	
	public Device getSource() {
		return source;
	}
	
	public Device getTarget() {
		return target;
	}
	
	public double getWeight() {
		return weight;
	}
}
