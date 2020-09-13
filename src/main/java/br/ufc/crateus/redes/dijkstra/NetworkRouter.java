package br.ufc.crateus.redes.dijkstra;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import br.ufc.crateus.redes.models.Device;
import br.ufc.crateus.redes.models.Link;
import br.ufc.crateus.redes.models.Redirect;
import br.ufc.crateus.redes.models.Route;

public class NetworkRouter {
	private Device[] devices;
	private Link[] links;
	
	private boolean[] devicesClosed;
	private double[] distanceTo;
	private int[] parentIndex;
	
	private List<List<Redirect>> redirects;
	
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
			nomarlizeRedirecs();
		}
		System.out.println();
	}
	
	private void nomarlizeRedirecs() {
		for(int i = 1; i < this.redirects.size(); i++) {
			List<Redirect> previos = this.redirects.get(i-1);
			List<Redirect> current = this.redirects.get(i);
			previos.forEach(r -> this.add(current, r, true));
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
	
	private List<Redirect> getRedirectsFromInteraction(int interaction) {
		if(interaction > this.redirects.size() - 1) {
			if(interaction == 0) {
				this.redirects.add(new ArrayList<Redirect>());
			} else {
				List<Redirect> oldsRedirects = this.redirects.get(interaction-1);
				this.redirects.add(this.cloneRedirectSet(oldsRedirects));
			}
		}
		return this.redirects.get(interaction);
	}
	
	private int runInteraction(int interaction) {
		Integer index = this.findMinorDevice();
		if(index == null) {
			return interaction;
		}
		this.devicesClosed[index] = true;
		List<Redirect> redirectSet = this.getRedirectsFromInteraction(interaction);
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
				this.add(redirectSet, redirect, false);
			}
		}
		return runInteraction(interaction + 1);
	}
	
	private void add(List<Redirect> red, Redirect r, boolean noUpdated) {
		if(!red.contains(r)) {
			red.add(r);
		}else {
			if(!noUpdated) {
				red.set(red.indexOf(r), r);
			}
		}
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
	
	public Queue<Route> getRoutes(Route route) {
		List<Redirect> redirects = this.redirects.get(this.redirects.size()-1);
		Queue<Route> queue = new LinkedList<Route>();
		Device currentDevice = route.getSource();
		while(true) {
			final Device c = currentDevice; 
			Redirect redirect = redirects
				.stream()
				.filter(r -> c.equals(r.getSource()) && r.getTarget().equals(route.getTarget()))
				.findFirst()
				.orElse(null);
			if(redirect == null) {
				return null;
			}
			Device target = redirect.getLink().getOther(currentDevice);
			queue.add(new Route(currentDevice, target));
			if(target.equals(route.getTarget())) {
				return queue;
			}
			currentDevice = target;
		}
	}
	
	private List<Redirect> cloneRedirectSet(List<Redirect> redirects) {
		List<Redirect> newRedirect = new ArrayList<Redirect>();
		for(Redirect r : redirects) {
			newRedirect.add(r);
		}
		return newRedirect;
	}
	
	public List<List<Redirect>> getRedirects() {
		return redirects;
	}
}
