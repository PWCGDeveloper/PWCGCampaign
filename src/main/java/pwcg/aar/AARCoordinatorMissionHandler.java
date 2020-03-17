package pwcg.aar;

import java.util.Date;
import java.util.Map;

import pwcg.aar.campaign.update.CampaignUpdater;
import pwcg.aar.campaigndate.AARTimePassedAfterMission;
import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.AARCoordinatorInMission;
import pwcg.aar.inmission.phase1.parse.AARLogEvaluationCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.outofmission.AARCoordinatorOutOfMission;
import pwcg.aar.tabulate.AARTabulateCoordinator;
import pwcg.campaign.Campaign;
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

    public void tabulateMissionAAR(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
    {
        determineInMissionResults(playerDeclarations);
        elapsedTimeDuringMission();
        tabulate();
        updateCampaign();

        determineOutOfMissionResults();
        tabulate();
        updateCampaign();
    }

	private void determineInMissionResults(Map<Integer, PlayerDeclarations> playerDeclarations) throws PWCGException
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
         tabulateCoordinator.tabulateInMission();
     }

     private void updateCampaign() throws PWCGException
     {
         CampaignUpdater campaignUpdater = new CampaignUpdater(campaign, aarContext);
         campaignUpdater.updateCampaign();
     }
}
