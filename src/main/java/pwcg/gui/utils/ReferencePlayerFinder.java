package pwcg.gui.utils;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CoopHostUserBuilder;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.io.json.CoopPilotIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.coop.model.CoopPilot;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;

public class ReferencePlayerFinder
{
    public static SquadronMember findReferencePlayer(Campaign campaign)
    {
        SquadronMember representativePlayer = null;
        try
        {
            if (campaign.isCoop())
            {
                representativePlayer = getHostPlayer(campaign);
            }
            else
            {
                representativePlayer = getActivePlayer(campaign);
            }

            if (representativePlayer == null)
            {
                representativePlayer = getDeadPlayer(campaign);
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
        }

        return representativePlayer;
    }

    public static Squadron getRepresentativeSquadronForCampaign(Campaign campaign) throws PWCGException
    {
        SquadronMember representativePlayer = findReferencePlayer(campaign);
        Squadron representativePlayerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(representativePlayer.getSquadronId());
        return representativePlayerSquadron;
    }

    private static SquadronMember getHostPlayer(Campaign campaign) throws PWCGException
    {
        List<CoopPilot> coopPilots = CoopPilotIOJson.readCoopPilots();
        for (CoopPilot coopPilot : coopPilots)
        {
            for (SquadronMember player : campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
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

        return null;
    }

    private static SquadronMember getActivePlayer(Campaign campaign) throws PWCGException
    {
        SquadronMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        SquadronMember representativePlayer = null;
        if (players.getSquadronMemberList().size() > 0)
        {
            representativePlayer = players.getSquadronMemberList().get(0);
        }
        return representativePlayer;
    }

    private static SquadronMember getDeadPlayer(Campaign campaign) throws PWCGException
    {
        SquadronMember deadePlayer = null;
        SquadronMembers deadPlayers = campaign.getPersonnelManager().getDeadPlayers();
        if (deadPlayers.getSquadronMemberList().size() > 0)
        {
            deadePlayer = deadPlayers.getSquadronMemberList().get(0);
        }
        return deadePlayer;
    }
}
