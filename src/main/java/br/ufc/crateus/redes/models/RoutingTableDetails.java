package br.ufc.crateus.redes.models;

public class RoutingTableDetails {
	private Device target;
	private Link link;
	
	public RoutingTableDetails(Device target, Link link) {
		this.target = target;
		this.link = link;
	}
	
	public Device getTarget() {
		return target;
	}
	
	public Link getLink() {
		return link;
	}
	
	@Override
	public String toString() {
		return String.format("%s,(%s,%s),%f", target, link.getSource(), link.getTarget(), link.getWeight());
	}
}
