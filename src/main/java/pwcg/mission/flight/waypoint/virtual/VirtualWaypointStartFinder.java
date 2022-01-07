package pwcg.mission.flight.waypoint.virtual;

import java.util.Date;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinePoint;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.IFlight;

public class VirtualWaypointStartFinder
{
    public static final int IS_NOT_NEAR_AREA = -1;
   
    public static int findStartVwpByBox(IFlight flight, List<VirtualWayPointCoordinate> plotCoordinates)
    {
        CoordinateBox missionBorders = flight.getMission().getMissionBorders();
        for (int vwpIndex = 0; vwpIndex < plotCoordinates.size(); ++vwpIndex)
        {
            VirtualWayPointCoordinate vwpCoordinate = plotCoordinates.get(vwpIndex);
            if (missionBorders.isInBox(vwpCoordinate.getPosition()))
            {
                return vwpIndex;
            }
        }
        
        return IS_NOT_NEAR_AREA;
    }

    public static int findEndVwpByBox(IFlight flight, List<VirtualWayPointCoordinate> plotCoordinates)
    {
        int lastVwpInBox = IS_NOT_NEAR_AREA;
        CoordinateBox missionBorders = flight.getMission().getMissionBorders();
        for (int vwpIndex = 0; vwpIndex < plotCoordinates.size(); ++vwpIndex)
        {
            VirtualWayPointCoordinate vwpCoordinate = plotCoordinates.get(vwpIndex);
            if (missionBorders.isInBox(vwpCoordinate.getPosition()))
            {
                lastVwpInBox =  vwpIndex;
            }
        }
        
        return lastVwpInBox;
    }

    public static int findStartVwpProximityToFront(IFlight flight, List<VirtualWayPointCoordinate> plotCoordinates) throws PWCGException
    {
        for (int vwpIndex = 0; vwpIndex < plotCoordinates.size(); ++vwpIndex)
        {
            VirtualWayPointCoordinate vwpCoordinate = plotCoordinates.get(vwpIndex);
            double vwpDistanceToFront = getVwpDistanceFromFront(flight, vwpCoordinate);            
            IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            int vwpProximityToFrontDistance = productSpecific.getVwpProximityToFrontDistance();
            if (vwpDistanceToFront < vwpProximityToFrontDistance)
            {
                return vwpIndex;
            }
        }
        
        return IS_NOT_NEAR_AREA;
    }
    

    public static int findEndVwpProximityToFront(IFlight flight, List<VirtualWayPointCoordinate> plotCoordinates) throws PWCGException
    {
        int lastVwpNearFront = IS_NOT_NEAR_AREA;
        for (int vwpIndex = 0; vwpIndex < plotCoordinates.size(); ++vwpIndex)
        {
            VirtualWayPointCoordinate vwpCoordinate = plotCoordinates.get(vwpIndex);
            double vwpDistanceToFront = getVwpDistanceFromFront(flight, vwpCoordinate);
            IProductSpecificConfiguration productSpecific = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            int vwpProximityToFrontDistance = productSpecific.getVwpProximityToFrontDistance();
            if (vwpDistanceToFront < vwpProximityToFrontDistance)
            {
                lastVwpNearFront =  vwpIndex;
            }
        }
        
        lastVwpNearFront = addOneOutOfRangeToEndOfFlight(plotCoordinates, lastVwpNearFront);
        
        return lastVwpNearFront;
    }

    private static int addOneOutOfRangeToEndOfFlight(List<VirtualWayPointCoordinate> plotCoordinates, int lastVwpNearFront)
    {
        if (lastVwpNearFront != IS_NOT_NEAR_AREA)
        {
            if (plotCoordinates.size() > (lastVwpNearFront+1))
            {
                ++lastVwpNearFront;
            }
        }
        return lastVwpNearFront;
    }

    private static double getVwpDistanceFromFront(IFlight flight, VirtualWayPointCoordinate vwpCoordinate) throws PWCGException
    {
        Side side = flight.getFlightInformation().getCountry().getSide();
        Date campaignDate = flight.getCampaign().getDate();
        FrontLinePoint closestFrontPosition = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaignDate).findClosestFrontPositionForSide(vwpCoordinate.getPosition(), side);
        double vwpDistanceToFront = MathUtils.calcDist(closestFrontPosition.getPosition(), vwpCoordinate.getPosition());
        return vwpDistanceToFront;
    }
}
