package pwcg.mission.ground;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.unittypes.artillery.GroundAAABattery;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryUnit;
import pwcg.mission.ground.unittypes.infantry.GroundATArtillery;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultTankUnit;
import pwcg.mission.mcu.Coalition;

@RunWith(MockitoJUnitRunner.class)
public class GroundUnitFactoryRoFTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private ConfigManagerCampaign configManager;
    
    private ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);
    private MissionBeginUnitCheckZone missionBeginUnit = new MissionBeginUnitCheckZone();
    private Coordinate myTestPosition = new Coordinate (100000, 0, 100000);

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(campaign.getAirfieldName()).thenReturn("Staro-Nijne-Steblievskaya");
        Mockito.when(configManager.getIntConfigParam(ConfigItemKeys.MaxGroundTargetDistanceKey)).thenReturn(50000);
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);

        missionBeginUnit.initialize(myTestPosition, 10000, Coalition.COALITION_ALLIED);
}
    
    @Test
    public void createFriendlyArtilleryBatteryTest () throws PWCGException 
    {
    }

    @Test
    public void createBalloonUnitTest () throws PWCGException 
    {
    }

    @Test
    public void createTrainTargetTest () throws PWCGException 
    {
    }

    @Test
    public void createTrainUnitTest () throws PWCGException 
    {
    }

    @Test
    public void createAirfieldUnitTest () throws PWCGException 
    {
    }

    @Test
    public void createTruckConvoyTest () throws PWCGException 
    {
    }

    @Test
    public void createGroundArtilleryBatteryTest () throws PWCGException 
    {
    }

    @Test
    public void createAmmoDumpTest () throws PWCGException 
    {
    }

    @Test
    public void createAAAArtilleryBatteryTest () throws PWCGException 
    {
        GroundUnitAAAFactory groundUnitFactory = new GroundUnitAAAFactory(country, new Coordinate (100000, 0, 100000));
        GroundAAABattery groundUnit = groundUnitFactory.createAAAArtilleryBattery(4);
        assert (groundUnit.getSpawners().size() == 4);
        assert (groundUnit.isMg() == false);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createAAAMGBatteryTest () throws PWCGException 
    {
        GroundUnitAAAFactory groundUnitFactory = new GroundUnitAAAFactory(country, new Coordinate (100000, 0, 100000));
        GroundAAABattery groundUnit = groundUnitFactory.createAAAMGBattery(4);
        assert (groundUnit.getSpawners().size() == 4);
        assert (groundUnit.isMg() == true);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createArtilleryUnitTest () throws PWCGException 
    {
        GroundUnitFactory groundUnitFactory = new GroundUnitFactory(campaign, myTestPosition, country);
        GroundArtilleryUnit groundUnit = (GroundArtilleryUnit)groundUnitFactory.createArtilleryUnit(missionBeginUnit, country, new Coordinate (100000, 0, 100000), new Coordinate (102000, 0, 100000));
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createDefenseUnitTest () throws PWCGException 
    {
        GroundUnitAssaultFactory groundUnitFactory = new GroundUnitAssaultFactory(campaign);
        GroundATArtillery groundUnit = (GroundATArtillery)groundUnitFactory.createDefenseUnit(missionBeginUnit, country, new Coordinate (100000, 0, 100000), new Coordinate (102000, 0, 100000));
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createAssaultTankUnitTest () throws PWCGException 
    {
        GroundUnitAssaultFactory groundUnitFactory = new GroundUnitAssaultFactory(campaign);
        GroundAssaultTankUnit groundUnit = (GroundAssaultTankUnit)groundUnitFactory.createAssaultTankUnit(missionBeginUnit, country, new Coordinate (100000, 0, 100000), new Coordinate (102000, 0, 100000));
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createStandingInfantryUnitTest () throws PWCGException 
    {
    }

    @Test
    public void createAssaultInfantryUnitTest () throws PWCGException 
    {
    }

    @Test
    public void createMachineGunUnitTest () throws PWCGException 
    {
    }

    @Test
    public void createPillBoxUnitTest () throws PWCGException 
    {
    }

    @Test
    public void createPillBoxFlareUnitTest () throws PWCGException 
    {
    }

    @Test
    public void createDrifterUnitTest () throws PWCGException 
    {
    }

    @Test
    public void createShippingUnitTest () throws PWCGException 
    {
    }

    @Test
    public void createTroopConcentrationTest () throws PWCGException 
    {
    }

    @Test
    public void createSpotLightGroupTest () throws PWCGException 
    {
    }
}
