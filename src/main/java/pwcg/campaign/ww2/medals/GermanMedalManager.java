package pwcg.campaign.ww2.medals;

import java.util.ArrayList;
import java.util.List;

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
		
        medals.put(PILOTS_BADGE, new Medal ("Pilots Badge",                                "PB.jpg"));
        medals.put(IRON_CROSS_2, new Medal ("Iron Cross 2nd Class",                        "IC2.jpg"));
		medals.put(IRON_CROSS_1, new Medal ("Iron Cross 1st Class", 					   "IC1.jpg"));
		medals.put(GERMAN_CROSS_GOLD, new Medal ("German Cross Gold", 	                   "GCG.jpg"));
		medals.put(KNIGHTS_CROSS, new Medal ("Knights Cross", 	                           "KC.jpg"));
		medals.put(KNIGHTS_CROSS_OAK_LEAVES, new Medal ("Knights Cross with Oak Leaves",   "KCOL.jpg"));
        medals.put(KNIGHTS_CROSS_SWORDS, new Medal ("Knights Cross with Swords",         "KCS.jpg"));
        medals.put(KNIGHTS_CROSS_DIAMONDS, new Medal ("Knights Cross with Diamonds",         "KCD.jpg"));
		
		medals.put(WOUND_BADGE_BLACK, new Medal ("Wound Badge(Black)", 		"BWB.jpg"));
		medals.put(WOUND_BADGE_SILVER, new Medal ("Wound Badge(Silver)", 	"SWB.jpg"));
		medals.put(WOUND_BADGE_GOLD, new Medal ("Wound Badge(Gold)", 		"GWB.jpg"));
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

	protected Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
	{
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 2) && !hasMedal(pilot, medals.get(IRON_CROSS_2)))
		{
			return medals.get(IRON_CROSS_2);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 10)  && !hasMedal(pilot, medals.get(IRON_CROSS_1)))
		{
			return medals.get(IRON_CROSS_1);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 25) && !hasMedal(pilot, medals.get(GERMAN_CROSS_GOLD)))
		{
			return medals.get(GERMAN_CROSS_GOLD);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 40) && !hasMedal(pilot, medals.get(KNIGHTS_CROSS)))
		{
			return medals.get(KNIGHTS_CROSS);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 100) && !hasMedal(pilot, medals.get(KNIGHTS_CROSS_OAK_LEAVES)))
		{
			return medals.get(KNIGHTS_CROSS_OAK_LEAVES);
		}
        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 150) && !hasMedal(pilot, medals.get(KNIGHTS_CROSS_SWORDS)))
        {
            return medals.get(KNIGHTS_CROSS_SWORDS);
        }
        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 200) && !hasMedal(pilot, medals.get(KNIGHTS_CROSS_DIAMONDS)))
        {
            return medals.get(KNIGHTS_CROSS_DIAMONDS);
        }
		
		return null;
	}

    protected Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
    {
        if (!hasMedal(pilot, medals.get(IRON_CROSS_2)))
        {
	        if ((pilot.getMissionFlown() >= 15))
	        {
	            return medals.get(IRON_CROSS_2);
	        }
            if (pilot.getGroundVictories().size() > 5)
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
            if (pilot.getMissionFlown() >= 10 && pilot.getGroundVictories().size() > 20)
            {
                return medals.get(IRON_CROSS_1);
            }
        }
        
        if (!hasMedal(pilot, medals.get(GERMAN_CROSS_GOLD)))
        {
            if (pilot.getMissionFlown() >= 20 && pilot.getGroundVictories().size() > 35)
            {
                return medals.get(GERMAN_CROSS_GOLD);
            }
        }
        
        if (pilot.getMissionFlown() >= 30 && pilot.getGroundVictories().size() > 80)
        {
            return medals.get(KNIGHTS_CROSS);
        }
        
        if (!hasMedal(pilot, medals.get(KNIGHTS_CROSS_OAK_LEAVES)))
        {
            if (pilot.getMissionFlown() >= 40 && pilot.getGroundVictories().size() > 120)
            {
                return medals.get(KNIGHTS_CROSS_OAK_LEAVES);
            }
        }
        
        if (!hasMedal(pilot, medals.get(KNIGHTS_CROSS_SWORDS)))
        {
            if (pilot.getMissionFlown() >= 50 && pilot.getGroundVictories().size() > 200)
            {
                return medals.get(KNIGHTS_CROSS_SWORDS);
            }
        }
        
        if (!hasMedal(pilot, medals.get(KNIGHTS_CROSS_DIAMONDS)))
        {
            if (pilot.getMissionFlown() >= 50 && pilot.getGroundVictories().size() > 400)
            {
                return medals.get(KNIGHTS_CROSS_DIAMONDS);
            }
        }
        
        return awardFighter(pilot, service, victoriesThisMission);
    }

	@Override
	public List<Medal> getAllAwardsForService() throws PWCGException
	{
		List<Medal> medalsInOrder = new ArrayList<>();
		medalsInOrder.addAll(getWoundBadgesInOrder());
		medalsInOrder.addAll(getAllBadgesInOrder());
		return medalsInOrder;
	}

	@Override
	public List<Medal> getAllMedalsInOrder()
	{
		List<Medal> medalsInOrder = new ArrayList<>();
		medalsInOrder.add( medals.get(IRON_CROSS_2));
		medalsInOrder.add( medals.get(IRON_CROSS_1));
		medalsInOrder.add( medals.get(GERMAN_CROSS_GOLD));
		medalsInOrder.add( medals.get(KNIGHTS_CROSS));
		medalsInOrder.add( medals.get(KNIGHTS_CROSS_OAK_LEAVES));
		medalsInOrder.add( medals.get(KNIGHTS_CROSS_SWORDS));
		medalsInOrder.add( medals.get(KNIGHTS_CROSS_DIAMONDS));

		return medalsInOrder;
	}

	@Override
	public List<Medal> getWoundBadgesInOrder()
	{
		List<Medal> medalsInOrder = new ArrayList<>();
		medalsInOrder.add( medals.get(WOUND_BADGE_BLACK));		
		medalsInOrder.add( medals.get(WOUND_BADGE_SILVER));		
		medalsInOrder.add( medals.get(WOUND_BADGE_GOLD));		
		return medalsInOrder;
	}

	@Override
	public List<Medal> getAllBadgesInOrder()
	{
		List<Medal> medalsInOrder = new ArrayList<>();
		medalsInOrder.add( medals.get(PILOTS_BADGE));		
		return medalsInOrder;
	}
}
