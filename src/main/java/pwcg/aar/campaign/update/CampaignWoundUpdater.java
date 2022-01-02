package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;

public class CampaignWoundUpdater
{
    private Campaign campaign;
    
    CampaignWoundUpdater (Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void healWoundedCrewMembers(Date newDate) throws PWCGException
    {
        for (CrewMember crewMember : campaign.getPersonnelManager().getAllCampaignMembers().values())
        {
            if (!(crewMember instanceof TankAce))
            {
                if (crewMember.getRecoveryDate() != null)
                {
                    if (!(crewMember.getRecoveryDate().after(newDate)))
                    {
                        crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_ACTIVE, null, null);
                    }
                }
            }
        }
    }
}
