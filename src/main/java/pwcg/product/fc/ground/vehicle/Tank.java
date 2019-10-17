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

class Tank extends Vehicle
{
    private static final List<VehicleDefinition> germanTank = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "vehicles\\ba64\\", "ba64", Country.GERMANY));
        }
    };
    
    private static final List<VehicleDefinition> alliedTank = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "vehicles\\ba10m\\", "ba10m", Country.BRITAIN));
        }
    };
	
	protected Tank()
	{
	    super();
	}

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanTank);
        allvehicleDefinitions.addAll(alliedTank);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedTank;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanTank;
        }
        
        displayName = "Tank";
        makeRandomVehicleInstance(vehicleSet);
    }

	public Tank copy () 
	{
		Tank tank = new Tank();
		
		tank.index = IndexGenerator.getInstance().getNextIndex();
		
		tank.vehicleType = this.vehicleType;
		tank.displayName = this.displayName;
		tank.linkTrId = this.linkTrId;
		tank.script = this.script;
		tank.model = this.model;
		tank.Desc = this.Desc;
		tank.aiLevel = this.aiLevel;
		tank.numberInFormation = this.numberInFormation;
		tank.vulnerable = this.vulnerable;
		tank.engageable = this.engageable;
		tank.limitAmmo = this.limitAmmo;
		tank.damageReport = this.damageReport;
		tank.country = this.country;
		tank.damageThreshold = this.damageThreshold; 
		
		tank.position = new Coordinate();
		tank.orientation = new Orientation();
		
		tank.entity = new McuTREntity();
		
		tank.populateEntity();
		
		return tank;
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
