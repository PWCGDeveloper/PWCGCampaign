package pwcg.product.fc.ground.vehicle;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.mcu.McuTREntity;

public class SpotLight extends Vehicle
{
    private static final List<VehicleDefinition> germanSpotLight = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "artillery\\searchlightger\\", "searchlightger", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> britishSpotLight = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "artillery\\searchlightsu\\", "searchlightsu", Country.BRITAIN));
        }
    };

    protected SpotLight()
    {
        super();
    }

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanSpotLight);
        allvehicleDefinitions.addAll(britishSpotLight);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = britishSpotLight;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanSpotLight;
        }
        
        displayName = "Spot Light";
        makeRandomVehicleInstance(vehicleSet);
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
