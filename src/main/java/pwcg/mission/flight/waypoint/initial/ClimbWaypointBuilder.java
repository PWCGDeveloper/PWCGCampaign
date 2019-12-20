package pwcg.mission.flight.waypoint.initial;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointPriority;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.intercept.CircleWaypointPattern;
import pwcg.mission.mcu.McuWaypoint;

public class ClimbWaypointBuilder 
{
    protected Campaign campaign = null;
    protected Flight flight = null;
    private List<McuWaypoint> climbWPs = new ArrayList<McuWaypoint>();

	public ClimbWaypointBuilder(Flight flight) throws PWCGException 
	{
	    this.campaign = flight.getCampaign();
        this.flight = flight;
	}

    public List<McuWaypoint> createClimbWaypointsForPlayerFlight() throws PWCGException 
	{
		McuWaypoint clearTheDeckClimbWP = clearTheDeck();
		climbWPs.add(clearTheDeckClimbWP);
   
		if (isGenerateClimbWaypoints())
		{
		    double flightAlt = flight.getFlightAltitude();
		    int numClimbLegs = new Double(flightAlt).intValue() / new Integer(flight.getPlanes().get(0).getClimbOutRate());
		    if (numClimbLegs > 5)
		    {
		        numClimbLegs = 5;
		    }
		    
		    if (numClimbLegs > 0)
		    {
    		    CircleWaypointPattern circleWaypointGenerator = new CircleWaypointPattern(
    		    		campaign,
    		    		flight, 
    		    		WaypointType.CLIMB_WAYPOINT, 
    		    		WaypointAction.WP_ACTION_CLIMB,
    		    		McuWaypoint.CLIMB_AREA, 
    		    		numClimbLegs);
    		    
    		    IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
    		    List<McuWaypoint> climbCircleWPs = circleWaypointGenerator.generateCircleWPs(clearTheDeckClimbWP, flightAlt, productSpecificConfiguration.getClimbDistance());
    
    		    climbWPs.addAll(climbCircleWPs);
		    }
		}
        return climbWPs;
	}

    private boolean isGenerateClimbWaypoints() throws PWCGException
    {
        if (!flight.isPlayerFlight())
        {
            return false;
        }
        
        int generateClimbWPs = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.GenerateClimbWPKey);
        if (generateClimbWPs != 1)
        {
            return false;
        }
        
        if (flight.isLowAltFlightType())
        {
            return false;
        }
        
        if (!flight.getSquadron().isStartsCloseToFront(campaign, campaign.getDate()))
        {
            return false;
        }

        return true;
    }

    private McuWaypoint clearTheDeck() throws PWCGException 
    {
        Coordinate initialClimbCoords = createTakeOffCoords();

        McuWaypoint clearTheDeckClimbWP = WaypointFactory.createTakeOffWaypointType();
        clearTheDeckClimbWP.setTriggerArea(McuWaypoint.INITIAL_CLIMB_AREA);
        clearTheDeckClimbWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), WaypointType.CLIMB_WAYPOINT.getName());
        clearTheDeckClimbWP.setSpeed(flight.getFlightCruisingSpeed());
        clearTheDeckClimbWP.setPriority(WaypointPriority.PRIORITY_HIGH);          
        clearTheDeckClimbWP.setPosition(initialClimbCoords);
        clearTheDeckClimbWP.setOrientation(flight.getAirfield().getTakeoffLocation().getOrientation().copy());

        return clearTheDeckClimbWP;
    }

	private Coordinate createTakeOffCoords() throws PWCGException, PWCGException
	{
        int takeoffWaypointDistance = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.TakeoffWaypointDistanceKey);
        int takeoffWaypointAltitude = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.TakeoffWaypointAltitudeKey);

        double takeoffOrientation = flight.getAirfield().getTakeoffLocation().getOrientation().getyOri();
        Coordinate initialClimbCoords = MathUtils.calcNextCoord(flight.getAirfield().getTakeoffLocation().getPosition().copy(), takeoffOrientation, takeoffWaypointDistance);
        initialClimbCoords.setYPos(takeoffWaypointAltitude);
		return initialClimbCoords;
	}
    
 }
