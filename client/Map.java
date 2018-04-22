package client;

/** A map that contains the transition matrix of all locations
 */

public class Map{
	private double[][] matrix;
	private Location[] locations;

	public Map(double[][] matrix, Location[] locations){
		this.matrix = matrix;
		this.locations = locations;
	}

	public double[][] getMatrix(){
		return matrix;
	}

	public Location[] getLocations(){
		return locations;
	}
}
