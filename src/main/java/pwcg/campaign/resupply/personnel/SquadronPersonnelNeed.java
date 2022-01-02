package pwcg.campaign.resupply.personnel;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.resupply.ISquadronNeed;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;

public class SquadronPersonnelNeed implements ISquadronNeed
{
    private Campaign campaign;
    private Company squadron;
    private int transfersNeeded = 0;

    public SquadronPersonnelNeed(Campaign campaign, Company squadron)
    {
        this.campaign = campaign;
        this.squadron = squadron;
    }
    
    public void determineResupplyNeeded() throws PWCGException
    {
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(squadron.getSquadronId());
        CrewMembers activeCrewMembers = CrewMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        int activeSquadronSize = activeCrewMembers.getActiveCount(campaign.getDate());
        int getRecentlyInactive = squadronPersonnel.getRecentlyInactiveCrewMembers().getActiveCount(campaign.getDate());
      
        if ((Company.SQUADRON_STAFF_SIZE -  activeSquadronSize - getRecentlyInactive) > 0)
        {
            transfersNeeded = Company.SQUADRON_STAFF_SIZE -  activeSquadronSize - getRecentlyInactive;
        }
        else
        {
            transfersNeeded = 0;
        }
    }
    
    public int getSquadronId()
    {
        return squadron.getSquadronId();
    }

    public boolean needsResupply()
    {
        return (transfersNeeded > 0);
    }

    public void noteResupply()
    {
        --transfersNeeded;
    }

    @Override
    public int getNumNeeded()
    {
        return transfersNeeded;
    }
}
