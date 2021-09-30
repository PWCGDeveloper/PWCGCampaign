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

public class Hs129B2Payload extends PlanePayload
{
    public Hs129B2Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 34;
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "100000", PayloadElement.PEILG6);
        setAvailablePayload(-1, "1000000", PayloadElement.MIRROR);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.SC50_X4);
		setAvailablePayload(2, "1", PayloadElement.SC50_X6);
		setAvailablePayload(3, "1", PayloadElement.SC250_X1);
        setAvailablePayload(4, "1", PayloadElement.SC250_X1, PayloadElement.SC50_X2);
        setAvailablePayload(5, "101", PayloadElement.MG17_GUNPOD);
        setAvailablePayload(6, "101", PayloadElement.MG17_GUNPOD, PayloadElement.SC50_X2);
		setAvailablePayload(7, "1001", PayloadElement.MK101_30_AP_GUNPOD);
		setAvailablePayload(8, "1001", PayloadElement.MK101_30_HE_GUNPOD);
		setAvailablePayload(9, "1001", PayloadElement.MK101_30_AP_GUNPOD, PayloadElement.SC50_X2);
		setAvailablePayload(10, "1001", PayloadElement.MK101_30_HE_GUNPOD, PayloadElement.SC50_X2);
        setAvailablePayload(11, "10001", PayloadElement.MK103_30_AP_GUNPOD);
        setAvailablePayload(12, "10001", PayloadElement.MK103_30_AP_GUNPOD, PayloadElement.SC50_X2);
        setAvailablePayload(13, "10001", PayloadElement.MK103_30_HE_GUNPOD);
        setAvailablePayload(14, "10001", PayloadElement.MK103_30_HE_GUNPOD, PayloadElement.SC50_X2);
        setAvailablePayload(15, "10001", PayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(17, "11", PayloadElement.MG151_20_UPGRADE);
        setAvailablePayload(18, "11", PayloadElement.MG151_20_UPGRADE, PayloadElement.SC50_X4);
        setAvailablePayload(19, "11", PayloadElement.MG151_20_UPGRADE, PayloadElement.SC50_X6);
        setAvailablePayload(20, "11", PayloadElement.MG151_20_UPGRADE, PayloadElement.SC250_X1);
        setAvailablePayload(21, "11", PayloadElement.MG151_20_UPGRADE, PayloadElement.SC250_X1, PayloadElement.SC50_X2);
        setAvailablePayload(34, "1", PayloadElement.EMPTY);
	}

    @Override
    public IPlanePayload copy()
    {
        Hs129B2Payload clone = new Hs129B2Payload(planeType, date);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectGroundAttackPayload(flight);
        }

        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(IFlight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            if (date.before(DateUtils.getDateYYYYMMDD("19430702")))
            {
                selectedPrimaryPayloadId = 1;
            }
            else
            {
                selectedPrimaryPayloadId = 6;
            }
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            if (date.before(DateUtils.getDateYYYYMMDD("19430702")))
            {
                selectedPrimaryPayloadId = 3;
            }
            else
            {
                selectedPrimaryPayloadId = 7;
            }
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPrimaryPayloadId = 3;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPrimaryPayloadId = 3;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPrimaryPayloadId = 3;
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
            selectedPrimaryPayloadId == 5 ||
            selectedPrimaryPayloadId == 6 ||
            selectedPrimaryPayloadId == 7 ||
            selectedPrimaryPayloadId == 8 ||
            selectedPrimaryPayloadId == 11 ||
            selectedPrimaryPayloadId == 13 ||
            selectedPrimaryPayloadId == 15 ||
            selectedPrimaryPayloadId == 17)
        {
            return false;
        }

        return true;
    }
}
