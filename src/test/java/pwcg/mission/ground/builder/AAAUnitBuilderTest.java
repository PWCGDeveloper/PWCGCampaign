package pwcg.mission.ground.builder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.mission.ground.GroundUnitSize;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.vehicle.VehicleClass;

@RunWith(MockitoJUnitRunner.class)
public class AAAUnitBuilderTest
{
    @Mock private Campaign campaign;
    @Mock private ConfigManagerCampaign configManager;
    
    private ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
    }

    @Test
    public void createAAAArtilleryBatteryTest () throws PWCGException 
    {
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, country, new Coordinate (100000, 0, 100000));
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
        assert (groundUnitGroup.getGroundUnits().size() == 1);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
            if (groundUnit.getVehicleClass() == VehicleClass.AAAArtillery)
            {
                assert (groundUnit.getVehicles().size() >= 1);
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
    public void createAAAMGBatteryTest () throws PWCGException 
    {
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign,country, new Coordinate (100000, 0, 100000));
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_HIGH);
        assert (groundUnitGroup.getGroundUnits().size() == 1);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
            if (groundUnit.getVehicleClass() == VehicleClass.AAAMachineGun)
            {
                assert (groundUnit.getVehicles().size() >= 2);
                assert (groundUnit.getVehicles().size() <= 8);
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
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, country, new Coordinate (100000, 0, 100000));
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createAAAArtilleryBatteryWithSearchLight(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
        assert(groundUnitGroup.getGroundUnits().size() == 2);
        assert (groundUnitGroup.getGroundUnits().size() == 2);
        for (IGroundUnit groundUnit : groundUnitGroup.getGroundUnits())
        {
            assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
            if (groundUnit.getVehicleClass() == VehicleClass.AAAArtillery)
            {
                assert (groundUnit.getVehicles().size() >= 1);
                assert (groundUnit.getVehicles().size() <= 4);
            }
            else if (groundUnit.getVehicleClass() == VehicleClass.SearchLight)
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
}
