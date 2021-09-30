package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Ju87D3Payload extends PlanePayload
{    
    private Date bk37IntroDate;

    public Ju87D3Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            bk37IntroDate = DateUtils.getDateYYYYMMDD("19430302");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
	{
        setAvailablePayload(-2, "1000", PayloadElement.EXTRA_ARMOR);
        setAvailablePayload(-1, "10", PayloadElement.SIREN);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
		setAvailablePayload(1, "1", PayloadElement.SC250_X1, PayloadElement.SD70_X4);
        setAvailablePayload(2, "1", PayloadElement.SC500_X1);
        setAvailablePayload(3, "1", PayloadElement.SC500_X1, PayloadElement.SD70_X4);
        setAvailablePayload(4, "1", PayloadElement.SC500_X1, PayloadElement.SC250_X2);
		setAvailablePayload(5, "1", PayloadElement.SC250_X3);
		setAvailablePayload(6, "1", PayloadElement.SC1000_X1);
		setAvailablePayload(7, "101", PayloadElement.SC1800_X1);
		setAvailablePayload(9, "100001", PayloadElement.BK37_AP_GUNPOD);
		setAvailablePayload(10, "100001", PayloadElement.BK37_HE_GUNPOD);
        setAvailablePayload(11, "1", PayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        Ju87D3Payload clone = new Ju87D3Payload(planeType, date);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightType() == FlightTypes.DIVE_BOMB)
        {
            selectDiveBombPayload(flight);
        }
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectArmorAttackPayload();
        }
        return selectedPrimaryPayloadId;
    }    

    private void selectDiveBombPayload(IFlight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectArmorAttackPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPrimaryPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPrimaryPayloadId = 6;
        }
    }

    private void selectArmorAttackPayload() throws PWCGException
    {
        if (date.before(bk37IntroDate))
        {
            selectedPrimaryPayloadId = 2;
        }
        else
        {
            selectedPrimaryPayloadId = 9;
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
            selectedPrimaryPayloadId == 9 || 
            selectedPrimaryPayloadId == 10)
        {
            return false;
        }

        return true;
    }
}
