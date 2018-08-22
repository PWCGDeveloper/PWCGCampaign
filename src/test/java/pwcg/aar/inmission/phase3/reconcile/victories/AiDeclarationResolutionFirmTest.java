package pwcg.aar.inmission.phase3.reconcile.victories;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.phase2.logeval.AARMissionEvaluationData;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignData;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AiDeclarationResolutionFirmTest
{
    private static final String PLAYER_NAME = "Pilot Player";

    @Mock private List<LogVictory> confirmedAiVictories = new ArrayList<LogVictory>();
    @Mock private AARMissionEvaluationData evaluationData;
    @Mock private Campaign campaign;
    @Mock private CampaignData campaignData;
    @Mock private AARContext aarContext;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    @Mock private VictorySorter victorySorter;
    @Mock private SquadronMember player;
    @Mock private SquadronMember ai;
        
    private List<LogVictory> firmVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        
    private List<SquadronMember> players = new ArrayList<>();

    private LogPlane playerVictor = new LogPlane(1);
    private LogPlane aiVictor = new LogPlane(2);

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        
        firmVictories.clear();
        
        Mockito.when(aarContext.getMissionEvaluationData()).thenReturn(evaluationData);
        Mockito.when(evaluationData.getVictoryResults()).thenReturn(firmVictories);   

        playerVictor.setPilotSerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        playerVictor.setPlaneSerialNumber(SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 1);
        aiVictor.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        aiVictor.setPlaneSerialNumber(SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 2);

        createVictory(playerVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1000);
        createVictory(aiVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(aiVictor, SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
    }

    private void createVictory(LogPlane victor, Integer victimSerialNumber) throws PWCGException
    {        
        LogPlane victim = new LogPlane(3);
        victim.setPilotSerialNumber(victimSerialNumber);
        victim.setPlaneSerialNumber(SerialNumber.PLANE_STARTING_SERIAL_NUMBER + 100);
        
        LogVictory resultVictory = new LogVictory(10);
        resultVictory.setVictor(victor);
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);
        firmVictories.add(resultVictory);
        
        players = new ArrayList<>();
        players.add(player);

        Mockito.when(victorySorter.getFirmAirVictories()).thenReturn(firmVictories);
        Mockito.when(victorySorter.getFirmBalloonVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFuzzyAirVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getAllUnconfirmed()).thenReturn(emptyList);
        Mockito.when(campaign.getPlayers()).thenReturn(players);
        Mockito.when(campaign.getCampaignData()).thenReturn(campaignData);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(campaignData.getName()).thenReturn(PLAYER_NAME);

        Mockito.when(player.isPlayer()).thenReturn(true);
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(ai.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
    }
    
    @Test
    public void testAiFirmVictoryAward () throws PWCGException
    {   
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(playerVictor);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);

        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictor);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(ai);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(ai);
        
        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        assert (confirmedAiVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testAiFirmBalloonAward () throws PWCGException
    {   
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(playerVictor);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);

        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictor);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(ai);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(ai);
        
        Mockito.when(victorySorter.getFirmAirVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFirmBalloonVictories()).thenReturn(firmVictories);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        assert (confirmedAiVictories.getConfirmedVictories().size() == 2);
    }

    @Test
    public void testAiFirmVictoryAwardFailedBecusePlaneNotFound () throws PWCGException
    {   
        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER);
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(playerVictor);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.PLAYER_STARTING_SERIAL_NUMBER)).thenReturn(player);

        Mockito.when(ai.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictor);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(null);
        Mockito.when(personnelManager.getAnyCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(null);
        
        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        assert (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }

}
