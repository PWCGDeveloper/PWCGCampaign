package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class Wagon extends Vehicle
{
	// German
	private String[] germanWagon = 
	{
		"guncarriagede", "ammowagon" 
	};
	
	// British
	private String[] britishWagon = 
	{
       "guncarriagede", "ammowagon" 
	};
	
	public Wagon(ICountry country) throws PWCGException 
	{
        super(country);
        
		String wagonId= "";
		
        if (country.getSideNoNeutral() == Side.ALLIED)
		{
			int selectedWagon = RandomNumberGenerator.getRandom(britishWagon.length);
			wagonId = britishWagon[selectedWagon];			
			displayName = "Allied Wagon";
		}
		else
		{
			int selectedWagon = RandomNumberGenerator.getRandom(germanWagon.length);
			wagonId = germanWagon[selectedWagon];
			displayName = "German Wagon";
		}
		
		name = wagonId;
		script = "LuaScripts\\WorldObjects\\" + wagonId + ".txt";
		model = "graphics\\vehicles\\ammowagon\\" + wagonId + ".mgm";
	}

	public Wagon copy () throws PWCGException 
	{
		Wagon wagon = new Wagon(this.country);
		
		wagon.index = IndexGenerator.getInstance().getNextIndex();
		
		wagon.name = this.name;
		wagon.displayName = this.displayName;
		wagon.linkTrId = this.linkTrId;
		wagon.script = this.script;
		wagon.model = this.model;
		wagon.Desc = this.Desc;
		wagon.aiLevel = this.aiLevel;
		wagon.numberInFormation = this.numberInFormation;
		wagon.vulnerable = this.vulnerable;
		wagon.engageable = this.engageable;
		wagon.limitAmmo = this.limitAmmo;
		wagon.damageReport = this.damageReport;
		wagon.country = this.country;
		wagon.damageThreshold = this.damageThreshold; 
		
		wagon.position = new Coordinate();
		wagon.orientation = new Orientation();
		
		wagon.entity = new McuTREntity();
		
		wagon.populateEntity();
		
		return wagon;
	}
}
