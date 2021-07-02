package pwcg.aar.tabulate;

import java.util.List;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.ui.UIDebriefData;
import pwcg.aar.tabulate.combatreport.AARCombatReportTabulateCoordinator;
import pwcg.aar.tabulate.combatreport.UICombatReportData;
import pwcg.aar.tabulate.debrief.AARDebriefTabulator;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARTabulateCoordinator
{
    private Campaign campaign;
    private AARContext aarContext;

    public AARTabulateCoordinator (
                    Campaign campaign, 
                    AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public void tabulateInMission() throws PWCGException 
    {
        tabulateCombatReport();
    }
    
    public void tabulateOutOfMission() throws PWCGException 
    {
        tabulateFlightDebriefUI();
    }
    
    private void tabulateCombatReport() throws PWCGException
    {
        AARCombatReportTabulateCoordinator combatReportTabulator = new AARCombatReportTabulateCoordinator(campaign, aarContext);
        List<UICombatReportData> combatReportUiDataSet = combatReportTabulator.tabulate();
        aarContext.addUiCombatReportData(combatReportUiDataSet);
    }

    private void tabulateFlightDebriefUI() throws PWCGException 
    {
        AARDebriefTabulator uiDebriefTabulator = new AARDebriefTabulator(campaign, aarContext);
        UIDebriefData uiDebriefData = uiDebriefTabulator.tabulateForDebriefUI();
        aarContext.mergeDebriefUiData(uiDebriefData);
    }
}
