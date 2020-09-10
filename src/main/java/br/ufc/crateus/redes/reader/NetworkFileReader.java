package br.ufc.crateus.redes.reader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.ufc.crateus.redes.models.Device;
import br.ufc.crateus.redes.models.Link;
import br.ufc.crateus.redes.models.Route;

public class NetworkFileReader {
	private InputStream inputStream;
	private List<Device> devices;
	private List<Link> links;
	private List<Route> routes;

	public NetworkFileReader(InputStream inputStream) {
		this.inputStream = inputStream;
		this.devices = new ArrayList<Device>();
		this.links = new ArrayList<Link>();
		this.routes = new ArrayList<Route>();
	}
	
	public void read() {
		Scanner scanner = new Scanner(this.inputStream);
		int lineCount = 1;
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(lineCount == 1) {
				readDevices(line);
			} else if(lineCount == 2) {
				readLinks(line);
			} else if(lineCount == 3){
				readRoutes(line);
			}
			
			lineCount++;
		}
		scanner.close();
	}
	
	private void readDevices(String line) {
		String split[] = line.split(":");
		String devicesAsString[] = split[1].split(",");
		for(String deviceString : devicesAsString) {
			this.devices.add(new Device(deviceString));
		}
	}
	
	private void readLinks(String line) {
		String split[] = line.split(":");
		String linksAsString[] = split[1].split("/\\(|(\\),\\()|\\)/");
		linksAsString[0] = linksAsString[0].substring(1);
		linksAsString[linksAsString.length - 1] = linksAsString[linksAsString.length -1].substring(0, 5);
		
		for(String linkString : linksAsString) {
			String linkSplit[] = linkString.split(",");
			this.links.add(new Link(findDevice(linkSplit[0]), findDevice(linkSplit[1]), Double.parseDouble(linkSplit[2])));
		}
	}
	
	private void readRoutes(String line) {
		String split[] = line.split(":");
		String routesAsString[] = split[1].split("/\\(|(\\),\\()|\\)/");
		routesAsString[0] = routesAsString[0].substring(1);
		routesAsString[routesAsString.length - 1] = routesAsString[routesAsString.length -1].substring(0, 3);
		
		for (String routeString : routesAsString) {
			String routeSplit[] = routeString.split(",");
			this.routes.add(new Route(findDevice(routeSplit[0]), findDevice(routeSplit[1])));
		}
	}
	
	private Device findDevice(String label) {
		Device device = this.devices.stream()
				.filter(dvc -> dvc.getLabel().equals(label))
				.findFirst().orElse(null);
		if(device == null){
			throw new RuntimeException("Device with label = " + label + " not exists");
		}
		return device;
	}
	
	public List<Device> getDevices() {
		return devices;
	}
	
	public List<Link> getLinks() {
		return links;
	}
	
	public List<Route> getRoutes() {
		return routes;
	}
}
