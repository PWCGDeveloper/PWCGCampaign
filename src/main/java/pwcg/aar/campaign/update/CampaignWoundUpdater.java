package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;

public class CampaignWoundUpdater
{
    private Campaign campaign;
    
    CampaignWoundUpdater (Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void healWoundedPilots(Date newDate) throws PWCGException
    {
        for (SquadronMember squadronMember : campaign.getPersonnelManager().getAllNonAceCampaignMembers().values())
        {
            if (squadronMember.getRecoveryDate() != null)
            {
                if (!(squadronMember.getRecoveryDate().after(newDate)))
                {
                    squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, null, null);
                }
            }
        }
    }
}
