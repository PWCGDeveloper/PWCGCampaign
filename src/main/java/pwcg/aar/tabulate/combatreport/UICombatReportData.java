package pwcg.aar.tabulate.combatreport;

import pwcg.aar.ui.display.model.AARCombatReportMapData;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;
import pwcg.campaign.Campaign;

public class UICombatReportData
{
    private AARCombatReportPanelData combatReportPanelData;
    private AARCombatReportMapData combatReportMapData;

    public UICombatReportData(Campaign campaign)
    {
        this.combatReportPanelData = new AARCombatReportPanelData();
        this.combatReportMapData = new AARCombatReportMapData(campaign);
    }
    
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
