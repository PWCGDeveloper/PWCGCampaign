package pwcg.mission.flight.plane.payload.aircraft;

import java.util.Date;

import pwcg.campaign.context.Country;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.campaign.plane.payload.PlanePayloadElement;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.flight.IFlight;

public class HurricaneMkIIPayload extends PlanePayload implements IPlanePayload
{    
    private Date boostIntroDate;

    public HurricaneMkIIPayload(PlaneType planeType, Date date)
    {
        super(planeType, date);
        setNoOrdnancePayloadId(5);
    }

    @Override
    protected void createWeaponsModAvailabilityDates()
    {
        try
        {
            boostIntroDate = DateUtils.getDateYYYYMMDD("19430102");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }    

    @Override
    protected void initialize()
    {        
        setAvailablePayload(-3, "100000000", PlanePayloadElement.AIR_FILTER);
        setAvailablePayload(-2, "10000000", PlanePayloadElement.MIRROR);
        setAvailablePayload(-1, "1000000", PlanePayloadElement.LB_14_BOOST);

        setAvailablePayload(0, "1", PlanePayloadElement.STANDARD);
        setAvailablePayload(1, "1", PlanePayloadElement.EXTRA_AMMO);
        setAvailablePayload(2, "1", PlanePayloadElement.LB250x2);
        setAvailablePayload(3, "1", PlanePayloadElement.LB250x2, PlanePayloadElement.EXTRA_AMMO);
        setAvailablePayload(4, "1", PlanePayloadElement.LB500x2);
        setAvailablePayload(5, "1", PlanePayloadElement.LB500x2, PlanePayloadElement.EXTRA_AMMO);

        setAvailablePayload(5, "11", PlanePayloadElement.BROWNING_303_X4);
        setAvailablePayload(7, "11", PlanePayloadElement.BROWNING_303_X4, PlanePayloadElement.EXTRA_AMMO);
        setAvailablePayload(8, "11", PlanePayloadElement.BROWNING_303_X4, PlanePayloadElement.LB250x2);
        setAvailablePayload(9, "11", PlanePayloadElement.BROWNING_303_X4, PlanePayloadElement.LB250x2, PlanePayloadElement.EXTRA_AMMO);
        setAvailablePayload(10, "11", PlanePayloadElement.BROWNING_303_X4, PlanePayloadElement.LB500x2);
        setAvailablePayload(11, "11", PlanePayloadElement.BROWNING_303_X4, PlanePayloadElement.LB500x2, PlanePayloadElement.EXTRA_AMMO);

        setAvailablePayload(12, "101", PlanePayloadElement.HISPANO_MKII_X4);
        setAvailablePayload(13, "101", PlanePayloadElement.HISPANO_MKII_X4, PlanePayloadElement.LB250x2);
        setAvailablePayload(14, "101", PlanePayloadElement.HISPANO_MKII_X4, PlanePayloadElement.LB500x2);

        setAvailablePayload(15, "1001", PlanePayloadElement.VICKERS_S40_X2, PlanePayloadElement.AP_AMMO);
        setAvailablePayload(16, "1001", PlanePayloadElement.VICKERS_S40_X2, PlanePayloadElement.HE_AMMO);

        setAvailablePayload(17, "100001", PlanePayloadElement.SHVAK_X2);
        setAvailablePayload(18, "100001", PlanePayloadElement.SHVAK_X2, PlanePayloadElement.FAB100M_X2);
        setAvailablePayload(19, "100001", PlanePayloadElement.SHVAK_X2, PlanePayloadElement.ROS82_X6);
        setAvailablePayload(23, "100001", PlanePayloadElement.SHVAK_X2, PlanePayloadElement.FAB100M_X2, PlanePayloadElement.ROS82_X6);
    }

    @Override
    public IPlanePayload copy()
    {
        HurricaneMkIIPayload clone = new HurricaneMkIIPayload(getPlaneType(), getDate());

        return super.copy(clone);
    }

    @Override
    protected int createWeaponsPayloadForPlane(IFlight flight) throws PWCGException
    {
        int selectedPayloadId = 0;
        if (flight.getCountry().getCountry() == Country.RUSSIA)
        {
            HurricaneMkIIPayloadVVS hurricaneMkIIPayloadVVS = new HurricaneMkIIPayloadVVS(getDate());
            selectedPayloadId = hurricaneMkIIPayloadVVS.createWeaponsPayload(flight);
        }
        else
        {
            HurricaneMkIIPayloadRAF hurricaneMkIIPayloadRAF = new HurricaneMkIIPayloadRAF(getDate());
            selectedPayloadId = hurricaneMkIIPayloadRAF.createWeaponsPayload(flight);
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
            selectedPayloadId == 5 ||
            selectedPayloadId == 7 ||
            selectedPayloadId == 12 ||
            selectedPayloadId == 15 ||
            selectedPayloadId == 16 ||
            selectedPayloadId == 17)
        {
            return false;
        }

        return true;
    }

    @Override
    protected void loadAvailableStockModifications()
    {
        registerStockModification(PlanePayloadElement.MIRROR);
        if (getDate().after(boostIntroDate))
        {
            registerStockModification(PlanePayloadElement.LB_14_BOOST);
        }
    }
}
