package pwcg.aar;

import java.util.Date;

import pwcg.aar.campaign.update.CampaignUpdater;
import pwcg.aar.campaigndate.AARTimePassedAfterMission;
import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.AARCoordinatorInMission;
import pwcg.aar.inmission.phase1.parse.AARLogEvaluationCoordinator;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase3.reconcile.victories.PlayerDeclarations;
import pwcg.aar.outofmission.AARCoordinatorOutOfMission;
import pwcg.aar.tabulate.AARTabulateCoordinator;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class AARCoordinatorMissionHandler
{
    private Campaign campaign;
	private AARContext aarContext; 

	public AARCoordinatorMissionHandler(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

    public void handleInMissionAAR(PlayerDeclarations playerDeclarations) throws PWCGException
    {
        inMission(playerDeclarations);
        generateEventsForTimeWounded();
        generateEventsForNotViableSquadron();
    }

    private void inMission(PlayerDeclarations playerDeclarations) throws PWCGException
    {
        determineInMissionResults(playerDeclarations);
        elapsedTimeDuringMission();
        determineOutOfMissionResults();
        tabulate();
        updateCampaignFromMission();
    }

	private void determineInMissionResults(PlayerDeclarations playerDeclarations) throws PWCGException
	{
        AARLogEvaluationCoordinator inMissionCoordinator = new AARLogEvaluationCoordinator();
        AARCoordinatorInMission coordinatorInMission = new AARCoordinatorInMission(campaign, aarContext, inMissionCoordinator);
        coordinatorInMission.coordinateInMissionAAR(playerDeclarations);
	}

    private void elapsedTimeDuringMission() throws PWCGException
    {
        AARTimePassedAfterMission newDateCalculator = new AARTimePassedAfterMission(campaign);
        Date newDateAfterMission = newDateCalculator.calcNewDate();
        aarContext.setNewDate(newDateAfterMission);
    }

	private void determineOutOfMissionResults() throws PWCGException
	{
        AARCoordinatorOutOfMission coordinatorOutOfMission = new AARCoordinatorOutOfMission(campaign, aarContext);
        coordinatorOutOfMission.coordinateOutOfMissionAAR();
	}
    
     private void tabulate() throws PWCGException
     {
         AARTabulateCoordinator tabulateCoordinator = new AARTabulateCoordinator(campaign, aarContext);
         tabulateCoordinator.tabulate();
     }

	private void updateCampaignFromMission() throws PWCGException
	{
        CampaignUpdater campaignUpdater = new CampaignUpdater(campaign, aarContext);
        campaignUpdater.updateCampaign();
	}

    private void generateEventsForTimeWounded() throws PWCGException
    {
        LogPilot playerCrewMember = aarContext.getMissionEvaluationData().getPlayerCrewMember();
        if (playerCrewMember.getStatus() <= SquadronMemberStatus.STATUS_WOUNDED)
        {
            AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);      
            extendedTimeHandler.timePassedForWounds(playerCrewMember);
        }
    }

    private void generateEventsForNotViableSquadron() throws PWCGException
    {
        Squadron squadron = PWCGContextManager.getInstance().getSquadronManager().getSquadron(campaign.getSquadronId());
        if (squadron.isSquadronViable(campaign))
        {
             AARExtendedTimeHandler extendedTimeHandler = new AARExtendedTimeHandler(campaign, aarContext);      
            extendedTimeHandler.timePassedForSquadronNotViable();
        }
    }
}
