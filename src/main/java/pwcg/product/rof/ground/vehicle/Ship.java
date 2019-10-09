package pwcg.product.rof.ground.vehicle;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit.ShipConvoyTypes;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.mcu.McuTREntity;

public class Ship extends Vehicle
{
    private ShipConvoyTypes shipConvoyType;
    
    private static final List<VehicleDefinition> germanWarships = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "ships\\gerships\\", "gercruiser", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> germanSubmarines = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "ships\\gerships\\", "gersubmarine", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> germanMerchants = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "ships\\merchant\\", "ship_cargo", Country.GERMANY));
            add(new VehicleDefinition("", "ships\\merchant\\", "ship_tank", Country.GERMANY));
            add(new VehicleDefinition("", "ships\\merchant\\", "ship_pass", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> britishWarships = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "ships\\gbrships\\", "hmscruiser", Country.BRITAIN));
        }
    };

    private static final List<VehicleDefinition> britishSubmarines = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "ships\\gerships\\", "gersubmarine", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> britishMerchants = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("", "ships\\merchant\\", "ship_cargo", Country.BRITAIN));
            add(new VehicleDefinition("", "ships\\merchant\\", "ship_tank", Country.BRITAIN));
            add(new VehicleDefinition("", "ships\\merchant\\", "ship_pass", Country.BRITAIN));
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
        allvehicleDefinitions.addAll(germanSubmarines);
        allvehicleDefinitions.addAll(britishWarships);
        allvehicleDefinitions.addAll(britishMerchants);
        allvehicleDefinitions.addAll(britishSubmarines);
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
                vehicleSet = britishWarships;
                displayName = "Warship";
            }
            else if (shipConvoyType == ShipConvoyTypes.SUBMARINE)
            {
                vehicleSet = britishSubmarines;
                displayName = "Submarine";
            }
            else
            {
                vehicleSet = britishMerchants;
                displayName = "Merchant Ship";
            }
        }
        else if (country.getSideNoNeutral() == Side.AXIS)
        {
            if (shipConvoyType == ShipConvoyTypes.WARSHIP)
            {
                vehicleSet = germanWarships;
                displayName = "Warship";
            }
            else if (shipConvoyType == ShipConvoyTypes.SUBMARINE)
            {
                vehicleSet = germanSubmarines;
                displayName = "Submarine";
            }
            else
            {
                vehicleSet = germanMerchants;
                displayName = "Merchant Ship";
            }
        }
        
        makeRandomVehicleInstance(vehicleSet);
    }

	public Ship copy () throws PWCGException 
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
