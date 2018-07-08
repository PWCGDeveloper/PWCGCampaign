package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.prelim.AARPreliminaryData;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.CampaignCacheRoF;

@RunWith(MockitoJUnitRunner.class)
public class OutOfMissionVictoryEventHandlerTest
{
    private Campaign campaign;

    @Mock
    private SquadronMember squadronMember;
    
    @Mock
    private AARContext aarContext;

    @Mock
    private AARPreliminaryData preliminaryData;

    private SquadronMembers outOfMissionSquadronMembers;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        campaign = CampaignCache.makeCampaign(CampaignCacheRoF.ESC_103_PROFILE);
        
        Mockito.when(squadronMember.determineService(Matchers.<Date>any())).thenReturn(campaign.determineSquadron().determineServiceForSquadron(campaign.getDate()));
        Mockito.when(squadronMember.determineSquadron()).thenReturn(campaign.determineSquadron());
        Mockito.when(aarContext.getPreliminaryData()).thenReturn(preliminaryData);
        
        outOfMissionSquadronMembers = new SquadronMembers();
        outOfMissionSquadronMembers.setSquadronMemberCollection(campaign.getPersonnelManager().getSquadronPersonnel(501012).getActiveSquadronMembers().getSquadronMemberCollection());
        Mockito.when(preliminaryData.getCampaignMembersOutOfMission()).thenReturn(outOfMissionSquadronMembers);
    }

    @Test
    public void testOutOfMissionVictoriesAwardedForVictories () throws PWCGException
    {     
        Mockito.when(squadronMember.getAiSkillLevel()).thenReturn(AiSkillLevel.ACE);
        OutOfMissionVictoryEventHandler victoryGenerator = new OutOfMissionVictoryEventHandler(campaign, aarContext);
        
        int outOfMissionVictoriesAwarded = 0;
        for (int i = 0; i < 1000; ++i)
        {
            OutOfMissionVictoryData victoriesOutOMission = victoryGenerator.generateOutOfMissionVictories();
            if (!victoriesOutOMission.getVictoryAwardsBySquadronMember().isEmpty())
            {
                for (List<Victory> victories : victoriesOutOMission.getVictoryAwardsBySquadronMember().values())
                {
                    outOfMissionVictoriesAwarded += victories.size();
                    if (outOfMissionVictoriesAwarded > 1)
                    {
                        break;
                    }
                }
            }
        }

        assert (outOfMissionVictoriesAwarded > 1);
    }

    
    @Test
    public void testPilotOutOfMissionVictoryNovice() throws PWCGException
    {
        Mockito.when(squadronMember.getAiSkillLevel()).thenReturn(AiSkillLevel.NOVICE);
        testPilotOutOfMissionVictory();
    }
    
    @Test
    public void testPilotOutOfMissionVictoryCommon() throws PWCGException
    {
        Mockito.when(squadronMember.getAiSkillLevel()).thenReturn(AiSkillLevel.COMMON);
        testPilotOutOfMissionVictory();
    }
    
    @Test
    public void testPilotOutOfMissionVictoryVeteran() throws PWCGException
    {
        Mockito.when(squadronMember.getAiSkillLevel()).thenReturn(AiSkillLevel.VETERAN);
        testPilotOutOfMissionVictory();
    }
    
    @Test
    public void testPilotOutOfMissionVictoryAce() throws PWCGException
    {
        Mockito.when(squadronMember.getAiSkillLevel()).thenReturn(AiSkillLevel.ACE);
        testPilotOutOfMissionVictory();
    }
    
    public void testPilotOutOfMissionVictory() throws PWCGException
    {
        OutOfMissionVictoryEventHandler victoryGenerator = new OutOfMissionVictoryEventHandler(campaign, aarContext);
        victoryGenerator.outOfMissionVictoriesForSquadronMember(squadronMember);        
        PilotVictoryRunner pilotVictoryRunner = new PilotVictoryRunner(victoryGenerator);
        pilotVictoryRunner.testPilotOutOfMissionLostAllPossibleStatus(squadronMember);
    }


}
