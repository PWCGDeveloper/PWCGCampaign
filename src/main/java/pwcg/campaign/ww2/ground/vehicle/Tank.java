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

class Tank extends Vehicle
{
	// German
	private String[][] germanTanks = 
	{
        { "pz38t", "pz38t" },
        { "pziii-h", "pziii-h" },
        { "pziii-l", "pziii-l" },
        { "pziv-f1", "pziv-f1" },
        { "pziv-g", "pziv-g" },
        { "stug37l24", "stug37l24" },
        { "stug40l43", "stug40l43" },
        { "marderiii-h", "marderiii-h" },
	};
	
	// Russian
	private String[][] russianTanks = 
	{
        { "ba10m", "ba10m" },
        { "ba64", "ba64" },
		{ "bt7m", "bt7m" },
        { "kv1-41", "kv1-41" },
        { "kv1-42", "kv1-42" },
        { "t34-76stz-41", "t34-76stz-41" },
        { "t34-76stz", "t34-76stz" },
        { "t70", "t70" },
	};
	
	
	protected Tank()
	{
	}

	public Tank(ICountry country) throws PWCGException 
	{
		super();
		
		this.country = country;
		
		String tankId= "unknownTank";
		String tankDir = "unknownTankDir";
		
		if (country.getSideNoNeutral() == Side.ALLIED)
		{
			int selectedTank = RandomNumberGenerator.getRandom(russianTanks.length);
			tankId = russianTanks[selectedTank] [0];
			tankDir = russianTanks[selectedTank] [1];
			displayName = "Russian Tank";
		}
        else 
        {
            int selectedTank = RandomNumberGenerator.getRandom(germanTanks.length);
            tankId = germanTanks[selectedTank] [0];
            tankDir = germanTanks[selectedTank] [1];
            displayName = "German Tank";
        }
		
		name = tankId;
		script = "LuaScripts\\WorldObjects\\vehicles\\" + tankId + ".txt";
		model = "graphics\\vehicles\\" + tankDir + "\\" + tankId + ".mgm";
	}

	public Tank copy () 
	{
		Tank tank = new Tank();
		
		tank.index = IndexGenerator.getInstance().getNextIndex();
		
		tank.name = this.name;
		tank.displayName = this.displayName;
		tank.linkTrId = this.linkTrId;
		tank.script = this.script;
		tank.model = this.model;
		tank.Desc = this.Desc;
		tank.aiLevel = this.aiLevel;
		tank.numberInFormation = this.numberInFormation;
		tank.vulnerable = this.vulnerable;
		tank.engageable = this.engageable;
		tank.limitAmmo = this.limitAmmo;
		tank.damageReport = this.damageReport;
		tank.country = this.country;
		tank.damageThreshold = this.damageThreshold; 
		
		tank.position = new Coordinate();
		tank.orientation = new Orientation();
		
		tank.entity = new McuTREntity();
		
		tank.populateEntity();
		
		return tank;
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
