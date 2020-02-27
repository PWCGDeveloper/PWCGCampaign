package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.InitialSquadronBuilder;
import pwcg.campaign.resupply.depo.EquipmentArchTypeChangeHandler;
import pwcg.campaign.resupply.depo.EquipmentDepoReplenisher;
import pwcg.core.exception.PWCGException;

public class CampaignUpdater 
{
	private Campaign campaign;
	private AARContext aarContext;
    
    public CampaignUpdater (Campaign campaign, AARContext aarContext) 
    {
        this.campaign = campaign;
        this.aarContext = aarContext;
    }

	public void updateCampaign() throws PWCGException 
    {
        CampaignPilotAwardsUpdater pilotUpdater = new CampaignPilotAwardsUpdater(campaign, aarContext.getCampaignUpdateData().getPersonnelAwards());
        pilotUpdater.updatesForMissionEvents();
        
        CampaignAceUpdater aceUpdater = new CampaignAceUpdater(campaign, aarContext.getCampaignUpdateData().getPersonnelAwards().getHistoricalAceAwards().getAceVictories());
        aceUpdater.updatesCampaignAces();
        
        CampaignPersonnelUpdater personnelUpdater = new CampaignPersonnelUpdater(campaign, aarContext);
        personnelUpdater.personnelUpdates();
        
        CampaignSquadronEquipmentUpdater squadronEquipmentUpdater = new CampaignSquadronEquipmentUpdater(campaign, aarContext);
        squadronEquipmentUpdater.equipmentUpdatesForSquadrons();

        CampaignServiceChangeHandler serviceChangeHandler = new CampaignServiceChangeHandler(campaign);
        serviceChangeHandler.handleChangeOfService(aarContext.getCampaignUpdateData().getNewDate());

        CampaignPersonnelReplacementUpdater personnelReplacementUpdater = new CampaignPersonnelReplacementUpdater(campaign, aarContext);
        personnelReplacementUpdater.updateCampaignPersonnelReplacements();
        
        EquipmentDepoReplenisher equipmentReplacementUpdater = new EquipmentDepoReplenisher(campaign);
        equipmentReplacementUpdater.replenishDeposForServices();
        
        EquipmentArchTypeChangeHandler archtypeChangeHandler = new EquipmentArchTypeChangeHandler(campaign, aarContext.getNewDate());
        archtypeChangeHandler.updateCampaignEquipmentForArchtypeChange();
        
        finishCampaignUpdates(aarContext.getCampaignUpdateData().getNewDate());
    }
    
    private void finishCampaignUpdates(Date newDate) throws PWCGException
    {
        campaign.getCampaignLogs().parseEventsToCampaignLogs(campaign, aarContext.getCampaignUpdateData().getLogEvents().getCampaignLogEvents());
        campaign.setDate(newDate);
        
        InitialSquadronBuilder initialSquadronBuilder = new InitialSquadronBuilder();
        initialSquadronBuilder.buildNewSquadrons(campaign);
    }
 }
