package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.ww1.country.RoFCountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class PlayerDeclarationResolutionFirmVictoryTest
{
    @Mock 
    private Campaign campaign;
    
    @Mock
    private CampaignPersonnelManager personnelManager;

    @Mock
    private PlayerVictoryDeclaration mockPlayerDeclaration;

    @Mock
    private AARMissionEvaluationData evaluationData;

    @Mock
    private VictorySorter victorySorter;
    
    @Mock
    private SquadronMember player;
    
    @Mock
    private SquadronMember ai;

    @Mock
    private PlaneTypeFactory planeFactory;
    
    private PlayerDeclarations playerDeclarations;


    private List<LogVictory> firmVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        

    private LogPlane playerVictor = new LogPlane();
    private LogPlane aiVictor = new LogPlane();
    
    private static String PLAYER_NAME = "Player Name";

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        
        firmVictories.clear();
        
        playerVictor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        playerVictor.setCountry(new RoFCountry(Country.FRANCE));
        
        aiVictor.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        aiVictor.setCountry(new RoFCountry(Country.FRANCE));
        
        createMocks();
    }
    
    private void createMocks() throws PWCGException
    {
        
        Mockito.when(victorySorter.getFirmAirVictories()).thenReturn(firmVictories);
        Mockito.when(victorySorter.getFirmBalloonVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFuzzyAirVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getAllUnconfirmed()).thenReturn(emptyList);
        Mockito.when(campaign.getPlayer()).thenReturn(player);
        Mockito.when(campaign.getName()).thenReturn(PLAYER_NAME);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getBeginningOfWar());
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(ai);

        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(playerVictor);
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictor);

        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(ai.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
    }

    private void createVictory(Integer victorSerialNumber, Integer victimSerialNumber)
    {        
        LogPlane victim = new LogPlane();
        victim.setPilotSerialNumber(victimSerialNumber);
        victim.setVehicleType("albatrosd3");
        victim.setCountry(new RoFCountry(Country.GERMANY));

        LogPlane victor = new LogPlane();
        victor.setVehicleType("spad7");
        victor.setPilotSerialNumber(victorSerialNumber);
        victor.setCountry(new RoFCountry(Country.FRANCE));

        LogVictory resultVictory = new LogVictory();
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);
        
        firmVictories.add(resultVictory);
    }
    
    private void createPlayerDeclarations(int numDeclarations) throws PWCGException
    {
        playerDeclarations = new PlayerDeclarations();
        for (int i = 0; i < numDeclarations; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.confirmDeclaration(true, "albatrosd3");
            playerDeclarations.addPlayerDeclaration(declaration);
        }        
    }
    
    @Test
    public void testPlayerFirmVictoryAward () throws PWCGException
    {   
        
        createPlayerDeclarations(1);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims(playerDeclarations);
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerMultipleFirmVictoryAwards () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims(playerDeclarations);
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testPlayerOneAwardForTwoDeclarationsButOnlyOneVictory () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims(playerDeclarations);
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerZeroAwardForTwoDeclarationsButNoVictories () throws PWCGException
    {   
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims(playerDeclarations);
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testPlayerMoreDeclarationsThanVictories () throws PWCGException
    {   
        createPlayerDeclarations(2);
        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims(playerDeclarations);
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }

}
