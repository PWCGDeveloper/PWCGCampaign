package pwcg.mission.ground;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.artySpot.ArtillerySpotArtilleryGroup;
import pwcg.mission.ground.AssaultGenerator.BattleSize;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.factory.AssaultFactory;
import pwcg.mission.ground.factory.DrifterUnitFactory;
import pwcg.mission.ground.factory.ShippingUnitFactory;
import pwcg.mission.ground.factory.TroopConcentrationFactory;
import pwcg.mission.ground.unittypes.SpotLightGroup;
import pwcg.mission.ground.unittypes.artillery.GroundAAABattery;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryUnit;
import pwcg.mission.ground.unittypes.infantry.DrifterUnit;
import pwcg.mission.ground.unittypes.infantry.GroundATArtillery;
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
        AssaultFactory groundUnitFactory =  new AssaultFactory(campaign, BattleSize.BATTLE_SIZE_ASSAULT);
        GroundATArtillery groundUnit = (GroundATArtillery)groundUnitFactory.createDefenseUnit(missionBeginUnit, country, new Coordinate (100000, 0, 100000), new Coordinate (100000, 0, 100000));
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createAssaultTankUnitTest () throws PWCGException 
    {
        AssaultFactory groundUnitFactory =  new AssaultFactory(campaign, BattleSize.BATTLE_SIZE_ASSAULT);
        GroundAssaultTankUnit groundUnit = (GroundAssaultTankUnit)groundUnitFactory.createAssaultTankUnit(missionBeginUnit, country, new Coordinate (100000, 0, 100000), new Coordinate (100000, 0, 100000));
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createMachineGunUnitTest () throws PWCGException 
    {
        AssaultFactory groundUnitFactory =  new AssaultFactory(campaign, BattleSize.BATTLE_SIZE_ASSAULT);
        GroundMachineGunUnit groundUnit = (GroundMachineGunUnit)groundUnitFactory.createMachineGunUnit(missionBeginUnit, country, new Coordinate (100000, 0, 100000), new Coordinate (100000, 0, 100000));
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createDrifterUnitTest () throws PWCGException 
    {
        DrifterUnitFactory groundUnitFactory =  new DrifterUnitFactory(campaign, new Coordinate (100000, 0, 100000), new Orientation(), country);
        DrifterUnit groundUnit = (DrifterUnit)groundUnitFactory.createDrifterUnit(2, 4);
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createShippingUnitTest () throws PWCGException 
    {
        ShippingUnitFactory groundUnitFactory =  new ShippingUnitFactory(campaign, new Coordinate (100000, 0, 100000), country);        
        ShipConvoyUnit groundUnit = (ShipConvoyUnit)groundUnitFactory.createShippingUnit();
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createTroopConcentrationTest () throws PWCGException 
    {
        TroopConcentrationFactory groundUnitFactory =  new TroopConcentrationFactory(campaign, new Coordinate (100000, 0, 100000), country);        
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
        GroundUnitFactory groundUnitFactory =  new GroundUnitFactory(campaign, new Coordinate (100000, 0, 100000), country);
        GroundArtilleryUnit groundUnit = (GroundArtilleryUnit)groundUnitFactory.createArtilleryUnit(missionBeginUnit, country, new Coordinate (100000, 0, 100000), new Coordinate (100000, 0, 100000));
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }
    
    
    @Test
    public void createFriendlyArtilleryBatteryTest () throws PWCGException 
    {
        GroundUnitFactory groundUnitFactory =  new GroundUnitFactory(campaign, new Coordinate (100000, 0, 100000), country);
        ArtillerySpotArtilleryGroup groundUnit = (ArtillerySpotArtilleryGroup)groundUnitFactory.createFriendlyArtilleryBattery();
        assert (groundUnit.getVehicles().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

}
