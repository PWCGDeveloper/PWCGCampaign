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
            Squadron representativePlayerSquadron = getRepresentativeSquadronForCampaign(campaign);
            List<FrontMapIdentifier> mapIdentifiers = MapForAirfieldFinder.getMapForAirfield(representativePlayerSquadron.determineCurrentAirfieldName(campaign.getDate()));
            if (mapIdentifiers.size() > 0)
            {
                return mapIdentifiers.get(0);
            }
        }
        return null;
    }

    private static Squadron getRepresentativeSquadronForCampaign(Campaign campaign) throws PWCGException 
    {
        SquadronMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        SquadronMember representativePlayer = players.getSquadronMemberList().get(0);
        Squadron representativePlayerSquadron = PWCGContext.getInstance().getSquadronManager().getSquadron(representativePlayer.getSquadronId());
        return representativePlayerSquadron;
    }
}
