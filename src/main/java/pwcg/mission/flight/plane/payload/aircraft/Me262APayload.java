package pwcg.mission.flight.plane.payload.aircraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadDesignation;
import pwcg.campaign.plane.payload.PlanePayloadElement;
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
        setNoOrdnancePayloadId(0);
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
        setAvailablePayload(-4, "10000", PlanePayloadElement.REMOVE_ARMOR);
        setAvailablePayload(-3, "10000", PlanePayloadElement.EXTRA_ARMOR);

        setAvailablePayload(-5, "100000000", PlanePayloadElement.AUTO_VALVE);
        setAvailablePayload(-2, "1000", PlanePayloadElement.ARMORED_HEADREST);
        setAvailablePayload(-1, "10", PlanePayloadElement.GYRO_GUNSIGHT);
        
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "1000001", PlanePayloadElement.REMOVE_INNER_GUNS);
        setAvailablePayload(2, "101", PlanePayloadElement.R4M_X26);
        setAvailablePayload(3, "10000001", PlanePayloadElement.KG500x1);
    }

    @Override
    public IPlanePayload copy()
    {
        Me262APayload clone = new Me262APayload(getPlaneType(), getDate());
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

    protected int selectGroundAttackPayload(IFlight flight)
    {
        return 3;
    }

    private int selectInterceptPayload()
    {
        int selectedPayloadId = 0;
        if (getDate().before(r4mIntroDate))
        {
            selectedPayloadId = 0;
        }
        else
        {
            selectedPayloadId = 2;
        }
        return selectedPayloadId;
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
            selectedPayloadId == 1 || 
            selectedPayloadId == 2)
        {
            return false;
        }

        return true;
    }

    @Override
    protected void loadAvailableStockModifications()
    {
        registerStockModification(PlanePayloadElement.ARMORED_HEADREST);        
        if (getDate().after(gyroGunsightIntroDate))
        {
            registerStockModification(PlanePayloadElement.GYRO_GUNSIGHT);
        }
        
        if (getDate().after(autoValveIntroDate))
        {
            registerStockModification(PlanePayloadElement.AUTO_VALVE);
        }
    }
    
    @Override
    protected List<PlanePayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight)
    {
        List<Integer>availablePayloads = new ArrayList<>();
        List<Integer>alwaysAvailablePayloads = Arrays.asList(0, 1, 3);
        availablePayloads.addAll(alwaysAvailablePayloads);
        if (getDate().after(r4mIntroDate))
        {
            availablePayloads.add(2);
        }
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
