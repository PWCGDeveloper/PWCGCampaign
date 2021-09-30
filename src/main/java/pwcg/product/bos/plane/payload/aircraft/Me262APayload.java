package pwcg.product.bos.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;

public class Me262APayload extends PlanePayload implements IPlanePayload
{
    private Date r4mIntroDate;
    private Date gyroGunsightIntroDate;
    private Date autoValveIntroDate;

    public Me262APayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        noOrdnancePayloadElement = 0;
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            r4mIntroDate = DateUtils.getDateYYYYMMDD("19450318");
            gyroGunsightIntroDate = DateUtils.getDateYYYYMMDD("19450318");
            autoValveIntroDate = DateUtils.getDateYYYYMMDD("19441216");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
    {        
        setAvailablePayload(-4, "10000", PayloadElement.REMOVE_ARMOR);
        setAvailablePayload(-3, "10000", PayloadElement.EXTRA_ARMOR);

        setAvailablePayload(-5, "100000000", PayloadElement.AUTO_VALVE);
        setAvailablePayload(-2, "1000", PayloadElement.ARMORED_HEADREST);
        setAvailablePayload(-1, "10", PayloadElement.GYRO_GUNSIGHT);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1000001", PayloadElement.REMOVE_INNER_GUNS);
        setAvailablePayload(2, "101", PayloadElement.R4M_X26);
        setAvailablePayload(3, "10000001", PayloadElement.KG500x1);
    }

    @Override
    public IPlanePayload copy()
    {
        Me262APayload clone = new Me262APayload(planeType, date);
        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 0;
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

    protected void selectGroundAttackPayload(IFlight flight)
    {
        selectedPrimaryPayloadId = 3;
    }

    protected void selectInterceptPayload()
    {
        if (date.before(r4mIntroDate))
        {
            selectedPrimaryPayloadId = 0;
        }
        else
        {
            selectedPrimaryPayloadId = 2;
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
            selectedPrimaryPayloadId == 1 || 
            selectedPrimaryPayloadId == 2)
        {
            return false;
        }

        return true;
    }

    @Override
    protected void loadStockModifications()
    {
        stockModifications.add(PayloadElement.ARMORED_HEADREST);        
        if (date.after(gyroGunsightIntroDate))
        {
            stockModifications.add(PayloadElement.GYRO_GUNSIGHT);
        }
        
        if (date.after(autoValveIntroDate))
        {
            stockModifications.add(PayloadElement.AUTO_VALVE);
        }
    }
}
