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
    private Company company;
    private CrewMembers companyMembers = new CrewMembers();

    public CompanyPersonnel (Campaign campaign, Company company)
    {
        this.campaign = campaign;
        this.company = company;
    }

    public void setCrewMembers(CrewMembers companyMembers)
    {
        this.companyMembers = companyMembers;
    }

    public void addCrewMember(CrewMember crewMember) throws PWCGException
    {
        companyMembers.addToCrewMemberCollection(crewMember);
    }

    public void removeCrewMember(CrewMember crewMember) throws PWCGException
    {
        if (!companyMembers.isCrewMember(crewMember.getSerialNumber()))
        {
            throw new PWCGException("Not member of company");
        }
        companyMembers.removeCrewMember(crewMember.getSerialNumber());
    }

    public boolean isSquadronPersonnelViable() throws PWCGException
    {
        int vialbleCrewMembers = companyMembers.getActiveCount(campaign.getDate());
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
        return companyMembers.getCrewMemberCollection().get(serialNumber);
    }

    public boolean isActiveCrewMember(Integer serialNumber) throws PWCGException
    {
        List<CrewMember> companyMembersList = new ArrayList<>();
        companyMembersList.addAll(companyMembers.getCrewMemberCollection().values());
        for (CrewMember crewMember : companyMembersList)
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
            if (crewMember.getCompanyId() == company.getCompanyId())
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
        return company;
    }

    public CrewMembers getCrewMembersWithAces() throws PWCGException
    {
        CrewMembers activeCrewMembersAndAces = new CrewMembers();
        for (CrewMember crewMember : companyMembers.getCrewMemberList())
        {
            activeCrewMembersAndAces.addToCrewMemberCollection(crewMember);
        }

        List<TankAce> aces = campaign.getPersonnelManager().getCampaignAces().getActiveCampaignAcesBySquadron(company.getCompanyId());
        for (CrewMember ace : aces)
        {
            activeCrewMembersAndAces.addToCrewMemberCollection(ace);
        }

        return activeCrewMembersAndAces;
    }
    
    public boolean isPlayerSquadron()
    {
        for (CrewMember crewMember : companyMembers.getCrewMemberList())
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
        return companyMembers;
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
        for (CrewMember crewMember : companyMembers.getCrewMemberList())
        {
            if (crewMember.isPlayer())
            {
                players.addToCrewMemberCollection(crewMember);
            }
        }
        return players;
    }
}
