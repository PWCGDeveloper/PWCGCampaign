package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.shipping.ShippingLaneManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class MissionAntiShippingSeaLaneFinder
{
    public static ShippingLane getShippingLaneForMission(Mission mission, Side side) throws PWCGException
    {
        ShippingLane shippingLane = null;
        PWCGMap map = PWCGContext.getInstance().getCurrentMap();
        if (map.getMapIdentifier()  == FrontMapIdentifier.KUBAN_MAP)
        {
            ShippingLaneManager shippingLaneManager = map.getShippingLaneManager();
            
            shippingLane = shippingLaneManager.getClosestShippingLaneBySide(mission.getMissionBorders().getCenter(), side);
        }
        
        return shippingLane;
    }
    
    public static boolean canFlyAntiShipping(Campaign campaign, Squadron squadron,ShippingLane shippingLane) throws PWCGException
    {
        List<FrontMapIdentifier> squadronMaps = AirfieldManager.getMapIdForAirfield(squadron.determineCurrentAirfieldName(campaign.getDate()));
        if (squadronMaps.contains(FrontMapIdentifier.KUBAN_MAP))
        {            
            Coordinate shippingLaneCenter = shippingLane.getShippingLaneBox().getCenter();
            return SquadronRange.positionIsInRange(campaign, squadron, shippingLaneCenter);
        }
        
        return false;
    }
}
