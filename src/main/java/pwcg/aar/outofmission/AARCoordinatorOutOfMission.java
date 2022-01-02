package pwcg.aar.outofmission;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARPersonnelAwards;
import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.outofmission.phase1.elapsedtime.AARMissionsFlownUpdater;
import pwcg.aar.outofmission.phase1.elapsedtime.AARSimulatedMission;
import pwcg.aar.outofmission.phase2.awards.CampaignAwardsGenerator;
import pwcg.aar.outofmission.phase2.awards.HistoricalAceAwards;
import pwcg.aar.outofmission.phase2.awards.HistoricalAceAwardsGenerator;
import pwcg.aar.outofmission.phase3.resupply.AARResupplyCoordinator;
import pwcg.aar.outofmission.phase3.resupply.AARResupplyData;
import pwcg.aar.outofmission.phase4.ElapsedTIme.ElapsedTimeEventGenerator;
import pwcg.aar.outofmission.phase4.ElapsedTIme.ElapsedTimeEvents;
import pwcg.aar.outofmission.phase4.ElapsedTIme.OutOfMissionCommandChangeHandler;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AARCoordinatorOutOfMission
{
    private Campaign campaign;
    private AARContext aarContext;

    public AARCoordinatorOutOfMission(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public void coordinateOutOfMissionAAR() throws PWCGException
    {
        reconcileOutOfMission();  
    	resupply();
    }

    public void reconcileOutOfMission() throws PWCGException 
    {
        Date theEnd = DateUtils.getEndOfWar();        
        if (aarContext.getNewDate().after(theEnd))
        {
            aarContext.setNewDate(endOfWar(theEnd));
        }
        else
        {
            eventsForNextCampaignDate();
        }
    }

    private Date endOfWar(Date theEnd) throws PWCGException
    {
        createElapsedTimeEvents();
        return DateUtils.getEndOfWar();
    }

    private void eventsForNextCampaignDate() throws PWCGException
    {
        simulateMission();
        missionsFlown();
        aceElapsedTimeEvents();
        outOfMissionAwards();
        changeInCommand();
        createElapsedTimeEvents();
        resupply();
    }

    private void simulateMission() throws PWCGException 
    {
        AARSimulatedMission simulatedMission = new AARSimulatedMission(campaign, aarContext);
        simulatedMission.simulateMissionEvents();
    }

    private void missionsFlown() throws PWCGException
    {
        AARMissionsFlownUpdater missionsFlown = new AARMissionsFlownUpdater(campaign, aarContext);
        missionsFlown.updateMissionsFlown();
    }

    private void aceElapsedTimeEvents() throws PWCGException 
    {
        HistoricalAceAwardsGenerator historicalAceAwardsGenerator =  new HistoricalAceAwardsGenerator(campaign, aarContext.getNewDate());
        HistoricalAceAwards historicalAceEvents = historicalAceAwardsGenerator.aceEvents();
        aarContext.getHistoricalAceEvents().merge(historicalAceEvents);
    }

    private void outOfMissionAwards() throws PWCGException 
    {
        Map<Integer, CrewMember> squadronMembersToEvaluate = campaign.getPersonnelManager().getActiveCampaignMembers();
        
        CampaignAwardsGenerator campaignMemberAwardsGenerator = new CampaignAwardsGenerator(campaign, aarContext);
        AARPersonnelAwards campaignMemberAwards = campaignMemberAwardsGenerator.createCampaignMemberAwards(new ArrayList<>(squadronMembersToEvaluate.values()));
        aarContext.getPersonnelAwards().merge(campaignMemberAwards);
    }

    private void changeInCommand() throws PWCGException
    {
        OutOfMissionCommandChangeHandler commandChangeHandler = new OutOfMissionCommandChangeHandler(campaign);
        AARPersonnelLosses personnelLossesTransferHome = commandChangeHandler.replaceCommanderWithPlayer();
        aarContext.getPersonnelLosses().merge(personnelLossesTransferHome);
    }

    private void createElapsedTimeEvents() throws PWCGException
    {
        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        aarContext.addElapsedTimeEvents(elapsedTimeEvents);
    }

    private void resupply() throws PWCGException
    {
        AARResupplyCoordinator resupplyCoordinator = new AARResupplyCoordinator(campaign, aarContext);
        AARResupplyData resupplyData = resupplyCoordinator.handleResupply();
        aarContext.addResupplyData(resupplyData);
    }
}
