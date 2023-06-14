package pwcg.product.fc.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class BelgianMedalManager extends FCMedalManager 
{
    public static int PILOTS_BADGE = 1;

	public static int MILITARY_MEDAL = 2;
	public static int CROIX_DE_GUERRE = 3;
	public static int CROIX_DE_GUERRE_BRONZE_STAR = 4;
	public static int CROIX_DE_GUERRE_SILVER_STAR = 5;
	public static int CROIX_DE_GUERRE_GILT_STAR = 6;
	public static int CROIX_DE_GUERRE_BRONZE_PALM = 7;
	public static int CROIX_DE_GUERRE_SILVER_PALM = 8;
	public static int MEDAILLE_DE_HONNEUR = 9;
	public static int LEGION_DE_HONNEUR = 10;

	public static int DFC = 11;
	public static int DFC_BAR_1 = 12;
	public static int DFC_BAR_2 = 13;
	public static int DSO = 14;
	public static int DSO_BAR = 15;
	public static int DSC =16;  // Naval medal.  Not given out but used for historical aces
	public static int VC = 17;
	public static int MC = 18;

	public static int BEL_CROIX_DE_GUERRE = 19;
	public static int BEL_ORDRE_DE_LA_COURONNE = 20;
	public static int BEL_ORDRE_DE_LEOPOLD = 21;
	

	public static int BEL_WOUND_BADGE = 30;


	BelgianMedalManager (Campaign campaign)
    {
        super(campaign);
        

        medals.put(PILOTS_BADGE, new Medal ("Pilots Badge",                                             "bel_pilot_badge.png"));
        medals.put(MILITARY_MEDAL, new Medal ("Medaille Militaire",                                     "fr_medaille_militaire.png"));
        medals.put(CROIX_DE_GUERRE, new Medal (CROIX_DE_GUERRE_NAME,                                    "fr_croix_de_guerre.png"));
        medals.put(CROIX_DE_GUERRE_BRONZE_STAR, new Medal (CROIX_DE_GUERRE_NAME + " with Bronze Star",  "fr_croix_de_guerre_bronze_star.png"));
        medals.put(CROIX_DE_GUERRE_SILVER_STAR, new Medal (CROIX_DE_GUERRE_NAME + " with Silver Star",  "fr_croix_de_guerre_silver_star.png"));
        medals.put(CROIX_DE_GUERRE_GILT_STAR, new Medal (CROIX_DE_GUERRE_NAME + " with Gilt Star",      "fr_croix_de_guerre_gilt_star.png"));
        medals.put(CROIX_DE_GUERRE_BRONZE_PALM, new Medal (CROIX_DE_GUERRE_NAME + " with Bronze Palm",  "fr_croix_de_guerre_bronze_palm.png"));
        medals.put(CROIX_DE_GUERRE_SILVER_PALM, new Medal (CROIX_DE_GUERRE_NAME + " with Silver Palm",  "fr_croix_de_guerre_silver_palm.png"));
        medals.put(MEDAILLE_DE_HONNEUR, new Medal ("Medaille d' Honneur",                               "fr_medaille_d_honneur.png"));
        medals.put(LEGION_DE_HONNEUR, new Medal ("Legion d' Honneur",                                   "fr_legion_d_honneur.png"));
        
        medals.put(DFC, new Medal (DISTINGUISHED_FLYING_CROSS_NAME,                                     "gb_distinguished_flying_cross.png"));
        medals.put(DFC_BAR_1, new Medal (DISTINGUISHED_FLYING_CROSS_NAME + " With Bar",                 "gb_distinguished_flying_cross_bar.png"));
        medals.put(DFC_BAR_2, new Medal (DISTINGUISHED_FLYING_CROSS_NAME + " With 2 Bars",              "gb_distinguished_flying_cross_2_bars.png"));
        medals.put(DSO, new Medal (DISTINGUISHED_SERVICE_ORDER_NAME,                                    "gb_distinguished_service_order.png"));
        medals.put(DSO_BAR, new Medal (DISTINGUISHED_SERVICE_ORDER_NAME + " With Bar",                  "gb_distinguished_service_order_bar.png"));
        medals.put(DSC, new Medal ("Distinguished Service Cross",                                       "gb_dsc.png"));
        medals.put(VC, new Medal ("Victoria Cross",                                                     "gb_victoria_cross.png"));
        medals.put(MC, new Medal ("Military Cross",                                                     "gb_military_cross.png"));

        medals.put(BEL_CROIX_DE_GUERRE, new Medal ("Bel. Croix de Guerre",                                   "bel_croix_de_guerre.png"));
        medals.put(BEL_ORDRE_DE_LA_COURONNE, new Medal ("Ordre de la Couronne",                         "bel_couronne.png"));
        medals.put(BEL_ORDRE_DE_LEOPOLD, new Medal ("Ordre de Leopold",                                 "bel_order_of_leopold.png"));

        medals.put(BEL_WOUND_BADGE, new Medal ("Belgium Wound Badge", 		"bel_wound_badge.png"));
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
		return medals.get(BEL_WOUND_BADGE);
	}

	public Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException 
	{
	    int pilotVictories = pilot.getSquadronMemberVictories().getAirToAirVictoryCount();
		if ((pilotVictories >= 2) && !hasMedal(pilot, medals.get(MILITARY_MEDAL)))
		{
			return medals.get(MILITARY_MEDAL);
		}
		if ((pilotVictories >= 3) && !hasMedal(pilot, medals.get(BEL_CROIX_DE_GUERRE)))
		{
			return medals.get(BEL_CROIX_DE_GUERRE);
		}
		if ((pilotVictories >= 5) && !hasMedal(pilot, medals.get(CROIX_DE_GUERRE)))
		{
			return medals.get(CROIX_DE_GUERRE);
		}
		if ((pilotVictories >= 7) && !hasMedal(pilot, medals.get(CROIX_DE_GUERRE_BRONZE_STAR)))
		{
			return medals.get(CROIX_DE_GUERRE_BRONZE_STAR);
		}
		if ((pilotVictories >= 8) && !hasMedal(pilot, medals.get(BEL_ORDRE_DE_LA_COURONNE)))
		{
			return medals.get(BEL_ORDRE_DE_LA_COURONNE);
		}
		if ((pilotVictories >= 10) && !hasMedal(pilot, medals.get(MEDAILLE_DE_HONNEUR)))
		{
			return medals.get(MEDAILLE_DE_HONNEUR);
		}
		if ((pilotVictories >= 12) && !hasMedal(pilot, medals.get(BEL_ORDRE_DE_LEOPOLD)))
		{
			return medals.get(BEL_ORDRE_DE_LEOPOLD);
		}
		if ((pilotVictories >= 30) && !hasMedal(pilot, medals.get(LEGION_DE_HONNEUR)))
		{
			return medals.get(LEGION_DE_HONNEUR);
		}
		
		return null;
	}

    public Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        int numPilotGroundVictoryPoints = pilot.getSquadronMemberVictories().getGroundVictoryPointTotal();
        if (!hasMedal(pilot, medals.get(MILITARY_MEDAL)))
        {
            if (pilot.getMissionFlown() >= 10)
            {
                return medals.get(MILITARY_MEDAL);
            }
        }
        if (!hasMedal(pilot, medals.get(BEL_CROIX_DE_GUERRE)))
        {
            if (pilot.getMissionFlown() >= 10 && numPilotGroundVictoryPoints > 10)
            {
                return medals.get(BEL_CROIX_DE_GUERRE);
            }
        }
        if (!hasMedal(pilot, medals.get(CROIX_DE_GUERRE)))
        {
            if (pilot.getMissionFlown() >= 15 && numPilotGroundVictoryPoints > 12)
            {
                return medals.get(CROIX_DE_GUERRE);
            }
        }
        if (!hasMedal(pilot, medals.get(CROIX_DE_GUERRE_BRONZE_STAR)))
        {
            if (pilot.getMissionFlown() >= 20 && numPilotGroundVictoryPoints > 15)
            {
                return medals.get(CROIX_DE_GUERRE_BRONZE_STAR);
            }
        }
        if (!hasMedal(pilot, medals.get(CROIX_DE_GUERRE_SILVER_STAR)))
        {
            if (pilot.getMissionFlown() >= 30 && numPilotGroundVictoryPoints > 20)
            {
                return medals.get(CROIX_DE_GUERRE_SILVER_STAR);
            }
        }
        if (!hasMedal(pilot, medals.get(BEL_ORDRE_DE_LA_COURONNE)))
        {
            if (pilot.getMissionFlown() >= 35 && numPilotGroundVictoryPoints > 23)
            {
                return medals.get(BEL_ORDRE_DE_LA_COURONNE);
            }
        }
        if (!hasMedal(pilot, medals.get(MEDAILLE_DE_HONNEUR)))
        {
            if (pilot.getMissionFlown() >= 40 && numPilotGroundVictoryPoints > 25)
            {
                return medals.get(MEDAILLE_DE_HONNEUR);
            }
        }
        if (!hasMedal(pilot, medals.get(CROIX_DE_GUERRE_GILT_STAR)))
        {
            if (pilot.getMissionFlown() >= 45 && numPilotGroundVictoryPoints > 30)
            {
                return medals.get(CROIX_DE_GUERRE_GILT_STAR);
            }
        }
        if (!hasMedal(pilot, medals.get(CROIX_DE_GUERRE_BRONZE_PALM)))
        {
            if (pilot.getMissionFlown() >= 60 && numPilotGroundVictoryPoints > 45)
            {
                return medals.get(CROIX_DE_GUERRE_BRONZE_PALM);
            }
        }
        if (!hasMedal(pilot, medals.get(CROIX_DE_GUERRE_SILVER_PALM)))
        {
            if (pilot.getMissionFlown() >= 70 && numPilotGroundVictoryPoints > 60)
            {
                return medals.get(CROIX_DE_GUERRE_SILVER_PALM);
            }
        }
        if (hasMedal(pilot, medals.get(BEL_ORDRE_DE_LEOPOLD)))
        {
            if (pilot.getMissionFlown() >= 80 && numPilotGroundVictoryPoints > 70)
            {
                return medals.get(BEL_ORDRE_DE_LEOPOLD);
            }
        }
        if (!hasMedal(pilot, medals.get(LEGION_DE_HONNEUR)))
        {
            if (pilot.getMissionFlown() >= 100 && numPilotGroundVictoryPoints > 80)
            {
                return medals.get(LEGION_DE_HONNEUR);
            }
        }
        
        return awardFighter(pilot, service, victoriesThisMission);
    }
}
