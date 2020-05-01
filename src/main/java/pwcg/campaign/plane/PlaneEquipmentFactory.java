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
    private Campaign campaign;

    public PlaneEquipmentFactory(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public EquippedPlane makePlaneForSquadron (String planeTypeName, int squadronId) throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        PlaneType planeType = planeTypeFactory.createPlaneTypeByType(planeTypeName);
        EquippedPlane equippedPlane = new EquippedPlane(planeType, campaign.getSerialNumber().getNextPlaneSerialNumber(), squadronId, PlaneStatus.STATUS_DEPLOYED);

        return equippedPlane;
    }

    public EquippedPlane makePlaneForDepot (String planeTypeName, int service) throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        PlaneType planeType = planeTypeFactory.createPlaneTypeByType(planeTypeName);
        EquippedPlane equippedPlane = new EquippedPlane(planeType, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, PlaneStatus.STATUS_DEPOT);
        PWCGContext.getInstance().getPlaneMarkingManager().generatePlaneSerial(campaign.getDate(), equippedPlane, service);

        return equippedPlane;
    }

    public EquippedPlane makePlaneForBeforeCampaign (Side side, Date date) throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
        List<PlaneType> planeTypes = planeTypeFactory.createActivePlaneTypesForDateAndSide(side, date);
        int index = RandomNumberGenerator.getRandom(planeTypes.size());
        PlaneType planeType = planeTypes.get(index);
        
        EquippedPlane equippedPlane = new EquippedPlane(planeType, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, PlaneStatus.STATUS_DEPOT);
        return equippedPlane;
    }

}
