package pwcg.mission.ground.builder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
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
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

@ExtendWith(MockitoExtension.class)
@Tag("BOS")
public class AAAUnitBuilderTest
{
    @Mock private Campaign campaign;
    @Mock private ConfigManagerCampaign configManager;
    
    private ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_HIGH);
    }

    @Test
    public void createAAAArtilleryBatteryTest () throws PWCGException 
    {
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, new Coordinate (100000, 0, 100000), country, "Artillery");
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);
        GroundUnitCollection groundUnitGroup = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
        Assertions.assertTrue (groundUnitGroup.getGroundUnits().size() == 1);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            Assertions.assertTrue (groundUnit.getCountry().getCountry() == Country.GERMANY);
            if (groundUnit.getVehicleClass() == VehicleClass.AAAArtillery)
            {
                Assertions.assertTrue (groundUnit.getVehicles().size() >= 1);
                Assertions.assertTrue (groundUnit.getVehicles().size() <= 4);
            }
            else
            {
                throw new PWCGException("Unexpected unit type");
            }
        }
        groundUnitGroup.validate();
    }

    @Test
    public void createAAAMGBatteryTest () throws PWCGException 
    {
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, new Coordinate (100000, 0, 100000), country, "Artillery");
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);
        GroundUnitCollection groundUnitGroup = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_HIGH);
        Assertions.assertTrue (groundUnitGroup.getGroundUnits().size() == 1);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            Assertions.assertTrue (groundUnit.getCountry().getCountry() == Country.GERMANY);
            if (groundUnit.getVehicleClass() == VehicleClass.AAAMachineGun)
            {
                Assertions.assertTrue (groundUnit.getVehicles().size() >= 2);
                Assertions.assertTrue (groundUnit.getVehicles().size() <= 8);
            }
            else
            {
                throw new PWCGException("Unexpected unit type");
            }
        }
        groundUnitGroup.validate();
    }
    

    @Test
    public void createAAAArtilleryBatteryWithSearchLightTest () throws PWCGException 
    {
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, new Coordinate (100000, 0, 100000), country, "Artillery");
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, targetDefinition);
        GroundUnitCollection groundUnitGroup = groundUnitFactory.createAAAArtilleryBatteryWithSearchLight(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
        assert(groundUnitGroup.getGroundUnits().size() == 2);
        Assertions.assertTrue (groundUnitGroup.getGroundUnits().size() == 2);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            Assertions.assertTrue (groundUnit.getCountry().getCountry() == Country.GERMANY);
            if (groundUnit.getVehicleClass() == VehicleClass.AAAArtillery)
            {
                Assertions.assertTrue (groundUnit.getVehicles().size() >= 1);
                Assertions.assertTrue (groundUnit.getVehicles().size() <= 4);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.SearchLight)
            {
                Assertions.assertTrue (groundUnit.getVehicles().size() == 1);
            }
            else
            {
                throw new PWCGException("Unexpected unit type");
            }
        }
        groundUnitGroup.validate();
    }
}
