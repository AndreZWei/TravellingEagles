package client;/* client.Eagle object stores the user information
 */

import java.util.*;
import java.io.Serializable;

public class Eagle implements Serializable{
	private int eagleID;
	private String name;
	private Location currentLocation;
	private List<Gift> bag;

	public Eagle(int eagleID, String name, Location home){
		this.eagleID = eagleID;
		this.name = name;
		this.currentLocation = home;
		this.bag = new ArrayList<Gift>();
	}

	public int getID(){
		return eagleID;
	}
	
	public Location getLocation(){
		return currentLocation;
	}

	public List<Gift> getBag(){
		return bag;
	}

	public void addGift(Gift gift){
		bag.add(gift);
	}

	// Use the transition matrix to select a place to travel
	public void travel(Map map){
		double[][] matrix = map.getMatrix();
		Location[] locs = map.getLocations();

		// find the index of currentLocation
		int currentIndex = 0;
		for (int i = 0; i < locs.length; i++){
			if (locs[i].equals(currentLocation)){
				currentIndex = i;
				break;
			}
		}

		// randomly select a place based on the transition matrix
		Random r = new Random();
		int maxIndex = 0;
		double max = r.nextDouble() * matrix[currentIndex][0];
		for (int i = 1; i < matrix.length; i++){
			double prob = r.nextDouble() * matrix[currentIndex][i];
			if (prob > max){
				max = prob;
				maxIndex = i;
			}
		}

		travelTo(locs[maxIndex]);
	}

	public void travelTo(Location loc){
		if (loc.equals(currentLocation)){
			System.out.println(name + " is staying in " + loc.getName());
		}
		else {
			System.out.println(name + " is travelling to " + loc.getName());
			Random r = new Random();
			if (r.nextDouble() > 0.5){
				addGift(loc.getGift());
				System.out.println(name + " gets the gift " + loc.getGift().getGift());
			}
			currentLocation = loc;
		}
	}

}