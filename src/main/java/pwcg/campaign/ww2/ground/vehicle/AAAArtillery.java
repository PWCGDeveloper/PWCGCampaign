package pwcg.campaign.ww2.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

class AAAArtillery extends Vehicle
{
	// German
	private String[][] germanAAA = 
	{
        { "flak36", "flak36" },
        { "flak37", "flak37" },
        { "flak38", "flak38" },
	};
	
	// Allied
	private String[][] alliedAAA = 
	{
	   { "52k", "52k" },
	   { "61k", "61k" },
	   { "72k", "72k" },
	};
	
	protected AAAArtillery()
	{
	}

	public AAAArtillery(ICountry country) throws PWCGException 
	{
		super();
				
		this.country = country;
		
		// Make AAA not engageable to avoid aircraft diving on every MG that they come across
        this.engageable = 0;
		
		String aaaArtyId= "unknownAAA";
		String aaaArtyDir = "unknownAAADir";
		
		if (country.getSideNoNeutral() == Side.ALLIED)
		{
			int selectedMG = RandomNumberGenerator.getRandom(alliedAAA.length);
			aaaArtyId = alliedAAA[selectedMG] [0];
			aaaArtyDir = alliedAAA[selectedMG] [1];
			displayName = "Russian MG";
		}
		else
		{
			int selectedMG = RandomNumberGenerator.getRandom(germanAAA.length);
			aaaArtyId = germanAAA[selectedMG] [0];
			aaaArtyDir = germanAAA[selectedMG] [1];
			displayName = "German MG";
		}
		
		vehicleType = aaaArtyId;
        script = "LuaScripts\\WorldObjects\\vehicles\\" + aaaArtyId + ".txt";        
		model = "graphics\\artillery\\" + aaaArtyDir + "\\" + aaaArtyId + ".mgm";
	}

	public AAAArtillery copy () 
	{
		AAAArtillery mg = new AAAArtillery();
		
		mg.index = IndexGenerator.getInstance().getNextIndex();
		
		mg.vehicleType = this.vehicleType;
		mg.displayName = this.displayName;
		mg.linkTrId = this.linkTrId;
		mg.script = this.script;
		mg.model = this.model;
		mg.Desc = this.Desc;
		mg.aiLevel = this.aiLevel;
		mg.numberInFormation = this.numberInFormation;
		mg.vulnerable = this.vulnerable;
		mg.engageable = this.engageable;
		mg.limitAmmo = this.limitAmmo;
		mg.damageReport = this.damageReport;
		mg.country = this.country;
		mg.damageThreshold = this.damageThreshold; 
		
		mg.position = new Coordinate();
		mg.orientation = new Orientation();
		
		mg.entity = new McuTREntity();
		
		mg.populateEntity();
		
		return mg;
	}
}
