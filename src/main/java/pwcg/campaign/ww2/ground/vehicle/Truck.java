package pwcg.campaign.ww2.ground.vehicle;

import java.io.BufferedWriter;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGIOException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

class Truck extends Vehicle
{
    public enum TruckType
    {
        TRUCK_CARGO,
        TRUCK_AAA,
        CAR;
    }
    
	private String[][] germanTrucks = 
	{
        { "ford", "ford-g917" },
        { "ford", "ford-g917" },
        { "opel", "opel-blitz" },
        { "opel", "opel-blitz" },
        { "opel", "opel-blitz" },
        { "sdkfz251", "sdkfz251-1c" },
        { "sdkfz251", "sdkfz251-1c" },
        { "sdkfz251", "sdkfz251-szf" }
	};

	private String[][] germanAAATrucks = 
	{
	    { "sdkfz10-flak38", "sdkfz10-flak38" },
	};

    private String[][] germanCars = 
    {
        { "horch", "horch830" }
    };

	private String[][] alliedTrucks = 
	{
        { "gaz", "gaz-aa" },
        { "gaz", "gaz-aa" },
        { "gaz", "gaz-aa" },
        { "zis", "zis5" },
        { "zis", "zis5" },
        { "zis", "zis5" },
        { "zis", "bm13" }
	};

    private String[][] alliedAAATrucks = 
    {
        { "gaz", "gaz-aa-m4-aa" },
        { "zis", "zis5-72k" }
    };

    private String[][] alliedCars = 
    {
        { "gaz", "gaz-m" }
    };

	protected Truck()
	{
	}

	public Truck(ICountry country, TruckType truckType) throws PWCGException 
	{
		super();
		
		this.country = country;
        
        if (truckType == TruckType.TRUCK_CARGO)
        {
            makeTruck(country);
        }
        else if (truckType == TruckType.TRUCK_AAA)
        {
            makeAAATruck(country);
        }
        else if (truckType == TruckType.CAR)
        {
            makeCar(country);
        }
	}

    private void makeTruck(ICountry countryObj) throws PWCGException
    {
        if (countryObj.getSideNoNeutral() == Side.ALLIED)
        {
            makeVehicleInstance(alliedTrucks, "Russian Truck");
        }
        else
        {
            makeVehicleInstance(germanTrucks, "German Truck");
        }
    }

    private void makeAAATruck(ICountry countryObj) throws PWCGException
    {
        if (countryObj.getSideNoNeutral() == Side.ALLIED)
        {
            makeVehicleInstance(alliedAAATrucks, "Russian AAA Truck");
        }
        else
        {
            makeVehicleInstance(germanAAATrucks, "German AAA Truck");
        }
    }

    private void makeCar(ICountry countryObj) throws PWCGException
    {
        if (countryObj.getSideNoNeutral() == Side.ALLIED)
        {
            makeVehicleInstance(alliedCars, "Russian AAA Truck");
        }
        else
        {
            makeVehicleInstance(germanCars, "German AAA Truck");
        }
    }
	
	private void makeVehicleInstance(String[][] vehicleChoices, String displayName)
	{
        int selectedTruck = RandomNumberGenerator.getRandom(vehicleChoices.length);
        String truckDir = vehicleChoices[selectedTruck] [0];
        String truckId = vehicleChoices[selectedTruck] [1];
        
        this.displayName = displayName;
        vehicleType = truckId;
        script = "LuaScripts\\WorldObjects\\vehicles\\" + truckId + ".txt";
        model = "graphics\\vehicles\\" + truckDir + "\\" + truckId + ".mgm";
	}

	public Truck copy () 
	{
		Truck truck = new Truck();
		
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
