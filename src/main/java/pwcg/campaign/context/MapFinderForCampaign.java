package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
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

    private static FrontMapIdentifier findMapByActivePlayer(Campaign campaign) throws PWCGException
    {
        CrewMembers players = campaign.getPersonnelManager().getAllActivePlayers();
        FrontMapIdentifier mapIdentifier = findMapByPlayers(campaign, players.getCrewMemberList());
        return mapIdentifier;
    }

    private static FrontMapIdentifier findMapByInactivePlayer(Campaign campaign) throws PWCGException
    {
        CrewMembers players = campaign.getPersonnelManager().getAllPlayers();
        TreeMap<Date, CrewMember> inactivePlayersSortedByDate = new TreeMap<>(Collections.reverseOrder());
        for (CrewMember player : players.getCrewMemberList())
        {
            if (player.getInactiveDate() != null)
            {
                inactivePlayersSortedByDate.put(player.getInactiveDate(), player);
            }
        }
        
        List<CrewMember> inactivePlayersSortedByDateList = new ArrayList<>();
        for (CrewMember inactivePlayer : inactivePlayersSortedByDate.values())
        {
            inactivePlayersSortedByDateList.add(inactivePlayer);
        }
        
        FrontMapIdentifier mapIdentifier = findMapByPlayers(campaign, inactivePlayersSortedByDateList);
        return mapIdentifier;
    }

    private static FrontMapIdentifier findMapByPlayers(Campaign campaign, List<CrewMember> players) throws PWCGException
    {
        FrontMapIdentifier mapIdentifier = FrontMapIdentifier.NO_MAP;
        for (CrewMember player : players)
        {
            mapIdentifier = findMapForPlayer(campaign, player);
            if (mapIdentifier != FrontMapIdentifier.NO_MAP)
            {
                break;
            }
        }
        return mapIdentifier;
    }

    private static FrontMapIdentifier findMapForPlayer(Campaign campaign, CrewMember player) throws PWCGException
    {
        FrontMapIdentifier mapIdentifier = FrontMapIdentifier.NO_MAP;

        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(player.getCompanyId());
        List<FrontMapIdentifier> mapIdentifiers = MapForAirfieldFinder.getMapForAirfield(squadron.determineCurrentAirfieldName(campaign.getDate()));
        if (mapIdentifiers.size() > 0)
        {
            mapIdentifier = mapIdentifiers.get(0);
            mapIdentifier = StalingradMapResolver.resolveStalingradMap(campaign, mapIdentifier);
            return mapIdentifier;
        }
        
        return mapIdentifier;
    }
}
