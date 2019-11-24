package pwcg.gui.utils;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.coop.CoopPersonaManager;
import pwcg.coop.model.CoopPersona;
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
                representativePlayer = getHostPersoForCoopCampaign(campaign);
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

    public static SquadronMember getHostPersoForCoopCampaign(Campaign campaign) throws PWCGException
    {
        for (CoopPersona coopPersona : CoopPersonaManager.getIntance().getHostPersonaForCampaign(campaign))
        {
            for (SquadronMember player : campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
            {
                if (coopPersona.getPilotName().equals(player.getName()))
                {
                    return player;
                }
            }
        }
    
        return null;
    }

    public static Squadron getRepresentativeSquadronForCampaign(Campaign campaign) throws PWCGException
    {
        SquadronMember representativePlayer = findReferencePlayer(campaign);
        Squadron representativePlayerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(representativePlayer.getSquadronId());
        return representativePlayerSquadron;
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
