package pwcg.mission.ground.builder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.Bridge;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.DateUtils;
import pwcg.mission.MissionGroundUnitResourceManager;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;

@RunWith(MockitoJUnitRunner.class)
public class TruckConvoyBuilderTest
{
    @Mock private MissionGroundUnitResourceManager missionGroundUnitManager;
    @Mock private Campaign campaign;
    @Mock private ConfigManagerCampaign configManager;
    @Mock private Bridge bridge;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
        Mockito.when(bridge.getPosition()).thenReturn(new Coordinate (100000, 0, 100000));
        Mockito.when(bridge.getOrientation()).thenReturn(new Orientation (40));
    }

    @Test
    public void createSearchLightBatteryTest () throws PWCGException 
    {
        TruckConvoyBuilder groundUnitFactory =  new TruckConvoyBuilder(campaign, bridge, CountryFactory.makeCountryByCountry(Country.RUSSIA));
        GroundUnitCollection groundUnitGroup = groundUnitFactory.createTruckConvoy();
        assert (groundUnitGroup.getGroundUnits().size() == 3);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            assert (groundUnit.getCountry().getCountry() == Country.RUSSIA);
            if (groundUnit.getVehicleClass() == VehicleClass.Truck)
            {
                assert (groundUnit.getVehicles().size() >= 3);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.TruckAAA)
            {
                assert (groundUnit.getVehicles().size() == 2);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.TruckAmmo)
            {
                assert (groundUnit.getVehicles().size() > 0);
            }
            else
            {
                throw new PWCGException("Unexpected unit type");
            }
        }
        groundUnitGroup.validate();
    }
}
