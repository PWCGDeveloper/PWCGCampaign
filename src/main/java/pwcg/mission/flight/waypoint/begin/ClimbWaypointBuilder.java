package pwcg.mission.flight.waypoint.begin;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.patterns.CircleWaypointPattern;
import pwcg.mission.mcu.McuWaypoint;

public class ClimbWaypointBuilder 
{
    protected Campaign campaign = null;
    protected IFlight flight = null;
    private List<McuWaypoint> climbWPs = new ArrayList<McuWaypoint>();

	public ClimbWaypointBuilder(IFlight flight) throws PWCGException 
	{
	    this.campaign = flight.getCampaign();
        this.flight = flight;
	}

    public List<McuWaypoint> createClimbWaypointsForPlayerFlight(McuWaypoint takeoffWP) throws PWCGException 
	{
		if (isGenerateClimbWaypoints())
		{
		    double flightAlt = flight.getFlightData().getFlightInformation().getAltitude();
		    int numClimbLegs = Double.valueOf(flightAlt).intValue() / Integer.valueOf(flight.getFlightData().getFlightPlanes().getFlightLeader().getClimbOutRate());
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
    		    List<McuWaypoint> climbCircleWPs = circleWaypointGenerator.generateCircleWPs(takeoffWP, flightAlt, productSpecificConfiguration.getClimbDistance());
    
    		    climbWPs.addAll(climbCircleWPs);
		    }
		}
        return climbWPs;
	}

    private boolean isGenerateClimbWaypoints() throws PWCGException
    {
        if (!flight.getFlightData().getFlightInformation().isPlayerFlight())
        {
            return false;
        }
        
        int generateClimbWPs = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.GenerateClimbWPKey);
        if (generateClimbWPs != 1)
        {
            return false;
        }
        
        if (FlightTypes.isLowAltFlightType(flight.getFlightData().getFlightInformation().getFlightType()))
        {
            return false;
        }
        
        if (!flight.getFlightData().getFlightInformation().getSquadron().isStartsCloseToFront(campaign, campaign.getDate()))
        {
            return false;
        }

        return true;
    }    
 }
