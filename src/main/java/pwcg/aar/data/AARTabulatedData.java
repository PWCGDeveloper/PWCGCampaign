package pwcg.aar.data;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.data.ui.UIDebriefData;
import pwcg.aar.tabulate.combatreport.UICombatReportData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

/*
AAR Components:  
Combat Report: One for each human participant (serial number key)
    - Mission data
    - Mission participants
    - Pilots lost in mission;
    - Equipment lost in mission;
    - Victories in mission;
    - Claims denied in mission;

Debrief Data: One for each squadron (change to flight eventually) (squadron id key)
    - Aces on leave;
    - Medals awarded to squadron members;
    - Promotions awarded to squadron members;
    - Victories awarded to squadron members;
    - Squadron members lost;
    - Squadron Equipment lost;
    - Transfers into, out of squadron;

Global data for time passed
    - Newspapers
*/

public class AARTabulatedData
{
    private CampaignUpdateData campaignUpdateData;
    private List<UICombatReportData> uiCombatReportData = new ArrayList<>();
    private UIDebriefData uiDebriefData = new UIDebriefData();
    
    public AARTabulatedData()
    {
    }
    
    public AARTabulatedData(Campaign campaign)
    {
        this.campaignUpdateData = new CampaignUpdateData(campaign);
        this.uiCombatReportData = new ArrayList<>();
        this.uiDebriefData = new UIDebriefData();
    }

    public CampaignUpdateData getCampaignUpdateData()
    {
        return campaignUpdateData;
    }

    public void setCampaignUpdateData(CampaignUpdateData campaignUpdateData)
    {
        this.campaignUpdateData = campaignUpdateData;
    }

    public List<UICombatReportData> getUiCombatReportData()
    {
        return uiCombatReportData;
    }

    public void setUiCombatReportData(List<UICombatReportData> uiCombatReportData)
    {
        this.uiCombatReportData = uiCombatReportData;
    }

    public UIDebriefData getUiDebriefData()
    {
        return uiDebriefData;
    }

    public void setUiDebriefData(UIDebriefData uiDebriefData)
    {
        this.uiDebriefData = uiDebriefData;
    }
    
    public UICombatReportData findUiCombatReportDataForSquadron(int squadronId) throws PWCGException
    {
        for (UICombatReportData uiCombatReportDataForSquadron : uiCombatReportData)
        {
            if (uiCombatReportDataForSquadron.getSquadronId() == squadronId)
            {
                return uiCombatReportDataForSquadron;
            }
        }
        return new UICombatReportData(squadronId);
    }

}
