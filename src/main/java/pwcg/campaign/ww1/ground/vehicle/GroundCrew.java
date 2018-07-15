package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class GroundCrew extends Vehicle
{
	private String[] germanGroundCrew = 
	{
		"groundcrew_01", "groundcrew_02" 
	};
	
	private String[] britishGroundCrew = 
	{
		"groundcrewen_01", "groundcrewen_02" 
	};
	
	
	public GroundCrew(ICountry country) throws PWCGException 
	{
        super(country);
		
		String groundCrewId= "";
		
        if (country.getSideNoNeutral() == Side.ALLIED)
		{
			int selectedGroundCrew = RandomNumberGenerator.getRandom(britishGroundCrew.length);
			groundCrewId = britishGroundCrew[selectedGroundCrew];			
			displayName = "Allied Ground Crew";
		}
		else
		{
			int selectedGroundCrew = RandomNumberGenerator.getRandom(germanGroundCrew.length);
			groundCrewId = germanGroundCrew[selectedGroundCrew];
			displayName = "German Ground Crew";
		}
		
		vehicleType = groundCrewId;
		script = "LuaScripts\\WorldObjects\\" + groundCrewId + ".txt";
		model = "graphics\\firingpoint\\soldiers\\" + groundCrewId + ".mgm";
		
		populateEntity();
        getEntity().setEnabled(1);
	}

	public GroundCrew copy () throws PWCGException 
	{
		GroundCrew groundCrew = new GroundCrew(this.country);
		
		groundCrew.index = IndexGenerator.getInstance().getNextIndex();
		
		groundCrew.vehicleType = this.vehicleType;
		groundCrew.displayName = this.displayName;
		groundCrew.linkTrId = this.linkTrId;
		groundCrew.script = this.script;
		groundCrew.model = this.model;
		groundCrew.Desc = this.Desc;
		groundCrew.aiLevel = this.aiLevel;
		groundCrew.numberInFormation = this.numberInFormation;
		groundCrew.vulnerable = this.vulnerable;
		groundCrew.engageable = this.engageable;
		groundCrew.limitAmmo = this.limitAmmo;
		groundCrew.damageReport = this.damageReport;
		groundCrew.country = this.country;
		groundCrew.damageThreshold = this.damageThreshold; 
		
		groundCrew.position = new Coordinate();
		groundCrew.orientation = new Orientation();
		
		groundCrew.entity = new McuTREntity();
		
		groundCrew.populateEntity();
		
		return groundCrew;
	}

}
