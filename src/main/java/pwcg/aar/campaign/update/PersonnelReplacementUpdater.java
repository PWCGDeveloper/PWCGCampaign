package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.aar.data.AARContext;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.factory.ArmedServiceFactory;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.personnel.SquadronMemberReplacementFactory;
import pwcg.campaign.squadmember.SquadronMember;
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
		int numReplacementPilots = replacementService.getNumReplacements();
		buildReplacementPilots(replacementService, numReplacementPilots);
		removeReplacementPoints(replacementService);
		updateReplacementDate(replacementService);
	}

	private void buildReplacementPilots(PersonnelReplacementsService replacementService, int numReplacementPilots) throws PWCGException 
	{
		ArmedService service = ArmedServiceFactory.createServiceManager().getArmedService(replacementService.getServiceId());
		SquadronMemberReplacementFactory replacementFactory = new SquadronMemberReplacementFactory(campaign, service);
		for (int i = 0; i < numReplacementPilots; ++i)
		{
			SquadronMember replacement = replacementFactory.createAIReplacementPilot();
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
