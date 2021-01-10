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
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.location.PWCGLocation;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.vehicle.VehicleClass;

@RunWith(MockitoJUnitRunner.class)
public class DrifterUnitBuilderTest
{
    @Mock private Campaign campaign;
    @Mock private Mission mission;
    @Mock private ConfigManagerCampaign configManager;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
    }

    @Test
    public void createDrifterUnitTest () throws PWCGException 
    {
        PWCGLocation location = new PWCGLocation();
        location.setName("Drifter loc");
        location.setPosition(new Coordinate (100000, 0, 100000));
        location.setOrientation(new Orientation (50));
        
        DrifterUnitBuilder groundUnitFactory = new DrifterUnitBuilder(campaign, location, CountryFactory.makeCountryByCountry(Country.RUSSIA));
        GroundUnitCollection groundUnitGroup = groundUnitFactory.createDrifterUnit();
        assert (groundUnitGroup.getGroundUnits().size() >= 2);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            assert (groundUnit.getCountry().getCountry() == Country.RUSSIA);
            if (groundUnit.getVehicleClass() == VehicleClass.Drifter)
            {
                assert (groundUnit.getVehicles().size() >= 2);
                assert (groundUnit.getVehicles().size() <= 4);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.DrifterAAA)
            {
                assert (groundUnit.getVehicles().size() == 2);
            }
            else
            {
                throw new PWCGException("Unexpected unit type");
            }
        }
        groundUnitGroup.validate();
    }
}
