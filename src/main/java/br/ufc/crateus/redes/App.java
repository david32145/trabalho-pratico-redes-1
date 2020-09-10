package br.ufc.crateus.redes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import br.ufc.crateus.redes.reader.NetworkFileReader;

public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
        String networkFileName = "networkInput.txt";
        InputStream inputStream = new FileInputStream(new File(networkFileName));
        NetworkFileReader networkFileReader = new NetworkFileReader(inputStream);
        networkFileReader.read();
        
        networkFileReader
        	.getDevices()
        	.forEach(device -> System.out.printf("%s ", device));
        
        System.out.println();
        
        networkFileReader.getLinks()
        	.forEach(link -> System.out.printf("(%s, %s, %d)\n", link.getSource(), link.getTarget(), (int)link.getWeight()));
        
        System.out.println();
        
        networkFileReader.getRoutes()
    	.forEach(route -> System.out.printf("(%s, %s)\n", route.getSource(), route.getTarget()));
    
    }
}
