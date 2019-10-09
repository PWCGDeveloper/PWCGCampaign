package pwcg.product.fc.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.product.rof.country.RoFServiceManager;

public class GermanMedalManager extends FCMedalManager 
{
    public static int PILOTS_BADGE = 1;
	public static int IRON_CROSS_2 = 2;
	public static int IRON_CROSS_1 = 3;
	public static int ORDER_HOUSE_HOHENZOLLERN = 4;
	public static int POUR_LE_MERIT = 5;
	public static int ORDER_RED_EAGLE = 6;
	
	// Bavaria
    public static int B_PILOTS_BADGE = 11;
    public static int B_MEDAL_BRAVERY = 12;
	public static int B_MILITARY_MERIT = 13;
	public static int B_MAX_JOSEPH = 14;
	
	// Wurtemburg
	public static int W_MILITARY_MERIT = 20;
	public static int W_FREDRICH_ORDER = 21;
	
	// Saxony
	public static int S_WAR_MERIT_CROSS = 30;
	public static int S_ORDER_ALBERT = 31;
	public static int S_MILITARY_ORDER_ST_HENRY = 32;
	
	// Prussia
	public static int P_WAR_MERIT_MEDAL = 41;
	public static int P_MILITARY_MERIT_CROSS = 42;
	
	// Wound badges
	public static int WOUND_BADGE_BLACK = 51;
	public static int WOUND_BADGE_SILVER = 52;

	public static int NAVAL_WOUND_BADGE_BLACK = 53;
	public static int NAVAL_WOUND_BADGE_SILVER = 54;

	public GermanMedalManager (Campaign campaign)
    {
        super(campaign);

		
        medals.put(PILOTS_BADGE, new Medal ("Pilots Badge",                             "PB.jpg"));
        medals.put(IRON_CROSS_2, new Medal ("Iron Cross 2nd Class",                     "IC2.jpg"));
		medals.put(IRON_CROSS_1, new Medal ("Iron Cross 1st Class", 					"IC1.jpg"));
		medals.put(ORDER_HOUSE_HOHENZOLLERN, new Medal ("House Order of Hohenzollern", 	"HH.jpg"));
		medals.put(POUR_LE_MERIT, new Medal ("Pour Le Merit", 							"PLM.jpg"));
		medals.put(ORDER_RED_EAGLE, new Medal ("Order of the Red Eagle", 				"ore.jpg"));
		
        medals.put(B_PILOTS_BADGE, new Medal ("Bavarian Pilots Badge",                "B_PB.jpg"));
		medals.put(B_MEDAL_BRAVERY, new Medal ("Medal for Bravery", 		        "B_MB.jpg"));
		medals.put(B_MILITARY_MERIT, new Medal ("Bavarian Order of Military Merit",          "B_OMM.jpg"));
		medals.put(B_MAX_JOSEPH, new Medal ("Military Order of Maximillian Joseph", "B_MOoMJ.jpg"));
		
		medals.put(W_MILITARY_MERIT, new Medal ("Wurttemberg Order of Military Merit", 	"W_OMM.jpg"));
		medals.put(W_FREDRICH_ORDER, new Medal ("Freidrich Order", 			"W_FO.jpg"));

		medals.put(S_WAR_MERIT_CROSS, new Medal ("War Merit Cross", 					"S_WMC.jpg"));
		medals.put(S_ORDER_ALBERT, new Medal ("Order of Albert", 						"S_OA.jpg"));
		medals.put(S_MILITARY_ORDER_ST_HENRY, new Medal ("Military Order of St. Henry", "S_MOSH.jpg"));

		medals.put(P_WAR_MERIT_MEDAL, new Medal ("War Merit Medal", 			"P_WMM.jpg"));
		medals.put(P_MILITARY_MERIT_CROSS, new Medal ("Military Merit Cross", 	"P_MMC.jpg"));
		
		medals.put(WOUND_BADGE_BLACK, new Medal ("Wound Badge(Black)", 		"BWB.jpg"));
		medals.put(WOUND_BADGE_SILVER, new Medal ("Wound Badge(Silver)", 	"SWB.jpg"));
		
		medals.put(NAVAL_WOUND_BADGE_BLACK, new Medal ("N. Wound Badge(Black)", 	"NBWB.jpg"));
		medals.put(NAVAL_WOUND_BADGE_SILVER, new Medal ("N. Wound Badge(Silver)", 	"NSWB.jpg"));
	} 

