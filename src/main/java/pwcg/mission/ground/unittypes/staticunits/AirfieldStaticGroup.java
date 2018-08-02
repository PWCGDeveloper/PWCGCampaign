package pwcg.mission.ground.unittypes.staticunits;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.VehicleFactory;
import pwcg.campaign.group.AirfieldManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleFactory;

public class AirfieldStaticGroup extends GroundUnit
{
    protected Campaign campaign;
    protected ArrayList<IVehicle> trucks = new ArrayList<IVehicle>();
    protected ArrayList<IVehicle> staticPlanes = new ArrayList<IVehicle>();
    protected IAirfield airfield;

	public AirfieldStaticGroup(Campaign campaign, GroundUnitInformation pwcgGroundUnitInformation) 
	{
		super(pwcgGroundUnitInformation);
        this.campaign = campaign;
	}

	@Override
	public void createUnitMission() throws PWCGException  
	{
        AirfieldManager airfieldManager = PWCGContextManager.getInstance().getCurrentMap().getAirfieldManager();
        this.airfield = airfieldManager.getAirfieldFinder().findClosestAirfield(pwcgGroundUnitInformation.getPosition());

        createTrucks();
	}

    private void createTrucks() throws PWCGException 
    {
        IVehicleFactory vehicleFactory = VehicleFactory.createVehicleFactory();
        IVehicle truckType = vehicleFactory.createCargoTruck(pwcgGroundUnitInformation.getCountry());

        int numTrucks = calcNumUnits();

        Coordinate initialTruckLocation = findTruckStartLocation();
        for (int i = 0; i < numTrucks; ++i)
        {
            IVehicle truck = vehicleFactory.cloneTruck(truckType);
            
            Coordinate truckLocation = findTruckLocation(initialTruckLocation, i);
            truck.setPosition(truckLocation);
            truck.setOrientation(new Orientation(RandomNumberGenerator.getRandom(360)));

            trucks.add(truck);
        }       
    }

	private Coordinate findTruckStartLocation() throws PWCGException, PWCGException
	{
		ConfigManager configManager = campaign.getCampaignConfigManager();
		int truckDistance = configManager.getIntConfigParam(ConfigItemKeys.WindsockDistanceKey);
		Double angleTrucksLeft = MathUtils.adjustAngle(pwcgGroundUnitInformation.getOrientation().getyOri(), -90);
		Coordinate initialTruckLocation = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), angleTrucksLeft, truckDistance);
		
		return initialTruckLocation;
	}

	private int calcNumUnits()
    {
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            setMinMaxRequested(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            setMinMaxRequested(2, 3);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            setMinMaxRequested(2, 4);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            setMinMaxRequested(3, 6);
        }

        return calculateForMinMaxRequested();
    }


	private Coordinate findTruckLocation(Coordinate initialTruckLocation, int truckNumber) throws PWCGException, PWCGException
	{
		Coordinate truckLocation = MathUtils.calcNextCoord(initialTruckLocation, pwcgGroundUnitInformation.getOrientation().getyOri(), (10 * truckNumber));
		
		return truckLocation;
	}


	@Override
	public List<IVehicle> getVehicles() 
	{
		return trucks;
	}

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {      
    	for (IVehicle truck : trucks)
    	{
    		truck.write(writer);
    	}
    }

	@Override
	protected void createGroundTargetAssociations()
	{
	}

    public IAirfield getAirfield()
    {
        return airfield;
    }
}	

