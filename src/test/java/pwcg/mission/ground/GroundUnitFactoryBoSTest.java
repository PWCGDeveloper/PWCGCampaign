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
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.DateUtils;
import pwcg.mission.MissionBeginUnitCheckZone;
import pwcg.mission.ground.factory.AAAUnitBuilder;
import pwcg.mission.ground.unittypes.artillery.GroundAAArtilleryBattery;
import pwcg.mission.ground.unittypes.artillery.GroundAAMachineGunBattery;
import pwcg.mission.ground.unittypes.artillery.GroundArtilleryBattery;
import pwcg.mission.ground.unittypes.infantry.AssaultGroundUnitFactory;
import pwcg.mission.ground.unittypes.infantry.GroundAntiTankArtillery;
import pwcg.mission.ground.unittypes.infantry.GroundAssaultTankUnit;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TacticalTarget;

@RunWith(MockitoJUnitRunner.class)
public class GroundUnitFactoryBoSTest
{
    @Mock private Campaign campaign;
    @Mock private ConfigManagerCampaign configManager;
    
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
        missionBeginUnit.getSelfDeactivatingCheckZone().setCheckZoneCoalition(Coalition.COALITION_ALLIED);
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
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign, country, new Coordinate (100000, 0, 100000));
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createAAAArtilleryBattery(GroundUnitSize.GROUND_UNIT_SIZE_MEDIUM);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() >= 1);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() <= 4);
        assert (groundUnitGroup instanceof GroundAAArtilleryBattery);
        assert (groundUnitGroup.getGroundUnit().getCountry().getCountry() == Country.GERMANY);
        groundUnitGroup.validate();
    }

    @Test
    public void createAAAMGBatteryTest () throws PWCGException 
    {
        AAAUnitBuilder groundUnitFactory = new AAAUnitBuilder(campaign,country, new Coordinate (100000, 0, 100000));
        IGroundUnitCollection groundUnitGroup = groundUnitFactory.createAAAMGBattery(GroundUnitSize.GROUND_UNIT_SIZE_HIGH);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() >= 2);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() <= 8);
        assert (groundUnitGroup instanceof GroundAAMachineGunBattery);
        assert (groundUnitGroup.getGroundUnit().getCountry().getCountry() == Country.GERMANY);
        groundUnitGroup.validate();
    }

    @Test
    public void createArtilleryUnitTest () throws PWCGException 
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

        AssaultGroundUnitFactory groundUnitFactory = new AssaultGroundUnitFactory();
        GroundArtilleryBattery groundUnitGroup = (GroundArtilleryBattery)groundUnitFactory.createAssaultArtilleryUnit(groundUnitInformation);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() > 0);
        assert (groundUnitGroup.getGroundUnit().getCountry().getCountry() == Country.GERMANY);
        groundUnitGroup.validate();
    }

    @Test
    public void createDefenseUnitTest () throws PWCGException 
    {
        String name = country.getCountryName() + " AntiTank";
        GroundUnitInformation groundUnitInformation = GroundUnitInformationFactory.buildGroundUnitInformation(
                campaign, 
                country,
                name, 
                TacticalTarget.TARGET_ARTILLERY, 
                new Coordinate (100000, 0, 100000), 
                new Coordinate (102000, 0, 100000), 
                new Orientation(), 
                true);

        AssaultGroundUnitFactory groundUnitFactory = new AssaultGroundUnitFactory();
        GroundAntiTankArtillery groundUnitGroup = (GroundAntiTankArtillery)groundUnitFactory.createAntiTankGunUnit(groundUnitInformation);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() > 0);
        assert (groundUnitGroup.getGroundUnit().getCountry().getCountry() == Country.GERMANY);
        groundUnitGroup.validate();
    }

    @Test
    public void createAssaultTankUnitTest () throws PWCGException 
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

        AssaultGroundUnitFactory groundUnitFactory = new AssaultGroundUnitFactory();
        GroundAssaultTankUnit groundUnitGroup = (GroundAssaultTankUnit)groundUnitFactory.createAssaultTankUnit(groundUnitInformation);
        assert (groundUnitGroup.getGroundUnit().getSpawners().size() > 0);
        assert (groundUnitGroup.getGroundUnit().getCountry().getCountry() == Country.GERMANY);
        groundUnitGroup.validate();
    }

    @Test
    public void createMachineGunUnitTest () throws PWCGException 
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
    public void createSpotLightGroupTest () throws PWCGException 
    {
    }
}
