package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.mcu.McuTREntity;

public class SpotLight extends Vehicle
{
	// German
	private String[] germanSpotLight = { "benz_p", "benz" };
	private String[] alliedSpotLight = { "quad_p", "quad" };

	public SpotLight(ICountry country) throws PWCGException 
	{
        super(country);
        
		String spotLightId= "";
		String spotLightDir = "";
		
		this.getEntity().setEnabled(1);
			
        if (country.getSideNoNeutral() == Side.ALLIED)
		{
			spotLightId = alliedSpotLight[0];
			spotLightDir = alliedSpotLight [1];
			displayName = "British SpotLight";
		}
		else
		{
			spotLightId = germanSpotLight[0];
			spotLightDir = germanSpotLight[1];
			displayName = "German SpotLight";
		}
		
		name = spotLightId;
		script = "LuaScripts\\WorldObjects\\" + spotLightId + ".txt";
		model = "graphics\\vehicles\\" + spotLightDir + "\\" + spotLightId + ".mgm";
	}

	public SpotLight copy () throws PWCGException 
	{
	    SpotLight spotLight = new SpotLight(this.country);
		
		spotLight.index = IndexGenerator.getInstance().getNextIndex();
		
		spotLight.name = this.name;
		spotLight.displayName = this.displayName;
		spotLight.linkTrId = this.linkTrId;
		spotLight.script = this.script;
		spotLight.model = this.model;
		spotLight.Desc = this.Desc;
		spotLight.aiLevel = this.aiLevel;
		spotLight.numberInFormation = this.numberInFormation;
		spotLight.vulnerable = this.vulnerable;
		spotLight.engageable = this.engageable;
		spotLight.limitAmmo = this.limitAmmo;
		spotLight.damageReport = this.damageReport;
		spotLight.country = this.country;
		spotLight.damageThreshold = this.damageThreshold; 
		
		spotLight.position = new Coordinate();
		spotLight.orientation = new Orientation();
		
		spotLight.entity = new McuTREntity();
		
		spotLight.populateEntity();
		spotLight.getEntity().setEnabled(1);

		return spotLight;
	}
}
