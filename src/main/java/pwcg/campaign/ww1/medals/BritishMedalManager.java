package pwcg.campaign.ww1.medals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.ww1.country.RoFServiceManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class BritishMedalManager extends RoFMedalManager 
{
    public static int PILOTS_BADGE = 1;

	public static int DFC = 2;
	public static int DFC_BAR_1 = 3;
	public static int DFC_BAR_2 = 4;
	public static int DSO = 5;
	public static int DSO_BAR = 6;
	public static int DSC = 7;  // Naval medal.  Not given out but used for historical aces
	public static int DSC_BAR = 8;  // Naval medal.  Not given out but used for historical aces
	public static int VC = 9;
	public static int MC = 10;
	
	public static int MILITARY_MEDAL = 11;
	public static int CROIX_DE_GUERRE = 12;
	public static int CROIX_DE_GUERRE_BRONZE_STAR = 13;
	public static int CROIX_DE_GUERRE_SILVER_STAR = 14;
	public static int CROIX_DE_GUERRE_GILT_STAR = 15;
	public static int CROIX_DE_GUERRE_BRONZE_PALM = 16;
	public static int CROIX_DE_GUERRE_SILVER_PALM = 17;
	public static int MEDAILLE_DE_HONNEUR = 18;
	public static int LEGION_DE_HONNEUR = 19;
	
	public static int WOUND_STRIPE = 20;


	BritishMedalManager (Campaign campaign)
	{
	    super(campaign);
		
        medals.put(PILOTS_BADGE, new Medal ("Pilots Badge",                         "brit_pb.jpg"));
		medals.put(MC, 		new Medal ("Military Cross", 							"mc.jpg"));
		medals.put(DFC, 	new Medal ("Distinguished Flying Cross", 				"dfc.jpg"));
		medals.put(DFC_BAR_1, new Medal ("Distinguished Flying Cross With Bar", 	"dfc_bar.jpg"));
		medals.put(DFC_BAR_2, new Medal ("Distinguished Flying Cross With 2 Bars", 	"dfc_bar2.jpg"));
		medals.put(DSC, 	new Medal ("Distinguished Service Cross", 				"dsc_brit.jpg"));
		medals.put(DSC_BAR, new Medal ("Distinguished Service Cross With Bar", 		"dsc_bar.jpg"));
		medals.put(DSO,		new Medal ("Distinguished Service Order", 				"dso.jpg"));
		medals.put(DSO_BAR, new Medal ("Distinguished Service Order With Bar", 		"dso_bar.jpg"));
		medals.put(VC, 		new Medal ("Victoria Cross", 							"vc.jpg"));
		
		medals.put(MILITARY_MEDAL, new Medal ("Medaille Militaire", 							"MM.jpg"));
		medals.put(CROIX_DE_GUERRE, new Medal ("Croix de Guerre", 								"CdG.jpg"));
		medals.put(CROIX_DE_GUERRE_BRONZE_STAR, new Medal ("Croix de Guerre with Bronze Star", 	"cdg_bronze_star.jpg"));
		medals.put(CROIX_DE_GUERRE_SILVER_STAR, new Medal ("Croix de Guerre with Silver Star", 	"cdg _silver_star.jpg"));
		medals.put(CROIX_DE_GUERRE_GILT_STAR, new Medal ("Croix de Guerre with Gilt Star", 		"cdg_gilt_star.jpg"));
		medals.put(CROIX_DE_GUERRE_BRONZE_PALM, new Medal ("Croix de Guerre with Bronze Palm", 	"cdg_bronze_palm.jpg"));
		medals.put(CROIX_DE_GUERRE_SILVER_PALM, new Medal ("Croix de Guerre with Silver Palm", 	"cdg_silver_palm.jpg"));
		medals.put(MEDAILLE_DE_HONNEUR, new Medal ("Medaille d' Honneur", 						"MdH.jpg"));
		medals.put(LEGION_DE_HONNEUR, new Medal ("Legion d' Honneur", 							"LdH.jpg"));

		medals.put(WOUND_STRIPE, new Medal ("Wound Stripe", 							"WoundStripe.jpg"));
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
		return medals.get(WOUND_STRIPE);
	}

	public Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException 
	{
		Date rafStartDate = DateUtils.getRAFDate();
		
		Medal medal = null;
		
        if (campaign.getDate().before(rafStartDate))
        {
            medal = getBeforeRAF(pilot, victoriesThisMission);
            if (medal != null)
            {
                return medal;
            }
        }
        else
        {
            medal = getAfterRAF(pilot, victoriesThisMission);
            if (medal != null)
            {
                return medal;
            }
        }

        if ((pilot.getSquadronMemberVictories().getAirToAirVictories() >= 15) && !hasMedal(pilot, medals.get(DSO)))
        {
            return medals.get(DSO);
        }

        if (victoriesThisMission >= 2)
        {
            if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 25   && 
                hasMedal(pilot, medals.get(DSO))    &&
                !hasMedal(pilot, medals.get(DSO_BAR)))
            {
                return medals.get(DSO_BAR);
            }
        }
		
		
        if (!campaign.getDate().before(rafStartDate))
        {
            if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 30       &&
                hasMedal(pilot, medals.get(DFC))        &&
                hasMedal(pilot, medals.get(DFC_BAR_1))  &&
                !hasMedal(pilot, medals.get(DFC_BAR_2)))

            {
                if (victoriesThisMission >= 3)
                {
                    return medals.get(DFC_BAR_2);
                }
            }
        }

		if (!hasMedal(pilot, medals.get(VC)))
		{
			if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 40)
			{
				if (victoriesThisMission >= 2)
				{
					return medals.get(VC);
				}
			}
			if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 35)
			{
				if (victoriesThisMission >= 3)
				{
					return medals.get(VC);
				}
			}
		}
		
		return null;
	}

    private Medal getBeforeRAF(SquadronMember pilot, int victoriesThisMission) throws PWCGException
    {
    	ArmedService service = pilot.determineService(campaign.getDate());
        if (service.getServiceId() == RoFServiceManager.RFC)
        {
            if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 3   && 
                !hasMedal(pilot, medals.get(MC)))
            {
                return medals.get(MC);
            }
        }
        
        if (service.getServiceId() == RoFServiceManager.RNAS)
        {
            if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 3   && 
                !hasMedal(pilot, medals.get(DSC)))
            {
                return medals.get(DSC);
            }
        }
        
        if (service.getServiceId() == RoFServiceManager.RNAS)
        {
            if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 12   && 
                    hasMedal(pilot, medals.get(DSC))    &&
                    !hasMedal(pilot, medals.get(DSC_BAR)))
            {
                if (victoriesThisMission >= 1)
                {
                    return medals.get(DSC_BAR);
                }
            }
        }
        
        return null;
    }

    private Medal getAfterRAF(SquadronMember pilot, int victoriesThisMission)
    {
        if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 8 && !hasMedal(pilot, medals.get(DFC)))
        {
            return medals.get(DFC);
        }
                    
        // if we did not award a third order medal, consider a second order medal.
        // DFC With BAR for RAF
        if (pilot.getSquadronMemberVictories().getAirToAirVictories() >= 12   && 
            hasMedal(pilot, medals.get(DFC))    &&
            !hasMedal(pilot, medals.get(DFC_BAR_1)))
        {
            if (victoriesThisMission >= 1)
            {
                return medals.get(DFC_BAR_1);
            }
        }

        return null;
    }


    /* (non-Javadoc)
     * @see rof.campaign.campaign.medals.MedalManager#award(rof.campaign.campaign.Pilot, int, int)
     */
    public Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        Date rafStartDate = DateUtils.getRAFDate();

        // Military Cross
        if (pilot.getMissionFlown() >= 20 && pilot.getGroundVictories().size() > 15)
        {
            // MC for RFC
            if (campaign.getDate().before(rafStartDate))
            {
                if (pilot.determineService(campaign.getDate()).getServiceId() == RoFServiceManager.RFC)
                {
                    if (pilot.getGroundVictories().size() >= 10   && 
                        !hasMedal(pilot, medals.get(MC)))
                    {
                        return medals.get(MC);
                    }
                }
                else if (pilot.determineService(campaign.getDate()).getServiceId() == RoFServiceManager.RNAS)
                {
                    if (pilot.getGroundVictories().size() >= 10   && 
                        !hasMedal(pilot, medals.get(DSC)))
                    {
                        return medals.get(DSC);
                    }
                }
            }
        }
        
        // if we did not award a third order medal, consider a second order medal.
        // DFC With BAR for RAF
        if (!campaign.getDate().before(rafStartDate))
        {
            if (pilot.getGroundVictories().size() >= 40   && 
                hasMedal(pilot, medals.get(DFC))    &&
                !hasMedal(pilot, medals.get(DFC_BAR_1)))
            {
                return medals.get(DFC_BAR_1);
            }
        }
        else if (pilot.determineService(campaign.getDate()).getServiceId() == RoFServiceManager.RNAS)
        {
            // DSC With BAR for RNAS
            if (pilot.getGroundVictories().size() >= 40   && 
                    hasMedal(pilot, medals.get(DSC))    &&
                    !hasMedal(pilot, medals.get(DSC_BAR)))
            {
                return medals.get(DFC_BAR_1);
            }
        }
        
        // if we did not award a second order medal, consider the DSO.
        // For all forces throughout the war
        if (!hasMedal(pilot, medals.get(DSO)))
        {
            if (pilot.getMissionFlown() >= 60 && pilot.getGroundVictories().size() > 60)
            {
                return medals.get(DSO);
            }
        }

        // DSO with bar (4)
        if (hasMedal(pilot, medals.get(DSO)) && !hasMedal(pilot, medals.get(DSO_BAR)))
        {
            if (pilot.getMissionFlown() >= 80 && pilot.getGroundVictories().size() > 80)
            {
                return medals.get(DSO);
            }
        }
        
        
        // DFC with 2 bars (2) - very rare
        if (campaign.getDate().after(rafStartDate))
        {
            if (hasMedal(pilot, medals.get(DFC)) && hasMedal(pilot, medals.get(DFC_BAR_1)) && !hasMedal(pilot, medals.get(DFC_BAR_2)))
            {
                if (pilot.getMissionFlown() >= 140 && pilot.getGroundVictories().size() > 80)
                {
                    return medals.get(DFC_BAR_2);
                }
            }
        }

        // VC (5)
        if (!hasMedal(pilot, medals.get(VC)))
        {
            if (pilot.getMissionFlown() >= 110 && pilot.getGroundVictories().size() > 120)
            {
                return medals.get(VC);
            }
        }
        
        // If we made no awards based on recon criteria, maybe we award based on scout criteria
        return awardFighter(pilot, service, victoriesThisMission);
    }
    

	@Override
	public List<Medal> getAllMedalsInOrder() throws PWCGException
	{
	    ArmedService service = campaign.determineSquadron().determineServiceForSquadron(campaign.getDate());

		Date rafStartDate = DateUtils.getRAFDate();
        if (campaign.getDate().after(rafStartDate))
        {
        	return getRAFMedals();
        }
        else if (service.getServiceId() == RoFServiceManager.RNAS)
        {
        	return getRNASMedals();
        }
        else if (service.getServiceId() == RoFServiceManager.RFC)
        {
        	return getRFCMedals();
        }
		
		throw new PWCGException("Service not determined for British medal manager");
	}

	@Override
	public List<Medal> getAllAwardsForService() throws PWCGException
	{
		List<Medal> medalsInOrder = new ArrayList<>();
		medalsInOrder = getRAFMedals();
		medalsInOrder.addAll(getRNASMedals());
		medalsInOrder.addAll(getRFCMedals());
		medalsInOrder.addAll(getWoundBadgesInOrder());
		medalsInOrder.addAll(getAllBadgesInOrder());
		return medalsInOrder;
	}

	private List<Medal> getRAFMedals()
	{
		List<Medal> medalsInOrder = new ArrayList<>();
		
		medalsInOrder.add( medals.get(DFC));
		medalsInOrder.add( medals.get(DFC_BAR_1));
		medalsInOrder.add( medals.get(DSO));
		medalsInOrder.add( medals.get(DSO_BAR));
		medalsInOrder.add( medals.get(DFC_BAR_2));
		medalsInOrder.add( medals.get(VC));

		return medalsInOrder;
	}
	

	private List<Medal> getRNASMedals()
	{
		List<Medal> medalsInOrder = new ArrayList<>();

		medalsInOrder.add( medals.get(DSC));
		medalsInOrder.add( medals.get(DSC_BAR));
		medalsInOrder.add( medals.get(DSO));
		medalsInOrder.add( medals.get(DSO_BAR));
		medalsInOrder.add( medals.get(VC));

		return medalsInOrder;
	}


	private List<Medal> getRFCMedals()
	{
		List<Medal> medalsInOrder = new ArrayList<>();

		medalsInOrder.add( medals.get(MC));
		medalsInOrder.add( medals.get(DSO));
		medalsInOrder.add( medals.get(DSO_BAR));
		medalsInOrder.add( medals.get(VC));

		return medalsInOrder;
	}

	@Override
	public List<Medal> getWoundBadgesInOrder()
	{
		List<Medal> medalsInOrder = new ArrayList<>();
		medalsInOrder.add( medals.get(WOUND_STRIPE));		
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
