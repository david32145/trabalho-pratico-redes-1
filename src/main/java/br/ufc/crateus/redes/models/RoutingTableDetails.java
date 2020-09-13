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
	public boolean equals(Object obj) {
		return target.equals(((RoutingTableDetails)obj).target);
	}
	
	@Override
	public String toString() {
		return String.format("%s,(%s,%s),%.2f", target, link.getSource(), link.getTarget(), link.getWeight());
	}
}
