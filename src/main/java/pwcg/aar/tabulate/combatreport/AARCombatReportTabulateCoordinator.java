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
    private UICombatReportData combatReportUiData;

    public AARCombatReportTabulateCoordinator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
        this.combatReportUiData = new UICombatReportData(campaign);
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
    
    private void tabulateCombatReportMap() throws PWCGException
    {
        AARCombatReportMapData combatReportMapData = new AARCombatReportMapData(campaign);
        combatReportMapData.addChronologicalEvents(aarContext.getMissionEvaluationData().getChronologicalEvents());
        combatReportUiData.setCombatReportMapData(combatReportMapData);
    }
}
