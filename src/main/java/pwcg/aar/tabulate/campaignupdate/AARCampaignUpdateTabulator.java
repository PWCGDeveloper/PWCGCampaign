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
        campaignUpdateData.getPersonnelAwards().mergeVictories(aarContext.getReconciledInMissionData().getReconciledVictoryData().getVictoryAwardsByPilot());
        campaignUpdateData.getPersonnelAwards().merge(aarContext.getReconciledInMissionData().getPersonnelAwards());
        campaignUpdateData.getPersonnelAwards().merge(aarContext.getReconciledOutOfMissionData().getPersonnelAwards());
    }
    
    private void tabulatePersonnelChanges()
    {
        campaignUpdateData.getPersonnelLosses().merge(aarContext.getReconciledInMissionData().getPersonnelLossesInMission());
        campaignUpdateData.getPersonnelLosses().merge(aarContext.getReconciledOutOfMissionData().getPersonnelLossesOutOfMission());
    }
    
    private void tabulateEquipmentChanges()
    {
        campaignUpdateData.getEquipmentLosses().merge(aarContext.getReconciledInMissionData().getEquipmentLossesInMission());
        campaignUpdateData.getEquipmentLosses().merge(aarContext.getReconciledOutOfMissionData().getEquipmentLossesOutOfMission());
    }

    private void tabulateTransfers()
    {
        campaignUpdateData.getResupplyData().merge(aarContext.getReconciledOutOfMissionData().getResupplyData());
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
