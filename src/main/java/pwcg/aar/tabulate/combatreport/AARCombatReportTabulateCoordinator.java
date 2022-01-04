package pwcg.aar.tabulate.combatreport;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.data.AARContext;
import pwcg.aar.ui.display.model.AARCombatReportMapData;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;

public class AARCombatReportTabulateCoordinator
{
    private Campaign campaign;
    private AARContext aarContext;
    private List<UICombatReportData> combatReportUiDataSet = new ArrayList<>();;

    public AARCombatReportTabulateCoordinator (Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public List<UICombatReportData> tabulate() throws PWCGException 
    {
        tabulateCombatReport();
        return combatReportUiDataSet;
    }

    private void tabulateCombatReport() throws PWCGException
    {
        List<Company> playerSquadronsInMission = aarContext.getPreliminaryData().getPlayerSquadronsInMission();
        for (Company playerSquadron : playerSquadronsInMission)
        {
            AARCombatReportTabulator combatReportTabulator = new AARCombatReportTabulator(campaign, playerSquadron, aarContext);
            AARCombatReportPanelData combatReportPanelData = combatReportTabulator.tabulateForAARCombatReportPanel();
            
            UICombatReportData combatReportUiData = new UICombatReportData(playerSquadron.getCompanyId());
            combatReportUiData.setCombatReportPanelData(combatReportPanelData);

            AARCombatReportMapData combatReportMapData = tabulateCombatReportMap();
            combatReportUiData.setCombatReportMapData(combatReportMapData);
            
            combatReportUiDataSet.add(combatReportUiData);
        }
    }
    
    private AARCombatReportMapData tabulateCombatReportMap() throws PWCGException
    {
        AARCombatReportMapData combatReportMapData = new AARCombatReportMapData();
        combatReportMapData.addChronologicalEvents(aarContext.getMissionEvaluationData().getChronologicalEvents());
        return combatReportMapData;
    }
}
