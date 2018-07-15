package pwcg.campaign.ww1.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

public class TrainCar extends Vehicle
{
	private TrainDO car = null;

	// Cars
	private TrainDO[] cars = 
	{
			new TrainDO("pass", "pass", "Passenger", 10.6),
			new TrainDO("pass", "passc", "Passenger", 10.6),
			new TrainDO("platform", "platformb", "Platform", 10.6),
			new TrainDO("platform", "platformemptyb", "Platform", 10.6),
			new TrainDO("platform", "platformnb", "Platform", 10.6),
			new TrainDO("platform", "platformemptynb", "Platform", 10.6),
			new TrainDO("platform", "gondolab", "Freight", 8.65),
			new TrainDO("platform", "gondolanb", "Freight", 7.65),
			new TrainDO("box", "boxb", "Boxcar", 8.65),
			new TrainDO("box", "boxnb", "Boxcar", 7.65),
	};
	
	// Tanker
	private TrainDO[] tankers = 
	{
			new TrainDO("box", "tankb", "Tanker", 8.8),
			new TrainDO("box", "tanknb", "Tanker", 7.85),
	};
	
	// Ambulance
	private TrainDO[] ambulance = 
	{
			new TrainDO("pass", "passa", "Ambulance", 10.6),
	};
	
	public TrainCar(ICountry country) 
	{
        super(country);
        
		// 80% car, 10% tanker,10% ambulance
		TrainDO[] trainArray = null;
		int roll = RandomNumberGenerator.getRandom(100);
		if (roll < 10)
		{
			trainArray = tankers;
		}
		else if (roll < 20)
		{
			trainArray = ambulance;			
		}
		else
		{
			trainArray = cars;			
		}
		
		// Now pick one from the selected category
		roll = RandomNumberGenerator.getRandom(trainArray.length);
		car = trainArray[roll];
		
		displayName = car.getName();
		
		vehicleType = car.getName();
		script = "LuaScripts\\WorldObjects\\Trains\\" + car.getId() + ".txt";
		model = "graphics\\trains\\" + car.getCategory() + "\\" + car.getId() + ".mgm";
	}

	public TrainCar copy () throws PWCGException 
	{		
		TrainCar train = new TrainCar(country);
		train.index = IndexGenerator.getInstance().getNextIndex();
		
		train.vehicleType = this.vehicleType;
		train.displayName = this.displayName;
		train.linkTrId = this.linkTrId;
		train.script = this.script;
		train.model = this.model;
		train.Desc = this.Desc;
		train.aiLevel = this.aiLevel;
		train.numberInFormation = this.numberInFormation;
		train.vulnerable = this.vulnerable;
		train.engageable = this.engageable;
		train.limitAmmo = this.limitAmmo;
		train.damageReport = this.damageReport;
		train.country = this.country;
		train.damageThreshold = this.damageThreshold; 
		
		train.position = new Coordinate();
		train.orientation = new Orientation();
		
		train.entity = new McuTREntity();
		
		train.populateEntity();
		
		train.car = this.car.copy();
		
		return train;
	}
}
