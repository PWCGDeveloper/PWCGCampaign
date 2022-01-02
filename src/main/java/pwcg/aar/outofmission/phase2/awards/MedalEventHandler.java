package pwcg.aar.outofmission.phase2.awards;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.medals.IMedalManager;
import pwcg.campaign.medals.Medal;
import pwcg.core.exception.PWCGException;

public class MedalEventHandler 
{
	private Campaign campaign = null;
	private Map<Integer, Map<String, Medal>> medalAwards = new HashMap<>();

	public MedalEventHandler (Campaign campaign) 
	{
		this.campaign = campaign;
	}

    public void awardMedals(CrewMember crewMember, int victoriesThisMission) throws PWCGException 
	{
		if (crewMember instanceof TankAce)
		{
			return;
		}
		
        ArmedService service = crewMember.determineService(campaign.getDate());
        IMedalManager mm = MedalManagerFactory.createMedalManager(crewMember.determineCountry(campaign.getDate()), campaign);
		Medal medal = mm.award(campaign, crewMember, service,  victoriesThisMission);
		if (medal != null)
		{
		    if (!alreadAwarded(crewMember, medal.getMedalName()))
		    {
		        Map<String, Medal> medalsAwardedInElapsedTime = getMedalsInElapsedTimeForCrewMember(crewMember);
		        medal.setMedalDate(campaign.getDate());
		        medalsAwardedInElapsedTime.put(medal.getMedalName(), medal);
		    }
		}
	}

    public void awardWoundMedals(CrewMember crewMember) throws PWCGException 
    {
        IMedalManager mm = MedalManagerFactory.createMedalManager(crewMember.determineCountry(campaign.getDate()), campaign);
        Medal woundMedal = mm.awardWoundedAward(crewMember, crewMember.determineService(campaign.getDate()));
        if (woundMedal != null)
        {
            if (!alreadAwarded(crewMember, woundMedal.getMedalName()))
            {
                Map<String, Medal> medalsAwardedInElapsedTime = getMedalsInElapsedTimeForCrewMember(crewMember);
                woundMedal.setMedalDate(campaign.getDate());
                medalsAwardedInElapsedTime.put(woundMedal.getMedalName(), woundMedal);
            }
        }
    }

    private boolean alreadAwarded(CrewMember crewMember, String medalName)
    {
        for (Medal medal : crewMember.getMedals())
        {
            if (medal.getMedalName().equals(medalName))
            {
                return true;
            }
        }    
        
        return alreadyAwardedMedalDuringElapsedTime(crewMember.getSerialNumber(), medalName);
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

    private Map<String, Medal> getMedalsInElapsedTimeForCrewMember(CrewMember crewMember)
    {
        if (!medalAwards.containsKey(crewMember.getSerialNumber()))
        {
            Map <String, Medal> medalsAwardedInElapsedTime = new HashMap<>();
            medalAwards.put(crewMember.getSerialNumber(), medalsAwardedInElapsedTime);
        }
        Map <String, Medal> medalsAwardedInElapsedTime = medalAwards.get(crewMember.getSerialNumber());
        return medalsAwardedInElapsedTime;
    }

	public List<Medal> getMedalAwardsForCrewMember(Integer serialNumber) 
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
