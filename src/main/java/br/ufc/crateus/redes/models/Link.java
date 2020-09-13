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
	
	public Device getOther(Device device) {
		if(device.equals(source)) {
			return target;
		}
		return source;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public boolean containDevice(Device device) {
		return source.equals(device) || target.equals(device);
	}
	
	@Override
	public String toString() {
		return  String.format("(%s, %s, %.1f)", source, target, weight);
	}
}
