package pwcg.campaign.medals;

import java.util.List;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public interface IMedalManager
{
	Medal getWoundedAward(SquadronMember pilot, ArmedService service);
	Medal award(Campaign campaign, SquadronMember pilot, ArmedService service, int victoriesThisMission) throws PWCGException;
	boolean hasMedal(SquadronMember pilot, Medal medal);
	public List<MedalManager> getAllManagers(Campaign campaign);
	public Medal getMedalFromManager(String type);
	List<Medal> getAllMedalsInOrder() throws PWCGException;
	List<Medal> getWoundBadgesInOrder() throws PWCGException;
	List<Medal> getAllBadgesInOrder() throws PWCGException;
	List<Medal> getAllAwardsForService() throws PWCGException;
	Medal getMedal(int medalId);
}