package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;

public class ItalianMedalManager extends BoSMedalManager 
{
    public static int PILOTS_BADGE = 1;
    public static int MEDAL_MILITARY_VALOR_BRONZE = 4;
    public static int MEDAL_MILITARY_VALOR_SILVER = 5;
    public static int MEDAL_MILITARY_VALOR_GOLD = 6;
	public static int CROSS_WAR_MERIT = 7;
	public static int CROSS_MILITARY_VALOR = 8;
	
	public static int WOUND_STRIPE = 99;

	ItalianMedalManager (Campaign campaign)
    {
        super(campaign);

		
        medals.put(PILOTS_BADGE, new Medal ("Pilots Badge",                                "ita_pilots_badge.png"));
        medals.put(WOUND_STRIPE, new Medal ("Wound Stripe",                                "ita_wound_stripe.png"));
        
        medals.put(MEDAL_MILITARY_VALOR_BRONZE, new Medal ("Al Valore Militar Bronze",     "ita_al_valore_militar_bronze.png"));
        medals.put(MEDAL_MILITARY_VALOR_SILVER, new Medal ("Al Valore Militar Silver",     "ita_al_valore_militar_silver.png"));
        medals.put(MEDAL_MILITARY_VALOR_GOLD, new Medal ("Al Valore Militar Gold",         "ita_al_valore_militar_gold.png"));
        medals.put(CROSS_WAR_MERIT, new Medal ("Cross of Merit of War",                    "ita_cross_of_merit_war.png"));
        medals.put(CROSS_MILITARY_VALOR, new Medal ("Cross of Military Valour",            "ita_cross_of_merit_valor.png"));
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

	protected Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
	{
		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 1) && !hasMedal(pilot, medals.get(MEDAL_MILITARY_VALOR_BRONZE)))
		{
			return medals.get(MEDAL_MILITARY_VALOR_BRONZE);
		}

		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 5)  && !hasMedal(pilot, medals.get(MEDAL_MILITARY_VALOR_SILVER)))
		{
			return medals.get(MEDAL_MILITARY_VALOR_SILVER);
		}

		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 13) && !hasMedal(pilot, medals.get(MEDAL_MILITARY_VALOR_GOLD)))
		{
			return medals.get(MEDAL_MILITARY_VALOR_GOLD);
		}

		if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 18) && !hasMedal(pilot, medals.get(CROSS_WAR_MERIT)))
		{
			return medals.get(CROSS_WAR_MERIT);
		}

        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 25) && !hasMedal(pilot, medals.get(CROSS_MILITARY_VALOR)))
        {
            return medals.get(CROSS_MILITARY_VALOR);
        }

		return null;
	}

    protected Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) 
    {
        // MMV Bronze
        if ((pilot.getMissionFlown() >= 15) && !hasMedal(pilot, medals.get(MEDAL_MILITARY_VALOR_BRONZE)))
        {
            return medals.get(MEDAL_MILITARY_VALOR_BRONZE);
        }
        
        if (!hasMedal(pilot, medals.get(MEDAL_MILITARY_VALOR_SILVER)))
        {
            if (pilot.getMissionFlown() >= 40)
            {
                return medals.get(MEDAL_MILITARY_VALOR_SILVER);
            }
            if (pilot.getMissionFlown() >= 20 && pilot.getGroundVictories().size() > 15)
            {
                return medals.get(MEDAL_MILITARY_VALOR_SILVER);
            }
        }
        
        if (pilot.getMissionFlown() >= 80 && pilot.getGroundVictories().size() > 40)
        {
            return medals.get(MEDAL_MILITARY_VALOR_GOLD);
        }

        if (!hasMedal(pilot, medals.get(CROSS_WAR_MERIT)))
        {
            if (pilot.getMissionFlown() >= 100 && pilot.getGroundVictories().size() > 60)
            {
                return medals.get(CROSS_WAR_MERIT);
            }
        }

        if (!hasMedal(pilot, medals.get(CROSS_MILITARY_VALOR)))
        {
            if (pilot.getMissionFlown() >= 100 && pilot.getGroundVictories().size() > 100)
            {
                return medals.get(CROSS_MILITARY_VALOR);
            }
        }
        
        return awardFighter(pilot, service, victoriesThisMission);
    }
}
