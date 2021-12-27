package pwcg.product.fc.plane.payload;

import java.util.Date;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.target.TargetCategory;

public class dfwc5Payload extends PlanePayload implements IPlanePayload
{
    public dfwc5Payload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(0);
    }

    protected void initialize()
    {
        setAvailablePayload(-8, "1000000000", PayloadElement.RADIO);
        setAvailablePayload(-7, "100000000", PayloadElement.CAMERA);
        setAvailablePayload(-6, "1000000", PayloadElement.COCKPIT_LIGHT);        
        setAvailablePayload(-5, "100000", PayloadElement.GUAGE_SET);
        setAvailablePayload(-4, "10000", PayloadElement.IRON_SIGHT);
        setAvailablePayload(-3, "1000", PayloadElement.ALDIS_SIGHT);
        setAvailablePayload(-2, "100", PayloadElement.BECKER_GUN_TURRET);
        setAvailablePayload(-1, "10", PayloadElement.TWIN_GUN_TURRET);
        
        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "10000001", PayloadElement.KG12_X4);
        setAvailablePayload(2, "10000001", PayloadElement.KG12_X12);
        setAvailablePayload(3, "10000001", PayloadElement.KG12_X16);
        setAvailablePayload(4, "10000001", PayloadElement.KG50x1);
        setAvailablePayload(5, "10000001", PayloadElement.KG50x3);
        setAvailablePayload(6, "10000001", PayloadElement.KG50x1, PayloadElement.KG12_X4);
    }

    @Override
    public IPlanePayload copy()
    {
        dfwc5Payload clone = new dfwc5Payload(getPlaneType(), getDate());
        return super.copy(clone);
    }

    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (FlightTypes.isBombingFlight(flight.getFlightType()))
        {
            selectedPayloadId = selectBombPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.RECON)
        {
            selectedPayloadId = selectReconPayload(flight);
        }
        else if (flight.getFlightType() == FlightTypes.ARTILLERY_SPOT)
        {
            selectedPayloadId = selectArtillerySpotPayload(flight);
        }
        return selectedPayloadId;
    }

    private int selectReconPayload(IFlight flight)
    {
        return 0;
    }

    private int selectArtillerySpotPayload(IFlight flight)
    {
        return 0;
    }    

    private int selectBombPayload(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 1;
        if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_SOFT)
        {
            selectedPayloadId = 1;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_ARMORED)
        {
            selectedPayloadId = 4;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_MEDIUM)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_HEAVY)
        {
            selectedPayloadId = 2;
        }
        else if (flight.getTargetDefinition().getTargetCategory() == TargetCategory.TARGET_CATEGORY_STRUCTURE)
        {
            selectedPayloadId = 4;
        }
        return selectedPayloadId;
    }

    @Override
    public boolean isOrdnance()
    {
        int selectedPayloadId = this.getSelectedPayload();
        if (selectedPayloadId > 0)
        {
            return true;
        }

        return false;
    }
}
