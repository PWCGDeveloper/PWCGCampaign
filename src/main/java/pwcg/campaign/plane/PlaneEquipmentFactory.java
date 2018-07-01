package pwcg.campaign.plane;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.core.exception.PWCGException;

public class PlaneEquipmentFactory
{
    private Campaign campaign;

    public PlaneEquipmentFactory(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public EquippedPlane getPlaneByPlaneType (String planteTypeName) throws PWCGException
    {
        PlaneTypeFactory planeTypeFactory = PWCGContextManager.getInstance().getPlaneTypeFactory();
        PlaneType planeType = planeTypeFactory.createPlaneTypeByType(planteTypeName);        
        EquippedPlane equippedPlane = new EquippedPlane(planeType, campaign.getSerialNumber().getNextPlaneSerialNumber());

        return equippedPlane;
    }

}
