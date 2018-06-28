package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class MovingInfantry extends Vehicle
{
	// German
	private String[] germanInfantry = 
	{
        "platoonde",
        "platoonde02" 
	};
    
    // British
    private String[] frenchInfantry = 
    {
        "platoonfr",
        "platoonfr02" 
    };
    
    // British
    private String[] britishInfantry = 
    {
        "platoonen",
        "platoonen02" 
    };

	public MovingInfantry(ICountry country) throws PWCGException 
	{
        super(country);

		String infantryId= "";
		
        if (country.getSideNoNeutral() == Side.ALLIED)
		{
		    if (country.isCountry(Country.FRANCE))
		    {
                int selectedInfantry = RandomNumberGenerator.getRandom(frenchInfantry.length);
                infantryId = frenchInfantry[selectedInfantry];         
                displayName = "French Infantry";
		    }
		    else
		    {
    			int selectedInfantry = RandomNumberGenerator.getRandom(britishInfantry.length);
    			infantryId = britishInfantry[selectedInfantry];			
    			displayName = "British Infantry";
		    }
		}
		else
		{
			int selectedInfantry = RandomNumberGenerator.getRandom(germanInfantry.length);
			infantryId = germanInfantry[selectedInfantry];
			displayName = "German Infantry";
		}
		
		name = infantryId;
		script = "LuaScripts\\WorldObjects\\" + infantryId + ".txt";
		model = "graphics\\vehicles\\platoon\\" + infantryId + ".mgm";
	}

	public MovingInfantry copy () throws PWCGException 
	{
		MovingInfantry infantry = new MovingInfantry(this.country);
		
		infantry.index = IndexGenerator.getInstance().getNextIndex();
		
		infantry.name = this.name;
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
