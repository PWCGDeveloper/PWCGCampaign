package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.aar.data.AARContext;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.GreatAce;
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
        CampaignPilotUpdater pilotUpdater = new CampaignPilotUpdater(campaign, aarContext.getCampaignUpdateData().getPersonnelAwards());
        pilotUpdater.updatesForMissionEvents();
        
        CampaignAceUpdater aceUpdater = new CampaignAceUpdater(campaign, aarContext.getCampaignUpdateData().getPersonnelAwards().getHistoricalAceAwards().getAceVictories());
        aceUpdater.updatesCampaignAces();
        
        CampaignPersonnelUpdater personnelUpdater = new CampaignPersonnelUpdater(campaign, aarContext);
        personnelUpdater.personnelUpdates();
        
        CampaignEquipmentUpdater equipmentUpdater = new CampaignEquipmentUpdater(campaign, aarContext);
        equipmentUpdater.equipmentUpdates();

        CampaignServiceChangeHandler serviceChangeHandler = new CampaignServiceChangeHandler(campaign);
        serviceChangeHandler.handleChangeOfService(aarContext.getCampaignUpdateData().getNewDate());

        CampaignPersonnelReplacementUpdater personnelReplacementUpdater = new CampaignPersonnelReplacementUpdater(campaign, aarContext);
        personnelReplacementUpdater.updateCampaignPersonnelReplacements();
        
        CampaignEquipmentReplacementUpdater equipmentReplacementUpdater = new CampaignEquipmentReplacementUpdater(campaign);
        equipmentReplacementUpdater.updateCampaignEquipmentReplacements();
        
        finishCampaignUpdates(aarContext.getCampaignUpdateData().getNewDate());
    }
    
    private void finishCampaignUpdates(Date newDate) throws PWCGException
    {
        campaign.setGreatAce(GreatAce.isGreatAce(campaign));
        
        campaign.getCampaignLogs().setCampaignLogs(campaign, aarContext.getCampaignUpdateData().getLogEvents().getCampaignLogEvents());
        
        campaign.setDate(newDate);

        CampaignUpdateNewSquadronStaffer newSquadronStaffer = new CampaignUpdateNewSquadronStaffer(campaign);
        newSquadronStaffer.staffNewSquadrons();

        CampaignUpdateNewSquadronEquipper newSquadronEquipper = new CampaignUpdateNewSquadronEquipper(campaign);
        newSquadronEquipper.equipNewSquadrons();
    }
 }
