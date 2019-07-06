package pwcg.gui.utils;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CoopHostUserBuilder;
import pwcg.campaign.io.json.CoopPilotIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.model.CoopPilot;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;

public class ReferencePlayerFinder
{
    public static SquadronMember findReferencePlayer(Campaign campaign)
    {
        try
        {
            if (campaign.getCampaignData().isCoop())
            {
                return getHostPlayer(campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList());
            }
            else
            {
                return campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList().get(0);
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
        }

        return null;
    }
    
    private static SquadronMember getHostPlayer(List<SquadronMember> players) throws PWCGException
    {
        List<CoopPilot> coopPilots = CoopPilotIOJson.readCoopPilots();
        for (CoopPilot coopPilot : coopPilots)
        {
            for (SquadronMember player : players)
            {
                if (coopPilot.getUsername().equals(CoopHostUserBuilder.HOST_USER_NAME))
                {
                    if (coopPilot.getPilotName().equals(player.getName()))
                    {
                        return player;
                    }
                }
            }
        }

        return players.get(0);
    }
}
