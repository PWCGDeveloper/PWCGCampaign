package pwcg.campaign.medals;

import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;

public interface IMedalManager
{
	Medal awardWoundedAward(CrewMember crewMember, ArmedService service);
	Medal award(Campaign campaign, CrewMember crewMember, ArmedService service, int victoriesThisMission) throws PWCGException;
	boolean hasMedal(CrewMember crewMember, Medal medal);
	List<MedalManager> getAllManagers(Campaign campaign);
	Medal getMedalFromManager(String type);
	List<Medal> getAllAwardsForService() throws PWCGException;
	Medal getMedal(int medalId);
	Map<Integer, Medal> getMedals();
    List<Medal> getMedalsWithHighestOrderOnly(List<Medal> medalsAwarded) throws PWCGException;
}