	public Medal getWoundedAward(SquadronMember pilot, ArmedService service) 
	{
		if (service.getServiceId() == RoFServiceManager.DEUTSCHE_LUFTSTREITKRAFTE)
		{
		    if (!hasMedal(pilot, medals.get(WOUND_BADGE_BLACK)))
		    {
		        return medals.get(WOUND_BADGE_BLACK);
		    }
		    else if (!hasMedal(pilot, medals.get(WOUND_BADGE_SILVER)))
		    {
                return medals.get(WOUND_BADGE_SILVER);
		    }
		}
		
		if (service.getServiceId() == RoFServiceManager.MARINE_FLIEGER_CORP)
		{
            if (!hasMedal(pilot, medals.get(NAVAL_WOUND_BADGE_BLACK)))
            {
                return medals.get(NAVAL_WOUND_BADGE_BLACK);
            }
            else if (!hasMedal(pilot, medals.get(NAVAL_WOUND_BADGE_SILVER)))
            {
                return medals.get(NAVAL_WOUND_BADGE_SILVER);
            }
		}
		
		return null;
	}

    protected Medal awardWings(SquadronMember pilot) 
    {
        if (pilot.getPlayerRegion().equalsIgnoreCase(SquadronMember.BAVARIA))
        {
            if (!hasMedal(pilot, medals.get(B_PILOTS_BADGE)))
            {
                return medals.get(B_PILOTS_BADGE);
            }
        }
        else
        {
            if (!hasMedal(pilot, medals.get(PILOTS_BADGE)))
            {
                return medals.get(PILOTS_BADGE);
            }
        }
        
        return null;
    }

