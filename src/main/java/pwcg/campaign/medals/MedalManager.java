package pwcg.campaign.medals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.MedalManagerFactory;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public abstract class MedalManager implements IMedalManager 
{
    protected static int MEDAL_NOT_FOUND = 0;
    protected static final String CROIX_DE_GUERRE_NAME = "Croix de Guerre";
    protected static final String DISTINGUISHED_FLYING_CROSS_NAME = "Distinguished Flying Cross";
    protected static final String DISTINGUISHED_SERVICE_CROSS_NAME = "Distinguished Service Cross";
    protected static final String DISTINGUISHED_SERVICE_ORDER_NAME = "Distinguished Service Order";
    protected static final String KNIGHTS_CROSS_NAME = "Knights Cross";
    protected static final String GERMAN_WOUND_BADGE = "Wound Badge";

	protected Map<Integer, Medal> medals = new TreeMap<Integer, Medal>();

	protected abstract Medal awardFighter(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException ;
    protected abstract Medal awardBomber(CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException ;
    protected abstract Medal awardWings(CrewMember crewMember)  ;

	@Override
	public abstract Medal awardWoundedAward(CrewMember crewMember, ArmedService service);

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
	public final Medal award(Campaign campaign, CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException 
    {
        Medal medal = null;
        
        medal = awardWings (crewMember);
        if (medal == null)
        {
            medal = awardFighter(crewMember, service, victoriesThisMission);
            if (medal == null)
            {
                Company squadron =  crewMember.determineSquadron();
                if (squadron != null && !squadron.isSquadronThisRole(campaign.getDate(), PwcgRole.ROLE_FIGHTER))
                {
                    medal = awardBomber(crewMember, service, victoriesThisMission);
                }
            }
        }
        
        return medal;
    }

	@Override
	public boolean hasMedal(CrewMember crewMember, Medal medal)
	{
		boolean hasMedal = false;
		for (int i = 0; i < crewMember.getMedals().size(); ++i)
		{
			Medal crewMemberMedal = crewMember.getMedals().get(i);
			if (crewMemberMedal.getMedalName().equalsIgnoreCase(medal.getMedalName()))
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

    @Override
    public List<Medal> getMedalsWithHighestOrderOnly(List<Medal> medalsAwarded) throws PWCGException
    {
        Map<Integer, Medal> consolidatedMedals = new TreeMap<>();
        for (Medal medal : medalsAwarded)
        {
            Medal highestOrderOfMedal = medal;
            if (medal.getMedalName().contains(CROIX_DE_GUERRE_NAME))
            {
                highestOrderOfMedal = getHighestOrderMedal(medalsAwarded, CROIX_DE_GUERRE_NAME);
            }

            if (medal.getMedalName().contains(DISTINGUISHED_FLYING_CROSS_NAME))
            {
                highestOrderOfMedal = getHighestOrderMedal(medalsAwarded, DISTINGUISHED_FLYING_CROSS_NAME);
            }

            if (medal.getMedalName().contains(DISTINGUISHED_SERVICE_ORDER_NAME))
            {
                highestOrderOfMedal = getHighestOrderMedal(medalsAwarded, DISTINGUISHED_SERVICE_ORDER_NAME);
            }

            if (medal.getMedalName().contains(KNIGHTS_CROSS_NAME))
            {
                highestOrderOfMedal = getHighestOrderMedal(medalsAwarded, KNIGHTS_CROSS_NAME);
            }

            if (medal.getMedalName().contains(GERMAN_WOUND_BADGE))
            {
                highestOrderOfMedal = getHighestOrderMedal(medalsAwarded, GERMAN_WOUND_BADGE);
            }
            
            // Counts on highest order of medal overwriting such that only the highest order is in the map
            int medalKey = getMedalKey(highestOrderOfMedal);
            if (medalKey != MEDAL_NOT_FOUND)
            {
                consolidatedMedals.put(medalKey, highestOrderOfMedal);
            }
        }
        
        return new ArrayList<>(consolidatedMedals.values());
    }
    

    private Medal getHighestOrderMedal(List<Medal> medals, String medalRoot) throws PWCGException
    {
        Medal highestOrderOfMedal = null;
        for (Medal medal : medals)
        {
            if (medal.getMedalName().contains(medalRoot))
            {
                highestOrderOfMedal = medal;
            }
        }

        return highestOrderOfMedal;
    }

    private int getMedalKey(Medal medalToConsolidate)
    {
        for (int medalKey : medals.keySet())
        {
            Medal medal = medals.get(medalKey);
            if (medal.getMedalName().equalsIgnoreCase(medalToConsolidate.getMedalName()))
            {
                return medalKey;
            }
        }
        
        return MEDAL_NOT_FOUND;
    }
}
