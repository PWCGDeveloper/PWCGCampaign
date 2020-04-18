package pwcg.mission;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.exception.PWCGException;
import pwcg.testutils.CampaignCache;
import pwcg.testutils.SquadronTestProfile;

@RunWith(MockitoJUnitRunner.class)
public class MissionCenterDistanceCalculatorTest
{
    Campaign campaign;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }
    
    @Test
    public void verifySmallerDistanceToFront () throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.JG_51_PROFILE_MOSCOW);
        
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        SquadronMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        for (SquadronMember player : players.getSquadronMemberList())
        {
            participatingPlayers.addSquadronMember(player);
        }
        
        MissionCenterDistanceCalculator missionCenterDistanceCalculator = new MissionCenterDistanceCalculator(campaign, participatingPlayers);
        int maxDistanceForMissionCenter = missionCenterDistanceCalculator.determineMaxDistanceForMissionCenter();

        int missionCenterMinDistanceFromBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxMinDistanceFromBaseKey) * 1000;
        int missionCenterMaxDistanceFromBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxMaxDistanceFromBaseKey) * 1000;
        assert(maxDistanceForMissionCenter >= missionCenterMinDistanceFromBase);
        assert(maxDistanceForMissionCenter <= missionCenterMaxDistanceFromBase);
    }
    
    
    @Test
    public void verifyUseOfRangeIfMaxIsTooLarge () throws PWCGException
    {
        campaign = CampaignCache.makeCampaign(SquadronTestProfile.KG53_PROFILE);
        
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        SquadronMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        for (SquadronMember player : players.getSquadronMemberList())
        {
            participatingPlayers.addSquadronMember(player);
        }
        
        int wayOutOfRange = 10000000;
        int inRange = 500000;
        campaign.getCampaignConfigManager().setConfigParam(ConfigItemKeys.MissionBoxMaxDistanceFromBaseKey, "" + wayOutOfRange);
        MissionCenterDistanceCalculator missionCenterDistanceCalculator = new MissionCenterDistanceCalculator(campaign, participatingPlayers);
        int maxDistanceForMissionCenter = missionCenterDistanceCalculator.determineMaxDistanceForMissionCenter();

        int missionCenterMinDistanceFromBase = campaign.getCampaignConfigManager().getIntConfigParam(ConfigItemKeys.MissionBoxMinDistanceFromBaseKey) * 1000;
        assert(maxDistanceForMissionCenter >= missionCenterMinDistanceFromBase);
        assert(maxDistanceForMissionCenter <= inRange);
    }
}
