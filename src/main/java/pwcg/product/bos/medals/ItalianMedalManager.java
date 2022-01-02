package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;

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

		
        medals.put(PILOTS_BADGE, new Medal ("CrewMembers Badge",                                "ita_crewMembers_badge.png"));
        medals.put(WOUND_STRIPE, new Medal ("Wound Stripe",                                "ita_wound_stripe.png"));
        
        medals.put(MEDAL_MILITARY_VALOR_BRONZE, new Medal ("Al Valore Militar Bronze",     "ita_al_valore_militar_bronze.png"));
        medals.put(MEDAL_MILITARY_VALOR_SILVER, new Medal ("Al Valore Militar Silver",     "ita_al_valore_militar_silver.png"));
        medals.put(MEDAL_MILITARY_VALOR_GOLD, new Medal ("Al Valore Militar Gold",         "ita_al_valore_militar_gold.png"));
        medals.put(CROSS_WAR_MERIT, new Medal ("Cross of Merit of War",                    "ita_cross_of_merit_war.png"));
        medals.put(CROSS_MILITARY_VALOR, new Medal ("Cross of Military Valour",            "ita_cross_of_merit_valor.png"));
	} 

	public Medal awardWoundedAward(CrewMember crewMember, ArmedService service) 
	{
        return medals.get(WOUND_STRIPE);
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
		if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 1) && !hasMedal(crewMember, medals.get(MEDAL_MILITARY_VALOR_BRONZE)))
		{
			return medals.get(MEDAL_MILITARY_VALOR_BRONZE);
		}

		if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 5)  && !hasMedal(crewMember, medals.get(MEDAL_MILITARY_VALOR_SILVER)))
		{
			return medals.get(MEDAL_MILITARY_VALOR_SILVER);
		}

		if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 13) && !hasMedal(crewMember, medals.get(MEDAL_MILITARY_VALOR_GOLD)))
		{
			return medals.get(MEDAL_MILITARY_VALOR_GOLD);
		}

		if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 18) && !hasMedal(crewMember, medals.get(CROSS_WAR_MERIT)))
		{
			return medals.get(CROSS_WAR_MERIT);
		}

        if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 25) && !hasMedal(crewMember, medals.get(CROSS_MILITARY_VALOR)))
        {
            return medals.get(CROSS_MILITARY_VALOR);
        }

		return null;
	}

    protected Medal awardBomber(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        int numCrewMemberGroundVictoryPoints = crewMember.getCrewMemberVictories().getGroundVictoryPointTotal();

        if ((crewMember.getBattlesFought() >= 15) && !hasMedal(crewMember, medals.get(MEDAL_MILITARY_VALOR_BRONZE)))
        {
            return medals.get(MEDAL_MILITARY_VALOR_BRONZE);
        }
        
        if (!hasMedal(crewMember, medals.get(MEDAL_MILITARY_VALOR_SILVER)))
        {
            if (crewMember.getBattlesFought() >= 40)
            {
                return medals.get(MEDAL_MILITARY_VALOR_SILVER);
            }
            if (crewMember.getBattlesFought() >= 20 && numCrewMemberGroundVictoryPoints > 15)
            {
                return medals.get(MEDAL_MILITARY_VALOR_SILVER);
            }
        }
        
        if (crewMember.getBattlesFought() >= 80 && numCrewMemberGroundVictoryPoints > 40)
        {
            return medals.get(MEDAL_MILITARY_VALOR_GOLD);
        }

        if (!hasMedal(crewMember, medals.get(CROSS_WAR_MERIT)))
        {
            if (crewMember.getBattlesFought() >= 100 && numCrewMemberGroundVictoryPoints > 60)
            {
                return medals.get(CROSS_WAR_MERIT);
            }
        }

        if (!hasMedal(crewMember, medals.get(CROSS_MILITARY_VALOR)))
        {
            if (crewMember.getBattlesFought() >= 100 && numCrewMemberGroundVictoryPoints > 100)
            {
                return medals.get(CROSS_MILITARY_VALOR);
            }
        }
        
        return awardFighter(crewMember, service, victoriesThisMission);
    }
}
