package pwcg.campaign.personnel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public SquadronMembers getSquadronMembers() throws PWCGException
    {
        Map<Integer, SquadronMember> campaignMembers = new HashMap<>(squadronMembers.getSquadronMemberCollection());
        SquadronMembers allSquadronMembers = new SquadronMembers();
        allSquadronMembers.setSquadronMemberCollection(campaignMembers);

        return allSquadronMembers;
    }

    public SquadronMembers getActiveSquadronMembers() throws PWCGException
    {
        Map<Integer, SquadronMember> activeCampaignMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronMembers.getSquadronMemberCollection(), campaign.getDate());
        SquadronMembers activeSquadronMembers = new SquadronMembers();
        for (SquadronMember squadronMember : activeCampaignMembers.values())
        {
            activeSquadronMembers.addToSquadronMemberCollection(squadronMember);
        }
        
        return activeSquadronMembers;
    }
    
    public SquadronMembers getActiveSquadronMembersWithAces() throws PWCGException
    {
        SquadronMembers activeSquadronMembersAndAces = getActiveSquadronMembers();
        
        List<Ace> aces = campaign.getPersonnelManager().getCampaignAces().getCampaignAcesBySquadron(squadron.getSquadronId());
        for (SquadronMember ace : aces)
        {
            activeSquadronMembersAndAces.addToSquadronMemberCollection(ace);
        }

        return activeSquadronMembersAndAces;
    }

    public SquadronMembers getRecentlyInactiveSquadronMembers() throws PWCGException
    {
        Map<Integer, SquadronMember> inactiveCampaignMembers = SquadronMemberFilter.filterInactiveAIAndPlayerAndAces(squadronMembers.getSquadronMemberCollection(), campaign.getDate());
        SquadronMembers inactiveSquadronMembers = new SquadronMembers();
        for (SquadronMember squadronMember : inactiveCampaignMembers.values())
        {
            Date oneWeekAgo = DateUtils.removeTimeDays(campaign.getDate(), 7);
            if (squadronMember.getInactiveDate().after(oneWeekAgo))
            {
                inactiveSquadronMembers.addToSquadronMemberCollection(squadronMember);
            }
        }
        
        return inactiveSquadronMembers;
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

    public Squadron getSquadron()
    {
        return squadron;
    }
}
