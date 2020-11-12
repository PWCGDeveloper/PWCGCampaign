package pwcg.campaign.personnel;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class SquadronMemberFemaleGenerator
{
    public static SquadronMember convertToFemale(Campaign campaign, int squadronId, SquadronMember squadronMember) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        SquadronMemberFemaleConverter.convertNightWitchesToFemale(campaign, service, squadronMember);
        return squadronMember;
    }
}
