package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.IFlight;

public class Fw190A8Payload extends PlanePayload implements IPlanePayload
{
    public Fw190A8Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{        
        setAvailablePayload(-2, "1000000", PayloadElement.FW190A8_REM_GUNS);
        setAvailablePayload(-1, "10000", PayloadElement.EXTRA_ARMOR);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "101", PayloadElement.SD70_X4);
        setAvailablePayload(2, "101", PayloadElement.SC250_X1);
        setAvailablePayload(3, "101", PayloadElement.SC500_X1);
        setAvailablePayload(4, "1001", PayloadElement.BR21_X2);
        setAvailablePayload(16, "11", PayloadElement.MK108_30);
        
        setAvailablePayload(32, "100001", PayloadElement.FW190F8);
        setAvailablePayload(34, "100001", PayloadElement.FW190F8, PayloadElement.FW190F8_SC70_X4);
        setAvailablePayload(36, "100001", PayloadElement.FW190F8, PayloadElement.FW190F8_PB1_X12);
        setAvailablePayload(37, "100001", PayloadElement.FW190F8, PayloadElement.FW190F8_PB1M8_X12);        
	}
    
    @Override
    public IPlanePayload copy()
    {
        Fw190A8Payload clone = new Fw190A8Payload(planeType, date);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        PwcgRoleCategory squadronPrimaryRole = flight.getSquadron().determineSquadronPrimaryRoleCategory(flight.getCampaign().getDate());
        if (squadronPrimaryRole == PwcgRoleCategory.ATTACK)
        {
            selectedPrimaryPayloadId = createFW190F8Payload(flight);
        }
        else
        {
            selectedPrimaryPayloadId = Fw190A8PayloadHelper.selectFW190A8Payload(flight);;
        }
        return selectedPrimaryPayloadId;
    }    

    private int createFW190F8Payload(IFlight flight) throws PWCGException
    {
        if (date.before(DateUtils.getDateYYYYMMDD("19440101")))
        {
            return Fw190A8PayloadHelper.selectFW190A8Payload(flight);
        }
        else
        {
            return Fw190F8PayloadHelper.selectFW190F8Payload(flight);
        }
    }

    @Override
    public boolean isOrdnance()
    {
        if (isOrdnanceDroppedPayload())
        {
            return false;
        }
        
        if (selectedPrimaryPayloadId == 0 || 
            selectedPrimaryPayloadId == 4 ||
            selectedPrimaryPayloadId == 16)
        {
            return false;
        }

        return true;
    }
}
