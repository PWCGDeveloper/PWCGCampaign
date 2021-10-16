package pwcg.mission.ground.builder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

@ExtendWith(MockitoExtension.class)
public class SearchLightUnitBuilderTest
{
    @Mock private Campaign campaign;
    @Mock private ConfigManagerCampaign configManager;
    
    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
    }

    @Test
    public void createSearchLightBatteryTest () throws PWCGException 
    {
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_ARTILLERY, new Coordinate (100000, 0, 100000), CountryFactory.makeCountryByCountry(Country.RUSSIA), "Artillery");
        SearchLightBuilder groundUnitFactory =  new SearchLightBuilder(campaign);
        GroundUnitCollection groundUnitGroup = groundUnitFactory.createSearchLightGroup(targetDefinition);
        Assertions.assertTrue (groundUnitGroup.getGroundUnits().size() == 1);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            Assertions.assertTrue (groundUnit.getCountry().getCountry() == Country.RUSSIA);
            if (groundUnit.getVehicleClass() == VehicleClass.SearchLight)
            {
                Assertions.assertTrue (groundUnit.getVehicles().size() == 2);
            }
            else
            {
                throw new PWCGException("Unexpected unit type");
            }
        }
        groundUnitGroup.validate();
    }
    

    @Test
    public void createOneSearchLightTest () throws PWCGException 
    {
        SearchLightBuilder groundUnitFactory =  new SearchLightBuilder(campaign);
        GroundUnitCollection groundUnitGroup = groundUnitFactory.createOneSearchLight(
                CountryFactory.makeCountryByCountry(Country.GERMANY), new Coordinate (102000, 0, 100000));
        Assertions.assertTrue (groundUnitGroup.getGroundUnits().size() == 1);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            Assertions.assertTrue (groundUnit.getCountry().getCountry() == Country.GERMANY);
            if (groundUnit.getVehicleClass() == VehicleClass.SearchLight)
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
