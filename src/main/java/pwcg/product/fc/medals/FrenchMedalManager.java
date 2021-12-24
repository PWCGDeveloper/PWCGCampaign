package pwcg.product.fc.medals;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class FrenchMedalManager extends FCMedalManager 
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

	public static int BEL_CROIX_DE_GUERRE = 20;
	public static int BEL_ORDRE_DE_LA_COURONNE = 21;
	public static int BEL_ORDRE_DE_LEOPOLD = 22;

	public static int DFC = 30;
	public static int DFC_BAR_1 = 31;
	public static int DFC_BAR_2 = 32;
	public static int DSO = 33;
	public static int DSO_BAR = 34;
	public static int DSC =35;  // Naval medal.  Not given out but used for historical aces
	public static int VC = 36;
	public static int MC = 37;
	
	public static int IBM = 99;
	

	public FrenchMedalManager (Campaign campaign)
    {
        super(campaign);

        medals.put(PILOTS_BADGE, new Medal ("Pilots Badge",                             "fr_pb.jpg"));
		medals.put(MILITARY_MEDAL, new Medal ("Medaille Militaire", 							"MM.jpg"));
        medals.put(CROIX_DE_GUERRE, new Medal (CROIX_DE_GUERRE_NAME,                            "CdG.jpg"));
        medals.put(CROIX_DE_GUERRE_BRONZE_STAR, new Medal (CROIX_DE_GUERRE_NAME + " with Bronze Star",  "cdg_bronze_star.jpg"));
        medals.put(CROIX_DE_GUERRE_SILVER_STAR, new Medal (CROIX_DE_GUERRE_NAME + " with Silver Star",  "cdg _silver_star.jpg"));
        medals.put(CROIX_DE_GUERRE_GILT_STAR, new Medal (CROIX_DE_GUERRE_NAME + " with Gilt Star",      "cdg_gilt_star.jpg"));
        medals.put(CROIX_DE_GUERRE_BRONZE_PALM, new Medal (CROIX_DE_GUERRE_NAME + " with Bronze Palm",  "cdg_bronze_palm.jpg"));
        medals.put(CROIX_DE_GUERRE_SILVER_PALM, new Medal (CROIX_DE_GUERRE_NAME + " with Silver Palm",  "cdg_silver_palm.jpg"));
        medals.put(MEDAILLE_DE_HONNEUR, new Medal ("Medaille d' Honneur",                       "MdH.jpg"));
        medals.put(LEGION_DE_HONNEUR, new Medal ("Legion d' Honneur",                           "LdH.jpg"));
		
        medals.put(DFC, new Medal (DISTINGUISHED_FLYING_CROSS_NAME,                     "dfc.jpg"));
        medals.put(DFC_BAR_1, new Medal (DISTINGUISHED_FLYING_CROSS_NAME + " With Bar",     "dfc_bar.jpg"));
        medals.put(DFC_BAR_2, new Medal (DISTINGUISHED_FLYING_CROSS_NAME + " With 2 Bars",  "dfc_bar2.jpg"));
        medals.put(DSO, new Medal (DISTINGUISHED_SERVICE_ORDER_NAME,                    "dso.jpg"));
        medals.put(DSO_BAR, new Medal (DISTINGUISHED_SERVICE_ORDER_NAME + " With Bar",      "dso_bar.jpg"));
        medals.put(DSC, new Medal ("Distinguished Service Cross",                   "dsc.jpg"));
        medals.put(VC, new Medal ("Victoria Cross",                                 "vc.jpg"));
        medals.put(MC, new Medal ("Military Cross",                                 "mc.jpg"));

		medals.put(BEL_CROIX_DE_GUERRE, new Medal ("Croix de Guerre", 			"belcdg.jpg"));
		medals.put(BEL_ORDRE_DE_LA_COURONNE, new Medal ("Ordre de la Couronne", "belCouronne.jpg"));
		medals.put(BEL_ORDRE_DE_LEOPOLD, new Medal ("Ordre de Leopold", 		"belLeopold.jpg"));
		
		medals.put(IBM, new Medal ("Insigne des Blesses Militaires", 		"IBM.jpg"));
	} 

	public Medal awardWoundedAward(SquadronMember pilot, ArmedService service) 
	{
		return medals.get(IBM);
	}

    protected Medal awardWings(SquadronMember pilot) 
    {
        if (!hasMedal(pilot, medals.get(PILOTS_BADGE)))
        {
            return medals.get(PILOTS_BADGE);
        }
        
        return null;
    }

	public Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException 
	{
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 2) && !hasMedal(pilot, medals.get(MILITARY_MEDAL)))
		{
			return medals.get(MILITARY_MEDAL);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 3) && !hasMedal(pilot, medals.get(CROIX_DE_GUERRE)))
		{
			return medals.get(CROIX_DE_GUERRE);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 5) && !hasMedal(pilot, medals.get(CROIX_DE_GUERRE_BRONZE_STAR)))
		{
			return medals.get(CROIX_DE_GUERRE_BRONZE_STAR);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 7) && !hasMedal(pilot, medals.get(CROIX_DE_GUERRE_SILVER_STAR)))
		{
			return medals.get(CROIX_DE_GUERRE_SILVER_STAR);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 9) && !hasMedal(pilot, medals.get(MEDAILLE_DE_HONNEUR)))
		{
			return medals.get(MEDAILLE_DE_HONNEUR);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 11) && !hasMedal(pilot, medals.get(CROIX_DE_GUERRE_GILT_STAR)))
		{
			return medals.get(CROIX_DE_GUERRE_GILT_STAR);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 14) && !hasMedal(pilot, medals.get(CROIX_DE_GUERRE_BRONZE_PALM)))
		{
			return medals.get(CROIX_DE_GUERRE_BRONZE_PALM);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 18) && !hasMedal(pilot, medals.get(CROIX_DE_GUERRE_SILVER_PALM)))
		{
			return medals.get(CROIX_DE_GUERRE_SILVER_PALM);
		}
		if ((pilot.getSquadronMemberVictories().getAirToAirVictoryCount() >= 25) && !hasMedal(pilot, medals.get(LEGION_DE_HONNEUR)))
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
            if (pilot.getMissionFlown() >= 15)
            {
                return medals.get(MILITARY_MEDAL);
            }
        }
        if (!hasMedal(pilot, medals.get(CROIX_DE_GUERRE)))
        {
            if (pilot.getMissionFlown() >= 20 && numPilotGroundVictoryPoints > 10)
            {
                return medals.get(CROIX_DE_GUERRE);
            }
        }
        if (!hasMedal(pilot, medals.get(CROIX_DE_GUERRE_BRONZE_STAR)))
        {
            if (pilot.getMissionFlown() >= 25 && numPilotGroundVictoryPoints > 15)
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
        if (!hasMedal(pilot, medals.get(LEGION_DE_HONNEUR)))
        {
            if (pilot.getMissionFlown() >= 100 && numPilotGroundVictoryPoints > 80)
            {
                return medals.get(CROIX_DE_GUERRE_SILVER_PALM);
            }
        }
        
        return awardFighter(pilot, service, victoriesThisMission);
    }
}
