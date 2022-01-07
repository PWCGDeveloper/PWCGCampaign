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
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class Hs129B2Payload extends PlanePayload
{
    private Date mg17GunPodIntroDate;
    private Date mk101GunPodIntroDate;

    public Hs129B2Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(34);
    }

    protected void initialize()
	{
        setAvailablePayload(-2, "100000", PlanePayloadElement.PEILG6);
        setAvailablePayload(-1, "1000000", PlanePayloadElement.MIRROR);
        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "1", PlanePayloadElement.SC50_X4);
		setAvailablePayload(2, "1", PlanePayloadElement.SC50_X6);
		setAvailablePayload(3, "1", PlanePayloadElement.SC250_X1);
        setAvailablePayload(4, "1", PlanePayloadElement.SC250_X1, PlanePayloadElement.SC50_X2);
        setAvailablePayload(5, "101", PlanePayloadElement.MG17_GUNPOD);
        setAvailablePayload(6, "101", PlanePayloadElement.MG17_GUNPOD, PlanePayloadElement.SC50_X2);
		setAvailablePayload(7, "1001", PlanePayloadElement.MK101_30_AP_GUNPOD);
		setAvailablePayload(8, "1001", PlanePayloadElement.MK101_30_HE_GUNPOD);
		setAvailablePayload(9, "1001", PlanePayloadElement.MK101_30_AP_GUNPOD, PlanePayloadElement.SC50_X2);
		setAvailablePayload(10, "1001", PlanePayloadElement.MK101_30_HE_GUNPOD, PlanePayloadElement.SC50_X2);
        setAvailablePayload(11, "10001", PlanePayloadElement.MK103_30_AP_GUNPOD);
        setAvailablePayload(12, "10001", PlanePayloadElement.MK103_30_AP_GUNPOD, PlanePayloadElement.SC50_X2);
        setAvailablePayload(13, "10001", PlanePayloadElement.MK103_30_HE_GUNPOD);
        setAvailablePayload(14, "10001", PlanePayloadElement.MK103_30_HE_GUNPOD, PlanePayloadElement.SC50_X2);
        setAvailablePayload(15, "10001", PlanePayloadElement.MG151_20_GUNPOD);
        setAvailablePayload(17, "11", PlanePayloadElement.MG151_20_UPGRADE);
        setAvailablePayload(18, "11", PlanePayloadElement.MG151_20_UPGRADE, PlanePayloadElement.SC50_X4);
        setAvailablePayload(19, "11", PlanePayloadElement.MG151_20_UPGRADE, PlanePayloadElement.SC50_X6);
        setAvailablePayload(20, "11", PlanePayloadElement.MG151_20_UPGRADE, PlanePayloadElement.SC250_X1);
        setAvailablePayload(21, "11", PlanePayloadElement.MG151_20_UPGRADE, PlanePayloadElement.SC250_X1, PlanePayloadElement.SC50_X2);
        setAvailablePayload(34, "1", PlanePayloadElement.EMPTY);
	}

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            mg17GunPodIntroDate = DateUtils.getDateYYYYMMDD("19430702");
            mk101GunPodIntroDate = DateUtils.getDateYYYYMMDD("19430702");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    public IPlanePayload copy()
    {
        Hs129B2Payload clone = new Hs129B2Payload(getPlaneType(), getDate());
        
        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isGroundAttackFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectGroundAttackPayload(flight);
        }


        return selectedPayloadId;
    }    

    private int selectGroundAttackPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            if (getDate().before(mg17GunPodIntroDate))
            {
                selectedPayloadId = 1;
            }
            else
            {
                selectedPayloadId = 6;
            }
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            if (getDate().before(mk101GunPodIntroDate))
            {
                selectedPayloadId = 3;
            }
            else
            {
                selectedPayloadId = 7;
            }
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 3;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 3;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 3;
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
            selectedPayloadId == 5 ||
            selectedPayloadId == 6 ||
            selectedPayloadId == 7 ||
            selectedPayloadId == 8 ||
            selectedPayloadId == 11 ||
            selectedPayloadId == 13 ||
            selectedPayloadId == 15 ||
            selectedPayloadId == 17)
        {
            return false;
        }

        return true;
    }
    
    @Override
    protected List<PlanePayloadDesignation> getAvailablePayloadDesignationsForPlane(IFlight flight)
    {
        List<Integer>availablePayloads = new ArrayList<>();
        List<Integer>alwaysAvailablePayloads = Arrays.asList(0, 1, 2, 3, 4, 11, 12, 13, 14, 15, 17, 18, 19, 20, 21);
        availablePayloads.addAll(alwaysAvailablePayloads);
        
        if (getDate().after(mg17GunPodIntroDate))
        {
            List<Integer>availableShvakPayloads = Arrays.asList(5, 6);
            availablePayloads.addAll(availableShvakPayloads);
        }

        if (getDate().after(mk101GunPodIntroDate))
        {
            List<Integer>availableShvakPayloads = Arrays.asList(7,8, 9, 10);
            availablePayloads.addAll(availableShvakPayloads);
        }
        
        return getAvailablePayloadDesignations(availablePayloads);
    }
}
