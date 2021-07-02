package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.Victory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class OutOfMissionAirVictoryEventGeneratorTest
{
    private Campaign campaign;
    private SquadronMember squadronMember;
    
    @Mock private ArmedService service;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_STALINGRAD);
        
        for (SquadronMember pilot : campaign.getPersonnelManager().getSquadronPersonnel(SquadronTestProfile.JG_51_PROFILE_STALINGRAD.getSquadronId()).getActiveAiSquadronMembers().getSquadronMemberList())
        {
            if (pilot.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE && !pilot.isPlayer())
            {
                squadronMember = pilot;
                break;
            }
        }
    }
    
    @Test
    public void testPilotOutOfMissionVictoryNovice() throws PWCGException
    {
        testPilotOutOfMissionVictory(AiSkillLevel.NOVICE);
    }
    
    @Test
    public void testPilotOutOfMissionVictoryCommon() throws PWCGException
    {
        testPilotOutOfMissionVictory(AiSkillLevel.COMMON);
    }
    
    @Test
    public void testPilotOutOfMissionVictoryVeteran() throws PWCGException
    {
        testPilotOutOfMissionVictory(AiSkillLevel.VETERAN);
    }
    
    @Test
    public void testPilotOutOfMissionVictoryAce() throws PWCGException
    {
        testPilotOutOfMissionVictory(AiSkillLevel.ACE);
    }
    
    public void testPilotOutOfMissionVictory(AiSkillLevel aiSkillLevel) throws PWCGException
    {
        squadronMember.setAiSkillLevel(aiSkillLevel);
        
        OutOfMissionVictoryData victoryData = new OutOfMissionVictoryData();

        int numMissionsInWar = 1000;
        for (int j = 0; j < numMissionsInWar; ++j)
        {
            OutOfMissionAirVictoryEventGenerator victoryGenerator = new OutOfMissionAirVictoryEventGenerator(campaign, squadronMember);
            OutOfMissionVictoryData victoriesOutOMission = victoryGenerator.outOfMissionVictoriesForSquadronMember();    
            victoryData.merge(victoriesOutOMission);
        }
        
        assert(victoryData.getVictoryAwardsBySquadronMember().size() > 0);
        
        List<Victory> squadronMemberVictories = victoryData.getVictoryAwardsBySquadronMember().get(squadronMember.getSerialNumber());
        assert(squadronMemberVictories.size() > 0);
        
        Victory victory = squadronMemberVictories.get(0);
        assert(victory.getVictor().getPilotSerialNumber() == squadronMember.getSerialNumber());
        assert(victory.getVictim().getPilotSerialNumber() > 0);
    }

}
