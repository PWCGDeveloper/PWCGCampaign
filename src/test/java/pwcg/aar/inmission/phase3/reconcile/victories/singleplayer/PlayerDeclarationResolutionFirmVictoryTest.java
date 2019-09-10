package pwcg.aar.inmission.phase3.reconcile.victories.singleplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.inmission.phase3.reconcile.victories.common.ConfirmedVictories;
import pwcg.aar.inmission.phase3.reconcile.victories.common.VictorySorter;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignData;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.ww1.country.RoFCountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class PlayerDeclarationResolutionFirmVictoryTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignData campaignData;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private PlayerVictoryDeclaration mockPlayerDeclaration;
    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private VictorySorter victorySorter;
    @Mock private SquadronMembers playerMembers;
    @Mock private SquadronMember player;
    @Mock private SquadronMember ai;
    @Mock private PlaneTypeFactory planeFactory;
    
    private Map<Integer, PlayerDeclarations> playerDeclarations = new HashMap<>();
    private PlayerDeclarations playerDeclarationSet;

    private List<LogVictory> firmVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        
    private List<SquadronMember> players = new ArrayList<>();

    private LogPlane playerVictor = new LogPlane(1);
    private LogPlane aiVictor = new LogPlane(2);
    
    private static String PLAYER_NAME = "Player Name";

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        
        firmVictories.clear();
        
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAllActivePlayers()).thenReturn(playerMembers);   
        Mockito.when(playerMembers.getSquadronMemberList()).thenReturn(players);   

        playerVictor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        playerVictor.setCountry(new RoFCountry(Country.FRANCE));
        
        aiVictor.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        aiVictor.setCountry(new RoFCountry(Country.FRANCE));
        
        createMocks();
    }
    
    private void createMocks() throws PWCGException
    {
        players = new ArrayList<>();
        players.add(player);

        Mockito.when(victorySorter.getFirmAirVictories()).thenReturn(firmVictories);
        Mockito.when(victorySorter.getFirmBalloonVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFuzzyAirVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getAllUnconfirmed()).thenReturn(emptyList);
        Mockito.when(campaign.getCampaignData()).thenReturn(campaignData);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getBeginningOfWar());
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(campaignData.getName()).thenReturn(PLAYER_NAME);

        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(ai);

        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(playerVictor);
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictor);

        Mockito.when(player.isPlayer()).thenReturn(true);
        Mockito.when(player.getCountry()).thenReturn(Country.FRANCE);
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(ai.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
    }
    
    @Test
    public void testPlayerFirmVictoryAward () throws PWCGException
    {   
        
        createPlayerDeclarations(1);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerMultipleFirmVictoryAwards () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testPlayerOneAwardForTwoDeclarationsButOnlyOneVictory () throws PWCGException
    {   
        
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testPlayerZeroAwardForTwoDeclarationsButNoVictories () throws PWCGException
    {   
        createPlayerDeclarations(2);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1003);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testPlayerMoreDeclarationsThanVictories () throws PWCGException
    {   
        createPlayerDeclarations(2);
        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testNoFriendlyVictories () throws PWCGException
    {   
        
        createPlayerDeclarations(1);
        createFriendlyVictory(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);

        PlayerDeclarationResolution declarationResolution = new PlayerDeclarationResolution(campaign, evaluationData, victorySorter, playerDeclarations);
        ConfirmedVictories confirmedPlayerVictories = declarationResolution.determinePlayerAirResultsWithClaims();
        
        assert (confirmedPlayerVictories.getConfirmedVictories().size() == 0);
    }


    private void createVictory(Integer victorSerialNumber, Integer victimSerialNumber)
    {        
        LogPlane victim = new LogPlane(3);
        victim.setPilotSerialNumber(victimSerialNumber);
        victim.setVehicleType("albatrosd3");
        victim.setCountry(new RoFCountry(Country.GERMANY));

        LogPlane victor = new LogPlane(4);
        victor.setVehicleType("spad7");
        victor.setPilotSerialNumber(victorSerialNumber);
        victor.setCountry(new RoFCountry(Country.FRANCE));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);
        
        firmVictories.add(resultVictory);
    }

    private void createFriendlyVictory(Integer victorSerialNumber, Integer victimSerialNumber)
    {        
        LogPlane victim = new LogPlane(3);
        victim.setPilotSerialNumber(victimSerialNumber);
        victim.setVehicleType("spad7");
        victim.setCountry(new RoFCountry(Country.FRANCE));

        LogPlane victor = new LogPlane(4);
        victor.setVehicleType("spad7");
        victor.setPilotSerialNumber(victorSerialNumber);
        victor.setCountry(new RoFCountry(Country.FRANCE));

        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);
        
        firmVictories.add(resultVictory);
    }

    private void createPlayerDeclarations(int numDeclarations) throws PWCGException
    {
        playerDeclarationSet = new PlayerDeclarations();
        for (int i = 0; i < numDeclarations; ++i)
        {
            PlayerVictoryDeclaration declaration = new PlayerVictoryDeclaration();
            declaration.confirmDeclaration(true, "albatrosd3");
            playerDeclarationSet.addDeclaration(declaration);
        }        
        
        playerDeclarations.clear();
        playerDeclarations.put(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER, playerDeclarationSet);
    }

}
