package pwcg.product.fc.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.mcu.McuTREntity;

public class Ship extends Vehicle
{    
    private static final List<VehicleDefinition> germanMerchants = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("ships\\", "ships\\largecargoshiptype1\\", "largecargoshiptype1", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> alliedMerchants = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("ships\\", "ships\\largecargoshiptype1\\", "largecargoshiptype1", Country.BRITAIN));
        }
    };

    public Ship() 
	{
	    super();
	}

    
    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanMerchants);
        allvehicleDefinitions.addAll(alliedMerchants);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedMerchants;
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            vehicleSet = germanMerchants;
        }
        
        displayName = "Ship";
        makeRandomVehicleInstance(vehicleSet);
    }

	
	
	public Ship copy () 
	{
		Ship ship = new Ship();
		
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

	@Override
	public void populateEntity()
	{
		super.populateEntity();
		entity.setEnabled(1);
	}
}
