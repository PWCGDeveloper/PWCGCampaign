package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PwcgRoleCategory;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Fw190A5Payload extends PlanePayload implements IPlanePayload
{
    public Fw190A5Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    protected void initialize()
	{
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "11", PayloadElement.SC50_X4);
        setAvailablePayload(2, "101", PayloadElement.SC250_X1);
        setAvailablePayload(3, "1001", PayloadElement.SC500_X1);
        setAvailablePayload(4, "10001", PayloadElement.MGFF_WING_GUNS);
        setAvailablePayload(5, "100001", PayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(6, "1000001", PayloadElement.U_17);
        setAvailablePayload(7, "1000011", PayloadElement.SC50_X8);
        setAvailablePayload(8, "1000101", PayloadElement.SC250_X1, PayloadElement.SC50_X4);
        setAvailablePayload(9, "1000101", PayloadElement.SC500_X1, PayloadElement.SC50_X4);
	}

    @Override
    public IPlanePayload copy()
    {
        Fw190A5Payload clone = new Fw190A5Payload(planeType, date);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        selectDefaultPayload();
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.INTERCEPT)
        {
            selectInterceptPayload();
        }
        return selectedPrimaryPayloadId;
    }    

	private void selectGroundAttackPayload(IFlight flight) throws PWCGException
    {
        PwcgRoleCategory squadronPrimaryRole = flight.getSquadron().determineSquadronPrimaryRoleCategory(flight.getCampaign().getDate());
        if (squadronPrimaryRole == PwcgRoleCategory.ATTACK)
        {
            setU17Payload(flight);
        }
        else
        {
            setGenericBombLoad(flight);
        }
    }

    private void setU17Payload(IFlight flight) throws PWCGException
    {
        if (date.before(DateUtils.getDateYYYYMMDD("19430506")))
        {
            setGenericBombLoad(flight);
        }
        else
        {
            selectedPrimaryPayloadId = 6;
        }
    }

    private void setGenericBombLoad(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPrimaryPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPrimaryPayloadId = 2;
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
            selectedPrimaryPayloadId = 3;
        }
    }

    private void selectInterceptPayload() throws PWCGException
    {
        if (date.before(DateUtils.getDateYYYYMMDD("19430506")))
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPrimaryPayloadId = 5;
            }
            else
            {
                selectedPrimaryPayloadId = 4;
            }
        }
    }    

    private void selectDefaultPayload() throws PWCGException
    {
        if (date.before(DateUtils.getDateYYYYMMDD("19430506")))
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            int diceRoll = RandomNumberGenerator.getRandom(100);
            if (diceRoll < 40)
            {
                selectedPrimaryPayloadId = 4;
            }
            else
            {
                selectedPrimaryPayloadId = 0;
            }
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
            selectedPrimaryPayloadId == 5)
        {
            return false;
        }

        return true;
    }
}
