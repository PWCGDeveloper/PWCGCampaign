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
public class AiDeclarationResolutionFuzzyTest
{
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
    private SquadronMember aiSquadMember1;
    
    @Mock
    private SquadronMember aiSquadMember2;
    
    @Mock
    private SquadronMember aiNotSquadMember;
    
    private static String PLAYER_NAME = "Player Name";

    private List<LogVictory> fuzzyVictories = new ArrayList<>();        
    private List<LogVictory> emptyList = new ArrayList<>();        

    private LogPlane playerVictor = new LogPlane();
    private LogPlane aiVictorOne = new LogPlane();
    private LogPlane aiVictorTwo = new LogPlane();

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(false);

        fuzzyVictories.clear();
        campaignMembersInmission.clear();
        
        Mockito.when(aarContext.getMissionEvaluationData()).thenReturn(evaluationData);
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        Mockito.when(preliminaryData.getCampaignMembersInMission()).thenReturn(campaignMembersInmission);

        playerVictor.setPilotSerialNumber(SerialNumber.PLAYER_SERIAL_NUMBER);
        aiVictorOne.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        aiVictorTwo.setPilotSerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
        
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1000);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1001);
        createVictory(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1002);
        
        Mockito.when(victorySorter.getFirmAirVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFirmBalloonVictories()).thenReturn(emptyList);
        Mockito.when(victorySorter.getFuzzyAirVictories()).thenReturn(fuzzyVictories);
        Mockito.when(victorySorter.getAllUnconfirmed()).thenReturn(emptyList);
        
        Mockito.when(campaign.getPlayer()).thenReturn(player);
        Mockito.when(campaign.getName()).thenReturn(PLAYER_NAME);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(campaign.determineSquadron()).thenReturn(squadron);
        Mockito.when(campaign.getSquadronId()).thenReturn(501011);
                
        Mockito.when(squadron.getSquadronId()).thenReturn(501011);

        Mockito.when(player.getSerialNumber()).thenReturn(SerialNumber.PLAYER_SERIAL_NUMBER);
        Mockito.when(player.getSquadronId()).thenReturn(501011);
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.PLAYER_SERIAL_NUMBER)).thenReturn(playerVictor);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.PLAYER_SERIAL_NUMBER)).thenReturn(player);

        Mockito.when(aiSquadMember1.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1);
        Mockito.when(aiSquadMember1.getSquadronId()).thenReturn(501011);
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiVictorOne);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(aiSquadMember1);

        Mockito.when(aiSquadMember2.getSerialNumber()).thenReturn(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2);
        Mockito.when(aiSquadMember2.getSquadronId()).thenReturn(501011);
        Mockito.when(evaluationData.getPlaneInMissionBySerialNumber(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2)).thenReturn(aiVictorTwo);
        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 2)).thenReturn(aiSquadMember2);
    }

    private void createVictory(Integer victimSerialNumber) throws PWCGException
    {        
        LogPlane victim = new LogPlane();
        victim.setPilotSerialNumber(victimSerialNumber);
        
        LogVictory resultVictory = new LogVictory();
        resultVictory.setVictim(victim);
        resultVictory.setCrossedPlayerPath(true);
        fuzzyVictories.add(resultVictory);
   }
    
    @Test
    public void testAiOnlyOneFuzzyVictoryAward () throws PWCGException
    {   
        campaignMembersInmission.addSquadronMember(player);
        campaignMembersInmission.addSquadronMember(aiSquadMember1);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);

        assert (confirmedAiVictories.getConfirmedVictories().size() == 1);
    }
    
    @Test
    public void testAiOneFuzzyVictoryPerCrewAward () throws PWCGException
    {   
        campaignMembersInmission.addSquadronMember(player);
        campaignMembersInmission.addSquadronMember(aiSquadMember1);
        campaignMembersInmission.addSquadronMember(aiSquadMember2);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        assert (confirmedAiVictories.getConfirmedVictories().size() == 2);
    }
    
    @Test
    public void testAiFuzzyVictoryAwardFailedBecuseNoCrewOtherThanPlayer () throws PWCGException
    {   
        campaignMembersInmission.addSquadronMember(player);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        assert (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testAiFuzzyVictoryAwardFailedBecuseAllConfirmed () throws PWCGException
    {   
        campaignMembersInmission.addSquadronMember(player);
        campaignMembersInmission.addSquadronMember(aiSquadMember1);
        campaignMembersInmission.addSquadronMember(aiSquadMember2);

        for (LogVictory fuzzyVictory : fuzzyVictories)
        {
            fuzzyVictory.setConfirmed(true);
        }
        
        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        assert (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }
    
    @Test
    public void testAiFuzzyVictoryAwardFailedBecuseVictorIsNotSquadronMember () throws PWCGException
    {   
        campaignMembersInmission.addSquadronMember(player);
        campaignMembersInmission.addSquadronMember(aiSquadMember1);
        campaignMembersInmission.addSquadronMember(aiSquadMember2);

        Mockito.when(aiSquadMember1.getSquadronId()).thenReturn(501004);
        Mockito.when(aiSquadMember2.getSquadronId()).thenReturn(501004);

        Mockito.when(personnelManager.getActiveCampaignMember(SerialNumber.AI_STARTING_SERIAL_NUMBER + 1)).thenReturn(null);

        AiDeclarationResolver declarationResolution = new AiDeclarationResolver(campaign, aarContext);
        ConfirmedVictories confirmedAiVictories = declarationResolution.determineAiAirResults(victorySorter);
        
        assert (confirmedAiVictories.getConfirmedVictories().size() == 0);
    }
}
