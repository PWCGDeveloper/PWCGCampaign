package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;

public class GermanMedalManager extends BoSMedalManager 
{
    public static int PILOTS_BADGE = 1;
	public static int IRON_CROSS_2 = 2;
	public static int IRON_CROSS_1 = 3;
	public static int GERMAN_CROSS_GOLD = 4;
	public static int KNIGHTS_CROSS = 5;
	public static int KNIGHTS_CROSS_OAK_LEAVES = 6;
    public static int KNIGHTS_CROSS_SWORDS = 7;
    public static int KNIGHTS_CROSS_DIAMONDS = 8;
	
	public static int WOUND_BADGE_BLACK = 97;
	public static int WOUND_BADGE_SILVER = 98;
	public static int WOUND_BADGE_GOLD = 99;

	GermanMedalManager (Campaign campaign)
    {
        super(campaign);
		
        medals.put(PILOTS_BADGE, new Medal ("CrewMembers Badge",                                         "ger_crewMembers_badge.png"));
        medals.put(IRON_CROSS_2, new Medal ("Iron Cross 2nd Class",                                 "ger_iron_cross_second_class.png"));
		medals.put(IRON_CROSS_1, new Medal ("Iron Cross 1st Class", 					            "ger_iron_cross_first_class.png"));
		medals.put(GERMAN_CROSS_GOLD, new Medal ("German Cross Gold", 	                            "ger_german_cross_gold.png"));
		medals.put(KNIGHTS_CROSS, new Medal (KNIGHTS_CROSS_NAME, 	                                "ger_knights_cross.png"));
		medals.put(KNIGHTS_CROSS_OAK_LEAVES, new Medal (KNIGHTS_CROSS_NAME + " with Oak Leaves",    "ger_knights_cross_oak_leaves.png"));
        medals.put(KNIGHTS_CROSS_SWORDS, new Medal (KNIGHTS_CROSS_NAME + " with Swords",            "ger_knights_cross_swords.png"));
        medals.put(KNIGHTS_CROSS_DIAMONDS, new Medal (KNIGHTS_CROSS_NAME + " with Diamonds",        "ger_knights_cross_diamonds.png"));
		
		medals.put(WOUND_BADGE_BLACK, new Medal (GERMAN_WOUND_BADGE + "(Black)", 		"ger_wound_badge_black.png"));
		medals.put(WOUND_BADGE_SILVER, new Medal (GERMAN_WOUND_BADGE + "(Silver)", 	"ger_wound_badge_silver.png"));
		medals.put(WOUND_BADGE_GOLD, new Medal (GERMAN_WOUND_BADGE + "(Gold)", 		"ger_wound_badge_gold.png"));
	} 

	public Medal awardWoundedAward(CrewMember crewMember, ArmedService service) 
	{
	    if (!hasMedal(crewMember, medals.get(WOUND_BADGE_BLACK)))
	    {
	        return medals.get(WOUND_BADGE_BLACK);
	    }
	    else if (!hasMedal(crewMember, medals.get(WOUND_BADGE_SILVER)))
	    {
            return medals.get(WOUND_BADGE_SILVER);
	    }
	    else if (!hasMedal(crewMember, medals.get(WOUND_BADGE_GOLD)))
	    {
            return medals.get(WOUND_BADGE_GOLD);
	    }
        
        return null;
	}

    protected Medal awardWings(CrewMember crewMember) 
    {
        if (!hasMedal(crewMember, medals.get(PILOTS_BADGE)))
        {
            return medals.get(PILOTS_BADGE);
        }
        
        return null;
    }

	protected Medal awardFighter(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException 
	{
		if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 2) && !hasMedal(crewMember, medals.get(IRON_CROSS_2)))
		{
			return medals.get(IRON_CROSS_2);
		}
		if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 10)  && !hasMedal(crewMember, medals.get(IRON_CROSS_1)))
		{
			return medals.get(IRON_CROSS_1);
		}
		if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 25) && !hasMedal(crewMember, medals.get(GERMAN_CROSS_GOLD)))
		{
			return medals.get(GERMAN_CROSS_GOLD);
		}
		if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 40) && !hasMedal(crewMember, medals.get(KNIGHTS_CROSS)))
		{
			return medals.get(KNIGHTS_CROSS);
		}
		if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 100) && !hasMedal(crewMember, medals.get(KNIGHTS_CROSS_OAK_LEAVES)))
		{
			return medals.get(KNIGHTS_CROSS_OAK_LEAVES);
		}
        if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 150) && !hasMedal(crewMember, medals.get(KNIGHTS_CROSS_SWORDS)))
        {
            return medals.get(KNIGHTS_CROSS_SWORDS);
        }
        if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 200) && !hasMedal(crewMember, medals.get(KNIGHTS_CROSS_DIAMONDS)))
        {
            return medals.get(KNIGHTS_CROSS_DIAMONDS);
        }
		
		return null;
	}

    protected Medal awardBomber(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        int numCrewMemberGroundVictoryPoints = crewMember.getCrewMemberVictories().getGroundVictoryPointTotal();

        if (!hasMedal(crewMember, medals.get(IRON_CROSS_2)))
        {
	        if ((crewMember.getBattlesFought() >= 15))
	        {
                if (numCrewMemberGroundVictoryPoints > 10)
                {
                    return medals.get(IRON_CROSS_2);
                }
	        }
        }
        
        if (!hasMedal(crewMember, medals.get(IRON_CROSS_1)))
        {
            if (crewMember.getBattlesFought() >= 40)
            {
                if (numCrewMemberGroundVictoryPoints > 30)
                {
                    return medals.get(IRON_CROSS_1);
                }
            }
        }
        
        if (!hasMedal(crewMember, medals.get(GERMAN_CROSS_GOLD)))
        {
            if (crewMember.getBattlesFought() >= 50)
            {
                if (numCrewMemberGroundVictoryPoints > 45)
                {
                    return medals.get(GERMAN_CROSS_GOLD);
                }
            }
        }
        
        if (!hasMedal(crewMember, medals.get(KNIGHTS_CROSS)))
        {
            if (crewMember.getBattlesFought() >= 60)
            {
                if (numCrewMemberGroundVictoryPoints > 70)
                {
                    return medals.get(KNIGHTS_CROSS);
                }
            }
        }
        
        if (!hasMedal(crewMember, medals.get(KNIGHTS_CROSS_OAK_LEAVES)))
        {
            if (crewMember.getBattlesFought() >= 70)
            {
                if (numCrewMemberGroundVictoryPoints > 120)
                {
                    return medals.get(KNIGHTS_CROSS_OAK_LEAVES);
                }
            }
        }
        
        if (!hasMedal(crewMember, medals.get(KNIGHTS_CROSS_SWORDS)))
        {
            if (crewMember.getBattlesFought() >= 100)
            {
                if (numCrewMemberGroundVictoryPoints > 200)
                {
                    return medals.get(KNIGHTS_CROSS_SWORDS);
                }
            }
        }
        
        if (!hasMedal(crewMember, medals.get(KNIGHTS_CROSS_DIAMONDS)))
        {
            if (crewMember.getBattlesFought() >= 200)
            {
                if (numCrewMemberGroundVictoryPoints > 400)
                {
                    return medals.get(KNIGHTS_CROSS_DIAMONDS);
                }
            }
        }
        
        return awardFighter(crewMember, service, victoriesThisMission);
    }
}
