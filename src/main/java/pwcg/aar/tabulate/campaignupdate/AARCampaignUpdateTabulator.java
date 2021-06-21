package pwcg.aar.tabulate.campaignupdate;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARLogEvents;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.ui.display.model.AARElapsedTimeCombatResultsData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARCampaignUpdateTabulator
{
    private Campaign campaign;
    private AARContext aarContext;
    private CampaignUpdateData campaignUpdateData;
    
    public AARCampaignUpdateTabulator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext; 
        this.campaignUpdateData = new CampaignUpdateData(campaign); 
    }
    
    public CampaignUpdateData tabulateAARForCampaignUpdate() throws PWCGException
    {        
        tabulatePersonnelAwards();
        tabulatePersonnelChanges();
        tabulateEquipmentChanges();
        tabulateTransfers();
        addEventsToCampaignUpdateData();
        
        return campaignUpdateData;
    }

    private void tabulatePersonnelAwards()
    {
        campaignUpdateData.getPersonnelAcheivements().mergeVictories(aarContext.getReconciledMissionVictoryData().getVictoryAwardsByPilot());
        campaignUpdateData.getPersonnelAcheivements().merge(aarContext.getPersonnelAcheivements());
        campaignUpdateData.getPersonnelAwards().merge(aarContext.getPersonnelAwards());
    }
    
    private void tabulatePersonnelChanges()
    {
        campaignUpdateData.getPersonnelLosses().merge(aarContext.getPersonnelLosses());
    }
    
    private void tabulateEquipmentChanges()
    {
        campaignUpdateData.getEquipmentLosses().merge(aarContext.getEquipmentLosses());
    }

    private void tabulateTransfers()
    {
        campaignUpdateData.getResupplyData().merge(aarContext.getResupplyData());
    }

    private void addEventsToCampaignUpdateData() throws PWCGException
    {
        ElapsedTimeCombatResultsTabulator elapsedTimeCombatResultsTabulator = new ElapsedTimeCombatResultsTabulator(campaign, aarContext);
        AARElapsedTimeCombatResultsData elapsedTimeCombatResultsData = elapsedTimeCombatResultsTabulator.tabulateCombatResultsForElapsedTime();

    	CampaignLogGenerator campaignLogGenerator = new CampaignLogGenerator(campaign, aarContext, elapsedTimeCombatResultsData);
    	AARLogEvents campaignLogEvents = campaignLogGenerator.createCampaignLogEventsForCampaignUpdate();
    	campaignUpdateData.getLogEvents().merge(campaignLogEvents);
    }
}
