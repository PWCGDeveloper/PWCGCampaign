package pwcg.mission.ground.builder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

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
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderGround;

@RunWith(MockitoJUnitRunner.class)
public class BalloonUnitBuilderTest
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
    public void createAAAArtilleryBatteryTest () throws PWCGException 
    {
        TargetDefinitionBuilderGround targetDefinitionBuilder = new TargetDefinitionBuilderGround(campaign);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionBattle(
                CountryFactory.makeCountryByCountry(Country.GERMANY), 
                CountryFactory.makeCountryByCountry(Country.RUSSIA), 
                TacticalTarget.TARGET_BALLOON, new Coordinate (102000, 0, 100000), true);


        BalloonUnitBuilder groundUnitFactory = new BalloonUnitBuilder(campaign, targetDefinition);
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createBalloonUnit(CountryFactory.makeCountryByCountry(Country.RUSSIA));
        validateBalloonUnit(groundUnitGroup, Country.RUSSIA);
        groundUnitGroup.validate();
    }

    @Test
    public void createAAAArtilleryBatteryOppositeSideTest () throws PWCGException 
    {
        TargetDefinitionBuilderGround targetDefinitionBuilder = new TargetDefinitionBuilderGround(campaign);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionBattle(
                CountryFactory.makeCountryByCountry(Country.GERMANY), 
                CountryFactory.makeCountryByCountry(Country.RUSSIA), 
                TacticalTarget.TARGET_BALLOON, new Coordinate (102000, 0, 100000), true);


        BalloonUnitBuilder groundUnitFactory = new BalloonUnitBuilder(campaign, targetDefinition);
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createBalloonUnit(CountryFactory.makeCountryByCountry(Country.GERMANY));
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
                assert (groundUnit.getSpawners().size() == 1);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.AAAArtillery)
            {
                assert (groundUnit.getSpawners().size() >= 2);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.AAAMachineGun)
            {
                assert (groundUnit.getSpawners().size() >= 2);
            }
            else
            {
                throw new PWCGException("Unexpected unit type");
            }
            assert (groundUnit.getCountry().getCountry() == country);
        }
    }
}
