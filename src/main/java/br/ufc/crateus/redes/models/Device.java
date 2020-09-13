package br.ufc.crateus.redes.models;

public class Device implements Comparable<Device> {
	private String label;
	
	public Device(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return label;
	}
	
	@Override
	public int compareTo(Device arg0) {
		return this.label.compareTo(arg0.label);
	}
}
