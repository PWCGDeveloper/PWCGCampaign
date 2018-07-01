package pwcg.aar.outofmission.phase2.transfer;

import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class SquadronTransferNeed
{
    private Campaign campaign;
    private Squadron squadron;
    private int transfersNeeded = 0;

    public SquadronTransferNeed(Campaign campaign, Squadron squadron)
    {
        this.campaign = campaign;
        this.squadron = squadron;
    }
    
    public void determineTransfersNeeded() throws PWCGException
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

    public boolean needsTransfer()
    {
        return (transfersNeeded > 0);
    }

    public void noteTransfer()
    {
        --transfersNeeded;
    }
}
