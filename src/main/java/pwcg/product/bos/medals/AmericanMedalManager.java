package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;

public class AmericanMedalManager extends BoSMedalManager 
{
    public static int PILOTS_BADGE = 1;

    public static int BRONZE_STAR = 2;
	public static int DISTINGUISHED_FLYING_CROSS = 3;
    public static int SILVER_STAR = 4;
    public static int DISTINGUISHED_SERVICE_CROSS = 5;
	public static int MEDAL_OF_HONOR = 6;
	
	public static int PURPLE_HEART = 20;

    AmericanMedalManager (Campaign campaign)
    {
        super(campaign);
	        
        medals.put(PILOTS_BADGE, new Medal ("CrewMembers Badge",                                 "us_crewMembers_badge.png"));
		medals.put(BRONZE_STAR, new Medal ("Bronze Star",	                                "us_bronze_star.png"));
		medals.put(DISTINGUISHED_FLYING_CROSS, new Medal ("Distinguished Flying Cross",	    "us_distinguished_flying_cross.png"));
        medals.put(SILVER_STAR, new Medal ("Silver Star",                                   "us_silver_star.png"));
        medals.put(DISTINGUISHED_SERVICE_CROSS, new Medal ("Distinguished Service Cross",   "us_distinguished_service_cross.png"));
		medals.put(MEDAL_OF_HONOR, new Medal ("Medal of Honor",							    "us_medal_of_honor.png"));
		
		medals.put(PURPLE_HEART, new Medal ("Purple Heart", 							    "us_purple_heart.png"));
	} 

    protected Medal awardWings(CrewMember crewMember) 
    {
        if (!hasMedal(crewMember, medals.get(PILOTS_BADGE)))
        {
            return medals.get(PILOTS_BADGE);
        }
        
        return null;
    }

	public Medal awardWoundedAward(CrewMember crewMember, ArmedService service) 
	{
		return medals.get(PURPLE_HEART);
	}

	public Medal awardFighter(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException 
	{
        if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 3) && !hasMedal(crewMember, medals.get(BRONZE_STAR)))
        {
            return medals.get(BRONZE_STAR);
        }
        else if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 6) && !hasMedal(crewMember, medals.get(DISTINGUISHED_FLYING_CROSS)))
		{
			return medals.get(DISTINGUISHED_FLYING_CROSS);
		}
        else if (crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 15 && !hasMedal(crewMember, medals.get(SILVER_STAR)))
        {
            return medals.get(SILVER_STAR);
        }
        else if (crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 20 && !hasMedal(crewMember, medals.get(DISTINGUISHED_SERVICE_CROSS)))
        {
            return medals.get(DISTINGUISHED_SERVICE_CROSS);
        }
		else
		{
			if (!hasMedal(crewMember, medals.get(MEDAL_OF_HONOR)))
			{
				if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 20) && (victoriesThisMission >= 3))
				{
                    return medals.get(MEDAL_OF_HONOR);
				}
				else if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 30) && (victoriesThisMission >= 2))
				{
                    return medals.get(MEDAL_OF_HONOR);
				}
                else if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 35))
                {
                    return medals.get(MEDAL_OF_HONOR);
                }
			}
		}
		
		return null;
	}

    public Medal awardBomber(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        int numCrewMemberGroundVictoryPoints = crewMember.getCrewMemberVictories().getGroundVictoryPointTotal();

        if (!hasMedal(crewMember, medals.get(BRONZE_STAR)))
        {
            if (crewMember.getBattlesFought() >= 20)
            {
                if (numCrewMemberGroundVictoryPoints > 30)
                {
                    return medals.get(BRONZE_STAR);
                }
            }
        }
        else if (!hasMedal(crewMember, medals.get(DISTINGUISHED_FLYING_CROSS)))
        {
            if (crewMember.getBattlesFought() >= 30)
            {
                if (numCrewMemberGroundVictoryPoints > 45)
                {
                    return medals.get(DISTINGUISHED_FLYING_CROSS);
                }
            }
        }
        else if (!hasMedal(crewMember, medals.get(SILVER_STAR)))
        {
            if (crewMember.getBattlesFought() >= 40)
            {
                if (numCrewMemberGroundVictoryPoints > 80)
                {
                    return medals.get(SILVER_STAR);
                }
            }
        }
        else if (!hasMedal(crewMember, medals.get(DISTINGUISHED_SERVICE_CROSS)))
        {
            if (crewMember.getBattlesFought() >= 50)
            {
                if (numCrewMemberGroundVictoryPoints > 120)
                {
                    return medals.get(DISTINGUISHED_SERVICE_CROSS);
                }
            }
        }
        else if (!hasMedal(crewMember, medals.get(MEDAL_OF_HONOR)))
        {
            if (crewMember.getBattlesFought() >= 80)
            {
                if (numCrewMemberGroundVictoryPoints > 200)
                {
                    return medals.get(MEDAL_OF_HONOR);
                }
            }
        }

        return awardFighter(crewMember, service, victoriesThisMission);
    }
}
