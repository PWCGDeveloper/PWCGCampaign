package pwcg.product.bos.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
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

        medals.put(PILOTS_BADGE, new Medal("Pilots Badge", "gb_pilots_badge.png"));
        medals.put(DFC, new Medal(DISTINGUISHED_FLYING_CROSS_NAME, "gb_distinguished_flying_cross.png"));
        medals.put(DFC_BAR_1, new Medal(DISTINGUISHED_FLYING_CROSS_NAME + " With Bar", "gb_distinguished_flying_cross_bar.png"));
        medals.put(DSO, new Medal (DISTINGUISHED_SERVICE_ORDER_NAME,                    "gb_distinguished_service_order.png"));
        medals.put(DSO_BAR, new Medal (DISTINGUISHED_SERVICE_ORDER_NAME + " With Bar",      "gb_distinguished_service_order_bar.png"));
        medals.put(VC, new Medal("Victoria Cross", "gb_victoria_cross.png"));

        medals.put(WOUND_STRIPE, new Medal("Wound Stripe", "gb_wound_chevron.png"));
    }

    protected Medal awardWings(SquadronMember pilot)
    {
        if (!hasMedal(pilot, medals.get(PILOTS_BADGE)))
        {
            return medals.get(PILOTS_BADGE);
        }

        return null;
    }

    public Medal awardWoundedAward(SquadronMember pilot, ArmedService service)
    {
        return medals.get(WOUND_STRIPE);
    }

    public Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException
    {
        if (pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 5 && !hasMedal(pilot, medals.get(DFC)))
        {
            return medals.get(DFC);
        }

        if (pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 10 && hasMedal(pilot, medals.get(DFC)) && !hasMedal(pilot, medals.get(DFC_BAR_1)))
        {
            if (victoriesThisMission >= 1)
            {
                return medals.get(DFC_BAR_1);
            }
        }

        if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 15) && !hasMedal(pilot, medals.get(DSO)))
        {
            return medals.get(DSO);
        }

        if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 25) && !hasMedal(pilot, medals.get(DSO_BAR)))
        {
            return medals.get(DSO_BAR);            
        }

        if (!hasMedal(pilot, medals.get(VC)))
        {
            if (pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 40)
            {
                if (victoriesThisMission >= 3)
                {
                    return medals.get(VC);
                }
            }
            if (pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 30)
            {
                if (victoriesThisMission >= 5)
                {
                    return medals.get(VC);
                }
            }
        }

        return null;
    }

    public Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException
    {
        int numPilotGroundVictoryPoints = pilot.getSquadronMemberVictories().getGroundVictoryPointTotal();

        if (numPilotGroundVictoryPoints >= 15 && !hasMedal(pilot, medals.get(DFC)))
        {
            if (pilot.getMissionFlown() >= 30)
            {
                if (numPilotGroundVictoryPoints > 50)
                {
                    return medals.get(DFC);
                }
            }
        }

        if (numPilotGroundVictoryPoints >= 25 && !hasMedal(pilot, medals.get(DFC_BAR_1)))
        {
            if (pilot.getMissionFlown() >= 50)
            {
                if (numPilotGroundVictoryPoints > 70)
                {
                    return medals.get(DFC_BAR_1);
                }
            }
        }

        if (numPilotGroundVictoryPoints >= 50 && !hasMedal(pilot, medals.get(DSO)))
        {
            if (pilot.getMissionFlown() >= 70)
            {
                if (numPilotGroundVictoryPoints > 90)
                {
                    return medals.get(DSO);
                }
            }
        }

        if (numPilotGroundVictoryPoints >= 75 && !hasMedal(pilot, medals.get(DSO_BAR)))
        {
            if (pilot.getMissionFlown() >= 100)
            {
                if (numPilotGroundVictoryPoints > 120)
                {
                    return medals.get(DSO_BAR);
                }
            }
        }

        if (numPilotGroundVictoryPoints >= 120 && !hasMedal(pilot, medals.get(VC)))
        {
            if (pilot.getMissionFlown() >= 200)
            {
                if (numPilotGroundVictoryPoints > 400)
                {
                    return medals.get(VC);
                }
            }
        }

        return awardFighter(pilot, service, victoriesThisMission);
    }
}
