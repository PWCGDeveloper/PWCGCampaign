package pwcg.aar.awards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.medals.IMedalManager;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class MedalEventHandler 
{
	private Campaign campaign = null;
	private Map<Integer, Map<String, Medal>> medalAwards = new HashMap<>();

	public MedalEventHandler (Campaign campaign) 
	{
		this.campaign = campaign;
	}

    public void awardMedals(SquadronMember squadronMember, int victoriesThisMission) throws PWCGException 
	{
		if (squadronMember instanceof Ace)
		{
			return;
		}
		
        ArmedService service = squadronMember.determineService(campaign.getDate());
        IMedalManager mm = MedalManagerFactory.createMedalManager(squadronMember.determineCountry(campaign.getDate()), campaign);
		Medal medal = mm.award(campaign, squadronMember, service,  victoriesThisMission);
		if (medal != null)
		{
		    if (!alreadAwarded(squadronMember, medal.getMedalName()))
		    {
		        Map<String, Medal> medalsAwardedInElapsedTime = getMedalsInElapsedTimeForPilot(squadronMember);
		        medal.setMedalDate(campaign.getDate());
		        medalsAwardedInElapsedTime.put(medal.getMedalName(), medal);
		    }
		}
	}

    public void awardWoundMedals(SquadronMember squadronMember) throws PWCGException 
    {
        IMedalManager mm = MedalManagerFactory.createMedalManager(squadronMember.determineCountry(campaign.getDate()), campaign);
        Medal woundMedal = mm.getWoundedAward(squadronMember, squadronMember.determineService(campaign.getDate()));
        if (woundMedal != null)
        {
            if (!alreadAwarded(squadronMember, woundMedal.getMedalName()))
            {
                Map<String, Medal> medalsAwardedInElapsedTime = getMedalsInElapsedTimeForPilot(squadronMember);
                woundMedal.setMedalDate(campaign.getDate());
                medalsAwardedInElapsedTime.put(woundMedal.getMedalName(), woundMedal);
            }
        }
    }

    private boolean alreadAwarded(SquadronMember squadronMember, String medalName)
    {
        for (Medal medal : squadronMember.getMedals())
        {
            if (medal.getMedalName().equals(medalName))
            {
                return true;
            }
        }    
        
        return alreadyAwardedMedalDuringElapsedTime(squadronMember.getSerialNumber(), medalName);
    }
    
    private boolean alreadyAwardedMedalDuringElapsedTime(Integer serialNumber, String medalName)
    {
        if (medalAwards.containsKey(serialNumber))
        {
            Map <String, Medal> medalsAwardedInElapsedTime = medalAwards.get(serialNumber);
            if (medalsAwardedInElapsedTime.containsKey(medalName))
            {
                return true;
            }
        }
        
        return false;
    }

    private Map<String, Medal> getMedalsInElapsedTimeForPilot(SquadronMember squadronMember)
    {
        if (!medalAwards.containsKey(squadronMember.getSerialNumber()))
        {
            Map <String, Medal> medalsAwardedInElapsedTime = new HashMap<>();
            medalAwards.put(squadronMember.getSerialNumber(), medalsAwardedInElapsedTime);
        }
        Map <String, Medal> medalsAwardedInElapsedTime = medalAwards.get(squadronMember.getSerialNumber());
        return medalsAwardedInElapsedTime;
    }

	public List<Medal> getMedalAwardsForSquadronMember(Integer serialNumber) 
	{
        if (!medalAwards.containsKey(serialNumber))
        {
            return new ArrayList<Medal>();
        }
        else
        {
            return new ArrayList<Medal>(medalAwards.get(serialNumber).values());
        }
	}
}
