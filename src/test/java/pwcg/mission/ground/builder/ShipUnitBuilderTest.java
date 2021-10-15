package pwcg.mission.ground.builder;

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
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.vehicle.VehicleClass;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;

@ExtendWith(MockitoExtension.class)
public class ShipUnitBuilderTest
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
    public void createCargoShipTest () throws PWCGException 
    {
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_SHIPPING, new Coordinate (100000, 0, 100000), CountryFactory.makeCountryByCountry(Country.RUSSIA), "Shipping");
        ShippingUnitBuilder shippingFactory = new ShippingUnitBuilder(campaign, targetDefinition, makeRandomDestination(targetDefinition));
        GroundUnitCollection groundUnitGroup = shippingFactory.createShippingUnit(VehicleClass.ShipCargo);
        assert (groundUnitGroup.getGroundUnits().size() == 2);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            assert (groundUnit.getCountry().getCountry() == Country.RUSSIA);
            if (groundUnit.getVehicleClass() == VehicleClass.ShipCargo)
            {
                assert (groundUnit.getVehicles().size() >= 3);
                assert (groundUnit.getVehicles().size() <= 5);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.ShipWarship)
            {
                assert (groundUnit.getVehicles().size() >= 2);
                assert (groundUnit.getVehicles().size() <= 3);
            }
            else
            {
                throw new PWCGException("Unexpected unit type");
            }
        }
        groundUnitGroup.validate();
    }

    @Test
    public void createWarshipTest () throws PWCGException 
    {
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_SHIPPING, new Coordinate (100000, 0, 100000), CountryFactory.makeCountryByCountry(Country.RUSSIA), "Shipping");
        ShippingUnitBuilder shippingFactory = new ShippingUnitBuilder(campaign, targetDefinition, makeRandomDestination(targetDefinition));
        GroundUnitCollection groundUnitGroup = shippingFactory.createShippingUnit(VehicleClass.ShipWarship);
        assert (groundUnitGroup.getGroundUnits().size() == 1);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            assert (groundUnit.getCountry().getCountry() == Country.RUSSIA);
            if (groundUnit.getVehicleClass() == VehicleClass.ShipWarship)
            {
                assert (groundUnit.getVehicles().size() >= 2);
                assert (groundUnit.getVehicles().size() <= 4);
            }
            else
            {
                throw new PWCGException("Unexpected unit type");
            }
        }
        groundUnitGroup.validate();
    }

    @Test
    public void createSubmarineTest () throws PWCGException 
    {
        TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_SHIPPING, new Coordinate (100000, 0, 100000), CountryFactory.makeCountryByCountry(Country.RUSSIA), "Shipping");
        ShippingUnitBuilder shippingFactory = new ShippingUnitBuilder(campaign, targetDefinition, makeRandomDestination(targetDefinition));
        GroundUnitCollection groundUnitGroup = shippingFactory.createShippingUnit(VehicleClass.Submarine);
        assert (groundUnitGroup.getGroundUnits().size() == 1);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            assert (groundUnit.getCountry().getCountry() == Country.RUSSIA);
            if (groundUnit.getVehicleClass() == VehicleClass.Submarine)
            {
                assert (groundUnit.getVehicles().size() == 1);
            }
            else
            {
                throw new PWCGException("Unexpected unit type");
            }
        }
        groundUnitGroup.validate();
    }
    

    private Coordinate makeRandomDestination(TargetDefinition targetDefinition) throws PWCGException
    {
        int angle = RandomNumberGenerator.getRandom(360);
        Coordinate destination = MathUtils.calcNextCoord(targetDefinition.getPosition(), angle, 50000);
        return destination;
    }
}
