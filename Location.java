/** Location information
 */

public class Location{
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
