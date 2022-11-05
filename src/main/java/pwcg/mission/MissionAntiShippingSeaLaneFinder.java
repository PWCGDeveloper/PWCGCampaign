package pwcg.mission;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.shipping.ShippingLaneManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class MissionAntiShippingSeaLaneFinder
{
    public static ShippingLane getShippingLaneForMission(FrontMapIdentifier mapIdentifier, Coordinate basePosition, Side side) throws PWCGException
    {
        ShippingLane shippingLane = null;
        if (PWCGContext.getInstance().getMap(mapIdentifier).hasShips())
        {
            PWCGMap map = PWCGContext.getInstance().getMap(mapIdentifier);
            ShippingLaneManager shippingLaneManager = map.getShippingLaneManager();
            shippingLane = shippingLaneManager.getClosestShippingLaneBySide(basePosition, side);
        }
        
        return shippingLane;
    }
    
    public static boolean canFlyAntiShipping(Campaign campaign, Squadron squadron,ShippingLane shippingLane) throws PWCGException
    {
        if (PWCGContext.getInstance().getMap(campaign.getCampaignMap()).hasShips())
        {
            Coordinate shippingLaneCenter = shippingLane.getShippingLaneBox().getCenter();
            return SquadronRange.positionIsInRange(campaign, squadron, shippingLaneCenter);
        }
        else
        {
            return false;
        }
    }
}
