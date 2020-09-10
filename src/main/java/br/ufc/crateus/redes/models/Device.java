package br.ufc.crateus.redes.models;

public class Device {
	private String label;
	
	public Device(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	@Override
	public String toString() {
		return label;
	}
}
