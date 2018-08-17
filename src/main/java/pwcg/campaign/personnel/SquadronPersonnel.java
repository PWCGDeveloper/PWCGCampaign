package pwcg.campaign.personnel;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
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
        if (squadronMembers.getActiveCount(campaign.getDate()) > (Squadron.SQUADRON_STAFF_SIZE / 2))
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
        for (SquadronMember squadronMember : squadronMembers.getSquadronMemberCollection().values())
        {
            if (squadronMember.getSerialNumber() == serialNumber)
            {
                if (squadronMember.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
                return true;
            }
        }

        return false;
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

        List<Ace> aces = campaign.getPersonnelManager().getCampaignAces().getCampaignAcesBySquadron(squadron.getSquadronId());
        for (SquadronMember ace : aces)
        {
            activeSquadronMembersAndAces.addToSquadronMemberCollection(ace);
        }

        return activeSquadronMembersAndAces;
    }
}
