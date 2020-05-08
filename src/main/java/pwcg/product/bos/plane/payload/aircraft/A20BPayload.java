package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class A20BPayload extends PlanePayload implements IPlanePayload
{
    public A20BPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "1000", PayloadElement.RPK10);
        setAvailablePayload(1, "1", PayloadElement.FAB100M_X8);
        setAvailablePayload(2, "1", PayloadElement.FAB100M_X16);
        setAvailablePayload(3, "11", PayloadElement.FAB100M_X20);
        setAvailablePayload(4, "101", PayloadElement.FAB250SV_X4);
	}

    @Override
    public IPlanePayload copy()
    {
        A20BPayload clone = new A20BPayload(planeType);
        
        return super.copy(clone);
    }
    
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
    	if (flight.getFlightType() == FlightTypes.GROUND_ATTACK)
    	{
    		selectGroundAttackPayload(flight);
    	}
    	else
    	{
    		selectBombingPayload(flight);
    	}
        return selectedPrimaryPayloadId;
    }

    private void selectBombingPayload(IFlight flight)
    {
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectBombingSoftTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectBombingArmoredTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectBombingMediumTargetPayload();
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectBombingHeavyTargetPayload();
        }
    }

    private void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 1;
    }

    private void selectBombingSoftTargetPayload()
    {
        selectedPrimaryPayloadId = 1;
    }    

    private void selectBombingArmoredTargetPayload()
    {
        selectedPrimaryPayloadId = 4;
    }

    private void selectBombingMediumTargetPayload()
    {
        selectedPrimaryPayloadId = 4;
    }

    private void selectBombingHeavyTargetPayload()
    {
        selectedPrimaryPayloadId = 4;
    }
}
