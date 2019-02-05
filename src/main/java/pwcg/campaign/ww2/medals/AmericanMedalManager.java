package pwcg.campaign.ww2.medals;

import java.util.ArrayList;
import java.util.List;

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
	        
        medals.put(PILOTS_BADGE, new Medal ("Pilots Badge",                                 "us_pb.jpg"));
		medals.put(BRONZE_STAR, new Medal ("Bronze Star",	                                "us_bs.jpg"));
		medals.put(DISTINGUISHED_FLYING_CROSS, new Medal ("Distinguished Flying Cross",	    "us_dfc.jpg"));
        medals.put(SILVER_STAR, new Medal ("Silver Star",                                   "us_ss.jpg"));
        medals.put(DISTINGUISHED_SERVICE_CROSS, new Medal ("Distinguished Service Cross",   "us_dsc.jpg"));
		medals.put(MEDAL_OF_HONOR, new Medal ("Medal of Honor",							    "MoH.jpg"));
		
		medals.put(PURPLE_HEART, new Medal ("Purple Heart", 							    "WoundChev.jpg"));
	} 

    protected Medal awardWings(SquadronMember pilot) 
    {
        if (!hasMedal(pilot, medals.get(PILOTS_BADGE)))
        {
            return medals.get(PILOTS_BADGE);
        }
        
        return null;
    }

	public Medal getWoundedAward(SquadronMember pilot, ArmedService service) 
	{
		return medals.get(PURPLE_HEART);
	}

	public Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
	{
        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 3) && !hasMedal(pilot, medals.get(BRONZE_STAR)))
        {
            return medals.get(BRONZE_STAR);
        }
        else if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 6) && !hasMedal(pilot, medals.get(DISTINGUISHED_FLYING_CROSS)))
		{
			return medals.get(DISTINGUISHED_FLYING_CROSS);
		}
        else if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 15 && !hasMedal(pilot, medals.get(SILVER_STAR)))
        {
            return medals.get(SILVER_STAR);
        }
        else if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 20 && !hasMedal(pilot, medals.get(DISTINGUISHED_SERVICE_CROSS)))
        {
            return medals.get(DISTINGUISHED_SERVICE_CROSS);
        }
		else
		{
			if (!hasMedal(pilot, medals.get(MEDAL_OF_HONOR)))
			{
				if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 15))
				{
					if (victoriesThisMission >= 3)
					{
						return medals.get(MEDAL_OF_HONOR);
					}
				}
				else if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 20))
				{
					if (victoriesThisMission >= 2)
					{
						return medals.get(MEDAL_OF_HONOR);
					}
				}
                else if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 30))
                {
                    return medals.get(MEDAL_OF_HONOR);
                }
			}
		}
		
		return null;
	}

    public Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
    {
        if (!hasMedal(pilot, medals.get(BRONZE_STAR)))
        {
            if (pilot.getMissionFlown() >= 15 || pilot.getGroundVictories().size() > 5)
            {
                return medals.get(BRONZE_STAR);
            }
        }
        else if (!hasMedal(pilot, medals.get(DISTINGUISHED_FLYING_CROSS)))
        {
            if (pilot.getMissionFlown() >= 25 || pilot.getGroundVictories().size() > 10)
            {
                return medals.get(DISTINGUISHED_FLYING_CROSS);
            }
        }
        else if (!hasMedal(pilot, medals.get(SILVER_STAR)))
        {
            if (pilot.getMissionFlown() >= 30 || pilot.getGroundVictories().size() > 25)
            {
                return medals.get(SILVER_STAR);
            }
        }
        else if (!hasMedal(pilot, medals.get(SILVER_STAR)))
        {
            if (pilot.getMissionFlown() >= 30 || pilot.getGroundVictories().size() > 25)
            {
                return medals.get(SILVER_STAR);
            }
        }
        else if (!hasMedal(pilot, medals.get(DISTINGUISHED_SERVICE_CROSS)))
        {
            if (pilot.getMissionFlown() >= 50 || pilot.getGroundVictories().size() > 50)
            {
                return medals.get(DISTINGUISHED_SERVICE_CROSS);
            }
        }
        else if (!hasMedal(pilot, medals.get(MEDAL_OF_HONOR)))
        {
            if (pilot.getMissionFlown() >= 100 || pilot.getGroundVictories().size() > 100)
            {
                return medals.get(MEDAL_OF_HONOR);
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
        medalsInOrder.add( medals.get(BRONZE_STAR));
        medalsInOrder.add( medals.get(DISTINGUISHED_FLYING_CROSS));
        medalsInOrder.add( medals.get(SILVER_STAR));
		medalsInOrder.add( medals.get(DISTINGUISHED_SERVICE_CROSS));
		medalsInOrder.add( medals.get(MEDAL_OF_HONOR));
		
		return medalsInOrder;
	}

	@Override
	public List<Medal> getWoundBadgesInOrder()
	{
		List<Medal> medalsInOrder = new ArrayList<>();
		medalsInOrder.add( medals.get(PURPLE_HEART));		
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
