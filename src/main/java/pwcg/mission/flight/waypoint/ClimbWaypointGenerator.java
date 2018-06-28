package pwcg.mission.flight.waypoint;

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
import pwcg.mission.mcu.McuWaypoint;

public class ClimbWaypointGenerator 
{
    protected Campaign campaign = null;
    protected Flight flight = null;
    protected int waypointSpeed = 250;

	public ClimbWaypointGenerator(Campaign campaign, Flight flight) throws PWCGException 
	{
	    this.campaign = campaign;
        this.flight = flight;
	}

    public List<McuWaypoint> createClimbWaypoints(double flightAlt) throws PWCGException 
    {
        List<McuWaypoint> climbWPs = new ArrayList<McuWaypoint>();

        if (flight.isPlayerFlight())
        {
            createClimbWaypointsForPlayerFlight(flightAlt, climbWPs);
        }
        else
        {
            placeAIFlightOverAirfield(flightAlt, climbWPs);
        }
        
        return climbWPs;
    }

	private void createClimbWaypointsForPlayerFlight(double flightAlt, List<McuWaypoint> climbWPs) throws PWCGException 
	{
		McuWaypoint clearTheDeckClimbWP = clearTheDeck();
		climbWPs.add(clearTheDeckClimbWP);
   
		if (isGenerateClimbWaypoints())
		{
		    int numClimbLegs = new Double(flightAlt).intValue() / new Integer(flight.getPlanes().get(0).getClimbOutRate());
		    
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
	}

	private void placeAIFlightOverAirfield(double flightAlt, List<McuWaypoint> climbWPs) throws PWCGException {
		McuWaypoint aiStartWP = createAiStartWaypoint(flightAlt);
		climbWPs.add(aiStartWP);
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
        
        if (!flight.getSquadron().isStartsCloseToFront(campaign.getDate()))
        {
            return false;
        }

        return true;
    }

    private McuWaypoint createAiStartWaypoint(double flightAlt) throws PWCGException
    {        
        Coordinate startCoords = createAiStartCoords(flightAlt);

        McuWaypoint aiStartWP = WaypointFactory.createStartWaypointType();
        aiStartWP.setTriggerArea(McuWaypoint.CLIMB_AREA);
        aiStartWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), WaypointType.STARTING_WAYPOINT.getName());
        aiStartWP.setSpeed(waypointSpeed);
        aiStartWP.setOrientation(flight.getAirfield().getOrientation().copy());
        aiStartWP.setPosition(startCoords);
        
        return aiStartWP;
    }

	private Coordinate createAiStartCoords(double flightAlt)
	{
		Coordinate startCoords = flight.getAirfield().getPosition().copy();
        
        double startWPAltitude = flightAlt * .8;
        if (startWPAltitude < 1000)
        {
            startWPAltitude = 1000;
        }
        
        startCoords.setYPos(startWPAltitude);
		return startCoords;
	}

    private McuWaypoint clearTheDeck() throws PWCGException 
    {
        Coordinate initialClimbCoords = createTakeOffCoords();

        McuWaypoint clearTheDeckClimbWP = WaypointFactory.createTakeOffWaypointType();
        clearTheDeckClimbWP.setTriggerArea(McuWaypoint.INITIAL_CLIMB_AREA);
        clearTheDeckClimbWP.setDesc(flight.getSquadron().determineDisplayName(campaign.getDate()), WaypointType.CLIMB_WAYPOINT.getName());
        clearTheDeckClimbWP.setSpeed(waypointSpeed - 20);
        clearTheDeckClimbWP.setPriority(WaypointPriority.PRIORITY_HIGH);          
        clearTheDeckClimbWP.setPosition(initialClimbCoords);
        clearTheDeckClimbWP.setOrientation(flight.getAirfield().getOrientation().copy());

        return clearTheDeckClimbWP;
    }

	private Coordinate createTakeOffCoords() throws PWCGException, PWCGException
	{
		// Set distance and alt as configured - hopefully enough to stop crashes
        int takeoffWaypointDistance = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.TakeoffWaypointDistanceKey);
        int takeoffWaypointAltitude = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.TakeoffWaypointAltitudeKey);

        double airfieldOrientation = flight.getAirfield().getOrientation().getyOri();
        Coordinate initialClimbCoords = MathUtils.calcNextCoord(flight.getAirfield().getPosition().copy(), airfieldOrientation, takeoffWaypointDistance);
        initialClimbCoords.setYPos(takeoffWaypointAltitude);
		return initialClimbCoords;
	}
    
 }
