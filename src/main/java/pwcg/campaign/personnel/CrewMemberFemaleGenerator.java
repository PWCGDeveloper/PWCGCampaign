package pwcg.campaign.personnel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;

public class CrewMemberFemaleGenerator
{
    public static CrewMember convertToFemale(Campaign campaign, int squadronId, CrewMember crewMember) throws PWCGException
    {
        Company squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        CrewMemberFemaleConverter.convertNightWitchesToFemale(campaign, service, crewMember);
        return crewMember;
    }
}
