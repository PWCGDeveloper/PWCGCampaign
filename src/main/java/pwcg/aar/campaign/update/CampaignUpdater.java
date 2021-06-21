package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.InitialSquadronBuilder;
import pwcg.campaign.resupply.depot.EquipmentArchTypeChangeHandler;
import pwcg.campaign.resupply.depot.EquipmentDepotReplenisher;
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
        CampaignPilotAwardsUpdater pilotUpdater = new CampaignPilotAwardsUpdater(campaign, aarContext.getCampaignUpdateData());
        pilotUpdater.updatesForMissionEvents();
        
        CampaignAceUpdater aceUpdater = new CampaignAceUpdater(campaign, aarContext.getCampaignUpdateData().getPersonnelAwards().getHistoricalAceAwards().getAceVictories());
        aceUpdater.updatesCampaignAces();
        
        PersonnelUpdater personnelUpdater = new PersonnelUpdater(campaign, aarContext);
        personnelUpdater.personnelUpdates();
        
        EquipmentUpdater squadronEquipmentUpdater = new EquipmentUpdater(campaign, aarContext);
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
        
        aarContext.resetContextForNextTimeIncrement();
        
        PWCGContext.getInstance().setMapForCampaign(campaign);
    }
    
    private void finishCampaignUpdates(Date newDate) throws PWCGException
    {
        campaign.getCampaignLogs().parseEventsToCampaignLogs(campaign, aarContext.getCampaignUpdateData().getLogEvents().getCampaignLogEvents());
        campaign.setDate(newDate);
        
        InitialSquadronBuilder initialSquadronBuilder = new InitialSquadronBuilder();
        initialSquadronBuilder.buildNewSquadrons(campaign);
    }
 }
