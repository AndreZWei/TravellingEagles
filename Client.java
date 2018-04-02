/** The main class of the program
 */

import java.util.*;
import java.io.*;

public class Client{
	public static Map map;
	public static Eagle eagle;

	public static void main(String[] args){
		// read the map
		try{
			File file = new File("map.config");
			Scanner scan = new Scanner(file);
			map = readMap(scan);
		} catch (FileNotFoundException e){
			System.out.println(e);
		}

		// Create an eagle object
		Scanner scan = new Scanner(System.in);
		System.out.println("Give your eagle a name!");
		Eagle eagle = new Eagle(scan.nextLine(), map.getLocations()[0]);

		while(true){
			try{
				Thread.sleep(1000);
			} catch (Exception e){
				System.out.println(e);
			}
			eagle.travel(map);
		}
	}

	public static Map readMap(Scanner scan){
		int l = Integer.parseInt(scan.nextLine());

		Location[] locs = new Location[l];
		double[][] matrix = new double[l][l];

		int index = 0;
		while(scan.hasNextLine()){
			String[] tokens = scan.nextLine().split(" ");
			Location loc = new Location(tokens[0], tokens[1]);
			locs[index] = loc;

			// read the transition matrix
			for (int i = 2; i < tokens.length; i++){
				matrix[index][i-2] = Double.parseDouble(tokens[i]);
			}
			index++;
		}

		return new Map(matrix, locs);
	}
}
