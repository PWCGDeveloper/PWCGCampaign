package pwcg.campaign.squadmember;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class GreatAce
{
    public static boolean isGreatAce(Campaign campaign, SquadronMember pilot) throws PWCGException
    {
        int numPilotAirVictories = pilot.getSquadronMemberVictories().getAirToAirVictoryCount();
        ArmedService service = pilot.determineService(campaign.getDate());
        if (numPilotAirVictories >= service.getAirVictoriesForgreatAce())
        {
            return true;
        }
        
        int numPilotGroundVictories = pilot.getSquadronMemberVictories().getGroundVictoryCount();
        if (numPilotGroundVictories >= service.getGroundVictoriesForgreatAce())
        {
            return true;
        }
        
        return false;
    }
}
