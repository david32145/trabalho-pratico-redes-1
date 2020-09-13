package br.ufc.crateus.redes.models;

public class Route {
	private Device source;
	private Device target;
	
	public Route(Device source, Device target) {
		this.source = source;
		this.target = target;
	}
	
	public Device getSource() {
		return source;
	}
	
	public Device getTarget() {
		return target;
	}
	
	@Override
	public String toString() {
		return String.format("(%s,%s)", source, target);
	}
}
