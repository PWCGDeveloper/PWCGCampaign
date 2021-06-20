package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class PersonnelOutOfMissionAiStatusHandlerTest
{
    private Campaign campaign;
    
    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.FC);
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.ESC_103_PROFILE);
    }

    @Test
    public void testPersonnelLossesOutOfMission () throws PWCGException
    {     
        PersonnelOutOfMissionStatusHandler personnelLossOutOfMissionHandler = new PersonnelOutOfMissionStatusHandler();
        Map<Integer, SquadronMember> campaignMembers = campaign.getPersonnelManager().getActiveCampaignMembers();
        SquadronMembers squadronMembersInMissionOtherThanPlayer = SquadronMemberFilter.filterActiveAI(campaignMembers, campaign.getDate());
        personnelLossOutOfMissionHandler.determineFateOfShotDownPilots(squadronMembersInMissionOtherThanPlayer.getSquadronMemberCollection());
        
        Map<Integer, SquadronMember> aiKilled = new HashMap<>();
        Map<Integer, SquadronMember> aiMaimed = new HashMap<>();
        Map<Integer, SquadronMember> aiCaptured = new HashMap<>();

        for (int i = 0; i < 10; ++i)
        {
            AARPersonnelLosses outOfMissionPersonnelLosses = personnelLossOutOfMissionHandler.determineFateOfShotDownPilots(campaignMembers);
            aiKilled.putAll(outOfMissionPersonnelLosses.getPersonnelKilled());
            aiMaimed.putAll(outOfMissionPersonnelLosses.getPersonnelMaimed());
            aiCaptured.putAll(outOfMissionPersonnelLosses.getPersonnelCaptured());
        }
        
        assert (aiKilled.size() > 0);
        assert (aiMaimed.size() > 0);
        assert (aiCaptured.size() > 0);
    }

}
