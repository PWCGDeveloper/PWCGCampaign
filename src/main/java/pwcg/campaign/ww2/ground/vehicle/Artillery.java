package pwcg.campaign.ww2.ground.vehicle;

import java.io.BufferedWriter;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

class Artillery extends Vehicle
{
	// German
	private String[][] germanArtillery = 
	{
         { "lefh18", "lefh18" },
	};
	
	// Russian
	private String[][] russianArtillery = 
	{
         { "ml20", "ml20" },
	};
	
	protected Artillery()
	{
	}

	public Artillery(ICountry country) throws PWCGException 
	{
		super();
		
		this.country = country;
		
		String artilleryId= "unknownArty";
		String artilleryDir = "unknownArtyDir";
		
		if (country.getSideNoNeutral() == Side.ALLIED)
		{
			int selectedArtillery = RandomNumberGenerator.getRandom(russianArtillery.length);
			artilleryDir = russianArtillery[selectedArtillery] [0];
			artilleryId = russianArtillery[selectedArtillery] [1];
			displayName = "Russian Artillery";
		}
        else
        {
            int selectedArtillery = RandomNumberGenerator.getRandom(germanArtillery.length);
            artilleryDir = germanArtillery[selectedArtillery] [0];
            artilleryId = germanArtillery[selectedArtillery] [1];
            displayName = "German Artillery";
        }
		
		name = artilleryId;
		script = "LuaScripts\\WorldObjects\\vehicles\\" + artilleryId + ".txt";
		model = "graphics\\artillery\\" + artilleryDir + "\\" + artilleryId + ".mgm";
	}


	public Artillery copy () 
	{
		Artillery gun = new Artillery();
		
		gun.index = IndexGenerator.getInstance().getNextIndex();
		
		gun.name = this.name;
		gun.displayName = this.displayName;
		gun.linkTrId = this.linkTrId;
		gun.script = this.script;
		gun.model = this.model;
		gun.Desc = this.Desc;
		gun.aiLevel = this.aiLevel;
		gun.numberInFormation = this.numberInFormation;
		gun.vulnerable = this.vulnerable;
		gun.engageable = this.engageable;
		gun.limitAmmo = this.limitAmmo;
		gun.damageReport = this.damageReport;
		gun.country = this.country;
		gun.damageThreshold = this.damageThreshold; 
		
		gun.position = new Coordinate();
		gun.orientation = new Orientation();
		
		gun.entity = new McuTREntity();
		
		gun.populateEntity();
		
		return gun;
	}
	
	public void write(BufferedWriter writer) throws PWCGIOException
	{
		super.write(writer);
	}
	
	public void setOrientation (Orientation orient)
	{
		super.setOrientation(orient);
	}

	public void setPosition (Coordinate coord)
	{
		super.setPosition(coord);
	}
}
