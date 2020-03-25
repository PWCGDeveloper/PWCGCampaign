package pwcg.mission.ground.unittypes.staticunits;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.AirfieldManager;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.GroundUnitInformation;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnit;
import pwcg.mission.ground.org.GroundUnitNumberCalculator;
import pwcg.mission.ground.vehicle.VehicleClass;

public class AirfieldTargetGroup extends GroundUnit
{
    protected Campaign campaign;
    protected IAirfield targetAirfield;

	public AirfieldTargetGroup(Campaign campaign, IAirfield targetAirfield, GroundUnitInformation pwcgGroundUnitInformation) 
	{
        super(VehicleClass.Truck, pwcgGroundUnitInformation);
        this.campaign = campaign;
        this.targetAirfield = targetAirfield;
	}

    public IAirfield getAirfield()
    {
        return targetAirfield;
    }

    public void createAirfield() throws PWCGException  
	{
        AirfieldManager airfieldManager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();
        this.targetAirfield = airfieldManager.getAirfieldFinder().findClosestAirfield(pwcgGroundUnitInformation.getPosition());
	}

    protected List<Coordinate> createSpawnerLocations() throws PWCGException 
    {
        List<Coordinate> spawnerLocations = new ArrayList<>();

        int numTrucks = calcNumUnits();
        Coordinate initialTruckLocation = findTruckStartLocation();
        for (int i = 0; i < numTrucks; ++i)
        {
            Coordinate truckLocation = findNextTruckLocation(initialTruckLocation, i);
            spawnerLocations.add(truckLocation);
        }       

        return spawnerLocations;        
    }

    private int calcNumUnits() throws PWCGException
    {
        if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_TINY)
        {
            return GroundUnitNumberCalculator.calcNumUnits(1, 1);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_LOW)
        {
            return GroundUnitNumberCalculator.calcNumUnits(2, 4);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM)
        {
            return GroundUnitNumberCalculator.calcNumUnits(3, 6);
        }
        else if (pwcgGroundUnitInformation.getUnitSize() == GroundUnitSize.GROUND_UNIT_SIZE_HIGH)
        {
            return GroundUnitNumberCalculator.calcNumUnits(4, 8);
        }
        
        throw new PWCGException ("No unit size provided for ground unit");
    }

	private Coordinate findTruckStartLocation() throws PWCGException, PWCGException
	{
		ConfigManager configManager = campaign.getCampaignConfigManager();
		int truckDistance = configManager.getIntConfigParam(ConfigItemKeys.WindsockDistanceKey);
		Double angleTrucksLeft = MathUtils.adjustAngle(pwcgGroundUnitInformation.getOrientation().getyOri(), -90);
		Coordinate initialTruckLocation = MathUtils.calcNextCoord(pwcgGroundUnitInformation.getPosition(), angleTrucksLeft, truckDistance);
		
		return initialTruckLocation;
	}

    private Coordinate findNextTruckLocation(Coordinate initialTruckLocation, int truckNumber) throws PWCGException, PWCGException
    {
        Coordinate truckLocation = MathUtils.calcNextCoord(initialTruckLocation, pwcgGroundUnitInformation.getOrientation().getyOri(), (10 * truckNumber));
        
        return truckLocation;
    }

    @Override
    protected void addAspects()
    {
        // No elements for parked trucks
    }
}	

