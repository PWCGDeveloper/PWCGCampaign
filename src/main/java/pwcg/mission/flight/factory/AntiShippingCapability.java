package pwcg.mission.flight.factory;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionAntiShippingSeaLaneFinder;
import pwcg.mission.SquadronRange;

public class AntiShippingCapability
{
    public static boolean canFlyAntiShipping(Campaign campaign, Squadron squadron) throws PWCGException
    {
        List<FrontMapIdentifier> squadronMaps = AirfieldManager.getMapIdForAirfield(squadron.determineCurrentAirfieldName(campaign.getDate()));
        if (squadronMaps.contains(FrontMapIdentifier.KUBAN_MAP))
        {
            MissionAntiShippingSeaLaneFinder seaLaneFinder = new MissionAntiShippingSeaLaneFinder(campaign);
            ShippingLane shippingLane = seaLaneFinder.getShippingLaneForMission(squadron);
            
            Coordinate shippingLaneCenter = shippingLane.getShippingLaneBox().getCenter();
            return SquadronRange.positionIsInRange(campaign, squadron, shippingLaneCenter);
        }
        
        return false;
    }
}
