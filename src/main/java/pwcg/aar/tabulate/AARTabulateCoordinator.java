package pwcg.aar.tabulate;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.data.UIDebriefData;
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
        tabulateDebriefUI();
        tabulateAARForCampaignUpdate();
    }
    
    private void tabulateCombatReport() throws PWCGException
    {
        AARCombatReportTabulateCoordinator combatReportTabulator = new AARCombatReportTabulateCoordinator(campaign, aarContext);
        UICombatReportData combatReportUiData = combatReportTabulator.tabulate();
        aarContext.setUiCombatReportData(combatReportUiData);
    }

    private void tabulateDebriefUI() throws PWCGException 
    {
        AARDebriefTabulateCoordinator uiDebriefTabulator = new AARDebriefTabulateCoordinator(campaign, aarContext);
        UIDebriefData uiDebriefData = uiDebriefTabulator.tabulateForDebriefUI(); 
        aarContext.getUiDebriefData().merge(uiDebriefData);
    }
    
    private void tabulateAARForCampaignUpdate() throws PWCGException 
    {
        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        CampaignUpdateData campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();
        aarContext.setCampaignUpdateData(campaignUpdateData);
    }
}
