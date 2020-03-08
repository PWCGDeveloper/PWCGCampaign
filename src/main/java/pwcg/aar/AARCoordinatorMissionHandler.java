package pwcg.aar;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.aar.campaigndate.AARTimePassedAfterMission;
import pwcg.aar.data.AARContext;
import pwcg.aar.inmission.AARCoordinatorInMission;
import pwcg.aar.inmission.phase1.parse.AARLogEvaluationCoordinator;
import pwcg.aar.inmission.phase3.reconcile.victories.singleplayer.PlayerDeclarations;
import pwcg.aar.outofmission.AARCoordinatorOutOfMission;
import pwcg.aar.tabulate.combatreport.AARCombatReportTabulateCoordinator;
import pwcg.aar.tabulate.combatreport.UICombatReportData;
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
        determineOutOfMissionResults();
        tabulateInMission();
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
    
     private void tabulateInMission() throws PWCGException
     {
         AARCombatReportTabulateCoordinator combatReportTabulator = new AARCombatReportTabulateCoordinator(campaign, aarContext);
         List<UICombatReportData> uiCombatReportData = combatReportTabulator.tabulate();
         aarContext.setUiCombatReportData(uiCombatReportData);
     }
}
