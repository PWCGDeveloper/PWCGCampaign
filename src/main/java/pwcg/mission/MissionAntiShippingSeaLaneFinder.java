package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.locator.ShippingLane;
import pwcg.campaign.target.locator.ShippingLaneManager;
import pwcg.core.exception.PWCGException;

public class MissionAntiShippingSeaLaneFinder
{
    private Campaign campaign;
    
    public MissionAntiShippingSeaLaneFinder(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public ShippingLane getShippingLaneForMission(Squadron squadron) throws PWCGException
    {
        ShippingLane shippingLane = null;
        List<FrontMapIdentifier> squadronMaps = AirfieldManager.getMapIdForAirfield(squadron.determineCurrentAirfieldName(campaign.getDate()));
        if (squadronMaps.contains(FrontMapIdentifier.KUBAN_MAP))
        {
            PWCGMap map = PWCGContext.getInstance().getMapByMapId(FrontMapIdentifier.KUBAN_MAP);
            ShippingLaneManager shippingLaneManager = map.getShippingLaneManager();
            shippingLane = shippingLaneManager.getClosestShippingLaneBySide(squadron.determineCurrentPosition(campaign.getDate()), squadron.determineEnemySide());
        }
        
        return shippingLane;
    }
}
