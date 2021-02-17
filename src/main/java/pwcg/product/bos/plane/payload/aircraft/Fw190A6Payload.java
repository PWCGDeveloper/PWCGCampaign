package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class Fw190A6Payload extends PlanePayload implements IPlanePayload
{
    public Fw190A6Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "10000000", PayloadElement.SET_MG17);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "10001", PayloadElement.SC70_X4);
        setAvailablePayload(2, "10001", PayloadElement.SC250_X1);
        setAvailablePayload(3, "10001", PayloadElement.SC500_X1);
        setAvailablePayload(4, "100001", PayloadElement.BR21_X2);
        setAvailablePayload(8, "1000001", PayloadElement.FW190A6_REM_GUNS);
        setAvailablePayload(9, "1000001", PayloadElement.SC70_X4, PayloadElement.FW190A6_REM_GUNS);
        setAvailablePayload(10, "1000001", PayloadElement.SC250_X1, PayloadElement.FW190A6_REM_GUNS);
        setAvailablePayload(11, "1000001", PayloadElement.SC500_X1, PayloadElement.FW190A6_REM_GUNS);
        setAvailablePayload(16, "11", PayloadElement.FW190A6_STURMJAGER);

        setAvailablePayload(48, "101", PayloadElement.FW190G3);
        setAvailablePayload(49, "10101", PayloadElement.FW190G3, PayloadElement.SC70_X4);
        setAvailablePayload(50, "10101", PayloadElement.FW190G3, PayloadElement.SC250_X1);
        setAvailablePayload(51, "10101", PayloadElement.FW190G3, PayloadElement.SC250_X2);
        setAvailablePayload(52, "10101", PayloadElement.FW190G3, PayloadElement.SC250_X3);
        setAvailablePayload(53, "10101", PayloadElement.FW190G3, PayloadElement.SC250_X2, PayloadElement.SC70_X4);
        setAvailablePayload(54, "10101", PayloadElement.FW190G3, PayloadElement.SC500_X1);
	}

    @Override
    public IPlanePayload copy()
    {
        Fw190A6Payload clone = new Fw190A6Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        Role squadronPrimaryRole = flight.getSquadron().determineSquadronPrimaryRole(flight.getCampaign().getDate());
        if (squadronPrimaryRole.isRoleCategory(RoleCategory.ATTACK))
        {
            return createFW190G3Payload(flight);
        }
        else
        {
            return createFW190A6Payload(flight);
        }
    }    

    private int createFW190A6Payload(IFlight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = Fw190A6PayloadHelper.createFW190A6Payload(flight);
        return selectedPrimaryPayloadId;
    }

    private int createFW190G3Payload(IFlight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = Fw190G3PayloadHelper.selectFW190G3Payload(flight);
        return selectedPrimaryPayloadId;
    }
}
