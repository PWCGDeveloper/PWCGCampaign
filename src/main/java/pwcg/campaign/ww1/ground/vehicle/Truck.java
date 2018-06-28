package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class Truck extends Vehicle
{
	// German
	private String[][] germanTrucks = 
	{
		{ "benz_open", "benz" },
		{ "benz_soft", "benz" },
		{ "daimlermarienfelde", "daimlermarienfelde" },
		{ "daimlermarienfelde_s", "daimlermarienfelde" },
	};
	
	// British
	private String[][] alliedTrucks = 
	{
		{ "leyland", "leyland" },
		{ "leylands", "leyland" },
		{ "quad", "quad" },
		{ "quada", "quad" },
	};


	public Truck(ICountry country) 
	{
        super(country);
        
		String truckId= "";
		String truckDir = "";
		
		if (country.getCountry() == Country.FRANCE)
		{
			int selectedTruck = RandomNumberGenerator.getRandom(alliedTrucks.length);
			truckId = alliedTrucks[selectedTruck] [0];
			truckDir = alliedTrucks[selectedTruck] [1];
			displayName = "French Truck";
		}
		else if (country.getCountry() == Country.USA)
		{
			int selectedTruck = RandomNumberGenerator.getRandom(alliedTrucks.length);
			truckId = alliedTrucks[selectedTruck] [0];
			truckDir = alliedTrucks[selectedTruck] [1];
			displayName = "American Truck";
		}
		else if (country.getCountry() == Country.GERMANY)
		{
			int selectedTruck = RandomNumberGenerator.getRandom(germanTrucks.length);
			truckId = germanTrucks[selectedTruck] [0];
			truckDir = germanTrucks[selectedTruck] [1];
			displayName = "German Truck";
		}
		else
		{
			int selectedTruck = RandomNumberGenerator.getRandom(alliedTrucks.length);
			truckId = alliedTrucks[selectedTruck] [0];			
			truckDir = alliedTrucks[selectedTruck] [1];
			displayName = "British Truck";
		}
		
		name = truckId;
		script = "LuaScripts\\WorldObjects\\" + truckId + ".txt";
		model = "graphics\\vehicles\\" + truckDir + "\\" + truckId + ".mgm";
	}

	public Truck copy () 
	{
		Truck truck = new Truck(country);
		
		truck.index = IndexGenerator.getInstance().getNextIndex();
		
		truck.name = this.name;
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
	
}
