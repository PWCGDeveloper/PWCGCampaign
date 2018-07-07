package pwcg.campaign.plane;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class PlaneEquipmentFactory
{
    private Campaign campaign;

    public PlaneEquipmentFactory(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public EquippedPlane makePlaneForSquadron (String planteTypeName, int squadronId) throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
        PlaneType planeType = planeTypeFactory.createPlaneTypeByType(planteTypeName);        
        EquippedPlane equippedPlane = new EquippedPlane(planeType, campaign.getSerialNumber().getNextPlaneSerialNumber(), squadronId, PlaneStatus.STATUS_DEPLOYED);

        return equippedPlane;
    }

    public EquippedPlane makePlaneForDepo (String planteTypeName) throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
        PlaneType planeType = planeTypeFactory.createPlaneTypeByType(planteTypeName);        
        EquippedPlane equippedPlane = new EquippedPlane(planeType, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, PlaneStatus.STATUS_DEPO);

        return equippedPlane;
    }

    public EquippedPlane makePlaneForBeforeCampaign (Side side, Date date) throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
        List<PlaneType> planeTypes = planeTypeFactory.createActivePlaneTypesForDateAndSide(side, date);
        int index = RandomNumberGenerator.getRandom(planeTypes.size());
        PlaneType planeType = planeTypes.get(index);
        
        EquippedPlane equippedPlane = new EquippedPlane(planeType, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, PlaneStatus.STATUS_DEPO);
        return equippedPlane;
    }

}