	protected Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
	{
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 1) && !hasMedal(pilot, medals.get(IRON_CROSS_2)))
		{
			return medals.get(IRON_CROSS_2);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 6)  && !hasMedal(pilot, medals.get(IRON_CROSS_1)))
		{
			return medals.get(IRON_CROSS_1);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 13) && !hasMedal(pilot, medals.get(ORDER_HOUSE_HOHENZOLLERN)))
		{
			return medals.get(ORDER_HOUSE_HOHENZOLLERN);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 20) && !hasMedal(pilot, medals.get(POUR_LE_MERIT)))
		{
			return medals.get(POUR_LE_MERIT);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 70) && !hasMedal(pilot, medals.get(ORDER_RED_EAGLE)))
		{
			return medals.get(ORDER_RED_EAGLE);
		}

		Medal medal = getPrussianMedal(pilot, service);
		if (medal != null)
		{
			return medal;
		}

		medal = getBavarianMedal(pilot);
		if (medal != null)
		{
			return medal;
		}

		medal = getWurttemburgMedal(pilot);
		if (medal != null)
		{
			return medal;
		}
		
		medal = getSaxonyMedal(pilot);
		
		return medal;
	}

	private Medal getPrussianMedal(SquadronMember pilot, ArmedService service)
	{
		if (pilot.getPlayerRegion().equalsIgnoreCase(SquadronMember.PRUSSIA))
		{
			if (!hasMedal(pilot, medals.get(P_WAR_MERIT_MEDAL)))
			{
				if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 4)
				{
					return medals.get(P_WAR_MERIT_MEDAL);
				}
			}
			if (!hasMedal(pilot, medals.get(P_MILITARY_MERIT_CROSS)))
			{
				if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 8)
				{
					
					IRankHelper rankObj = RankFactory.createRankHelper();
					int rankPos = rankObj.getRankPosByService(pilot.getRank(), service);
					if (rankPos >= 3)
					{
						return medals.get(P_MILITARY_MERIT_CROSS);
					}
				}
			}
		}
		
		return null;
	}

	private Medal getBavarianMedal(SquadronMember pilot)
	{
		if (pilot.getPlayerRegion().equalsIgnoreCase(SquadronMember.BAVARIA))
		{
			if (!hasMedal(pilot, medals.get(B_MEDAL_BRAVERY)))
			{
				if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 4)
				{
					return medals.get(B_MEDAL_BRAVERY);
				}
			}
			if (!hasMedal(pilot, medals.get(B_MILITARY_MERIT)))
			{
				if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 8)
				{
					return medals.get(B_MILITARY_MERIT);
				}
			}
			if (!hasMedal(pilot, medals.get(B_MAX_JOSEPH)))
			{
				if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 35)
				{
					return medals.get(B_MAX_JOSEPH);
				}
			}
		}

		return null;
	}

	private Medal getWurttemburgMedal(SquadronMember pilot)
	{
		if (pilot.getPlayerRegion().equalsIgnoreCase(SquadronMember.WURTTEMBURG))
		{
			if (!hasMedal(pilot, medals.get(W_MILITARY_MERIT)))
			{
				if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 8)
				{
					return medals.get(W_MILITARY_MERIT);
				}
			}
			if (!hasMedal(pilot, medals.get(W_FREDRICH_ORDER)))
			{
				if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 25)
				{
					return medals.get(W_FREDRICH_ORDER);
				}
			}
		}
		
		return null;
	}

	private Medal getSaxonyMedal(SquadronMember pilot)
	{
		if (pilot.getPlayerRegion().equalsIgnoreCase(SquadronMember.SAXONY))
		{
			if (!hasMedal(pilot, medals.get(S_WAR_MERIT_CROSS)))
			{
				if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 4)
				{
					return medals.get(S_WAR_MERIT_CROSS);
				}
			}
			if (!hasMedal(pilot, medals.get(S_ORDER_ALBERT)))
			{
				if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 8)
				{
					return medals.get(S_ORDER_ALBERT);
				}
			}
			if (!hasMedal(pilot, medals.get(S_MILITARY_ORDER_ST_HENRY)))
			{
				if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 30)
				{
					return medals.get(S_MILITARY_ORDER_ST_HENRY);
				}
			}
		}

		return null;
	}

    protected Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
    {
        if (!(hasMedal(pilot, medals.get(IRON_CROSS_2))))
        {
            if ((pilot.getMissionFlown() >= 15) || (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 1))
            {
                return medals.get(IRON_CROSS_2);
            }
        }
        
        // IC 1st
        if (!hasMedal(pilot, medals.get(IRON_CROSS_1)))
        {
            if (pilot.getMissionFlown() >= 50)
            {
                return medals.get(IRON_CROSS_1);
            }
            if (pilot.getMissionFlown() >= 30 && pilot.getGroundVictories().size() > 15)
            {
                return medals.get(IRON_CROSS_1);
            }
        }
        
        // HC
        if (!hasMedal(pilot, medals.get(ORDER_HOUSE_HOHENZOLLERN)))
        {
            if (pilot.getMissionFlown() >= 150)
            {
                return medals.get(ORDER_HOUSE_HOHENZOLLERN);
            }
            if (pilot.getMissionFlown() >= 70 && pilot.getGroundVictories().size() > 30)
            {
                return medals.get(ORDER_HOUSE_HOHENZOLLERN);
            }
        }
        // PLM
        if (!hasMedal(pilot, medals.get(POUR_LE_MERIT)))
        {
            if (pilot.getMissionFlown() >= 70 && pilot.getGroundVictories().size() > 75)
            {
                return medals.get(POUR_LE_MERIT);
            }
        }
        // Prussian
        if (pilot.getPlayerRegion().equalsIgnoreCase(SquadronMember.PRUSSIA))
        {
            // War Merit Medal
            if (!hasMedal(pilot, medals.get(P_WAR_MERIT_MEDAL)))
            {
                if (pilot.getMissionFlown() >= 20 && pilot.getGroundVictories().size() > 5)
                {
                    return medals.get(P_WAR_MERIT_MEDAL);
                }
            }
            // Military Merit Cross
            if (!hasMedal(pilot, medals.get(P_MILITARY_MERIT_CROSS)))
            {
                if (pilot.getMissionFlown() >= 30 && pilot.getGroundVictories().size() > 25)
                {
                    IRankHelper rankObj = RankFactory.createRankHelper();
                    int rankPos = rankObj.getRankPosByService(pilot.getRank(), service);
                    if (rankPos >= 3)
                    {
                        return medals.get(P_MILITARY_MERIT_CROSS);
                    }
                }
            }
        }
        // Bavarian
        if (pilot.getPlayerRegion().equalsIgnoreCase(SquadronMember.BAVARIA))
        {
            // Medal for Bravery
            if (!hasMedal(pilot, medals.get(B_MEDAL_BRAVERY)))
            {
                if (pilot.getMissionFlown() >= 20 && pilot.getGroundVictories().size() > 15)
                {
                    return medals.get(B_MEDAL_BRAVERY);
                }
            }
            // Military Merit
            if (!hasMedal(pilot, medals.get(B_MILITARY_MERIT)))
            {
                if (pilot.getMissionFlown() >= 50 && pilot.getGroundVictories().size() > 25)
                {
                    return medals.get(B_MILITARY_MERIT);
                }
            }
            // Max Joseph
            if (!hasMedal(pilot, medals.get(B_MAX_JOSEPH)))
            {
                if (pilot.getMissionFlown() >= 100 && pilot.getGroundVictories().size() > 90)
                {
                    return medals.get(B_MILITARY_MERIT);
                }
            }
        }
        // Wurtemburg
        if (pilot.getPlayerRegion().equalsIgnoreCase(SquadronMember.WURTTEMBURG))
        {
            // Military Merit
            if (!hasMedal(pilot, medals.get(W_MILITARY_MERIT)))
            {
                if (pilot.getMissionFlown() >= 50 && pilot.getGroundVictories().size() > 25)
                {
                    return medals.get(W_MILITARY_MERIT);
                }
            }
            // Freidrich Order
            if (!hasMedal(pilot, medals.get(W_FREDRICH_ORDER)))
            {
                if (pilot.getMissionFlown() >= 100 && pilot.getGroundVictories().size() > 90)
                {
                    return medals.get(W_FREDRICH_ORDER);
                }
            }
        }
        // Saxony
        if (pilot.getPlayerRegion().equalsIgnoreCase(SquadronMember.SAXONY))
        {
            // Military Merit
            if (!hasMedal(pilot, medals.get(S_WAR_MERIT_CROSS)))
            {
                if (pilot.getMissionFlown() >= 20 && pilot.getGroundVictories().size() > 15)
                {
                    return medals.get(S_WAR_MERIT_CROSS);
                }
            }
            // Military Merit
            if (!hasMedal(pilot, medals.get(S_ORDER_ALBERT)))
            {
                if (pilot.getMissionFlown() >= 50 && pilot.getGroundVictories().size() > 25)
                {
                    return medals.get(S_ORDER_ALBERT);
                }
            }
            // St Henry Order 
            if (!hasMedal(pilot, medals.get(S_MILITARY_ORDER_ST_HENRY)))
            {
                if (pilot.getMissionFlown() >= 100 && pilot.getGroundVictories().size() > 90)
                {
                    return medals.get(W_FREDRICH_ORDER);
                }
            }
        }
        
        // If we made no awards based on recon criteria, maybe we award based on scout criteria
        return awardFighter(pilot, service, victoriesThisMission);
    }
}
