package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;
import java.util.Map;

import pwcg.aar.awards.CampaignMemberAwardsGeneratorOutOfMission;
import pwcg.aar.data.AARContext;
import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AAROutOfMissionEventCoordinator
{
    private Campaign campaign;
    private AARContext aarContext;
    private ReconciledOutOfMissionData reconciledOutOfMissionData = new ReconciledOutOfMissionData();
    

    public AAROutOfMissionEventCoordinator(Campaign campaign, AARContext aarContext)
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
        outOfMissionVictories();
        changeInCommand();
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
    
    private void outOfMissionVictories() throws PWCGException
    {
        OutOfMissionVictoryEventHandler victoryEventHandler = new OutOfMissionVictoryEventHandler(campaign, aarContext);
        OutOfMissionVictoryData victoriesOutOMission = victoryEventHandler.generateOutOfMissionVictories();
        reconciledOutOfMissionData.getPersonnelAwards().getVictoriesByPilot().putAll(victoriesOutOMission.getVictoryAwardsBySquadronMember());
        outOfMissionLosses(victoriesOutOMission.getShotDownPilots(), victoriesOutOMission.getShotDownPlanes());
    }
    
    private void changeInCommand() throws PWCGException
    {
        OutOfMissionCommandChangeHandler commandChangeHandler = new OutOfMissionCommandChangeHandler(campaign);
        AARPersonnelLosses personnelLossesTransferHome = commandChangeHandler.replaceCommanderWithPlayer();
        reconciledOutOfMissionData.getPersonnelLossesOutOfMission().mergePersonnelTransferredHome(personnelLossesTransferHome.getPersonnelTransferredHome());
    }

    private void outOfMissionLosses(Map<Integer, SquadronMember> shotDownPilots, Map<Integer, LogPlane> shotDownPlanes) throws PWCGException 
    {
        OutOfMissionLossHandler lossHandler = new  OutOfMissionLossHandler(campaign, aarContext);
        lossHandler.lossesOutOfMission(shotDownPilots, shotDownPlanes);
        
        AARPersonnelLosses personnelLosses = lossHandler.getOutOfMissionPersonnelLosses();
        reconciledOutOfMissionData.getPersonnelLossesOutOfMission().merge(personnelLosses);
        
        AAREquipmentLosses equipmentLosses = lossHandler.getOutOfMissionEquipmentLosses();
        reconciledOutOfMissionData.getEquipmentLossesOutOfMission().merge(equipmentLosses);
    }

    private void createElapsedTimeEvents() throws PWCGException
    {
        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        reconciledOutOfMissionData.setElapsedTimeEvents(elapsedTimeEvents);
    }
}
