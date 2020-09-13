package br.ufc.crateus.redes.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class RoutingTable {
	private Map<Device, Set<RoutingTableDetails>> routingMap;
	private int size = 0;
	
	public RoutingTable() {
		this.routingMap = new HashMap<>();
	}
	
	public void put(Device device, RoutingTableDetails details) {
		Set<RoutingTableDetails> detailList = this.routingMap.getOrDefault(device, new TreeSet<RoutingTableDetails>());
		detailList.add(details);
		this.size = detailList.size();
		this.routingMap.put(device, detailList);
	}
	
	public RoutingTableDetails getByDeviceByIndex(Device device, int index) {
		return new ArrayList<RoutingTableDetails>(routingMap
				.get(device))
				.get(index);
	}
	
	public RoutingTable clone() {
		RoutingTable r = new RoutingTable();
		this.routingMap.entrySet().forEach(entry -> {
			r.routingMap.put(entry.getKey(), entry.getValue());
		});
		r.size = this.size;
		return r;
	}
	
	public int size() {
		return size;
	}
}
