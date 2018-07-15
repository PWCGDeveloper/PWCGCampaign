package pwcg.campaign.ww2.ground.vehicle;

import java.io.BufferedWriter;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.mcu.McuTREntity;

public class SpotLight extends Vehicle
{
	// German
	private String[] germanSpotLight = { "searchlightger", "searchlightger" };
	private String[] alliedSpotLight = { "searchlightsu", "searchlightsu" };

    
    protected SpotLight()
    {
    }

	public SpotLight(ICountry country) throws PWCGException 
	{
        super();
        this.country = country;
        
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
		
		vehicleType = spotLightId;
		script = "LuaScripts\\WorldObjects\\vehicles\\" + spotLightId + ".txt";
		model = "graphics\\artillery\\" + spotLightDir + "\\" + spotLightId + ".mgm";

	}

	public SpotLight copy () 
	{
	    SpotLight spotLight = new SpotLight();
		
		spotLight.index = IndexGenerator.getInstance().getNextIndex();
		
		spotLight.vehicleType = this.vehicleType;
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
