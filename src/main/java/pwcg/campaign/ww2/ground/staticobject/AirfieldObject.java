package pwcg.campaign.ww2.ground.staticobject;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class AirfieldObject extends StaticObject
{
	private String[] airfieldObject = 
	{
		"art_position_amb1", "art_position_amb2"
	};

	public AirfieldObject(ICountry country) throws PWCGException 
	{
		super(country);
		
		this.country = country;
		
		String airfieldObjectId= "";
        int selectedAirfieldObject = RandomNumberGenerator.getRandom(airfieldObject.length);
        airfieldObjectId = airfieldObject[selectedAirfieldObject];           
        displayName = "Equipment";
		
		name = airfieldObjectId;
		script = "LuaScripts\\WorldObjects\\Blocks\\" + airfieldObjectId + ".txt";
		model = "graphics\\blocks\\" + airfieldObjectId + ".mgm";
	}

	public AirfieldObject copy () throws PWCGException 
	{
		AirfieldObject clonedObject = new AirfieldObject(this.country);
		
		super.copy(clonedObject);
		
		return clonedObject;
	}
}
