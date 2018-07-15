package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class Infantry extends Vehicle
{
	private String[] germanInfantry = 
	{
		"infantryde" 
	};
	
	private String[] britishInfantry = 
	{
		"infantryen" 
	};

	public Infantry(ICountry country) throws PWCGException 
	{
        super(country);

		String infantryId= "";
		
        if (country.getSideNoNeutral() == Side.ALLIED)
		{
			int selectedInfantry = RandomNumberGenerator.getRandom(britishInfantry.length);
			infantryId = britishInfantry[selectedInfantry];			
			displayName = "Allied Infantry";
		}
		else
		{
			int selectedInfantry = RandomNumberGenerator.getRandom(germanInfantry.length);
			infantryId = germanInfantry[selectedInfantry];
			displayName = "German Infantry";
		}
		
		vehicleType = infantryId;
		script = "LuaScripts\\WorldObjects\\" + infantryId + ".txt";
		model = "graphics\\firingpoint\\soldiers\\" + infantryId + ".mgm";
	}

	public Infantry copy () throws PWCGException 
	{
		Infantry infantry = new Infantry(this.country);
		
		infantry.index = IndexGenerator.getInstance().getNextIndex();
		
		infantry.vehicleType = this.vehicleType;
		infantry.displayName = this.displayName;
		infantry.linkTrId = this.linkTrId;
		infantry.script = this.script;
		infantry.model = this.model;
		infantry.Desc = this.Desc;
		infantry.aiLevel = this.aiLevel;
		infantry.numberInFormation = this.numberInFormation;
		infantry.vulnerable = this.vulnerable;
		infantry.engageable = this.engageable;
		infantry.limitAmmo = this.limitAmmo;
		infantry.damageReport = this.damageReport;
		infantry.country = this.country;
		infantry.damageThreshold = this.damageThreshold; 
		
		infantry.position = new Coordinate();
		infantry.orientation = new Orientation();
		
		infantry.entity = new McuTREntity();
		
		infantry.populateEntity();
		
		return infantry;
	}

}
