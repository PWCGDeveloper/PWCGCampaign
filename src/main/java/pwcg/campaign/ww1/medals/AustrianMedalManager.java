package pwcg.campaign.ww1.medals;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class AustrianMedalManager extends RoFMedalManager 
{
	public static int IRON_CROSS_2 = 21;

    public static int A_PILOTS_BADGE = 2;
    public static int A_ORDER_IRON_CROWN = 11;
    public static int A_ORDER_LEOPOLD = 12;
    public static int A_MEDAL_FOR_BRAVERY = 13;
    public static int A_MILITARY_MERIT_MEDAL = 14;
    
	public static int WOUND_BADGE_BLACK = 51;
	public static int WOUND_BADGE_SILVER = 52;

	AustrianMedalManager (Campaign campaign)
    {
        super(campaign);

		
        medals.put(A_PILOTS_BADGE, new Medal ("Pilots Badge",                           "A_PB.jpg"));
        medals.put(IRON_CROSS_2, new Medal ("Iron Cross 2nd Class",                     "IC2.jpg"));
		medals.put(A_ORDER_IRON_CROWN, new Medal ("Order of the Iron Crown", 	        "A_OIC.jpg"));
		medals.put(A_ORDER_LEOPOLD, new Medal ("Order of Leopold", 						"A_OL.jpg"));
        medals.put(A_MEDAL_FOR_BRAVERY, new Medal ("Medal for Bravery",                 "A_MFB.jpg"));
        medals.put(A_MILITARY_MERIT_MEDAL, new Medal ("Military Merit Medal",           "A_MMM.jpg"));
		

		medals.put(WOUND_BADGE_BLACK, new Medal ("Wound Badge(Black)", 		"BWB.jpg"));
		medals.put(WOUND_BADGE_SILVER, new Medal ("Wound Badge(Silver)", 	"SWB.jpg"));
	} 

	public Medal getWoundedAward(SquadronMember pilot, ArmedService service) 
	{
        return medals.get(WOUND_BADGE_BLACK);
	}

    protected Medal awardWings(SquadronMember pilot) 
    {
        if (!hasMedal(pilot, medals.get(A_PILOTS_BADGE)))
        {
            return medals.get(A_PILOTS_BADGE);
        }
        
        return null;
    }

	protected Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission)
	{
	    // Iron Crown
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 3) && !hasMedal(pilot, medals.get(A_ORDER_IRON_CROWN)))
		{
			return medals.get(A_ORDER_IRON_CROWN);
		}
		// Leopold
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 8) && !hasMedal(pilot, medals.get(A_ORDER_LEOPOLD)))
		{
			return medals.get(A_ORDER_LEOPOLD);
		}
        // IC 2nd for Austrians
        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 10)  && !hasMedal(pilot, medals.get(IRON_CROSS_2)))
        {
            return medals.get(IRON_CROSS_2);
        }
        // MfB
        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 12) && !hasMedal(pilot, medals.get(A_MEDAL_FOR_BRAVERY)))
        {
            return medals.get(A_MEDAL_FOR_BRAVERY);
        }
        // MfB
        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 20) && !hasMedal(pilot, medals.get(A_MILITARY_MERIT_MEDAL)))
        {
            return medals.get(A_MILITARY_MERIT_MEDAL);
        }
		
		return null;
	}
	
    
    /* (non-Javadoc)
     * @see rof.campaign.campaign.medals.MedalManager#award(rof.campaign.campaign.Pilot, int, int)
     */
    protected Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
    {
        // IC
        if ((pilot.getMissionFlown() >= 15) && !hasMedal(pilot, medals.get(A_ORDER_IRON_CROWN)))
        {
            return medals.get(A_ORDER_IRON_CROWN);
        }
        
        // OL
        if (!hasMedal(pilot, medals.get(A_ORDER_LEOPOLD)))
        {
            if (pilot.getMissionFlown() >= 50)
            {
                return medals.get(A_ORDER_LEOPOLD);
            }
            if (pilot.getMissionFlown() >= 30 && pilot.getGroundVictories().size() > 15)
            {
                return medals.get(A_ORDER_LEOPOLD);
            }
        }
        
        // MfB
        if (!hasMedal(pilot, medals.get(A_MEDAL_FOR_BRAVERY)))
        {
            if (pilot.getMissionFlown() >= 150)
            {
                return medals.get(A_MEDAL_FOR_BRAVERY);
            }
            if (pilot.getMissionFlown() >= 70 && pilot.getGroundVictories().size() > 30)
            {
                return medals.get(A_MEDAL_FOR_BRAVERY);
            }
        }
        
        // MMM
        if (!hasMedal(pilot, medals.get(A_MILITARY_MERIT_MEDAL)))
        {
            if (pilot.getMissionFlown() >= 70 && pilot.getGroundVictories().size() > 75)
            {
                return medals.get(A_MILITARY_MERIT_MEDAL);
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
		medalsInOrder.add( medals.get(A_ORDER_IRON_CROWN));
		medalsInOrder.add( medals.get(A_ORDER_LEOPOLD));
		medalsInOrder.add( medals.get(A_MEDAL_FOR_BRAVERY));
		medalsInOrder.add( medals.get(A_MILITARY_MERIT_MEDAL));
		
		return medalsInOrder;
	}

	@Override
	public List<Medal> getWoundBadgesInOrder()
	{
		List<Medal> medalsInOrder = new ArrayList<>();
		medalsInOrder.add( medals.get(WOUND_BADGE_BLACK));		
		medalsInOrder.add( medals.get(WOUND_BADGE_SILVER));		
		return medalsInOrder;
	}

	@Override
	public List<Medal> getAllBadgesInOrder()
	{
		List<Medal> medalsInOrder = new ArrayList<>();
		medalsInOrder.add( medals.get(A_PILOTS_BADGE));		
		return medalsInOrder;
	}

}
