package br.ufc.crateus.redes.dijkstra;

import java.util.ArrayList;
import java.util.List;

import br.ufc.crateus.redes.models.Device;
import br.ufc.crateus.redes.models.Link;
import br.ufc.crateus.redes.models.Redirect;

public class NetworkRouter {
	private Device[] devices;
	private Link[] links;
	
	private boolean[] devicesClosed;
	private double[] distanceTo;
	private int[] parentIndex;
	
	private List<Redirect> redirects;
	
	private int currentDeviceIndex;
	
	public NetworkRouter(Device[] devices, Link[] links) {
		this.devices = devices;
		this.links = links;
		this.redirects = new ArrayList<>();
	}
	
	public void buildRoutes() {
		for(int i = 0; i < this.devices.length; i++) {
			this.init();
			this.distanceTo[i] = 0;
			this.currentDeviceIndex = i;
			runInteraction(0);			
		}
	}
	
	public void init() {
		this.devicesClosed = new boolean[this.devices.length];
		this.parentIndex = new int[this.devices.length];
		this.distanceTo = new double[this.devices.length];
		for(int i = 0; i < this.devices.length; i++) {
			this.devicesClosed[i] = false;
			this.parentIndex[i] = -1;
			this.distanceTo[i] = Double.MAX_VALUE;
		}
	}
	
	private void runInteraction(int interaction) {
		Integer index = this.findMinorDevice();
		if(index == null) {
			return;
		}
		this.devicesClosed[index] = true;
		Device device = this.devices[index];
		double deviceDistance = this.distanceTo[index];
		List<Link> adjacentsLinks = this.findAdjacentsLinks(device);
		for(Link link : adjacentsLinks) {
			Device adjacentDevice = link.getOther(device);
			int adjacentDeviceIndex = this.findIndexOfDevice(adjacentDevice);
			double adjacentDeviceDistance = this.distanceTo[adjacentDeviceIndex];
			if(adjacentDeviceDistance > deviceDistance + link.getWeight()) {
				this.distanceTo[adjacentDeviceIndex] = deviceDistance + link.getWeight();
				this.parentIndex[adjacentDeviceIndex] = index;
				Redirect redirect = new Redirect(adjacentDevice, this.devices[currentDeviceIndex], new Link(adjacentDevice, device, this.distanceTo[adjacentDeviceIndex]));
				this.redirects.add(redirect);
			}
		}
		runInteraction(interaction + 1);
	}
	
	private Integer findMinorDevice() {
		Integer index = null;
		for(int j = 0; j < this.devices.length; j++) {
			if(devicesClosed[j]) continue;
			if(index == null || this.distanceTo[j] < this.distanceTo[index]) {
				index = j;
			}
		}
		return index;
	}
	
	private List<Link> findAdjacentsLinks(Device device) {
		List<Link> adjacentsLinks = new ArrayList<Link>();
		for(Link l : this.links) {
			if(l.containDevice(device)) {
				Device other = l.getOther(device);
				int deviceIndex = this.findIndexOfDevice(other);
				if(!devicesClosed[deviceIndex]) {
					adjacentsLinks.add(l);
				}
			}
		}
		return adjacentsLinks;
	}
	
	private int findIndexOfDevice(Device device) {
		for(int i = 0; i < this.devices.length; i++) {
			if(this.devices[i].equals(device)) {
				return i;
			}
		}
		return -1;
	}
	
	public List<Redirect> getRedirects() {
		return redirects;
	}
}
