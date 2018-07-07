package pwcg.aar.outofmission.phase2.resupply;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class SquadronPersonnelNeed implements ISquadronNeed
{
    private Campaign campaign;
    private Squadron squadron;
    private int transfersNeeded = 0;

    public SquadronPersonnelNeed(Campaign campaign, Squadron squadron)
    {
        this.campaign = campaign;
        this.squadron = squadron;
    }
    
    public void determineResupplyNeeded() throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId());
        int activeSquadronSize = squadronPersonnel.getActiveSquadronMembersWithAces().getActiveCount(campaign.getDate());
        int getRecentlyInactive = squadronPersonnel.getRecentlyInactiveSquadronMembers().getActiveCount(campaign.getDate());
      
        transfersNeeded = Squadron.SQUADRON_STAFF_SIZE -  activeSquadronSize - getRecentlyInactive;
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
}
