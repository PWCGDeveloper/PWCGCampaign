package pwcg.aar.tabulate;

import java.util.List;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARTabulatedData;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.data.ui.UIDebriefData;
import pwcg.aar.tabulate.campaignupdate.AARCampaignUpdateTabulator;
import pwcg.aar.tabulate.combatreport.AARCombatReportTabulateCoordinator;
import pwcg.aar.tabulate.combatreport.UICombatReportData;
import pwcg.aar.tabulate.debrief.AARDebriefTabulateCoordinator;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARTabulateCoordinator
{
    private Campaign campaign;
    private AARContext aarContext;
    private AARTabulatedData tabulatedData = new AARTabulatedData();

    public AARTabulateCoordinator (
                    Campaign campaign, 
                    AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }
    
    public void tabulate() throws PWCGException 
    {
        tabulateCombatReport();
        tabulateFlightDebriefUI();
        tabulateAARForCampaignUpdate();
        
        aarContext.setAarTabulatedData(tabulatedData);
    }
    
    private void tabulateCombatReport() throws PWCGException
    {
        AARCombatReportTabulateCoordinator combatReportTabulator = new AARCombatReportTabulateCoordinator(campaign, aarContext);
        List<UICombatReportData> combatReportUiDataSet = combatReportTabulator.tabulate();
        tabulatedData.setUiCombatReportData(combatReportUiDataSet);
    }

    private void tabulateFlightDebriefUI() throws PWCGException 
    {
        AARDebriefTabulateCoordinator uiDebriefTabulator = new AARDebriefTabulateCoordinator(campaign, aarContext);
        UIDebriefData uiDebriefData = uiDebriefTabulator.tabulateForDebriefUI();
        tabulatedData.setUiDebriefData(uiDebriefData);
    }
    
    private void tabulateAARForCampaignUpdate() throws PWCGException 
    {
        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();
        tabulatedData.setCampaignUpdateData(campaignUpdateData);
    }
}
