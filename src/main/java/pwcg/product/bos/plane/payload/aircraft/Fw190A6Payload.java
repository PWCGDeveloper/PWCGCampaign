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

public class Fw190A6Payload extends PlanePayload implements IPlanePayload
{
    private Date g3IntroDate;

    public Fw190A6Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            g3IntroDate = DateUtils.getDateYYYYMMDD("19441002");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        Fw190A6Payload clone = new Fw190A6Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        PwcgRoleCategory squadronPrimaryRole = flight.getSquadron().determineSquadronPrimaryRoleCategory(flight.getCampaign().getDate());
        if (squadronPrimaryRole == PwcgRoleCategory.ATTACK)
        {
            selectedPayloadId = createFW190G3Payload(flight);
        }
        else
        {
            selectedPayloadId = createFW190A6Payload(flight);
        }

        return selectedPayloadId;
    }    

    private int createFW190A6Payload(IFlight flight) throws PWCGException
    {
        return Fw190A6PayloadHelper.createFW190A6Payload(flight);
    }

    private int createFW190G3Payload(IFlight flight) throws PWCGException
    {
        if (getDate().before(g3IntroDate))
        {
            return createFW190A6Payload(flight);
        }
        else
        {
            return Fw190G3PayloadHelper.selectFW190G3Payload(flight);
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
            selectedPayloadId == 8 ||
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
        List<Integer>alwaysAvailablePayloads = Arrays.asList(0, 8);
        availablePayloads.addAll(alwaysAvailablePayloads);
        
        Company squadron = flight.getSquadron();
        PwcgRoleCategory squadronPrimaryRoleCategory = squadron.determineSquadronPrimaryRoleCategory(flight.getCampaign().getDate());
        
        if ((squadronPrimaryRoleCategory == PwcgRoleCategory.FIGHTER))
        {
            List<Integer>availableFighterPayloads = Arrays.asList(1, 2, 3, 4, 16);
            availablePayloads.addAll(availableFighterPayloads);
        }
        
        if ((squadronPrimaryRoleCategory == PwcgRoleCategory.ATTACK))
        {
            if (getDate().after(g3IntroDate))
            {
                List<Integer>availableG3Payloads = Arrays.asList(48, 49, 50,  51,52, 53, 54);
                availablePayloads.addAll(availableG3Payloads);
            }
            else
            {
                List<Integer>availablePreG3Payloads = Arrays.asList(9, 10, 11);
                availablePayloads.addAll(availablePreG3Payloads);
            }
        }
        
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
