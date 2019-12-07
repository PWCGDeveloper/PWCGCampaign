package pwcg.mission.ground;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.factory.AAAUnitBuilder;
import pwcg.mission.ground.factory.DrifterUnitBuilder;
import pwcg.mission.ground.factory.ShippingUnitBuilder;
import pwcg.mission.ground.factory.SpotLightBuilder;
import pwcg.mission.ground.unittypes.artillery.GroundAAArtilleryBattery;
import pwcg.mission.ground.unittypes.artillery.GroundAAMachineGunBattery;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryBattery;
import pwcg.mission.ground.unittypes.artillery.SpotlightUnit;
import pwcg.mission.ground.unittypes.infantry.AssaultGroundUnitFactory;
import pwcg.mission.ground.unittypes.infantry.DrifterUnit;
import pwcg.mission.ground.unittypes.infantry.GroundAntiTankArtillery;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultTankUnit;
import pwcg.mission.ground.unittypes.infantry.GroundMachineGunUnit;
import pwcg.mission.ground.unittypes.transport.ShipCargoConvoyUnit;
import pwcg.mission.target.TacticalTarget;
import pwcg.mission.target.TargetDefinition;
import pwcg.testutils.KubanAttackMockCampaign;

@RunWith(MockitoJUnitRunner.class)
public class GroundUnitFactoryTest extends KubanAttackMockCampaign
{
    private TargetDefinition targetDefinition = new TargetDefinition();

    @Before
    public void setup() throws PWCGException
    {
        mockCampaignSetup();
        targetDefinition = new TargetDefinition();
        targetDefinition.setTargetCountry(country);
        targetDefinition.setTargetPosition(new Coordinate (100000, 0, 100000));
        targetDefinition.setTargetOrientation(new Orientation());
        targetDefinition.setTargetType(TacticalTarget.TARGET_ANY);
    }

    @Test
    public void createAAAArtilleryBatteryTest () throws PWCGException 
    {
        AAAUnitBuilder groundUnitFactory =  new AAAUnitBuilder(campaign, country, new Coordinate (100000, 0, 100000));
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() >= 2);
        assert (groundUnitGroup instanceof GroundAAArtilleryBattery);
        assert (groundUnitGroup.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createAAAMGBatteryTest () throws PWCGException 
    {
        AAAUnitBuilder groundUnitFactory =  new AAAUnitBuilder(campaign, country, new Coordinate (100000, 0, 100000));
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_HIGH);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() >= 3);
        assert (groundUnitGroup instanceof GroundAAMachineGunBattery);
        assert (groundUnitGroup.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createDefenseUnitTest () throws PWCGException 
    {
        String name = country.getCountryName() + " AntiTank";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                country,
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                new Coordinate (100000, 0, 100000), 
                new Coordinate (102000, 0, 100000), 
                new Orientation(), 
                true);

        AssaultGroundUnitFactory groundUnitFactory =  new AssaultGroundUnitFactory();
        GroundAntiTankArtillery groundUnitGroup = (GroundAntiTankArtillery)groundUnitFactory.createAntiTankGunUnit(groundUnitInformation);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() > 0);
        assert (groundUnitGroup.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createAssaultTankUnitTest () throws PWCGException 
    {
        String name = country.getCountryName() + " Tank";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                country,
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                new Coordinate (100000, 0, 100000), 
                new Coordinate (102000, 0, 100000), 
                new Orientation(), 
                true);

        AssaultGroundUnitFactory groundUnitFactory =  new AssaultGroundUnitFactory();
        GroundAssaultTankUnit groundUnitGroup = (GroundAssaultTankUnit)groundUnitFactory.createAssaultTankUnit(groundUnitInformation);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() > 0);
        assert (groundUnitGroup.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createMachineGunUnitTest () throws PWCGException 
    {
        String name = country.getCountryName() + " MG";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                country,
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                new Coordinate (100000, 0, 100000), 
                new Coordinate (102000, 0, 100000), 
                new Orientation(), 
                true);

        AssaultGroundUnitFactory groundUnitFactory =  new AssaultGroundUnitFactory();
        GroundMachineGunUnit groundUnitGroup = (GroundMachineGunUnit)groundUnitFactory.createMachineGunUnit(groundUnitInformation);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() > 0);
        assert (groundUnitGroup.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createDrifterUnitTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_DRIFTER);

        DrifterUnitBuilder groundUnitFactory =  new DrifterUnitBuilder(campaign, targetDefinition);
        DrifterUnit groundUnitGroup = (DrifterUnit)groundUnitFactory.createDrifterUnit();
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() > 0);
        assert (groundUnitGroup.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createShippingUnitTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_SHIPPING);
        
        ShippingUnitBuilder groundUnitFactory =  new ShippingUnitBuilder(campaign, targetDefinition);        
        ShipCargoConvoyUnit groundUnitGroup = (ShipCargoConvoyUnit)groundUnitFactory.createShippingUnit();
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() > 0);
        assert (groundUnitGroup.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createSpotLightGroupTest () throws PWCGException 
    {
        targetDefinition.setTargetType(TacticalTarget.TARGET_AIRFIELD);

        SpotLightBuilder groundUnitFactory =  new SpotLightBuilder(campaign);
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createSpotLightGroup(targetDefinition);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() == 1);
        assert (groundUnitGroup instanceof SpotlightUnit);
        assert (groundUnitGroup.getCountry().getCountry() == Country.GERMANY);

    }

    @Test
    public void createArtilleryUnitTest () throws PWCGException 
    {
        String name = country.getCountryName() + " MG";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                country,
                name, 
                TacticalTarget.TARGET_ARTILLERY, 
                new Coordinate (100000, 0, 100000), 
                new Coordinate (102000, 0, 100000), 
                new Orientation(), 
                true);

        AssaultGroundUnitFactory groundUnitFactory =  new AssaultGroundUnitFactory();
        GroundArtilleryBattery groundUnitGroup = (GroundArtilleryBattery)groundUnitFactory.createAssaultArtilleryUnit(groundUnitInformation);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() > 0);
        assert (groundUnitGroup.getCountry().getCountry() == Country.GERMANY);
    }
}
