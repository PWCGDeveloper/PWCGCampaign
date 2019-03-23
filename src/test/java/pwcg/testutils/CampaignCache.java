package pwcg.testutils;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionHumanParticipants;

public class CampaignCache
{
    private static ICampaignCache rofCampaignCache = new CampaignCacheRoF();
    private static ICampaignCache bosCampaignCache = new CampaignCacheBoS();
    
    public static Campaign makeCampaign(SquadrontTestProfile campaignProfile) throws PWCGException
    {
        if (PWCGContextManager.isRoF())
        {
            return rofCampaignCache.makeCampaign(campaignProfile);
        }
        else
        {
            return bosCampaignCache.makeCampaign(campaignProfile);
        }
    }
    
    public static Campaign makeCampaignForceCreation(SquadrontTestProfile campaignProfile) throws PWCGException
    {
        if (PWCGContextManager.isRoF())
        {
            return rofCampaignCache.makeCampaignForceCreation(campaignProfile);
        }
        else
        {
            return bosCampaignCache.makeCampaignForceCreation(campaignProfile);
        }
    }
    
    public static MissionHumanParticipants buildParticipatingPlayers(SquadrontTestProfile campaignProfile) throws PWCGException
    {
        MissionHumanParticipants participatingPlayers = new MissionHumanParticipants();
        Campaign campaign = makeCampaignForceCreation(campaignProfile);
        SquadronMembers players = campaign.getPersonnelManager().getAllPlayers();
        for (SquadronMember player : players.getSquadronMemberList())
        {
            participatingPlayers.addSquadronMember(player);
        }
        return participatingPlayers;
    }

}
