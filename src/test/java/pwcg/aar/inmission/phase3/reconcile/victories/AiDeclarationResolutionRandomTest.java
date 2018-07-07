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
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.aar.prelim.PwcgMissionDataEvaluator;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class AiDeclarationResolutionRandomTest
{
    @Mock
    private List<LogVictory> confirmedAiVictories = new ArrayList<LogVictory>();

    @Mock
    private AARMissionEvaluationData evaluationData;

    @Mock
    private Campaign campaign;

    @Mock
    private Squadron squadron;

    @Mock
    private CampaignPersonnelManager personnelManager;
    
    @Mock
    private AARContext aarContext;

    @Mock 
    private AARPreliminaryData preliminaryData;
    
    private SquadronMembers campaignMembersInmission = new SquadronMembers();

    @Mock
    private PwcgMissionDataEvaluator pwcgMissionDataEvaluator;
    
    @Mock
    private VictorySorter victorySorter;
    
    @Mock
    private SquadronMember player;
    
    @Mock
    private SquadronMember aiSquadMember;
        
    private List<LogVictory> randomVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        

    private LogPlane playerVictor = new LogPlane();
    private LogPlane aiVictor = new LogPlane();

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);

        randomVictories.clear();
        campaignMembersInmission.clear();

        playerVictor.setPilotSerialNumber(SerialNumber.PLAYER_SERIAL_NUMBER);
        aiVictor.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);

        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1000, UnknownVictoryAssignments.RANDOM_ASSIGNMENT);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001, UnknownVictoryAssignments.UNKNOWN_ASSIGNMENT);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002, UnknownVictoryAssignments.RANDOM_ASSIGNMENT);
        
        Mockito.when(victorySorter.getFirmAirVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFirmBalloonVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFuzzyAirVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getAllUnconfirmed()).thenReturn(randomVictories);
        Mockito.when(campaign.getPlayer()).thenReturn(player);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(campaign.determineSquadron()).thenReturn(squadron);
        Mockito.when(squadron.getSquadronId()).thenReturn(Squadron.REPLACEMENT);

        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_SERIAL_NUMBER);
        Mockito.when(aiSquadMember.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);

        Mockito.when(aarContext.getMissionEvaluationData()).thenReturn(evaluationData);
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInmission);
    }

    private void createVictory(Integer victimSerialNumber, UnknownVictoryAssignments unknownVictoryAssignment)
    {        
        LogPlane victim = new LogPlane();
        victim.setPilotSerialNumber(victimSerialNumber);
        
        LogVictory resultVictory = new LogVictory();
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);

        LogUnknown unknownVictor = new LogUnknown();
        unknownVictor.setUnknownVictoryAssignment(unknownVictoryAssignment);
        resultVictory.setVictor(unknownVictor);
        randomVictories.add(resultVictory);
    }
    
    @Test
    public void testAiRandomVictoryAward () throws PWCGException
    {   
        campaignMembersInmission.addSquadronMember(player);
        campaignMembersInmission.addSquadronMember(aiSquadMember);

        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.PLAYER_SERIAL_NUMBER)).thenReturn(playerVictor);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.PLAYER_SERIAL_NUMBER)).thenReturn(player);

        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictor);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiSquadMember);
        
        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        assert (confirmedAiVictories.getConfirmedVictories().size() == 1);
    }

    @Test
    public void testAiRandomVictoryAwardFailedBecusePlaneNotFound () throws PWCGException
    {   
        campaignMembersInmission.addSquadronMember(player);

        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_SERIAL_NUMBER);
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.PLAYER_SERIAL_NUMBER)).thenReturn(playerVictor);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.PLAYER_SERIAL_NUMBER)).thenReturn(player);

        Mockito.when(aiSquadMember.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictor);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(null);
        
        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        assert (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }

}
