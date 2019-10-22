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
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.unit.TargetBuilder;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.MissionGroundUnitResourceManager;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.ground.GroundUnitCollection;

@RunWith(MockitoJUnitRunner.class)
public class TargetBuilderTest
{
    @Mock private TargetDefinition targetDefinition;
    @Mock private Campaign campaign;
    @Mock private Squadron squadron;
    @Mock private Mission mission;
    @Mock private ICountry friendlyCountry;
    @Mock private ICountry enemyCountry;
    @Mock private ConfigManagerCampaign configManager;
    @Mock private FlightInformation flightInformation;
    
    private MissionGroundUnitResourceManager groundUnitResourceManager = new MissionGroundUnitResourceManager();

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.KUBAN_MAP);
                
        Date date = DateUtils.getDateYYYYMMDD("1943030");
        Mockito.when(campaign.getCampaignConfigManager()).thenReturn(configManager);
        Mockito.when(campaign.getDate()).thenReturn(date);
        Mockito.when(targetDefinition.getTargetCountry()).thenReturn(enemyCountry);
        Mockito.when(targetDefinition.getAttackingCountry()).thenReturn(friendlyCountry);
        Mockito.when(targetDefinition.getTargetPosition()).thenReturn(new Coordinate(216336, 0, 184721));
        Mockito.when(targetDefinition.getTargetOrientation()).thenReturn(new Orientation(90));
        Mockito.when(enemyCountry.getSide()).thenReturn(Side.ALLIED);
        Mockito.when(enemyCountry.getSideNoNeutral()).thenReturn(Side.AXIS);
        Mockito.when(enemyCountry.getCountry()).thenReturn(Country.RUSSIA);
        Mockito.when(friendlyCountry.getSide()).thenReturn(Side.AXIS);
        Mockito.when(friendlyCountry.getSideNoNeutral()).thenReturn(Side.AXIS);
        Mockito.when(friendlyCountry.getCountry()).thenReturn(Country.GERMANY);
        Mockito.when(configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey)).thenReturn(ConfigSimple.CONFIG_LEVEL_MED);
        Mockito.when(mission.getMissionGroundUnitManager()).thenReturn(groundUnitResourceManager);
        Mockito.when(flightInformation.getMission()).thenReturn(mission);
        Mockito.when(flightInformation.getSquadron()).thenReturn(squadron);
        Mockito.when(flightInformation.getCampaign()).thenReturn(campaign);
        Mockito.when(flightInformation.getTargetDefinition()).thenReturn(targetDefinition);
    }
    
    @Test
    public void createShippingTest() throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_SHIPPING);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.ANTI_SHIPPING_BOMB);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);        
    }
    
    @Test
    public void createAssaultTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_ASSAULT);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.BOMB);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);        
        assert(groundUnits.getAllAxisGroundUnits().size() > 0);        
    }
    
    @Test
    public void createTroopConcentrationTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_TROOP_CONCENTRATION);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.LOW_ALT_BOMB);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);        
    }
    
    @Test
    public void createTransportTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_TRANSPORT);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.BOMB);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                
    }
    
    @Test
    public void createTrainTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_TRAIN);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.BOMB);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                        
    }
    
    @Test
    public void createArtilleryBatteryTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_ARTILLERY);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.GROUND_ATTACK);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                
    }
    
    @Test
    public void createAirfieldTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_AIRFIELD);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.BOMB);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
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
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.BALLOON_DEFENSE);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() == 0);
        assert(groundUnits.getAllAxisGroundUnits().size() > 0);                                                
    }
    
    @Test
    public void createBalloonTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_BALLOON);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.BALLOON_BUST);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                
    }
    
    @Test
    public void createDrifterTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_DRIFTER);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.DIVE_BOMB);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                
    }
    
    @Test
    public void createPortTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_PORT);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.BOMB);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                
     }
    
    @Test
    public void createRailTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_RAIL);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.BOMB);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                        
    }
    
    @Test
    public void createFactoryTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_FACTORY);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.BOMB);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                
    }
    
    @Test
    public void createCityTest()  throws PWCGException
    {
        Mockito.when(targetDefinition.getTargetType()).thenReturn(TacticalTarget.TARGET_CITY);
        Mockito.when(flightInformation.getFlightType()).thenReturn(FlightTypes.BOMB);
        TargetBuilder targetBuilder = new TargetBuilder(flightInformation);
        targetBuilder.buildTarget();
        GroundUnitCollection groundUnits = targetBuilder.getGroundUnits();
        assert(groundUnits.getAllAlliedGroundUnits().size() > 0);
        assert(groundUnits.getAllAxisGroundUnits().size() == 0);                                                
    }
}
