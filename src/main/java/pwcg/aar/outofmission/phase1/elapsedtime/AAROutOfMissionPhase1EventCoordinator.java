package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;
import java.util.Map;

import pwcg.aar.awards.CampaignMemberAwardsGeneratorOutOfMission;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AAROutOfMissionPhase1EventCoordinator
{
    private Campaign campaign;
    private AARContext aarContext;
    private ReconciledOutOfMissionData reconciledOutOfMissionData = new ReconciledOutOfMissionData();
    

    public AAROutOfMissionPhase1EventCoordinator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    
    public ReconciledOutOfMissionData reconcileOutOfMission() throws PWCGException 
    {
        Date theEnd = DateUtils.getEndOfWar();        
        if (aarContext.getNewDate().after(theEnd))
        {
            aarContext.setNewDate( endOfWar(theEnd));
        }
        else
        {
	        eventsForNextCampaignDate();
        }
        
        return reconciledOutOfMissionData;
    }

	private Date endOfWar(Date theEnd) throws PWCGException
	{
		createElapsedTimeEvents();
		return DateUtils.getEndOfWar();
	}

	private void eventsForNextCampaignDate() throws PWCGException
	{
        outOfMissionAwards();
		aceElapsedTimeEvents();
		Map<Integer, SquadronMember> shotDownPilots = outOfMissionVictories();
		outOfMissionLosses(shotDownPilots);
		createElapsedTimeEvents();
	}

    private void outOfMissionAwards() throws PWCGException 
    {
        CampaignMemberAwardsGeneratorOutOfMission campaignMemberAwardsGenerator = new CampaignMemberAwardsGeneratorOutOfMission(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = campaignMemberAwardsGenerator.createCampaignMemberAwards();
        reconciledOutOfMissionData.getPersonnelAwards().merge(campaignMemberAwards);
    }

    private void aceElapsedTimeEvents() throws PWCGException 
    {
        HistoricalAceAwardsGenerator historicalAceAwardsGenerator =  new HistoricalAceAwardsGenerator(campaign, aarContext.getNewDate());
        HistoricalAceAwards historicalAceEvents = historicalAceAwardsGenerator.aceEvents();
        reconciledOutOfMissionData.getHistoricalAceEvents().merge(historicalAceEvents);
    }
    
    private Map<Integer, SquadronMember> outOfMissionVictories() throws PWCGException
    {
        OutOfMissionVictoryEventHandler victoryEventHandler = new OutOfMissionVictoryEventHandler(campaign, aarContext);
        OutOfMissionVictoryData victoriesOutOMission = victoryEventHandler.generateOutOfMissionVictories();
        reconciledOutOfMissionData.getPersonnelAwards().getVictoriesByPilot().putAll(victoriesOutOMission.getVictoryAwardsBySquadronMember());
        return victoriesOutOMission.getShotDownPilots();
    }

    private void outOfMissionLosses(Map<Integer, SquadronMember> shotDownPilots) throws PWCGException 
    {
        OutOfMissionLossHandler personnelHandler = new  OutOfMissionLossHandler(campaign, aarContext);
        AARPersonnelLosses personnelLosses = personnelHandler.personellLosses(shotDownPilots);
        reconciledOutOfMissionData.getPersonnelLosses().merge(personnelLosses);
    }

    private void createElapsedTimeEvents() throws PWCGException
    {
        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        reconciledOutOfMissionData.setElapsedTimeEvents(elapsedTimeEvents);
    }
}
