package pwcg.campaign.crewmember;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class GreatAce
{
    public static boolean isGreatAce(Campaign campaign, CrewMember crewMember) throws PWCGException
    {
        int numCrewMemberAirVictories = crewMember.getCrewMemberVictories().getAirToAirVictoryCount();
        ArmedService service = crewMember.determineService(campaign.getDate());
        if (numCrewMemberAirVictories >= service.getAirVictoriesForgreatAce())
        {
            return true;
        }
        
        int numCrewMemberGroundVictories = crewMember.getCrewMemberVictories().getGroundVictoryCount();
        if (numCrewMemberGroundVictories >= service.getGroundVictoriesForgreatAce())
        {
            return true;
        }
        
        return false;
    }
}
