/** Location information
 */
import java.io.Serializable;

public class Location implements Serializable{
	private String name;
	private String gift;

	public Location(String name, String gift){
		this.name = name;
		this.gift = gift;
	}

	public String getName(){
		return name;
	}

	public String getGift(){
		return gift;
	}
}
