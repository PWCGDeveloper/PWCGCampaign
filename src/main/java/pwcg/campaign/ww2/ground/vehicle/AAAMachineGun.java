package pwcg.campaign.ww2.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

class AAAMachineGun extends Vehicle
{
	// German
	private String[][] germanMGs = 
	{
        { "mg34-aa", "mg34-aa" },
	};
	
	// Allied
	private String[][] alliedMGs = 
	{
        { "maksim4-aa", "maksim4-aa" },
	};
 

	
	protected AAAMachineGun()
	{
	}

	public AAAMachineGun(ICountry country) throws PWCGException 
	{
		super();
		
		this.country = country;
		
		// Make AAA not engageable to avoid aircraft diving on every MG that they come across
		this.engageable = 0;

		String mgId= "unknownMG";
		String mgDir = "unknownMGDir";
		
		if (country   .getSideNoNeutral() == Side.ALLIED)
		{
			int selectedMG = RandomNumberGenerator.getRandom(alliedMGs.length);
			mgId = alliedMGs[selectedMG] [0];
			mgDir = alliedMGs[selectedMG] [1];
			displayName = "Russian MG";
		}
        else
		{
			int selectedMG = RandomNumberGenerator.getRandom(germanMGs.length);
			mgId = germanMGs[selectedMG] [0];
			mgDir = germanMGs[selectedMG] [1];
			displayName = "German MG";
		}
		
		vehicleType = mgId;
		script = "LuaScripts\\WorldObjects\\vehicles\\" + mgId + ".txt";
		model = "graphics\\artillery\\" + mgDir + "\\" + mgId + ".mgm";
	}

	public AAAMachineGun copy () 
	{
		AAAMachineGun mg = new AAAMachineGun();
		
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
