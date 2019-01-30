package pwcg.campaign.ww2.plane.payload.aircraft;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;

public class U2VSPayload extends PlanePayload implements IPlanePayload
{
    public U2VSPayload(PlaneType planeType)
    {
        super(planeType);
    }

    protected void initialize()
	{
        setAvailablePayload(-1, "11", PayloadElement.U2_GUNNER);
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1001", PayloadElement.FAB_U2VS);
	}

    @Override
    public IPlanePayload copy()
    {
        U2VSPayload clone = new U2VSPayload(planeType);
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(Flight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = 1;
        u2Turret();
        return selectedPrimaryPayloadId;
    }    

    protected void selectGroundAttackPayload(Flight flight)
    {
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 30)
        {
            
        }
    }

    private void u2Turret() throws PWCGException
    {
        int roll = RandomNumberGenerator.getRandom(100);
        int gunnerOdds = 100;
        if (roll < gunnerOdds)
        {
            this.addModification(PayloadElement.U2_GUNNER);
        }
    }
}
