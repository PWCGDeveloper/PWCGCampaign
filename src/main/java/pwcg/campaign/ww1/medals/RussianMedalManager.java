package pwcg.campaign.ww1.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;

public class RussianMedalManager extends RoFMedalManager 
{
    public static int R_PILOTS_BADGE = 2;
    
    public static int R_ORDER_ST_GEORGE = 11;
    public static int R_ORDER_ST_VLADIMIR = 12;
    public static int R_ORDER_ST_STANISLAW = 13;
    public static int R_ORDER_ST_ANNE = 14;
    
    
	public static int WOUND_STRIPE = 51;

	RussianMedalManager (Campaign campaign)
    {
        super(campaign);

		
        medals.put(R_PILOTS_BADGE, new Medal ("Pilots Badge",                       "rus_pb.jpg"));
		medals.put(R_ORDER_ST_GEORGE, new Medal ("Order of St George", 	            "rus_osg.jpg"));
		medals.put(R_ORDER_ST_VLADIMIR, new Medal ("Order of St Vladimir", 			"rus_osv.jpg"));
        medals.put(R_ORDER_ST_STANISLAW, new Medal ("Order of St Stanislaw",        "rus_oss.jpg"));
        medals.put(R_ORDER_ST_ANNE, new Medal ("Order of St Anne",                  "rus_osa.jpg"));
		
		medals.put(WOUND_STRIPE, new Medal ("Wound Stripe", 		"WoundStripe.jpg"));
	} 
	
	public Medal getWoundedAward(SquadronMember pilot, ArmedService service) 
	{
        return medals.get(WOUND_STRIPE);
	}
    
    protected Medal awardWings(SquadronMember pilot) 
    {
        if (!hasMedal(pilot, medals.get(R_PILOTS_BADGE)))
        {
            return medals.get(R_PILOTS_BADGE);
        }
        
        return null;
    }

	protected Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
	{
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 3) && !hasMedal(pilot, medals.get(R_ORDER_ST_GEORGE)))
		{
			return medals.get(R_ORDER_ST_GEORGE);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 7) && !hasMedal(pilot, medals.get(R_ORDER_ST_VLADIMIR)))
		{
			return medals.get(R_ORDER_ST_VLADIMIR);
		}
        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 18) && !hasMedal(pilot, medals.get(R_ORDER_ST_STANISLAW)))
        {
            return medals.get(R_ORDER_ST_STANISLAW);
        }
        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 10) && !hasMedal(pilot, medals.get(R_ORDER_ST_ANNE)) && victoriesThisMission >= 2)
        {
            return medals.get(R_ORDER_ST_ANNE);
        }
		
		return null;
	}

    protected Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
    {
        if ((pilot.getMissionFlown() >= 15) && !hasMedal(pilot, medals.get(R_ORDER_ST_GEORGE)))
        {
            return medals.get(R_ORDER_ST_GEORGE);
        }
        
        if (!hasMedal(pilot, medals.get(R_ORDER_ST_VLADIMIR)))
        {
            if (pilot.getMissionFlown() >= 50)
            {
                return medals.get(R_ORDER_ST_VLADIMIR);
            }
        }
        
        return awardFighter(pilot, service, victoriesThisMission);
    }
}
