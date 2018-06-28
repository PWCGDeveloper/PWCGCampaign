package pwcg.aar.tabulate.combatreport;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AARCombatReportMapData;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARCombatReportTabulateCoordinator
{
    private Campaign campaign;
    private AARContext aarContext;
    private UICombatReportData combatReportUiData = new UICombatReportData();

    public AARCombatReportTabulateCoordinator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public UICombatReportData tabulate() throws PWCGException 
    {
        tabulateCombatReport();
        tabulateCombatReportMap();
        return combatReportUiData;
    }

    private void tabulateCombatReport() throws PWCGException
    {
        CombatReportTabulator combatReportTabulator = new CombatReportTabulator(campaign, aarContext);
        AARCombatReportPanelData combatReportData = combatReportTabulator.tabulateForAARCombatReportPanel();
        combatReportUiData.setCombatReportPanelData(combatReportData);
    }
    
    private void tabulateCombatReportMap()
    {
        AARCombatReportMapData combatReportMapData = new AARCombatReportMapData();
        combatReportMapData.addChronologicalEvents(aarContext.getMissionEvaluationData().getChronologicalEvents());
        combatReportUiData.setCombatReportMapData(combatReportMapData);
    }
}
