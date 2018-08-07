package pwcg.campaign.squadmember;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.Logger;

public class GreatAce
{
    public static boolean isGreatAce(Campaign campaign) throws PWCGException 
    {        
        if (campaign == null)
        {
            return false;
        }
        
        for (SquadronMember player : campaign.getPlayers())
        {
            int numVictories = player.getSquadronMemberVictories().getAirToAirVictories();
            if (isSquadronMemberGreatAce(campaign, numVictories))
            {
                return true;
            }
        }
        
        return false;
    }

    private static boolean isSquadronMemberGreatAce(Campaign campaign, int numVictories)
    {
        try
        {
            if (campaign.getDate().before(DateUtils.getDateNoCheck("01/08/1916")))
            {
                if (numVictories > 10)
                {
                    return true;
                }
            }
            else if (campaign.getDate().before(DateUtils.getDateNoCheck("01/02/1942")))
            {
                if (numVictories > 15)
                {
                    return true;
                }
            }
            else if (campaign.getDate().before(DateUtils.getDateNoCheck("01/06/1942")))
            {
                if (numVictories > 20)
                {
                    return true;
                }
            }
            else if (campaign.getDate().before(DateUtils.getDateNoCheck("01/12/1942")))
            {
                if (numVictories > 25)
                {
                    return true;
                }
            }
            else
            {
                if (numVictories > 30)
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            Logger.logException(e);
        }

        return false;
    }
}
