package pwcg.campaign.personnel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.RankFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class CompanyPersonnel
{
    private Campaign campaign;
    private Company squadron;
    private CrewMembers squadronMembers = new CrewMembers();

    public CompanyPersonnel (Campaign campaign, Company squadron)
    {
        this.campaign = campaign;
        this.squadron = squadron;
    }

    public void setCrewMembers(CrewMembers squadronMembers)
    {
        this.squadronMembers = squadronMembers;
    }

    public void addCrewMember(CrewMember crewMember) throws PWCGException
    {
        squadronMembers.addToCrewMemberCollection(crewMember);
    }

    public void removeCrewMember(CrewMember crewMember) throws PWCGException
    {
        if (!squadronMembers.isCrewMember(crewMember.getSerialNumber()))
        {
            throw new PWCGException("Not member of squadron");
        }
        squadronMembers.removeCrewMember(crewMember.getSerialNumber());
    }

    public boolean isSquadronPersonnelViable() throws PWCGException
    {
        int vialbleCrewMembers = squadronMembers.getActiveCount(campaign.getDate());
        if (vialbleCrewMembers > (Company.COMPANY_STAFF_SIZE / 2))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public CrewMember getCrewMember(Integer serialNumber) throws PWCGException
    {
        return squadronMembers.getCrewMemberCollection().get(serialNumber);
    }

    public boolean isActiveCrewMember(Integer serialNumber) throws PWCGException
    {
        List<CrewMember> squadronMembersList = new ArrayList<>();
        squadronMembersList.addAll(squadronMembers.getCrewMemberCollection().values());
        for (CrewMember crewMember : squadronMembersList)
        {
            if (crewMember.getSerialNumber() == serialNumber)
            {
                if (crewMember.getCrewMemberActiveStatus() == CrewMemberStatus.STATUS_ACTIVE)
                {
                	return true;
                }
            }
        }
        
        CrewMember crewMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
        if (crewMember != null)
        {
            if (crewMember.getCompanyId() == squadron.getCompanyId())
            {
                return true;
            }
        }

        return false;
    }

    public CrewMembers getActiveAiCrewMembers() throws PWCGException
    {
        CrewMembers campaignMembers = getCrewMembersWithAces();
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAIAndAcesNoWounded(campaignMembers.getCrewMemberCollection(), campaign.getDate());
        return activeCrewMembers;
    }

    public CrewMembers getRecentlyInactiveCrewMembers() throws PWCGException
    {
        CrewMembers campaignMembers = getCrewMembersWithAces();
        CrewMembers inactiveCrewMembers = CrewMemberFilter.filterInactiveAIAndPlayerAndAces(campaignMembers.getCrewMemberCollection(), campaign.getDate());
        CrewMembers recentlyInactiveCrewMembers = new CrewMembers();
        for (CrewMember crewMember : inactiveCrewMembers.getCrewMemberList())
        {
            Date oneWeekAgo = DateUtils.removeTimeDays(campaign.getDate(), 7);
            if (crewMember.getInactiveDate().after(oneWeekAgo))
            {
                recentlyInactiveCrewMembers.addToCrewMemberCollection(crewMember);
            }
        }
        
        return recentlyInactiveCrewMembers;
    }

    public Company getSquadron()
    {
        return squadron;
    }

    public CrewMembers getCrewMembersWithAces() throws PWCGException
    {
        CrewMembers activeCrewMembersAndAces = new CrewMembers();
        for (CrewMember crewMember : squadronMembers.getCrewMemberList())
        {
            activeCrewMembersAndAces.addToCrewMemberCollection(crewMember);
        }

        List<TankAce> aces = campaign.getPersonnelManager().getCampaignAces().getActiveCampaignAcesBySquadron(squadron.getCompanyId());
        for (CrewMember ace : aces)
        {
            activeCrewMembersAndAces.addToCrewMemberCollection(ace);
        }

        return activeCrewMembersAndAces;
    }
    
    public boolean isPlayerSquadron()
    {
        for (CrewMember crewMember : squadronMembers.getCrewMemberList())
        {
            if (crewMember.isPlayer())
            {
                return true;
            }
        }

        return false;
    }

    public CrewMembers getCrewMembers()
    {
        return squadronMembers;
    }

    public CrewMembers getPlayersByStatus(int status)
    {
        CrewMembers activePlayers = new CrewMembers();
        for (CrewMember player : getPlayers().getCrewMemberList())
        {
            if (player.getCrewMemberActiveStatus() >= status)
            {
                activePlayers.addToCrewMemberCollection(player);
            }
        }
        return activePlayers;
    }

	public boolean isPlayerCommander() throws PWCGException {
		CrewMembers players = getPlayersByStatus(CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED);
		for (CrewMember player : players.getCrewMemberList())
		{
            IRankHelper rank = RankFactory.createRankHelper();
			if (rank.isCommandRank(player.getRank(), player.determineService(campaign.getDate())))
			{
				return true;
			}
		}
		return false;
	}

    public CrewMembers getPlayers()
    {
        CrewMembers players = new CrewMembers();
        for (CrewMember crewMember : squadronMembers.getCrewMemberList())
        {
            if (crewMember.isPlayer())
            {
                players.addToCrewMemberCollection(crewMember);
            }
        }
        return players;
    }
}
