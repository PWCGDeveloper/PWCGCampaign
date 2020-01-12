package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Ma202Ser8Payload extends PlanePayload implements IPlanePayload
{
    public Ma202Ser8Payload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
		setAvailablePayload(-1, "10", PayloadElement.ARMORED_WINDSCREEN);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "101", PayloadElement.T50_X2);
		setAvailablePayload(2, "1001", PayloadElement.T100_X2);
		setAvailablePayload(3, "10001", PayloadElement.BREDA_GUNPOD);
		setAvailablePayload(4, "100001", PayloadElement.MG151_20_GUNPOD);
	}

    @Override
    public IPlanePayload copy()
    {
        Ma202Ser8Payload clone = new Ma202Ser8Payload(planeType);
        
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getFlightData().getFlightInformation().getFlightType() == FlightTypes.GROUND_ATTACK)
        {
            selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightData().getFlightInformation().getFlightType() == FlightTypes.INTERCEPT)
        {
            selectInterceptPayload();
        }
        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
        if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectSoftTargetPayload();
        }
        else if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectArmoredTargetPayload();
        }
        else if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectMediumTargetPayload();
        }
        else if (flight.getFlightData().getFlightInformation().getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectHeavyTargetPayload();
        }
    }

    protected void selectInterceptPayload()
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 60)
        {
            selectedPrimaryPayloadId = 0;
        }
        else if (diceRoll < 80)
        {
            selectedPrimaryPayloadId = 3;
        }
        else
        {
            selectedPrimaryPayloadId = 4;
        }
    }    

    protected void selectSoftTargetPayload()
    {
        selectedPrimaryPayloadId = 1;
    }    

    protected void selectArmoredTargetPayload()
    {
        selectedPrimaryPayloadId = 2;
    }

    protected void selectMediumTargetPayload()
    {
        selectedPrimaryPayloadId = 2;
    }

    protected void selectHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 2;
    }
}
