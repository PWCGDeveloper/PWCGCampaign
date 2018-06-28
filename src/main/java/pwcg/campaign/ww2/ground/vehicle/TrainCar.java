package pwcg.campaign.ww2.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuTREntity;

class TrainCar extends Vehicle
{
	private TrainDO car = null;

	private TrainDO[] cars = 
	{
	        new TrainDO("traincars", "boxb", "Boxcar", 8.65),
	        new TrainDO("traincars", "boxnb", "Boxcar", 7.65),
            new TrainDO("traincars", "gondolab", "Freight", 8.65),
            new TrainDO("traincars", "gondolanb", "Freight", 7.65),
			new TrainDO("traincars", "pass", "Passenger", 10.6),
			new TrainDO("traincars", "passc", "Passenger", 10.6),
			
			new TrainDO("traincars", "platformb", "Platform", 10.6),
            new TrainDO("traincars", "platformnb", "Platform", 10.6),
            new TrainDO("traincars", "platformemptyb", "Platform", 10.6),
            new TrainDO("traincars", "platformemptynb", "Platform", 10.6),
	};
	
    // Tanker
    private TrainDO[] tankers = 
    {
            new TrainDO("traincars", "tankb", "Tanker", 8.8),
            new TrainDO("traincars", "tanknb", "Tanker", 7.85),
    };
    
    // AAA
    private TrainDO[] aaaRussian = 
    {
            new TrainDO("traincars", "platformaa-m4", "AAA", 8.8),
            new TrainDO("traincars", "platformaa-61k", "AAA", 7.85),
    };
    
    // AAA
    private TrainDO[] aaaGerman = 
    {
        new TrainDO("traincars", "platformaa-mg34", "AAA", 8.8),
        new TrainDO("traincars", "platformaa-flak38", "AAA", 7.85),
};
        
	protected TrainCar()
	{
	}

	public TrainCar(ICountry country) throws PWCGException 
	{
		super();

		this.country = country;
		
		// 80% car, 10% tanker,10% ambulance
		TrainDO[] trainArray = null;
		int roll = RandomNumberGenerator.getRandom(100);
		if (roll < 20)
		{
			trainArray = tankers;
		}
		else if (roll < 35)
		{
            trainArray = aaaGerman;          
    		if (country.getSideNoNeutral() == Side.ALLIED)
		    {
	            trainArray = aaaRussian;          		        
		    }
		}
		else
		{
			trainArray = cars;			
		}
		
		// Now pick one from the selected category
		roll = RandomNumberGenerator.getRandom(trainArray.length);
		car = trainArray[roll];
		
		displayName = car.getName();
		
		name = car.getName();
				
		script = "LuaScripts\\WorldObjects\\Trains\\" + car.getId() + ".txt";
		model = "graphics\\trains\\" + car.getCategory() + "\\" + car.getId() + ".mgm";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public TrainCar copy () 
	{		
		TrainCar train = new TrainCar();
		train.index = IndexGenerator.getInstance().getNextIndex();
		
		train.name = this.name;
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

	public TrainDO getCar() 
	{
		return car;
	}
}
