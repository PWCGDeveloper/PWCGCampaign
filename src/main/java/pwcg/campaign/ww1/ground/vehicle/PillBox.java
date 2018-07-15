package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class PillBox extends Vehicle
{
	private String[] pillBoxes = 
	{
	    "pillbox01", "pillbox02", "pillbox03", "pillbox04"
	};
	
	public PillBox(ICountry country) 
	{
        super(country);

		this.setEngageable(1);

		String pillBoxId= "";
		
        int selectedPillBox = RandomNumberGenerator.getRandom(pillBoxes.length);
        pillBoxId = pillBoxes[selectedPillBox];
        displayName = "PillBox";
		
        vehicleType = pillBoxId;
		script = "LuaScripts\\WorldObjects\\" + pillBoxId + ".txt";
		model = "graphics\\firingpoint\\pillbox\\" + pillBoxId + ".mgm";
	}

	public PillBox copy () throws PWCGException 
	{
		PillBox pillBox = new PillBox(country);
		
		pillBox.index = IndexGenerator.getInstance().getNextIndex();
		
		pillBox.vehicleType = this.vehicleType;
		pillBox.displayName = this.displayName;
		pillBox.linkTrId = this.linkTrId;
		pillBox.script = this.script;
		pillBox.model = this.model;
		pillBox.Desc = this.Desc;
		pillBox.aiLevel = this.aiLevel;
		pillBox.numberInFormation = this.numberInFormation;
		pillBox.vulnerable = this.vulnerable;
		pillBox.engageable = this.engageable;
		pillBox.limitAmmo = this.limitAmmo;
		pillBox.damageReport = this.damageReport;
		pillBox.country = this.country;
		pillBox.damageThreshold = this.damageThreshold; 
		
		pillBox.position = new Coordinate();
		pillBox.orientation = new Orientation();
		
		pillBox.entity = new McuTREntity();
		
		pillBox.populateEntity();
		
		return pillBox;
	}
}
