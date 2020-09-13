package br.ufc.crateus.redes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import br.ufc.crateus.redes.models.Device;
import br.ufc.crateus.redes.models.Link;
import br.ufc.crateus.redes.models.Redirect;
import br.ufc.crateus.redes.models.Route;
import br.ufc.crateus.redes.reader.NetworkFileReader;
import network.NetworkRouter;

public class App {
	public static void main(String[] args) throws FileNotFoundException {
		String networkFileName = "networkInput.txt";
		InputStream inputStream = new FileInputStream(new File(networkFileName));
		NetworkFileReader networkFileReader = new NetworkFileReader(inputStream);
		networkFileReader.read();

		List<Device> devices = networkFileReader.getDevices();

//        System.out.println();

		List<Link> links = networkFileReader.getLinks();
	
		
		NetworkRouter networkRouter = new NetworkRouter(devices.toArray(new Device[devices.size()]), links.toArray(new Link[links.size()]));
		networkRouter.buildRoutes();
		
		List<List<Redirect>> reList = networkRouter.getRedirects();
		
		for (int i = 0; i < reList.size(); i++) {
			System.out.println("Interaction: " + i);
			devices.forEach(d -> System.out.printf("%s:\t\t", d));
			System.out.println();
			List<Redirect> list = reList.get(i);
			for(int l = 0; l < list.size()/devices.size(); l++) {
				for(int k = 0; k < devices.size(); k++) {
					final int index = k;
					Redirect red = list.stream()
								.filter(r -> r.isSourceByRedirect(devices.get(index)))
								.collect(Collectors.toList())
								.get(l);
					System.out.printf("%s,(%s, %s),%.0f\t", red.getSource(), red.getLink().getSource(), red.getLink().getTarget(), red.getLink().getWeight());
				}
				System.out.println();
			}
		}
		
		System.out.println();
		
		
		List<Route> routes = networkFileReader.getRoutes();
		for (Route route : routes) {
			Queue<Route> queueRoutes = networkRouter.getRoutes(route);
			System.out.println("Route: " + route);
			if(queueRoutes == null) {
				System.out.println("Não foi possível achar a rota");
				continue;
			}
			
			for(Route r : networkRouter.getRoutes(route)) {
				System.out.println(r);
			}
			System.out.println();
		}
	}
}
