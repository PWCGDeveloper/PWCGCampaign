package pwcg.aar.tabulate.combatreport;

import pwcg.aar.ui.display.model.AARCombatReportMapData;
import pwcg.aar.ui.display.model.AARCombatReportPanelData;

public class UICombatReportData
{
    private int squadronId;
    private AARCombatReportPanelData combatReportPanelData;
    private AARCombatReportMapData combatReportMapData;

    public UICombatReportData(int squadronId)
    {
        this.squadronId = squadronId;
        this.combatReportPanelData = new AARCombatReportPanelData();
        this.combatReportMapData = new AARCombatReportMapData();
    }

    public int getSquadronId()
    {
        return squadronId;
    }

    public void setSquadronId(int squadronId)
    {
        this.squadronId = squadronId;
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
