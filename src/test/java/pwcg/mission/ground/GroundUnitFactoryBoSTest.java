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
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.target.TacticalTarget;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.DateUtils;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.factory.AAAUnitFactory;
import pwcg.mission.ground.factory.AssaultFactory;
import pwcg.mission.ground.unittypes.artillery.GroundAAABattery;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryUnit;
import pwcg.mission.ground.unittypes.infantry.GroundAntiTankArtillery;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultTankUnit;
import pwcg.mission.mcu.Coalition;

@RunWith(MockitoJUnitRunner.class)
public class GroundUnitFactoryBoSTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private ConfigManagerCampaign configManager;
    
    private ICountry country = CountryFactory.makeCountryByCountry(Country.GERMANY);
    private MissionBeginUnitCheckZone missionBeginUnit;
    private Coordinate myTestPosition = new Coordinate (100000, 0, 100000);

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19430401"));
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);

        missionBeginUnit = new MissionBeginUnitCheckZone(myTestPosition, 10000);
        missionBeginUnit.getSelfDeactivatingCheckZone().getCheckZone().triggerCheckZoneByPlaneCoalition(Coalition.COALITION_ALLIED);
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
        AAAUnitFactory groundUnitFactory = new AAAUnitFactory(campaign, country, new Coordinate (100000, 0, 100000));
        GroundAAABattery groundUnit = groundUnitFactory.createAAAArtilleryBattery(1, 4);
        assert (groundUnit.getSpawners().size() >= 1);
        assert (groundUnit.getSpawners().size() <= 4);
        assert (groundUnit.isMg() == false);
        assert (groundUnit.getPwcgGroundUnitInformation().getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createAAAMGBatteryTest () throws PWCGException 
    {
        AAAUnitFactory groundUnitFactory = new AAAUnitFactory(campaign,country, new Coordinate (100000, 0, 100000));
        GroundAAABattery groundUnit = groundUnitFactory.createAAAMGBattery(4, 4);
        assert (groundUnit.getSpawners().size() == 4);
        assert (groundUnit.isMg() == true);
        assert (groundUnit.getPwcgGroundUnitInformation().getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createArtilleryUnitTest () throws PWCGException 
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

        AssaultFactory groundUnitFactory = new AssaultFactory();
        GroundArtilleryUnit groundUnit = (GroundArtilleryUnit)groundUnitFactory.createAssaultArtilleryUnit(groundUnitInformation);
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getPwcgGroundUnitInformation().getCountry().getCountry() == Country.GERMANY);
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
                TacticalTarget.TARGET_ARTILLERY, 
                new Coordinate (100000, 0, 100000), 
                new Coordinate (102000, 0, 100000), 
                new Orientation(), 
                true);

        AssaultFactory groundUnitFactory = new AssaultFactory();
        GroundAntiTankArtillery groundUnit = (GroundAntiTankArtillery)groundUnitFactory.createAntiTankGunUnit(groundUnitInformation);
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getPwcgGroundUnitInformation().getCountry().getCountry() == Country.GERMANY);
    }

    @Test
    public void createAssaultTankUnitTest () throws PWCGException 
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

        AssaultFactory groundUnitFactory = new AssaultFactory();
        GroundAssaultTankUnit groundUnit = (GroundAssaultTankUnit)groundUnitFactory.createAssaultTankUnit(groundUnitInformation);
        assert (groundUnit.getSpawners().size() > 0);
        assert (groundUnit.getPwcgGroundUnitInformation().getCountry().getCountry() == Country.GERMANY);
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
