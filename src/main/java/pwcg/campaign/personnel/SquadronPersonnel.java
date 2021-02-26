package pwcg.campaign.personnel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class SquadronPersonnel
{
    private Campaign campaign;
    private Squadron squadron;
    private SquadronMembers squadronMembers = new SquadronMembers();

    public SquadronPersonnel (Campaign campaign, Squadron squadron)
    {
        this.campaign = campaign;
        this.squadron = squadron;
    }

    public void setSquadronMembers(SquadronMembers squadronMembers)
    {
        this.squadronMembers = squadronMembers;
    }

    public void addSquadronMember(SquadronMember squadronMember) throws PWCGException
    {
        squadronMembers.addToSquadronMemberCollection(squadronMember);
    }

    public void removeSquadronMember(SquadronMember squadronMember) throws PWCGException
    {
        if (!squadronMembers.isSquadronMember(squadronMember.getSerialNumber()))
        {
            throw new PWCGException("Not member of squadron");
        }
        squadronMembers.removeSquadronMember(squadronMember.getSerialNumber());
    }

    public boolean isSquadronPersonnelViable() throws PWCGException
    {
        int vialbleSquadronMembers = squadronMembers.getActiveCount(campaign.getDate());
        if (vialbleSquadronMembers > (Squadron.SQUADRON_STAFF_SIZE / 2))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public SquadronMember getSquadronMember(Integer serialNumber) throws PWCGException
    {
        return squadronMembers.getSquadronMemberCollection().get(serialNumber);
    }

    public boolean isActiveSquadronMember(Integer serialNumber) throws PWCGException
    {
        List<SquadronMember> squadronMembersList = new ArrayList<>();
        squadronMembersList.addAll(squadronMembers.getSquadronMemberCollection().values());
        for (SquadronMember squadronMember : squadronMembersList)
        {
            if (squadronMember.getSerialNumber() == serialNumber)
            {
                if (squadronMember.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
                {
                	return true;
                }
            }
        }
        
        SquadronMember squadronMember = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
        if (squadronMember != null)
        {
            if (squadronMember.getSquadronId() == squadron.getSquadronId())
            {
                return true;
            }
        }

        return false;
    }

    public SquadronMembers getActiveAiSquadronMembers() throws PWCGException
    {
        SquadronMembers campaignMembers = getSquadronMembersWithAces();
        SquadronMembers activeSquadronMembers = SquadronMemberFilter.filterActiveAIAndAcesNoWounded(campaignMembers.getSquadronMemberCollection(), campaign.getDate());
        return activeSquadronMembers;
    }

    public SquadronMembers getRecentlyInactiveSquadronMembers() throws PWCGException
    {
        SquadronMembers campaignMembers = getSquadronMembersWithAces();
        SquadronMembers inactiveSquadronMembers = SquadronMemberFilter.filterInactiveAIAndPlayerAndAces(campaignMembers.getSquadronMemberCollection(), campaign.getDate());
        SquadronMembers recentlyInactiveSquadronMembers = new SquadronMembers();
        for (SquadronMember squadronMember : inactiveSquadronMembers.getSquadronMemberList())
        {
            Date oneWeekAgo = DateUtils.removeTimeDays(campaign.getDate(), 7);
            if (squadronMember.getInactiveDate().after(oneWeekAgo))
            {
                recentlyInactiveSquadronMembers.addToSquadronMemberCollection(squadronMember);
            }
        }
        
        return recentlyInactiveSquadronMembers;
    }

    public Squadron getSquadron()
    {
        return squadron;
    }

    public SquadronMembers getSquadronMembersWithAces() throws PWCGException
    {
        SquadronMembers activeSquadronMembersAndAces = new SquadronMembers();
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberList())
        {
            activeSquadronMembersAndAces.addToSquadronMemberCollection(squadronMember);
        }

        List<Ace> aces = campaign.getPersonnelManager().getCampaignAces().getActiveCampaignAcesBySquadron(squadron.getSquadronId());
        for (SquadronMember ace : aces)
        {
            activeSquadronMembersAndAces.addToSquadronMemberCollection(ace);
        }

        return activeSquadronMembersAndAces;
    }
    
    public boolean isPlayerSquadron()
    {
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberList())
        {
            if (squadronMember.isPlayer())
            {
                return true;
            }
        }

        return false;
    }

    public SquadronMembers getSquadronMembers()
    {
        return squadronMembers;
    }

    public SquadronMembers getPlayersByStatus(int status)
    {
        SquadronMembers activePlayers = new SquadronMembers();
        for (SquadronMember player : getPlayers().getSquadronMemberList())
        {
            if (player.getPilotActiveStatus() >= status)
            {
                activePlayers.addToSquadronMemberCollection(player);
            }
        }
        return activePlayers;
    }

	public boolean isPlayerCommander() throws PWCGException {
		SquadronMembers players = getPlayersByStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
		for (SquadronMember player : players.getSquadronMemberList())
		{
            IRankHelper rank = RankFactory.createRankHelper();
			if (rank.isCommandRank(player.getRank(), player.determineService(campaign.getDate())))
			{
				return true;
			}
		}
		return false;
	}

    public SquadronMembers getPlayers()
    {
        SquadronMembers players = new SquadronMembers();
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberList())
        {
            if (squadronMember.isPlayer())
            {
                players.addToSquadronMemberCollection(squadronMember);
            }
        }
        return players;
    }
}
