package pwcg.aar.tabulate.combatreport;

import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.aar.ui.display.model.AARCombatReportMapData;

public class UICombatReportData
{
    private AARCombatReportPanelData combatReportPanelData = new AARCombatReportPanelData();
    private AARCombatReportMapData combatReportMapData = new AARCombatReportMapData();

    public AARCombatReportPanelData getCombatReportPanelData()
    {
        return combatReportPanelData;
    }

    public void setCombatReportPanelData(AARCombatReportPanelData combatReportPanelData)
    {
        this.combatReportPanelData = combatReportPanelData;
    }

    public AARCombatReportMapData getCombatReportMapData()
    {
        return combatReportMapData;
    }

    public void setCombatReportMapData(AARCombatReportMapData combatReportMapData)
    {
        this.combatReportMapData = combatReportMapData;
    }

}
