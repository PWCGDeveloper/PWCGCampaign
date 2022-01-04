package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.company.CompanyViability;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.TankAce;
import pwcg.core.exception.PWCGException;

public class OutOfMissionCrewMemberSelector
{

    public static boolean shouldCrewMemberBeEvaluated (Campaign campaign, CrewMember crewMember) throws PWCGException
    {
        if (crewMember instanceof TankAce)
        {
            return false;
        }

        if (crewMember.isPlayer())
        {
            return false;
        }

        if (crewMember.getCrewMemberActiveStatus() != CrewMemberStatus.STATUS_ACTIVE)
        {
            return false;
        }
        
        if (!isSquadronViable(campaign, crewMember))
        {
            return false;
        }
        
        return true; 
    }

    private static boolean isSquadronViable(Campaign campaign, CrewMember crewMember) throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getCompanyManager().getCompany(crewMember.getCompanyId());
        return CompanyViability.isCompanyViable(squadron, campaign);
    }

}
