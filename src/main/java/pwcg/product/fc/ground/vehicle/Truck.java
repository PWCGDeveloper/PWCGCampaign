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

class Truck extends Vehicle
{
    private TruckType truckType;

    public enum TruckType
    {
        TRUCK_CARGO,
        TRUCK_AAA,
        CAR;
    }
    
    private static final List<VehicleDefinition> germanTrucks = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "vehicles\\opel\\", "opel-blitz", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> alliedTrucks = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("vehicles\\", "vehicles\\opel\\", "opel-blitz", Country.BRITAIN));
        }
    };

    public Truck()
    {
        super();
        this.truckType = TruckType.TRUCK_CARGO;
    }

    public Truck(TruckType truckType)
	{
        super();
	    this.truckType = truckType;
	}

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanTrucks);
        allvehicleDefinitions.addAll(alliedTrucks);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
	{		
		this.country = country;
        
		List<VehicleDefinition> vehicleSet = null;
        displayName = "Truck";
        vehicleSet = germanTrucks;          
        if (country.getSideNoNeutral() == Side.ALLIED)
        {
            vehicleSet = alliedTrucks;                       
        }

		makeRandomVehicleInstance(vehicleSet);
	}

    public Truck copy () 
	{
		Truck truck = new Truck(this.truckType);
		
		truck.index = IndexGenerator.getInstance().getNextIndex();
		
		truck.vehicleType = this.vehicleType;
		truck.displayName = this.displayName;
		truck.linkTrId = this.linkTrId;
		truck.script = this.script;
		truck.model = this.model;
		truck.Desc = this.Desc;
		truck.aiLevel = this.aiLevel;
		truck.numberInFormation = this.numberInFormation;
		truck.vulnerable = this.vulnerable;
		truck.engageable = this.engageable;
		truck.limitAmmo = this.limitAmmo;
		truck.damageReport = this.damageReport;
		truck.country = this.country;
		truck.damageThreshold = this.damageThreshold; 
		
		truck.position = new Coordinate();
		truck.orientation = new Orientation();
		
		truck.entity = new McuTREntity();
		
		truck.populateEntity();
		
		return truck;
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
