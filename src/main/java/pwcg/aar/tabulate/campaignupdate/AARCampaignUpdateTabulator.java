package pwcg.aar.tabulate.campaignupdate;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.AARLogEvents;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class AARCampaignUpdateTabulator
{
    private Campaign campaign;
    private AARContext aarContext;
    private CampaignUpdateData campaignUpdateData = new CampaignUpdateData();
    
    public AARCampaignUpdateTabulator(Campaign campaign, AARContext aarContext)
    {
        this.campaign = campaign;
        this.aarContext = aarContext;   
    }
    
    public CampaignUpdateData tabulateAARForCampaignUpdate() throws PWCGException
    {        
        campaignUpdateData.setNewDate(aarContext.getNewDate());        
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
    	CampaignLogGenerator campaignLogGenerator = new CampaignLogGenerator(campaign, aarContext);
    	AARLogEvents campaignLogEvents = campaignLogGenerator.createCampaignLogEventsForCampaignUpdate();
    	campaignUpdateData.getLogEvents().merge(campaignLogEvents);
    }

}
