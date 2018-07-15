package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class Drifter extends Vehicle
{
	// German
    private String unarmedDrifter = "hmsDrifter";
    private String germanArmedDrifter = "gerdrifter";
    private String britishArmedDrifter = "hmsdrifter6pdraaa";
	
	public Drifter(ICountry country) throws PWCGException 
	{
        super(country);
        this.country = country;
		
		String shipId= unarmedDrifter;
		displayName = "Drifter";
		
		// Armed drifter?
		int armedRoll = RandomNumberGenerator.getRandom(100);
		if (armedRoll < 20)
		{
		    displayName = "AAA Drifter";
		    
	        if (country.getSideNoNeutral() == Side.ALLIED)
    		{
    			shipId = britishArmedDrifter;
    		}
    		else
    		{
    			shipId = germanArmedDrifter;
    		}
		}
		
	    vehicleType = shipId;
	    script = "LuaScripts\\WorldObjects\\" + shipId + ".txt";
	    model = "graphics\\vehicles\\platoon\\" + shipId + ".mgm";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Drifter copy () throws PWCGException 
	{
		Drifter ship = new Drifter(this.country);
		
		ship.index = IndexGenerator.getInstance().getNextIndex();
		
		ship.vehicleType = this.vehicleType;
		ship.displayName = this.displayName;
		ship.linkTrId = this.linkTrId;
		ship.script = this.script;
		ship.model = this.model;
		ship.Desc = this.Desc;
		ship.aiLevel = this.aiLevel;
		ship.numberInFormation = this.numberInFormation;
		ship.vulnerable = this.vulnerable;
		ship.engageable = this.engageable;
		ship.limitAmmo = this.limitAmmo;
		ship.damageReport = this.damageReport;
		ship.country = this.country;
		ship.damageThreshold = this.damageThreshold; 
		
		ship.position = new Coordinate();
		ship.orientation = new Orientation();
		
		ship.entity = new McuTREntity();
		
		ship.populateEntity();
		
		return ship;
	}

	/**
	 * Override to always enable ships
	 * @throws PWCGException 
	 */
	@Override
	public void populateEntity() throws PWCGException
	{
		super.populateEntity();
		entity.setEnabled(1);
	}
}
