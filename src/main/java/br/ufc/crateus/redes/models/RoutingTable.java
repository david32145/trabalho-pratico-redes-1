package br.ufc.crateus.redes.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutingTable {
	private Map<Device, List<RoutingTableDetails>> routingMap;
	private int size = 0;
	
	public RoutingTable() {
		this.routingMap = new HashMap<>();
	}
	
	public void put(Device device, RoutingTableDetails details) {
		List<RoutingTableDetails> detailList = this.routingMap.getOrDefault(device, new ArrayList<RoutingTableDetails>());
		detailList.add(details);
		this.size = detailList.size();
		this.routingMap.put(device, detailList);
	}
	
	public RoutingTableDetails getByDeviceByIndex(Device device, int index) {
		System.out.println(routingMap.size());
		return routingMap
				.get(device)
				.get(index);
	}
	
	public int size() {
		return size;
	}
}
