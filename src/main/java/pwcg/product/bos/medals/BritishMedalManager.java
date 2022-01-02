package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;

public class BritishMedalManager extends BoSMedalManager
{
    public static int PILOTS_BADGE = 1;

    public static int DFC = 2;
    public static int DFC_BAR_1 = 3;
    public static int DSO = 5;
    public static int DSO_BAR = 6;
    public static int VC = 9;

    public static int WOUND_STRIPE = 20;

    BritishMedalManager(Campaign campaign)
    {
        super(campaign);

        medals.put(PILOTS_BADGE, new Medal("CrewMembers Badge", "gb_crewMembers_badge.png"));
        medals.put(DFC, new Medal(DISTINGUISHED_FLYING_CROSS_NAME, "gb_distinguished_flying_cross.png"));
        medals.put(DFC_BAR_1, new Medal(DISTINGUISHED_FLYING_CROSS_NAME + " With Bar", "gb_distinguished_flying_cross_bar.png"));
        medals.put(DSO, new Medal (DISTINGUISHED_SERVICE_ORDER_NAME,                    "gb_distinguished_service_order.png"));
        medals.put(DSO_BAR, new Medal (DISTINGUISHED_SERVICE_ORDER_NAME + " With Bar",      "gb_distinguished_service_order_bar.png"));
        medals.put(VC, new Medal("Victoria Cross", "gb_victoria_cross.png"));

        medals.put(WOUND_STRIPE, new Medal("Wound Stripe", "gb_wound_chevron.png"));
    }

    protected Medal awardWings(CrewMember crewMember)
    {
        if (!hasMedal(crewMember, medals.get(PILOTS_BADGE)))
        {
            return medals.get(PILOTS_BADGE);
        }

        return null;
    }

    public Medal awardWoundedAward(CrewMember crewMember, ArmedService service)
    {
        return medals.get(WOUND_STRIPE);
    }

    public Medal awardFighter(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException
    {
        if (crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 5 && !hasMedal(crewMember, medals.get(DFC)))
        {
            return medals.get(DFC);
        }

        if (crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 10 && hasMedal(crewMember, medals.get(DFC)) && !hasMedal(crewMember, medals.get(DFC_BAR_1)))
        {
            if (victoriesThisMission >= 1)
            {
                return medals.get(DFC_BAR_1);
            }
        }

        if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 15) && !hasMedal(crewMember, medals.get(DSO)))
        {
            return medals.get(DSO);
        }

        if ((crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 25) && !hasMedal(crewMember, medals.get(DSO_BAR)))
        {
            return medals.get(DSO_BAR);            
        }

        if (!hasMedal(crewMember, medals.get(VC)))
        {
            if (crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 40)
            {
                if (victoriesThisMission >= 3)
                {
                    return medals.get(VC);
                }
            }
            if (crewMember.getCrewMemberVictories().getAirToAirVictoryCount() >= 30)
            {
                if (victoriesThisMission >= 5)
                {
                    return medals.get(VC);
                }
            }
        }

        return null;
    }

    public Medal awardBomber(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException
    {
        int numCrewMemberGroundVictoryPoints = crewMember.getCrewMemberVictories().getGroundVictoryPointTotal();

        if (numCrewMemberGroundVictoryPoints >= 15 && !hasMedal(crewMember, medals.get(DFC)))
        {
            if (crewMember.getBattlesFought() >= 30)
            {
                if (numCrewMemberGroundVictoryPoints > 50)
                {
                    return medals.get(DFC);
                }
            }
        }

        if (numCrewMemberGroundVictoryPoints >= 25 && !hasMedal(crewMember, medals.get(DFC_BAR_1)))
        {
            if (crewMember.getBattlesFought() >= 50)
            {
                if (numCrewMemberGroundVictoryPoints > 70)
                {
                    return medals.get(DFC_BAR_1);
                }
            }
        }

        if (numCrewMemberGroundVictoryPoints >= 50 && !hasMedal(crewMember, medals.get(DSO)))
        {
            if (crewMember.getBattlesFought() >= 70)
            {
                if (numCrewMemberGroundVictoryPoints > 90)
                {
                    return medals.get(DSO);
                }
            }
        }

        if (numCrewMemberGroundVictoryPoints >= 75 && !hasMedal(crewMember, medals.get(DSO_BAR)))
        {
            if (crewMember.getBattlesFought() >= 100)
            {
                if (numCrewMemberGroundVictoryPoints > 120)
                {
                    return medals.get(DSO_BAR);
                }
            }
        }

        if (numCrewMemberGroundVictoryPoints >= 120 && !hasMedal(crewMember, medals.get(VC)))
        {
            if (crewMember.getBattlesFought() >= 200)
            {
                if (numCrewMemberGroundVictoryPoints > 400)
                {
                    return medals.get(VC);
                }
            }
        }

        return awardFighter(crewMember, service, victoriesThisMission);
    }
}
