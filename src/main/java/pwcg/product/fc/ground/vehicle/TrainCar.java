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
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.mcu.McuTREntity;

class TrainCar extends Vehicle
{
	private List<VehicleDefinition> vehicleSet;

    private static final List<VehicleDefinition> germanTrainCar = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "boxb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "boxnb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "gondolab", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "gondolanb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "pass", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "passc", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformnb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformemptyb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformemptynb", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> germanTankerCar = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\tankb\\", "tankb", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\tanknb\\", "tanknb", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> germanAAACar = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\platformaa-mg34\\", "platformaa-mg34", Country.GERMANY));
            add(new VehicleDefinition("trains\\", "trains\\platformaa-flak38\\", "platformaa-flak38", Country.GERMANY));
        }
    };

    private static final List<VehicleDefinition> britishTrainCar = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "boxb", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "boxnb", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "gondolab", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "gondolanb", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "pass", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "passc", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformb", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformnb", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformemptyb", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\traincars\\", "platformemptynb", Country.BRITAIN));
        }
    };

    private static final List<VehicleDefinition> britishTankerCar = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\tankb\\", "tankb", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\tanknb\\", "tanknb", Country.BRITAIN));
        }
    };

    private static final List<VehicleDefinition> britishAAACar = new ArrayList<VehicleDefinition>() 
    {
        private static final long serialVersionUID = 1L;
        {
            add(new VehicleDefinition("trains\\", "trains\\platformaa-m4\\", "platformaa-m4", Country.BRITAIN));
            add(new VehicleDefinition("trains\\", "trains\\platformaa-61k\\", "platformaa-61k", Country.BRITAIN));
        }
    };
        
	protected TrainCar()
	{
	}

    @Override
    public List<VehicleDefinition> getAllVehicleDefinitions()
    {
        List<VehicleDefinition> allvehicleDefinitions = new ArrayList<>();
        allvehicleDefinitions.addAll(germanTrainCar);
        allvehicleDefinitions.addAll(germanTankerCar);
        allvehicleDefinitions.addAll(germanAAACar);
        allvehicleDefinitions.addAll(britishTrainCar);
        allvehicleDefinitions.addAll(britishTankerCar);
        allvehicleDefinitions.addAll(britishAAACar);
        return allvehicleDefinitions;
    }

    @Override
    public void makeRandomVehicleFromSet(ICountry country) throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 20)
        {
            displayName = "Tanker Car";
            vehicleSet = germanTankerCar;          
            if (country.getSideNoNeutral() == Side.ALLIED)
            {
                vehicleSet = britishTankerCar;                       
            }
        }
        else if (roll < 30)
        {
            displayName = "AAA Car";
            vehicleSet = germanAAACar;          
            if (country.getSideNoNeutral() == Side.ALLIED)
            {
                vehicleSet = britishAAACar;                       
            }
        }
        else
        {
            displayName = "Freight Car";
            vehicleSet = germanTrainCar;          
            if (country.getSideNoNeutral() == Side.ALLIED)
            {
                vehicleSet = britishTrainCar;                       
            }
        }

        makeRandomVehicleInstance(vehicleSet);
    }

	public TrainCar copy () 
	{		
		TrainCar train = new TrainCar();
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
		
		return train;
	}
}
