package pwcg.mission.ground;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.Country;
import pwcg.campaign.target.TacticalTarget;
import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.target.TargetDefinitionBuilder;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.factory.AssaultFactory;
import pwcg.mission.ground.factory.DrifterUnitFactory;
import pwcg.mission.ground.factory.ShippingUnitFactory;
import pwcg.mission.ground.factory.TroopConcentrationFactory;
import pwcg.mission.ground.unittypes.SpotLightGroup;
import pwcg.mission.ground.unittypes.artillery.GroundAAABattery;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryUnit;
import pwcg.mission.ground.unittypes.infantry.DrifterUnit;
import pwcg.mission.ground.unittypes.infantry.GroundAntiTankArtillery;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultTankUnit;
import pwcg.mission.ground.unittypes.infantry.GroundMachineGunUnit;
import pwcg.mission.ground.unittypes.infantry.GroundTroopConcentration;
import pwcg.mission.ground.unittypes.transport.ShipConvoyUnit;
import pwcg.testutils.KubanAttackMockCampaign;

@RunWith(MockitoJUnitRunner.class)
public class GroundUnitFactoryTest extends KubanAttackMockCampaign
{
    @Before
    public void setup() throws PWCGException
    {
        mockCampaignSetup();
    }

    @Test
    public void createAAAArtilleryBatteryTest () throws PWCGException 
    {
        AAAUnitFactory groundUnitFactory =  new AAAUnitFactory(campaign, country, new Coordinate (100000, 0, 100000));
        GroundAAABattery groundUnit = groundUnitFactory.createAAAArtilleryBattery(4, 4);
        assert (groundUnit.getSpawners().size() == 4);
        assert (groundUnit.isMg() == false);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createAAAMGBatteryTest () throws PWCGException 
    {
        AAAUnitFactory groundUnitFactory =  new AAAUnitFactory(campaign, country, new Coordinate (100000, 0, 100000));
        GroundAAABattery groundUnit = groundUnitFactory.createAAAMGBattery(4, 4);
        assert (groundUnit.getSpawners().size() == 4);
        assert (groundUnit.isMg() == true);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createDefenseUnitTest () throws PWCGException 
    {
        String name = country.getCountryName() + " AntiTank";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                missionBeginUnit, 
                country,
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                new Coordinate (100000, 0, 100000), 
                new Coordinate (102000, 0, 100000), 
                new Orientation(), 
                true);

        AssaultFactory groundUnitFactory =  new AssaultFactory();
        GroundAntiTankArtillery groundUnit = (GroundAntiTankArtillery)groundUnitFactory.createAntiTankGunUnit(groundUnitInformation);
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createAssaultTankUnitTest () throws PWCGException 
    {
        String name = country.getCountryName() + " Tank";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                missionBeginUnit, 
                country,
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                new Coordinate (100000, 0, 100000), 
                new Coordinate (102000, 0, 100000), 
                new Orientation(), 
                true);

        AssaultFactory groundUnitFactory =  new AssaultFactory();
        GroundAssaultTankUnit groundUnit = (GroundAssaultTankUnit)groundUnitFactory.createAssaultTankUnit(groundUnitInformation);
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createMachineGunUnitTest () throws PWCGException 
    {
        String name = country.getCountryName() + " MG";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                missionBeginUnit, 
                country,
                name, 
                TacticalTarget.TARGET_INFANTRY, 
                new Coordinate (100000, 0, 100000), 
                new Coordinate (102000, 0, 100000), 
                new Orientation(), 
                true);

        AssaultFactory groundUnitFactory =  new AssaultFactory();
        GroundMachineGunUnit groundUnit = (GroundMachineGunUnit)groundUnitFactory.createMachineGunUnit(groundUnitInformation);
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createDrifterUnitTest () throws PWCGException 
    {
        TargetDefinition targetDefinition = new TargetDefinition();
        targetDefinition.setTargetCountry(country);
        targetDefinition.setTargetPosition(new Coordinate (100000, 0, 100000));
        targetDefinition.setTargetOrientation(new Orientation());
        DrifterUnitFactory groundUnitFactory =  new DrifterUnitFactory(campaign, targetDefinition);
        DrifterUnit groundUnit = (DrifterUnit)groundUnitFactory.createDrifterUnit();
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createShippingUnitTest () throws PWCGException 
    {
        boolean isPlayerTarget = true;
        TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder();
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionNoFlight(campaign, country, TacticalTarget.TARGET_SHIPPING, new Coordinate (100000, 0, 100000), isPlayerTarget);

        ShippingUnitFactory groundUnitFactory =  new ShippingUnitFactory(campaign, targetDefinition);        
        ShipConvoyUnit groundUnit = (ShipConvoyUnit)groundUnitFactory.createShippingUnit();
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createTroopConcentrationTest () throws PWCGException 
    {
        boolean isPlayerTarget = true;
        TargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilder();
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinitionNoFlight(campaign, country, TacticalTarget.TARGET_TROOP_CONCENTRATION, new Coordinate (100000, 0, 100000), isPlayerTarget);

        TroopConcentrationFactory groundUnitFactory =  new TroopConcentrationFactory(campaign, targetDefinition);        
        GroundTroopConcentration groundUnit = (GroundTroopConcentration)groundUnitFactory.createTroopConcentration();
        assert (groundUnit.getVehicles().size() > 5);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createSpotLightGroupTest () throws PWCGException 
    {
        GroundUnitFactory groundUnitFactory =  new GroundUnitFactory(campaign, new Coordinate (100000, 0, 100000), country);
        SpotLightGroup groundUnit = (SpotLightGroup)groundUnitFactory.createSpotLightGroup();
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createArtilleryUnitTest () throws PWCGException 
    {
        String name = country.getCountryName() + " MG";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                missionBeginUnit, 
                country,
                name, 
                TacticalTarget.TARGET_ARTILLERY, 
                new Coordinate (100000, 0, 100000), 
                new Coordinate (102000, 0, 100000), 
                new Orientation(), 
                true);

        AssaultFactory groundUnitFactory =  new AssaultFactory();
        GroundArtilleryUnit groundUnit = (GroundArtilleryUnit)groundUnitFactory.createAssaultArtilleryUnit(groundUnitInformation);
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }
    
    @Test
    public void createFriendlyArtilleryBatteryForPlayerTest () throws PWCGException 
    {
        ArtillerySpotBatteryFactory artillerySpotBatteryFactory = new ArtillerySpotBatteryFactory(campaign, new Coordinate (100000, 0, 100000), myTestPosition, country);
        ArtillerySpotArtilleryGroup friendlyArtilleryGroup = artillerySpotBatteryFactory.createFriendlyArtilleryBattery(true);

        assert (friendlyArtilleryGroup.getVehicles().size() > 0);
        assert (friendlyArtilleryGroup.getCountry().getCountry() == Country.GERMANY);
    }
    
    @Test
    public void createFriendlyArtilleryBatteryForAITest () throws PWCGException 
    {
        ArtillerySpotBatteryFactory artillerySpotBatteryFactory = new ArtillerySpotBatteryFactory(campaign, new Coordinate (100000, 0, 100000), myTestPosition, country);
        ArtillerySpotArtilleryGroup friendlyArtilleryGroup = artillerySpotBatteryFactory.createFriendlyArtilleryBattery(false);

        assert (friendlyArtilleryGroup.getVehicles().size() ==1);
        assert (friendlyArtilleryGroup.getCountry().getCountry() == Country.GERMANY);
    }

}
