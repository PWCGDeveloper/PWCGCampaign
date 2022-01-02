package pwcg.product.bos.plane.payload.aircraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadDesignation;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.IFlight;

public class Fw190A8Payload extends PlanePayload implements IPlanePayload
{
    private Date f8IntroDate;

    public Fw190A8Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
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
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            f8IntroDate = DateUtils.getDateYYYYMMDD("19440101");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    public IPlanePayload copy()
    {
        Fw190A8Payload clone = new Fw190A8Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        PwcgRoleCategory squadronPrimaryRole = flight.getSquadron().determineSquadronPrimaryRoleCategory(flight.getCampaign().getDate());
        if (squadronPrimaryRole == PwcgRoleCategory.ATTACK)
        {
            selectedPayloadId = createFW190F8Payload(flight);
        }
        else
        {
            selectedPayloadId = Fw190A8PayloadHelper.selectFW190A8Payload(flight);;
        }

        return selectedPayloadId;
    }    

    private int createFW190F8Payload(IFlight flight) throws PWCGException
    {
        if (getDate().before(f8IntroDate))
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
        
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId == 0 || 
            selectedPayloadId == 4 ||
            selectedPayloadId == 16)
        {
            return false;
        }

        return true;
    }
    
    @Override
    protected List<PayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight) throws PWCGException
    {
        List<Integer>availablePayloads = new ArrayList<>();
        availablePayloads.add(0);
         
        Company squadron = flight.getSquadron();
        PwcgRoleCategory squadronPrimaryRoleCategory = squadron.determineSquadronPrimaryRoleCategory(flight.getCampaign().getDate());
        
        if ((squadronPrimaryRoleCategory == PwcgRoleCategory.FIGHTER))
        {
            List<Integer>availableFighterPayloads = Arrays.asList(1, 2, 3, 4, 16);
            availablePayloads.addAll(availableFighterPayloads);
        }
        
        if ((squadronPrimaryRoleCategory == PwcgRoleCategory.ATTACK))
        {
            if (getDate().after(f8IntroDate))
            {
                List<Integer>availableG3Payloads = Arrays.asList(32, 34,36, 37);
                availablePayloads.addAll(availableG3Payloads);
            }
            else
            {
                List<Integer>availablePreF8Payloads = Arrays.asList(0, 1, 2, 3, 4, 16);
                availablePayloads.addAll(availablePreF8Payloads);
            }
        }
        
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
