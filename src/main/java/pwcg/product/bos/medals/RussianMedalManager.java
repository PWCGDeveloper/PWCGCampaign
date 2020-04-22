package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;

public class RussianMedalManager extends BoSMedalManager 
{
    public static int PILOTS_BADGE = 1;    
    public static int ORDER_RED_STAR = 2;
    public static int ORDER_OF_GLORY = 3;
    public static int ORDER_PATRIOTIC_WAR_2 = 4;
    public static int ORDER_PATRIOTIC_WAR_1 = 5;
    public static int ORDER_RED_BANNER = 6;
    public static int ORDER_ALEXANDER_NEVSKY = 7;
    public static int HERO_SOVIET_UNION = 8;
        
	public static int WOUND_STRIPE = 99;

	RussianMedalManager (Campaign campaign)
    {
        super(campaign);

        medals.put(PILOTS_BADGE, new Medal ("Pilots Badge",                                   "rus_pb.jpg"));
		medals.put(WOUND_STRIPE, new Medal ("Wound Stripe", 		                           "WoundStripe.jpg"));

        medals.put(ORDER_RED_STAR, new Medal ("Order of the Red Star",                        "rus_ors.jpg"));
        medals.put(ORDER_OF_GLORY, new Medal ("Order of Glory",                               "rus_og.jpg"));
		medals.put(ORDER_PATRIOTIC_WAR_2, new Medal ("Order of the Patriotic War 2nd Class",  "rus_opw2.jpg"));
        medals.put(ORDER_PATRIOTIC_WAR_1, new Medal ("Order of the Patriotic War 1st Class",  "rus_opw1.jpg"));
        medals.put(ORDER_RED_BANNER, new Medal ("Order of the Red Banner",                    "rus_orb.jpg"));
        medals.put(ORDER_ALEXANDER_NEVSKY, new Medal ("Order of Alexander Nevsky",            "rus_oan.jpg"));
        medals.put(HERO_SOVIET_UNION, new Medal ("Hero of the Soviet Union",                  "rus_hsu.jpg"));
	} 

	public Medal getWoundedAward(SquadronMember pilot, ArmedService service) 
	{
        return medals.get(WOUND_STRIPE);
	}

    protected Medal awardWings(SquadronMember pilot) 
    {
        if (!hasMedal(pilot, medals.get(PILOTS_BADGE)))
        {
            return medals.get(PILOTS_BADGE);
        }

        return null;
    }

	protected Medal awardFighter(SquadronMember pilot, ArmedService service, int numMissionVictories) 
	{
	    int pilotTotalVictories = pilot.getSquadronMemberVictories().getAirToAirVictories();
		if ((pilotTotalVictories >= 2) && !hasMedal(pilot, medals.get(ORDER_RED_STAR)))
		{
			return medals.get(ORDER_RED_STAR);
		}
		
		if ((pilotTotalVictories >= 5) && !hasMedal(pilot, medals.get(ORDER_OF_GLORY)))
		{
			return medals.get(ORDER_OF_GLORY);
		}
		
        if ((pilotTotalVictories >= 6) && !hasMedal(pilot, medals.get(ORDER_PATRIOTIC_WAR_2)))
        {
            return medals.get(ORDER_PATRIOTIC_WAR_2);
        }
        
        if ((pilotTotalVictories >= 15) && !hasMedal(pilot, medals.get(ORDER_PATRIOTIC_WAR_1)))
        {
            return medals.get(ORDER_PATRIOTIC_WAR_1);
        }
        
        if ((pilotTotalVictories >= 20) && numMissionVictories >= 2 && !hasMedal(pilot, medals.get(ORDER_RED_BANNER)))
        {
            return medals.get(ORDER_RED_BANNER);
        }
        
        if ((pilotTotalVictories >= 100) && !hasMedal(pilot, medals.get(ORDER_ALEXANDER_NEVSKY)))
        {
            return medals.get(ORDER_ALEXANDER_NEVSKY);
        }

        if ((pilotTotalVictories >= 30) && numMissionVictories >= 2 && !hasMedal(pilot, medals.get(HERO_SOVIET_UNION)))
        {
            return medals.get(HERO_SOVIET_UNION);
        }
		
		return null;
	}

    protected Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
    {
        if (!hasMedal(pilot, medals.get(ORDER_RED_STAR)))
        {
	        if ((pilot.getMissionFlown() >= 15))
	        {
	            return medals.get(ORDER_RED_STAR);
	        }
            if (pilot.getGroundVictories().size() > 5)
            {
                return medals.get(ORDER_RED_STAR);
            }
        }
        
        if (!hasMedal(pilot, medals.get(ORDER_OF_GLORY)))
        {
            if (pilot.getGroundVictories().size() > 20)
            {
                return medals.get(ORDER_OF_GLORY);
            }
        }
        
        if (!hasMedal(pilot, medals.get(ORDER_PATRIOTIC_WAR_2)))
        {
            if ((pilot.getMissionFlown() >= 20) && pilot.getGroundVictories().size() > 30)
            {
                return medals.get(ORDER_PATRIOTIC_WAR_2);
            }
        }
        
        if (!hasMedal(pilot, medals.get(ORDER_PATRIOTIC_WAR_1)))
        {
            if ((pilot.getMissionFlown() >= 30) && pilot.getGroundVictories().size() > 50)
            {
                return medals.get(ORDER_PATRIOTIC_WAR_1);
            }
        }
        
        if (!hasMedal(pilot, medals.get(ORDER_ALEXANDER_NEVSKY)))
        {
            if (pilot.getMissionFlown() >= 150)
            {
                return medals.get(ORDER_ALEXANDER_NEVSKY);
            }
        }
        
        if (!hasMedal(pilot, medals.get(HERO_SOVIET_UNION)))
        {
            if ((pilot.getMissionFlown() >= 40) && pilot.getGroundVictories().size() > 120)
            {
                return medals.get(HERO_SOVIET_UNION);
            }
        }
        
        return awardFighter(pilot, service, victoriesThisMission);
    }
}
