package pwcg.campaign.ww2.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.campaign.ww1.ground.vehicle.VehicleDefinition;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit.ShipConvoyTypes;
import pwcg.mission.mcu.McuTREntity;

public class Ship extends Vehicle
{
    private ShipConvoyTypes shipConvoyType;
    
    private static final List<VehicleDefinition> germanWarships = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("ships\\", "ships\\torpboats38\\", "torpboats38", Country.GERMANY));
            add(new VehicleDefinition("ships\\", "ships\\destroyertype7\\", "destroyertype7", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> germanMerchants = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("ships\\", "ships\\largecargoshiptype1\\", "largecargoshiptype1", Country.GERMANY));
            add(new VehicleDefinition("ships\\", "ships\\largetankershiptype1\\", "largetankershiptype1", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> russianWarships = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("ships\\", "ships\\destroyertype7\\", "destroyertype7", Country.RUSSIA));
            add(new VehicleDefinition("ships\\", "ships\\torpboatg5s11b\\", "torpboatg5s11b", Country.RUSSIA));
            add(new VehicleDefinition("ships\\", "ships\\torpboatg5s11b213\\", "torpboatg5s11b213", Country.RUSSIA));
            add(new VehicleDefinition("ships\\", "ships\\landboata\\", "landboata", Country.RUSSIA));
            add(new VehicleDefinition("ships\\", "ships\\landboata\\", "landboata", Country.RUSSIA));
        }
    };
	
    private static final List<VehicleDefinition> russianMerchants = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("ships\\", "ships\\largecargoshiptype1\\", "largecargoshiptype1", Country.RUSSIA));
            add(new VehicleDefinition("ships\\", "ships\\largetankershiptype1\\", "largetankershiptype1", Country.RUSSIA));
        }
    };

    public Ship() 
	{
	    super();
	}
	   
	public Ship(ShipConvoyTypes shipConvoyType) throws PWCGException 
	{
        super();
        this.shipConvoyType = shipConvoyType;
	}

    
    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanWarships);
        allvehicleDefinitions.addAll(germanMerchants);
        allvehicleDefinitions.addAll(russianWarships);
        allvehicleDefinitions.addAll(russianMerchants);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        List<VehicleDefinition> vehicleSet = null;;
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            if (shipConvoyType == ShipConvoyTypes.WARSHIP)
            {
                vehicleSet = russianWarships;
            }
            else
            {
                vehicleSet = russianMerchants;
            }
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            if (shipConvoyType == ShipConvoyTypes.WARSHIP)
            {
                vehicleSet = germanWarships;
            }
            else
            {
                vehicleSet = germanMerchants;
            }
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
