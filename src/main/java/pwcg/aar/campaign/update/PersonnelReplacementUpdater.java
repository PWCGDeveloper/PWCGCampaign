package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.aar.data.AARContext;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.personnel.CrewMemberReplacementFactory;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class PersonnelReplacementUpdater 
{
	private Campaign campaign;
	private AARContext aarContext;
	public PersonnelReplacementUpdater(Campaign campaign, AARContext aarContext)
	{
		this.campaign = campaign;
        this.aarContext = aarContext;
	}
	
	public void updateCampaignPersonnelReplacements () throws PWCGException
	{
		for (PersonnelReplacementsService replacementService : campaign.getPersonnelManager().getAllPersonnelReplacements())
		{
			updateReplacementsForService(replacementService);
			accumulateMoreReplacementPoints(replacementService);
		}
	}

	private void updateReplacementsForService(PersonnelReplacementsService replacementService) throws PWCGException 
	{
		Date nextReplacementDate = DateUtils.advanceTimeDays(replacementService.getLastReplacementDate(), 7);
		if (nextReplacementDate.before(campaign.getDate()))
		{
			replaceForService(replacementService);
		}
	}

	private void replaceForService(PersonnelReplacementsService replacementService) throws PWCGException 
	{
		int numReplacementCrewMembers = replacementService.getNumReplacements();
		buildReplacementCrewMembers(replacementService, numReplacementCrewMembers);
		removeReplacementPoints(replacementService);
		updateReplacementDate(replacementService);
	}

	private void buildReplacementCrewMembers(PersonnelReplacementsService replacementService, int numReplacementCrewMembers) throws PWCGException 
	{
		ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(replacementService.getServiceId());
		CrewMemberReplacementFactory replacementFactory = new CrewMemberReplacementFactory(campaign, service);
		for (int i = 0; i < numReplacementCrewMembers; ++i)
		{
			CrewMember replacement = replacementFactory.createAIReplacementCrewMember();
			replacementService.addReplacement(replacement);
		}
	}

	private void removeReplacementPoints(PersonnelReplacementsService replacementService) throws PWCGException 
	{
		replacementService.removeReplacementPoints();
	}

	private void accumulateMoreReplacementPoints(PersonnelReplacementsService replacementService) throws PWCGException 
	{
		int daysSinceLastUpdate = DateUtils.daysDifference(campaign.getDate(), aarContext.getNewDate());
		replacementService.addReplacementPoints(daysSinceLastUpdate);
	}

	private void updateReplacementDate(PersonnelReplacementsService replacementService)
	{
		replacementService.setLastReplacementDate(campaign.getDate());;		
	}
}
