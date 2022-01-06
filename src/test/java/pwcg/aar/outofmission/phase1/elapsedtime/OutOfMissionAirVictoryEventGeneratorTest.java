package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.Victory;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OutOfMissionAirVictoryEventGeneratorTest
{
    private Campaign campaign;
    private static CrewMember crewMember;
    
    @Mock private ArmedService service;

    @BeforeAll
    public void setupSuite() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE);
        
        for (CrewMember crewMember : campaign.getPersonnelManager().getCompanyPersonnel(SquadronTestProfile.GROSS_DEUTSCHLAND_PROFILE.getSquadronId()).getActiveAiCrewMembers().getCrewMemberList())
        {
            if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE && !crewMember.isPlayer())
            {
                crewMember = crewMember;
                break;
            }
        }
    }
    
    @Test
    public void testCrewMemberOutOfMissionVictoryNovice() throws PWCGException
    {
        testCrewMemberOutOfMissionVictory(AiSkillLevel.NOVICE);
    }
    
    @Test
    public void testCrewMemberOutOfMissionVictoryCommon() throws PWCGException
    {
        testCrewMemberOutOfMissionVictory(AiSkillLevel.COMMON);
    }
    
    @Test
    public void testCrewMemberOutOfMissionVictoryVeteran() throws PWCGException
    {
        testCrewMemberOutOfMissionVictory(AiSkillLevel.VETERAN);
    }
    
    @Test
    public void testCrewMemberOutOfMissionVictoryAce() throws PWCGException
    {
        testCrewMemberOutOfMissionVictory(AiSkillLevel.ACE);
    }
    
    public void testCrewMemberOutOfMissionVictory(AiSkillLevel aiSkillLevel) throws PWCGException
    {
        crewMember.setAiSkillLevel(aiSkillLevel);
        
        OutOfMissionVictoryData victoryData = new OutOfMissionVictoryData();

        int numMissionsInWar = 1000;
        for (int j = 0; j < numMissionsInWar; ++j)
        {
            OutOfMissionAirVictoryEventGenerator victoryGenerator = new OutOfMissionAirVictoryEventGenerator(campaign, crewMember);
            OutOfMissionVictoryData victoriesOutOMission = victoryGenerator.outOfMissionVictoriesForCrewMember();    
            victoryData.merge(victoriesOutOMission);
        }
        
        assert(victoryData.getVictoryAwardsByCrewMember().size() > 0);
        
        List<Victory> squadronMemberVictories = victoryData.getVictoryAwardsByCrewMember().get(crewMember.getSerialNumber());
        assert(squadronMemberVictories.size() > 0);
        
        Victory victory = squadronMemberVictories.get(0);
        assert(victory.getVictor().getCrewMemberSerialNumber() == crewMember.getSerialNumber());
        assert(victory.getVictim().getCrewMemberSerialNumber() > 0);
    }

}
