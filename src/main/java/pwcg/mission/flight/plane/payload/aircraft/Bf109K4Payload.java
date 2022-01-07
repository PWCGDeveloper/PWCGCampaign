package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Bf109K4Payload extends Bf109Payload implements IPlanePayload
{
    private Date db605cIntroDate;

    public Bf109K4Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            db605cIntroDate = DateUtils.getDateYYYYMMDD("19441216");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
	{        
        setAvailablePayload(-1, "10000", PlanePayloadElement.DB605DC_ENGINE);        
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "101", PlanePayloadElement.SC250_X1);
        setAvailablePayload(2, "1001", PlanePayloadElement.SC500_X1);
        setAvailablePayload(3, "11", PlanePayloadElement.MG151_20_GUNPOD);
	}

    @Override
    public IPlanePayload copy()
    {
        Bf109K4Payload clone = new Bf109K4Payload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight)
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.LOW_ALT_CAP)
        {
            selectedPayloadId = selectInterceptPayload();
        }

        return selectedPayloadId;
    }    

    @Override
    protected int selectGroundAttackPayload(IFlight flight)
    {
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 2;
        }
        return selectedPayloadId;
   }
    
    private int selectInterceptPayload()
    {
        int selectedPayloadId = 1;
        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 50)
        {
            selectedPayloadId = 3;
        }
        else
        {
            selectedPayloadId = 0;
        }
        return selectedPayloadId;
    }    

    @Override
    protected void loadAvailableStockModifications()
    {
        if (getDate().after(db605cIntroDate))
        {
            registerStockModification(PlanePayloadElement.DB605DC_ENGINE);
        }
    }
}
