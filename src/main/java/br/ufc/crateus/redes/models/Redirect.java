package br.ufc.crateus.redes.models;

public class Redirect {
	private Device source;
	private Device target;
	private Link link;
	
	public Redirect(Device source, Device target, Link link) {
		this.source = source;
		this.target = target;
		this.link = link;
	}
	
	@Override
	public String toString() {
		return String.format("%s -> %s: %s", source, target, link);
	}
	
	public boolean isSourceByRedirect(Device device) {
		return this.source.equals(device);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof Redirect)) {
			return false;
		}
		
		Redirect other = (Redirect) obj;
		return this.source.equals(other.source) && this.target.equals(other.target);
	}
	
	public Device getTarget() {
		return target;
	}
	
	public Device getSource() {
		return source;
	}
	
	public Link getLink() {
		return link;
	}
}
