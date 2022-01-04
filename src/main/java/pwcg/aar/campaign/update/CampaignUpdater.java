package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.tabulate.campaignupdate.AARCampaignUpdateTabulator;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.InitialCompanyBuilder;
import pwcg.campaign.resupply.depot.EquipmentArchTypeChangeHandler;
import pwcg.campaign.resupply.depot.EquipmentDepotReplenisher;
import pwcg.core.exception.PWCGException;

public class CampaignUpdater 
{
	private Campaign campaign;
	private AARContext aarContext;
	private CampaignUpdateData campaignUpdateData;
    
    public CampaignUpdater (Campaign campaign, AARContext aarContext) 
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

	public void updateCampaign() throws PWCGException 
    {
	    tabulateCampaignUpdateData();
	    
        CampaignCrewMemberAwardsUpdater crewMemberUpdater = new CampaignCrewMemberAwardsUpdater(campaign, campaignUpdateData);
        crewMemberUpdater.updatesForMissionEvents();
        
        CampaignAceUpdater aceUpdater = new CampaignAceUpdater(campaign, campaignUpdateData.getPersonnelAwards().getHistoricalAceAwards().getAceVictories());
        aceUpdater.updatesCampaignAces();
        
        PersonnelUpdater personnelUpdater = new PersonnelUpdater(campaign, campaignUpdateData);
        personnelUpdater.personnelUpdates();
        
        EquipmentUpdater squadronEquipmentUpdater = new EquipmentUpdater(campaign, campaignUpdateData);
        squadronEquipmentUpdater.equipmentUpdatesForSquadrons();

        ServiceChangeHandler serviceChangeHandler = new ServiceChangeHandler(campaign);
        serviceChangeHandler.handleChangeOfService(aarContext.getNewDate());

        PersonnelReplacementUpdater personnelReplacementUpdater = new PersonnelReplacementUpdater(campaign, aarContext);
        personnelReplacementUpdater.updateCampaignPersonnelReplacements();
        
        EquipmentDepotReplenisher equipmentReplacementUpdater = new EquipmentDepotReplenisher(campaign);
        equipmentReplacementUpdater.replenishDepotsForServices();
        
        EquipmentArchTypeChangeHandler archtypeChangeHandler = new EquipmentArchTypeChangeHandler(campaign, aarContext.getNewDate());
        archtypeChangeHandler.updateCampaignEquipmentForArchtypeChange();
        
        finishCampaignUpdates(aarContext.getNewDate());
                
        PWCGContext.getInstance().setMapForCampaign(campaign);
    }
    
    private void tabulateCampaignUpdateData() throws PWCGException 
    {
        AARCampaignUpdateTabulator campaignUpdateTabulator = new AARCampaignUpdateTabulator(campaign, aarContext);
        campaignUpdateData = campaignUpdateTabulator.tabulateAARForCampaignUpdate();
    }

    private void finishCampaignUpdates(Date newDate) throws PWCGException
    {
        campaign.getCampaignLogs().parseEventsToCampaignLogs(campaign, campaignUpdateData.getLogEvents().getCampaignLogEvents());
        campaign.setDate(newDate);
        
        InitialCompanyBuilder initialSquadronBuilder = new InitialCompanyBuilder();
        initialSquadronBuilder.buildNewCompanies(campaign);
    }
 }
