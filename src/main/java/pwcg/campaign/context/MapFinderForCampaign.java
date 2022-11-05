package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class MapFinderForCampaign
{

    public static FrontMapIdentifier findMapForCampaign(Campaign campaign) throws PWCGException
    {
        FrontMapIdentifier mapIdentifier = FrontMapIdentifier.NO_MAP;
        if (campaign != null)
        {
            mapIdentifier = findMapByActivePlayer(campaign);
            if (mapIdentifier != FrontMapIdentifier.NO_MAP)
            {
                return mapIdentifier;
            }
            
            mapIdentifier = findMapByInactivePlayer(campaign);
            if (mapIdentifier != FrontMapIdentifier.NO_MAP)
            {
                return mapIdentifier;
            }
            
            
        }
        return mapIdentifier;
    }

    public static FrontMapIdentifier findMapForSquadeonAndDate(Squadron squadron, Date date) throws PWCGException
    {
        FrontMapIdentifier mapIdentifier = FrontMapIdentifier.NO_MAP;

        List<FrontMapIdentifier> mapIdentifiers = MapForAirfieldFinder.getMapForAirfield(squadron.determineCurrentAirfieldName(date));
        if (mapIdentifiers.size() > 0)
        {
            mapIdentifier = mapIdentifiers.get(0);
            mapIdentifier = StalingradMapResolver.resolveStalingradMap(date, mapIdentifier);
            return mapIdentifier;
        }
        
        return mapIdentifier;
    }

    private static FrontMapIdentifier findMapByActivePlayer(Campaign campaign) throws PWCGException
    {
        SquadronMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        FrontMapIdentifier mapIdentifier = findMapByPlayers(campaign, players.getSquadronMemberList());
        return mapIdentifier;
    }

    private static FrontMapIdentifier findMapByInactivePlayer(Campaign campaign) throws PWCGException
    {
        SquadronMembers players = campaign.getPersonnelManager().getAllPlayers();
        TreeMap<Date, SquadronMember> inactivePlayersSortedByDate = new TreeMap<>(Collections.reverseOrder());
        for (SquadronMember player : players.getSquadronMemberList())
        {
            if (player.getInactiveDate() != null)
            {
                inactivePlayersSortedByDate.put(player.getInactiveDate(), player);
            }
        }
        
        List<SquadronMember> inactivePlayersSortedByDateList = new ArrayList<>();
        for (SquadronMember inactivePlayer : inactivePlayersSortedByDate.values())
        {
            inactivePlayersSortedByDateList.add(inactivePlayer);
        }
        
        FrontMapIdentifier mapIdentifier = findMapByPlayers(campaign, inactivePlayersSortedByDateList);
        return mapIdentifier;
    }

    private static FrontMapIdentifier findMapByPlayers(Campaign campaign, List<SquadronMember> players) throws PWCGException
    {
        FrontMapIdentifier mapIdentifier = FrontMapIdentifier.NO_MAP;
        for (SquadronMember player : players)
        {
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(player.getSquadronId());
            mapIdentifier = findMapForSquadeonAndDate(squadron, campaign.getDate());
            if (mapIdentifier != FrontMapIdentifier.NO_MAP)
            {
                break;
            }
        }
        return mapIdentifier;
    }
}
