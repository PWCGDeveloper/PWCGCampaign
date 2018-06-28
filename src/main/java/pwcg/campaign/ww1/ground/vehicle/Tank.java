package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class Tank extends Vehicle
{
	// German
	private String[][] germanTanks = 
	{
		{ "mk4fger", "mk4" },
		{ "mk4mger", "mk4" },
		{ "a7v", "a7v" },
	};
	
	// French
	private String[][] frenchTanks = 
	{
		{ "ft17c", "ft17" },
		{ "ft17m", "ft17" },
		{ "whippet", "whippet" },
		{ "ca1", "ca1" },
		{ "stchamond", "stchamond" },
	};
	
	// British
	private String[][] britishTanks = 
	{
		{ "mk4f", "mk4" },
		{ "mk4m", "mk4" },
		{ "mk5f", "mk5" },
		{ "mk5m", "mk5" },
	};
	
	public Tank(ICountry country) 
	{
        super(country);
        
		String tankId= "";
		String tankDir = "";
		
		if (country.getCountry() == Country.FRANCE)
		{
			int selectedTank = RandomNumberGenerator.getRandom(frenchTanks.length);
			tankId = frenchTanks[selectedTank] [0];
			tankDir = frenchTanks[selectedTank] [1];
			displayName = "French Tank";
		}
		else if (country.getCountry() == Country.USA)
		{
			int selectedTank = RandomNumberGenerator.getRandom(frenchTanks.length);
			tankId = frenchTanks[selectedTank] [0];
			tankDir = frenchTanks[selectedTank] [1];
			displayName = "American Tank";
		}
		else if (country.getCountry() == Country.GERMANY)
		{
			int selectedTank = RandomNumberGenerator.getRandom(germanTanks.length);
			tankId = germanTanks[selectedTank] [0];
			tankDir = germanTanks[selectedTank] [1];
			displayName = "German Tank";
		}
		else
		{
			int selectedTank = RandomNumberGenerator.getRandom(britishTanks.length);
			tankId = britishTanks[selectedTank] [0];			
			tankDir = britishTanks[selectedTank] [1];
			displayName = "British Tank";
		}
		
		name = tankId;
		script = "LuaScripts\\WorldObjects\\" + tankId + ".txt";
		model = "graphics\\vehicles\\" + tankDir + "\\" + tankId + ".mgm";
	}

	public Tank copy () 
	{
		Tank tank = new Tank(country);
		
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
}
