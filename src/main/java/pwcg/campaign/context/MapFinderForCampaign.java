package pwcg.campaign.context;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class MapFinderForCampaign
{

    public static FrontMapIdentifier findMapForCampaign(Campaign campaign) throws PWCGException
    {
        if (campaign != null)
        {
            SquadronMembers players = campaign.getPersonnelManager().getAllActivePlayers();
            for (SquadronMember player : players.getSquadronMemberList())
            {
                Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
                List<FrontMapIdentifier> mapIdentifiers = MapForAirfieldFinder.getMapForAirfield(squadron.determineCurrentAirfieldName(campaign.getDate()));
                if (mapIdentifiers.size() > 0)
                {
                    return mapIdentifiers.get(0);
                }
            }
        }
        return null;
    }
}
