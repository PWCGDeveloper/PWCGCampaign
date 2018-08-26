package pwcg.campaign.target;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGroundUnitResourceManager;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.GroundUnitCollection;

@RunWith(MockitoJUnitRunner.class)
public class TargetBuilderTest
{
    @Mock private TargetDefinition targetDefinition;
    @Mock private Campaign campaign;
    @Mock private Mission mission;
    @Mock private ICountry friendlyCountry;
    @Mock private ICountry enemyCountry;
    @Mock private ConfigManagerCampaign configManager;
    
    private MissionGroundUnitResourceManager groundUnitResourceManager = new MissionGroundUnitResourceManager();
    private Coordinate referencePosition = new Coordinate(150000, 0, 150000);

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);
                
        Date date = DateUtils.getDateYYYYMMDD("1943030");
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(date);
        Mockito.when(campaign.determineCountry()).thenReturn(friendlyCountry);
        Mockito.when(targetDefinition.getTargetCountry()).thenReturn(enemyCountry);
        Mockito.when(targetDefinition.getAttackingCountry()).thenReturn(friendlyCountry);
        Mockito.when(targetDefinition.getTargetGeneralPosition()).thenReturn(referencePosition);
        Mockito.when(targetDefinition.getTargetPosition()).thenReturn(new Coordinate(216336, 0, 184721));
        Mockito.when(targetDefinition.getTargetOrientation()).thenReturn(new Orientation(90));
        Mockito.when(enemyCountry.getSide()).thenReturn(Side.ALLIED);
        Mockito.when(enemyCountry.getSideNoNeutral()).thenReturn(Side.AXIS);
        Mockito.when(enemyCountry.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(friendlyCountry.getSide()).thenReturn(Side.AXIS);
        Mockito.when(friendlyCountry.getSideNoNeutral()).thenReturn(Side.AXIS);
        Mockito.when(friendlyCountry.getCountry()).thenReturn(Country.GERMANY);
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
        Mockito.when(mission.getMissionGroundUnitManager()).thenReturn(groundUnitResourceManager);
    }
    
    @Test
    public void createShippingTest() throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_SHIPPING);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.ANTI_SHIPPING);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);        
    }
    
    @Test
    public void createAssaultTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_ASSAULT);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.BOMB);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);        
        assert(groundUnits.getAllAxisGroundUnits().size() > 0);        
    }
    
    @Test
    public void createTroopConcentrationTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_TROOP_CONCENTRATION);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.LOW_ALT_BOMB);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);        
    }
    
    @Test
    public void createTransportTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_TRANSPORT);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.BOMB);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                
    }
    
    @Test
    public void createTrainTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_TRAIN);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.BOMB);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                        
    }
    
    @Test
    public void createArtilleryBatteryTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_ARTILLERY);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.GROUND_ATTACK);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                
    }
    
    @Test
    public void createAirfieldTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_AIRFIELD);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.BOMB);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                        
    }
    
    @Test
    public void createBalloonDefenseTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_BALLOON);
        Mockito.when(enemyCountry.getSide()).thenReturn(Side.AXIS);
        Mockito.when(friendlyCountry.getSide()).thenReturn(Side.ALLIED);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.BALLOON_DEFENSE);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() == 0);
        assert(groundUnits.getAllAxisGroundUnits().size() > 0);                                                
    }
    
    @Test
    public void createBalloonTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_BALLOON);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.BALLOON_BUST);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                
    }
    
    @Test
    public void createDrifterTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_DRIFTER);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.DIVE_BOMB);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                
    }
    
    @Test
    public void createPortTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_PORT);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.BOMB);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                
     }
    
    @Test
    public void createRailTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_RAIL);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.BOMB);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                        
    }
    
    @Test
    public void createFactoryTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_FACTORY);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.BOMB);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                
    }
    
    @Test
    public void createCityTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_CITY);

        TargetBuilder targetBuilder = new TargetBuilder(campaign, mission, targetDefinition);
        targetBuilder.buildTarget(FlightTypes.BOMB);
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                
    }
}
