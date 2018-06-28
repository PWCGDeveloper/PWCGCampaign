package pwcg.campaign.ww1.ground.staticobject;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class AirfieldObject extends StaticObject
{
    
	// German
	private String[] germanAirfieldObject = 
	{
		"airfieldobj_01", "airfieldobj_02", "airfieldobj_03", 
		"airfieldobj_04", "airfieldobj_05", "airfieldobj_06", 
		"airfieldobj_07"
	};
	
	// British
	private String[] britishAirfieldObject = 
	{
		"airfieldobj_01", "airfieldobj_02", "airfieldobj_03", 
		"airfieldobj_04", "airfieldobj_05", "airfieldobj_06", 
		"airfieldobj_07"
	};
	public AirfieldObject(ICountry country) throws PWCGException 
	{
		super(country);
				
		String airfieldObjectId= "";
		
        if (country.getSideNoNeutral() == Side.ALLIED)
		{
			int selectedAirfieldObject = RandomNumberGenerator.getRandom(britishAirfieldObject.length);
			airfieldObjectId = britishAirfieldObject[selectedAirfieldObject];			
			displayName = "Allied Static Truck";
	        finishAirfieldObject(airfieldObjectId);
		}
		else if (country.getSideNoNeutral() == Side.AXIS)
		{
			int selectedAirfieldObject = RandomNumberGenerator.getRandom(germanAirfieldObject.length);
			airfieldObjectId = germanAirfieldObject[selectedAirfieldObject];
			displayName = "German Static Truck";
	        finishAirfieldObject(airfieldObjectId);
		}		
	}

	private void finishAirfieldObject(String airfieldObjectId)
	{
		name = airfieldObjectId;
		script = "LuaScripts\\WorldObjects\\" + airfieldObjectId + ".txt";
		model = "graphics\\blocks\\" + airfieldObjectId + ".mgm";
	}
}
