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
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGroundUnitResourceManager;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

@RunWith(MockitoJUnitRunner.class)
public class BalloonUnitBuilderTest
{
    @Mock private Campaign campaign;
    @Mock private Mission mission;
    @Mock private ConfigManagerCampaign configManager;
    @Mock private MissionGroundUnitResourceManager missionGroundUnitManager;

    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(mission.getCampaign()).thenReturn(campaign);
        Mockito.when(mission.getMissionGroundUnitManager()).thenReturn(missionGroundUnitManager);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
    }

    @Test
    public void createAAAArtilleryBatteryTest () throws PWCGException 
    {
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_INFANTRY, new Coordinate (100000, 0, 100000), CountryFactory.makeCountryByCountry(Country.RUSSIA));
        BalloonUnitBuilder groundUnitFactory = new BalloonUnitBuilder(mission, targetDefinition);
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createBalloonUnit();
        validateBalloonUnit(groundUnitGroup, Country.RUSSIA);
        groundUnitGroup.validate();
    }

    @Test
    public void createAAAArtilleryBatteryOppositeSideTest () throws PWCGException 
    {
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_INFANTRY, new Coordinate (100000, 0, 100000), CountryFactory.makeCountryByCountry(Country.GERMANY));
        BalloonUnitBuilder groundUnitFactory = new BalloonUnitBuilder(mission, targetDefinition);
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createBalloonUnit();
        validateBalloonUnit(groundUnitGroup, Country.GERMANY);
        groundUnitGroup.validate();
    }

    private void validateBalloonUnit(IGroundUnitCollection groundUnitGroup, Country country) throws PWCGException
    {
        assert (groundUnitGroup.getGroundUnits().size() == 3);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            if (groundUnit.getVehicleClass() == VehicleClass.Balloon)
            {
                assert (groundUnit.getVehicles().size() == 1);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.AAAArtillery)
            {
                assert (groundUnit.getVehicles().size() >= 2);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.AAAMachineGun)
            {
                assert (groundUnit.getVehicles().size() >= 2);
            }
            else
            {
                throw new PWCGException("Unexpected unit type");
            }
            assert (groundUnit.getCountry().getCountry() == country);
        }
    }
}
