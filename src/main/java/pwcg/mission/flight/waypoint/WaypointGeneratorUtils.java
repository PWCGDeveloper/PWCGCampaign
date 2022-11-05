package pwcg.mission.flight.waypoint;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.mcu.McuWaypoint;

public class WaypointGeneratorUtils 
{
	public static McuWaypoint findWaypointByType(List<McuWaypoint> waypointList, String type)
	{
		McuWaypoint theWP = null;
		for (McuWaypoint waypoint : waypointList)
		{
			if (waypoint.getName().equals(type))
			{
				theWP = waypoint;
				break;
			}
		}
		
		return theWP;
	}

	public static boolean goNorth(Campaign campaign, int startFrontIndex, Side side) throws PWCGException 
    {
        final int closestToEdge = 10;

        boolean goNorth = true;
        
        // At northern edge - go south
        FrontLinesForMap frontLineMarker =  PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(side);
        if (startFrontIndex < closestToEdge)
        {
            goNorth = false;
        }
        // at southern edge - go north
        else if (startFrontIndex > (frontLines.size() - closestToEdge) )
        {
            goNorth = true;
        }
        // in the middle - either direction
        else
        {
            int goNorthInt = RandomNumberGenerator.getRandom(100);
            if (goNorthInt < 50)
            {
                goNorth = false;
            }
        }
        
        return goNorth;
    }

	public static int getNextFrontIndex(Campaign campaign, int startFrontIndex, boolean goNorth, int numToAdvance, Side side) throws PWCGException
    {
        int frontIndex;
        if (goNorth)
        {
            frontIndex = startFrontIndex - numToAdvance;
        }
        else
        {
            frontIndex = startFrontIndex + numToAdvance;
        }
        
        // Don't go too far North
        if (frontIndex < 3)
        {
            frontIndex = 2;
        }
        
        // Don't go too far South
        FrontLinesForMap frontLineMarker =  PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(side);
        if (frontIndex > frontLines.size())
        {
            frontIndex = frontLines.size()-2;
        }

        
        return frontIndex;
    }

	public static boolean isEdgeOfMap(Campaign campaign, int frontIndex, boolean goNorth, Side side) throws PWCGException
    {
        if (goNorth && frontIndex < 4)
        {
            return true;
        }
        
        FrontLinesForMap frontLineMarker =  PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getFrontLinesForMap(campaign.getDate());
        List<FrontLinePoint> frontLines = frontLineMarker.getFrontLines(side);
        if (!goNorth && (frontIndex > (frontLines.size() - 4)))
        {
            return true;
        }
        
        return false;
    }

	public static List<McuWaypoint> getTargetWaypoints(List<McuWaypoint> playerWaypoints)
    {
        List<McuWaypoint> selectedWaypoints = new ArrayList <McuWaypoint>();
        
        for (int i = 0; i < playerWaypoints.size(); ++i)
        {
            McuWaypoint playerWaypoint = playerWaypoints.get(i);
            if (playerWaypoint.getWpAction() == WaypointAction.WP_ACTION_PATROL ||
                playerWaypoint.getWpAction() == WaypointAction.WP_ACTION_RECON)
                            
            {
                selectedWaypoints.add(playerWaypoint);
            }
        }

        return selectedWaypoints;
    }
}
