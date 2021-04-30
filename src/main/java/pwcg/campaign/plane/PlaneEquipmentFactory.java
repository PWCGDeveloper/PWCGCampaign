package pwcg.campaign.plane;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class PlaneEquipmentFactory
{
    public static EquippedPlane makePlaneForSquadron (Campaign campaign, String planteTypeName, int squadronId) throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        PlaneType planeType = planeTypeFactory.createPlaneTypeByType(planteTypeName);        
        EquippedPlane equippedPlane = new EquippedPlane(planeType, campaign.getSerialNumber().getNextPlaneSerialNumber(), squadronId, PlaneStatus.STATUS_DEPLOYED);

        return equippedPlane;
    }

    public static EquippedPlane makePlaneForDepot (Campaign campaign, String planteTypeName) throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        PlaneType planeType = planeTypeFactory.createPlaneTypeByType(planteTypeName);        
        EquippedPlane equippedPlane = new EquippedPlane(planeType, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, PlaneStatus.STATUS_DEPOT);

        return equippedPlane;
    }

    public static EquippedPlane makePlaneForBeforeCampaign (Campaign campaign, Side side, Date date) throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        List<PlaneType> planeTypes = planeTypeFactory.createActivePlaneTypesForDateAndSide(side, date);
        int index = RandomNumberGenerator.getRandom(planeTypes.size());
        PlaneType planeType = planeTypes.get(index);
        
        EquippedPlane equippedPlane = new EquippedPlane(planeType, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, PlaneStatus.STATUS_DEPOT);
        return equippedPlane;
    }

}
