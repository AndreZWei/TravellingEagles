/** Location information
 */
import java.io.Serializable;

public class Location implements Serializable{
	private String name;
	private Gift gift;

	public Location(String name, Gift gift){
		this.name = name;
		this.gift = gift;
	}

	public String getName(){
		return name;
	}

	public Gift getGift(){
		return gift;
	}
}
