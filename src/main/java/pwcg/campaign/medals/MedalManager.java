package pwcg.campaign.medals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public abstract class MedalManager implements IMedalManager 
{
	protected Map<Integer, Medal> medals = new TreeMap<Integer, Medal>();

	protected abstract Medal awardFighter(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException ;
    protected abstract Medal awardBomber(SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException ;
    protected abstract Medal awardWings(SquadronMember pilot)  ;

	@Override
	public abstract Medal getWoundedAward(SquadronMember pilot, ArmedService service);

	protected Campaign campaign = null;
	
	public abstract List<MedalManager> getAllManagers(Campaign campaign);

	public MedalManager (Campaign campaign)
	{
	    this.campaign = campaign;
	}
	
	public static Medal getMedalFromAnyManager(ICountry country, Campaign campaign, String type) throws PWCGException 
	{
		IMedalManager thisCountryManager = MedalManagerFactory.createMedalManager(country, campaign);
		Medal medal = thisCountryManager.getMedalFromManager(type);
		
		if (medal == null)
		{
			List<MedalManager> allManagers = thisCountryManager.getAllManagers(campaign);
			for (MedalManager manager : allManagers)
			{
				medal = manager.getMedalFromManager(type);
				if (medal != null)
				{
					break;
				}
			}
		}
		
		if (medal == null)
		{
		    PWCGLogger.log(LogLevel.ERROR, "Invalid medal: " + type);
		}
		

		return medal;
	}

    @Override
	public final Medal award(Campaign campaign, SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        Medal medal = null;
        
        medal = awardWings (pilot);
        if (medal == null)
        {
            medal = awardFighter(pilot, service, victoriesThisMission);
            if (medal == null)
            {
                Squadron squadron =  pilot.determineSquadron();
                if (squadron != null && !squadron.isSquadronThisRole(campaign.getDate(), Role.ROLE_FIGHTER))
                {
                    medal = awardBomber(pilot, service, victoriesThisMission);
                }
            }
        }
        
        return medal;
    }

	@Override
	public boolean hasMedal(SquadronMember pilot, Medal medal)
	{
		boolean hasMedal = false;
		for (int i = 0; i < pilot.getMedals().size(); ++i)
		{
			Medal pilotMedal = pilot.getMedals().get(i);
			if (pilotMedal.getMedalName().equalsIgnoreCase(medal.getMedalName()))
			{
				hasMedal = true;
				break;
			}
		}
		
		return hasMedal;
	}

	public Medal getMedalFromManager(String type)
    {
        Medal medal = null;
        for (Medal thisMedal : medals.values())
        {
            if (thisMedal.getMedalName().equalsIgnoreCase(type))
            {
                medal = thisMedal;
                break;
            }
        }
        
        return medal;
    }
    
    public Map<Integer, Medal> getMedals()
    {
        return medals;
    }
    
    public  Medal getMedal(int medalId)
    {
        return medals.get(medalId);
    }

	public List<Medal> getAllAwardsForService() throws PWCGException
	{
		return new ArrayList<>(medals.values());
	}
}
