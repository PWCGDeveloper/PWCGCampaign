package pwcg.mission;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.shipping.ShippingLane;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public class MissionCenterBuilderAntiShipping  implements IMissionCenterBuilder
{
    private FrontMapIdentifier mapidentifier;
    private Coordinate missionCenter;
    private Side side;
    
    MissionCenterBuilderAntiShipping (FrontMapIdentifier mapidentifier, Coordinate missionCenter, Side side)
    {
        this.mapidentifier = mapidentifier;
        this.missionCenter = missionCenter;
        this.side = side;
    }
    
    @Override
    public Coordinate findMissionCenter(int missionBoxRadius) throws PWCGException
    {
        ShippingLane shippingLane = MissionAntiShippingSeaLaneFinder.getShippingLaneForMission(mapidentifier, missionCenter, side);
        Coordinate missionCenter = shippingLane.getShippingLaneBorders().getCoordinateInBox();
        return missionCenter;
    }

}
