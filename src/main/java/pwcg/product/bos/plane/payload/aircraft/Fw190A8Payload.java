package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.Role;
import pwcg.campaign.plane.RoleCategory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class Fw190A8Payload extends PlanePayload implements IPlanePayload
{
    public Fw190A8Payload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{        
        setAvailablePayload(-2, "1000000", PayloadElement.FW190_REM_GUNS);
        setAvailablePayload(-1, "10000", PayloadElement.EXTRA_ARMOR);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "101", PayloadElement.SD70_X4);
        setAvailablePayload(2, "101", PayloadElement.SC250_X1);
        setAvailablePayload(3, "101", PayloadElement.SC500_X1);
        setAvailablePayload(4, "1001", PayloadElement.BR21_X2);
        setAvailablePayload(16, "11", PayloadElement.MK108_30);
        
        setAvailablePayload(32, "100001", PayloadElement.FW190F8);
        setAvailablePayload(34, "100001", PayloadElement.FW190F8_SC70_X4);
        setAvailablePayload(36, "100001", PayloadElement.FW190F8_PB1_X12);
        setAvailablePayload(37, "100001", PayloadElement.FW190F8_PB1M8_X12);
	}
    
    @Override
    public IPlanePayload copy()
    {
        Fw190A8Payload clone = new Fw190A8Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        Role squadronPrimaryRole = flight.getSquadron().determineSquadronPrimaryRole(flight.getCampaign().getDate());
        if (squadronPrimaryRole.isRoleCategory(RoleCategory.ATTACK))
        {
            return createFW190F8Payload(flight);
        }
        else
        {
            return createFW190A8Payload(flight);
        }
    }    

    private int createFW190A8Payload(IFlight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = Fw190A8PayloadHelper.selectFW190A8Payload(flight);
        return selectedPrimaryPayloadId;
    }

    private int createFW190F8Payload(IFlight flight)
    {
        selectedPrimaryPayloadId = Fw190F8PayloadHelper.selectFW190F8Payload(flight);
        return selectedPrimaryPayloadId;
    }
}
