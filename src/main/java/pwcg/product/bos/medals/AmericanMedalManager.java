package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
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
	        
        medals.put(PILOTS_BADGE, new Medal ("Pilots Badge",                                 "us_pilots_badge.png"));
		medals.put(BRONZE_STAR, new Medal ("Bronze Star",	                                "us_bronze_star.png"));
		medals.put(DISTINGUISHED_FLYING_CROSS, new Medal ("Distinguished Flying Cross",	    "us_distinguished_flying_cross.png"));
        medals.put(SILVER_STAR, new Medal ("Silver Star",                                   "us_silver_star.png"));
        medals.put(DISTINGUISHED_SERVICE_CROSS, new Medal ("Distinguished Service Cross",   "us_distinguished_service_cross.png"));
		medals.put(MEDAL_OF_HONOR, new Medal ("Medal of Honor",							    "us_medal_of_honor.png"));
		
		medals.put(PURPLE_HEART, new Medal ("Purple Heart", 							    "us_purple_heart.png"));
	} 

    protected Medal awardWings(SquadronMember pilot) 
    {
        if (!hasMedal(pilot, medals.get(PILOTS_BADGE)))
        {
            return medals.get(PILOTS_BADGE);
        }
        
        return null;
    }

	public Medal awardWoundedAward(SquadronMember pilot, ArmedService service) 
	{
		return medals.get(PURPLE_HEART);
	}

	public Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException 
	{
        if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 3) && !hasMedal(pilot, medals.get(BRONZE_STAR)))
        {
            return medals.get(BRONZE_STAR);
        }
        else if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 6) && !hasMedal(pilot, medals.get(DISTINGUISHED_FLYING_CROSS)))
		{
			return medals.get(DISTINGUISHED_FLYING_CROSS);
		}
        else if (pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 15 && !hasMedal(pilot, medals.get(SILVER_STAR)))
        {
            return medals.get(SILVER_STAR);
        }
        else if (pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 20 && !hasMedal(pilot, medals.get(DISTINGUISHED_SERVICE_CROSS)))
        {
            return medals.get(DISTINGUISHED_SERVICE_CROSS);
        }
		else
		{
			if (!hasMedal(pilot, medals.get(MEDAL_OF_HONOR)))
			{
				if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 20) && (victoriesThisMission >= 3))
				{
                    return medals.get(MEDAL_OF_HONOR);
				}
				else if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 30) && (victoriesThisMission >= 2))
				{
                    return medals.get(MEDAL_OF_HONOR);
				}
                else if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 35))
                {
                    return medals.get(MEDAL_OF_HONOR);
                }
			}
		}
		
		return null;
	}

    public Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        int numPilotGroundVictoryPoints = pilot.getSquadronMemberVictories().getGroundVictoryPointTotal();

        if (!hasMedal(pilot, medals.get(BRONZE_STAR)))
        {
            if (pilot.getMissionFlown() >= 20)
            {
                if (numPilotGroundVictoryPoints > 30)
                {
                    return medals.get(BRONZE_STAR);
                }
            }
        }
        else if (!hasMedal(pilot, medals.get(DISTINGUISHED_FLYING_CROSS)))
        {
            if (pilot.getMissionFlown() >= 30)
            {
                if (numPilotGroundVictoryPoints > 45)
                {
                    return medals.get(DISTINGUISHED_FLYING_CROSS);
                }
            }
        }
        else if (!hasMedal(pilot, medals.get(SILVER_STAR)))
        {
            if (pilot.getMissionFlown() >= 40)
            {
                if (numPilotGroundVictoryPoints > 80)
                {
                    return medals.get(SILVER_STAR);
                }
            }
        }
        else if (!hasMedal(pilot, medals.get(DISTINGUISHED_SERVICE_CROSS)))
        {
            if (pilot.getMissionFlown() >= 50)
            {
                if (numPilotGroundVictoryPoints > 120)
                {
                    return medals.get(DISTINGUISHED_SERVICE_CROSS);
                }
            }
        }
        else if (!hasMedal(pilot, medals.get(MEDAL_OF_HONOR)))
        {
            if (pilot.getMissionFlown() >= 80)
            {
                if (numPilotGroundVictoryPoints > 200)
                {
                    return medals.get(MEDAL_OF_HONOR);
                }
            }
        }

        return awardFighter(pilot, service, victoriesThisMission);
    }
}
