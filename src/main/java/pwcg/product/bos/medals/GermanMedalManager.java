package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
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
		
        medals.put(PILOTS_BADGE, new Medal ("Pilots Badge",                                "ger_pilots_badge.png"));
        medals.put(IRON_CROSS_2, new Medal ("Iron Cross 2nd Class",                        "ger_iron_cross_second_class.png"));
		medals.put(IRON_CROSS_1, new Medal ("Iron Cross 1st Class", 					   "ger_iron_cross_first_class.png"));
		medals.put(GERMAN_CROSS_GOLD, new Medal ("German Cross Gold", 	                   "ger_german_cross_gold.png"));
		medals.put(KNIGHTS_CROSS, new Medal ("Knights Cross", 	                           "ger_knights_cross.png"));
		medals.put(KNIGHTS_CROSS_OAK_LEAVES, new Medal ("Knights Cross with Oak Leaves",   "ger_knights_cross_oak_leaves.png"));
        medals.put(KNIGHTS_CROSS_SWORDS, new Medal ("Knights Cross with Swords",         "ger_knights_cross_swords.png"));
        medals.put(KNIGHTS_CROSS_DIAMONDS, new Medal ("Knights Cross with Diamonds",         "ger_knights_cross_diamonds.png"));
		
		medals.put(WOUND_BADGE_BLACK, new Medal ("Wound Badge(Black)", 		"ger_wound_badge_black.png"));
		medals.put(WOUND_BADGE_SILVER, new Medal ("Wound Badge(Silver)", 	"ger_wound_badge_silver.png"));
		medals.put(WOUND_BADGE_GOLD, new Medal ("Wound Badge(Gold)", 		"ger_wound_badge_gold.png"));
	} 

	public Medal getWoundedAward(SquadronMember pilot, ArmedService service) 
	{
	    if (!hasMedal(pilot, medals.get(WOUND_BADGE_BLACK)))
	    {
	        return medals.get(WOUND_BADGE_BLACK);
	    }
	    else if (!hasMedal(pilot, medals.get(WOUND_BADGE_SILVER)))
	    {
            return medals.get(WOUND_BADGE_SILVER);
	    }
	    else if (!hasMedal(pilot, medals.get(WOUND_BADGE_GOLD)))
	    {
            return medals.get(WOUND_BADGE_GOLD);
	    }
        
        return null;
	}

    protected Medal awardWings(SquadronMember pilot) 
    {
        if (!hasMedal(pilot, medals.get(PILOTS_BADGE)))
        {
            return medals.get(PILOTS_BADGE);
        }
        
        return null;
    }

	protected Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException 
	{
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 2) && !hasMedal(pilot, medals.get(IRON_CROSS_2)))
		{
			return medals.get(IRON_CROSS_2);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 10)  && !hasMedal(pilot, medals.get(IRON_CROSS_1)))
		{
			return medals.get(IRON_CROSS_1);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 25) && !hasMedal(pilot, medals.get(GERMAN_CROSS_GOLD)))
		{
			return medals.get(GERMAN_CROSS_GOLD);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 40) && !hasMedal(pilot, medals.get(KNIGHTS_CROSS)))
		{
			return medals.get(KNIGHTS_CROSS);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 100) && !hasMedal(pilot, medals.get(KNIGHTS_CROSS_OAK_LEAVES)))
		{
			return medals.get(KNIGHTS_CROSS_OAK_LEAVES);
		}
        if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 150) && !hasMedal(pilot, medals.get(KNIGHTS_CROSS_SWORDS)))
        {
            return medals.get(KNIGHTS_CROSS_SWORDS);
        }
        if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 200) && !hasMedal(pilot, medals.get(KNIGHTS_CROSS_DIAMONDS)))
        {
            return medals.get(KNIGHTS_CROSS_DIAMONDS);
        }
		
		return null;
	}

    protected Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        int numPilotGroundVictoryPoints = pilot.getSquadronMemberVictories().getGroundVictoryPointTotal();

        if (!hasMedal(pilot, medals.get(IRON_CROSS_2)))
        {
	        if ((pilot.getMissionFlown() >= 15))
	        {
	            return medals.get(IRON_CROSS_2);
	        }
            if (numPilotGroundVictoryPoints > 5)
            {
                return medals.get(IRON_CROSS_2);
            }
        }
        
        if (!hasMedal(pilot, medals.get(IRON_CROSS_1)))
        {
            if (pilot.getMissionFlown() >= 50)
            {
                return medals.get(IRON_CROSS_1);
            }
            if (pilot.getMissionFlown() >= 10 && numPilotGroundVictoryPoints > 20)
            {
                return medals.get(IRON_CROSS_1);
            }
        }
        
        if (!hasMedal(pilot, medals.get(GERMAN_CROSS_GOLD)))
        {
            if (pilot.getMissionFlown() >= 20 && numPilotGroundVictoryPoints > 35)
            {
                return medals.get(GERMAN_CROSS_GOLD);
            }
        }
        
        if (pilot.getMissionFlown() >= 30 && numPilotGroundVictoryPoints > 80)
        {
            return medals.get(KNIGHTS_CROSS);
        }
        
        if (!hasMedal(pilot, medals.get(KNIGHTS_CROSS_OAK_LEAVES)))
        {
            if (pilot.getMissionFlown() >= 40 && numPilotGroundVictoryPoints > 120)
            {
                return medals.get(KNIGHTS_CROSS_OAK_LEAVES);
            }
        }
        
        if (!hasMedal(pilot, medals.get(KNIGHTS_CROSS_SWORDS)))
        {
            if (pilot.getMissionFlown() >= 50 && numPilotGroundVictoryPoints > 200)
            {
                return medals.get(KNIGHTS_CROSS_SWORDS);
            }
        }
        
        if (!hasMedal(pilot, medals.get(KNIGHTS_CROSS_DIAMONDS)))
        {
            if (pilot.getMissionFlown() >= 50 && numPilotGroundVictoryPoints > 400)
            {
                return medals.get(KNIGHTS_CROSS_DIAMONDS);
            }
        }
        
        return awardFighter(pilot, service, victoriesThisMission);
    }
}
