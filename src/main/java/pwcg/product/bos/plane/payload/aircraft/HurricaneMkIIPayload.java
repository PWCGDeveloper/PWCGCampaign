package pwcg.product.bos.plane.payload.aircraft;

import pwcg.campaign.context.Country;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.payload.IPlanePayload;
import pwcg.campaign.plane.payload.PayloadElement;
import pwcg.campaign.plane.payload.PlanePayload;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;

public class HurricaneMkIIPayload extends PlanePayload implements IPlanePayload
{    
    public HurricaneMkIIPayload(PlaneType planeType)
    {
        super(planeType);
        noOrdnancePayloadElement = 12;
    }

    protected void initialize()
    {        
        setAvailablePayload(-3, "100000000", PayloadElement.AIR_FILTER);
        setAvailablePayload(-2, "10000000", PayloadElement.MIRROR);
        setAvailablePayload(-1, "1000000", PayloadElement.LB_14_BOOST);

        setAvailablePayload(0, "1", PayloadElement.STANDARD);
        setAvailablePayload(1, "1", PayloadElement.EXTRA_AMMO);
        setAvailablePayload(2, "1", PayloadElement.LB250x2);
        setAvailablePayload(3, "1", PayloadElement.LB250x2, PayloadElement.EXTRA_AMMO);
        setAvailablePayload(4, "1", PayloadElement.LB500x2);
        setAvailablePayload(5, "1", PayloadElement.LB500x2, PayloadElement.EXTRA_AMMO);

        setAvailablePayload(5, "11", PayloadElement.BROWNING_303_X4);
        setAvailablePayload(7, "11", PayloadElement.BROWNING_303_X4, PayloadElement.EXTRA_AMMO);
        setAvailablePayload(8, "11", PayloadElement.BROWNING_303_X4, PayloadElement.LB250x2);
        setAvailablePayload(9, "11", PayloadElement.BROWNING_303_X4, PayloadElement.LB250x2, PayloadElement.EXTRA_AMMO);
        setAvailablePayload(10, "11", PayloadElement.BROWNING_303_X4, PayloadElement.LB500x2);
        setAvailablePayload(11, "11", PayloadElement.BROWNING_303_X4, PayloadElement.LB500x2, PayloadElement.EXTRA_AMMO);

        setAvailablePayload(12, "101", PayloadElement.HISPANO_MKII_X4);
        setAvailablePayload(13, "101", PayloadElement.HISPANO_MKII_X4, PayloadElement.LB250x2);
        setAvailablePayload(14, "101", PayloadElement.HISPANO_MKII_X4, PayloadElement.LB500x2);

        setAvailablePayload(15, "1001", PayloadElement.VICKERS_S40_X2, PayloadElement.AP_AMMO);
        setAvailablePayload(16, "1001", PayloadElement.VICKERS_S40_X2, PayloadElement.HE_AMMO);

        setAvailablePayload(17, "100001", PayloadElement.SHVAK_X2);
        setAvailablePayload(18, "100001", PayloadElement.SHVAK_X2, PayloadElement.FAB100M_X2);
        setAvailablePayload(19, "100001", PayloadElement.SHVAK_X2, PayloadElement.ROS82_X6);
        setAvailablePayload(23, "100001", PayloadElement.SHVAK_X2, PayloadElement.FAB100M_X2, PayloadElement.ROS82_X6);

        addStockModifications(PayloadElement.MIRROR);
    }

    @Override
    public IPlanePayload copy()
    {
        HurricaneMkIIPayload clone = new HurricaneMkIIPayload(planeType);

        return super.copy(clone);
    }

    @Override
    public int createWeaponsPayload(IFlight flight) throws PWCGException
    {
        selectedPrimaryPayloadId = 0;
        if (flight.getSquadron().getCountry().getCountry() == Country.RUSSIA)
        {
            HurricaneMkIIPayloadVVS hurricaneMkIIPayloadVVS = new HurricaneMkIIPayloadVVS();
            selectedPrimaryPayloadId = hurricaneMkIIPayloadVVS.createWeaponsPayload(flight);
        }
        else
        {
            HurricaneMkIIPayloadRAF hurricaneMkIIPayloadRAF = new HurricaneMkIIPayloadRAF();
            selectedPrimaryPayloadId = hurricaneMkIIPayloadRAF.createWeaponsPayload(flight);
        }
        
        return selectedPrimaryPayloadId;
    }    
}
