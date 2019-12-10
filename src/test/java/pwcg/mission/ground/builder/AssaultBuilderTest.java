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
public class AssaultBuilderTest
{
    @Mock private Campaign campaign;
    @Mock private Mission mission;
    @Mock private ConfigManagerCampaign configManager;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(mission.getCampaign()).thenReturn(campaign);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
    }

    @Test
    public void createLargeAssaultTest () throws PWCGException 
    {
        TargetDefinitionBuilderGround targetDefinitionBuilder = new TargetDefinitionBuilderGround(campaign);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionBattle(
                CountryFactory.makeCountryByCountry(Country.GERMANY), 
                CountryFactory.makeCountryByCountry(Country.RUSSIA), 
                TacticalTarget.TARGET_ASSAULT, new Coordinate (102000, 0, 100000), true);

        IGroundUnitCollection groundUnitGroup = AssaultBuilder.generateAssault(mission, targetDefinition);
        
        assert (groundUnitGroup.getGroundUnits().size() >= 10);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            if (groundUnit.getCountry().getCountry() == Country.GERMANY)
            {
                if (groundUnit.getVehicleClass() == VehicleClass.Tank)
                {
                    assert (groundUnit.getSpawners().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryHowitzer)
                {
                    assert (groundUnit.getSpawners().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.MachineGun)
                {
                    assert (groundUnit.getSpawners().size() >= 2);
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
            }
            else if (groundUnit.getCountry().getCountry() == Country.RUSSIA)
            {
                if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryAntiTank)
                {
                    assert (groundUnit.getSpawners().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.ArtilleryHowitzer)
                {
                    assert (groundUnit.getSpawners().size() >= 2);
                }
                else if (groundUnit.getVehicleClass() == VehicleClass.MachineGun)
                {
                    assert (groundUnit.getSpawners().size() >= 2);
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
            }
            else
            {
                throw new PWCGException("Unit from unidentified nation in assault");
            }
        }
        groundUnitGroup.validate();
    }
}
