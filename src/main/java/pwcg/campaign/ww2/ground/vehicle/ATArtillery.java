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

class ATArtillery extends Vehicle
{
	// German
	private String[][] germanATArtillery = 
	{
        { "pak35", "pak35" },
        { "pak38", "pak38" },
		{ "pak40", "pak40" },
	};
	
	// Russian
	private String[][] russianATArtillery = 
	{
        { "53k", "53k" },
        { "zis2gun", "zis2gun" },
		{ "zis3gun", "zis3gun" },
	};
	
	protected ATArtillery()
	{
	}

	public ATArtillery(ICountry country) throws PWCGException 
	{
		super();
		
		this.country = country;
		
		String atArtilleryId= "unknownAT";
		String atArtilleryDir = "unknownATDir";
		
		if (country.getSideNoNeutral() == Side.ALLIED)
		{
			int selectedArtillery = RandomNumberGenerator.getRandom(russianATArtillery.length);
			atArtilleryId = russianATArtillery[selectedArtillery] [0];
			atArtilleryDir = russianATArtillery[selectedArtillery] [1];
			displayName = "Russian Artillery";
		}
        else
        {
            int selectedArtillery = RandomNumberGenerator.getRandom(germanATArtillery.length);
            atArtilleryId = germanATArtillery[selectedArtillery] [0];
            atArtilleryDir = germanATArtillery[selectedArtillery] [1];
            displayName = "German Artillery";
        }
		
		name = atArtilleryId;
		script = "LuaScripts\\WorldObjects\\vehicles\\" + atArtilleryId + ".txt";
		model = "graphics\\artillery\\" + atArtilleryDir + "\\" + atArtilleryId + ".mgm";
	}


	public ATArtillery copy () 
	{
		ATArtillery gun = new ATArtillery();
		
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
