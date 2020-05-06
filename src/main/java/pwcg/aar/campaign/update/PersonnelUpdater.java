package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.aar.campaigndate.WoundRecovery;
import pwcg.aar.data.AARContext;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class PersonnelUpdater 
{
	private Campaign campaign;
    private AARContext aarContext;

	public PersonnelUpdater (Campaign campaign, AARContext aarContext) 
	{
        this.campaign = campaign;
        this.aarContext = aarContext;
	}
	
    public void personnelUpdates() throws PWCGException 
    {
        personnelAceRemovals();
        personnelPilotLosses();        
        personnelAceAdditions();
        personnelHealWoundedPilots();
    }

    private void personnelAceRemovals()
    {
        acesKilled();
        acesTransferredOut();
    }

    private void acesTransferredOut()
	{
	}

	private void acesKilled()
    {
        for (Integer serialNumber : aarContext.getCampaignUpdateData().getPersonnelLosses().getAcesKilled().keySet())
        {
            setAceKilledInCampaign(serialNumber);
        }
    }

    private void setAceKilledInCampaign(Integer serialNumber)
    {
        Ace ace = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(serialNumber);
        ace.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
    }

    private void personnelPilotLosses() throws PWCGException 
    {        
        squadronMembersKilled();
        squadronMembersCaptured();
        squadronMembersMaimed();
        squadronMembersWounded();
        squadronMembersTransferredHome();
        squadronMembersTransfers();
    }

    private void squadronMembersKilled()
    {
        for (SquadronMember pilot : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelKilled().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        }
    }

    private void squadronMembersCaptured()
    {
        for (SquadronMember pilot : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelCaptured().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        }
    }

    private void squadronMembersWounded() throws PWCGException
    {
        for (SquadronMember pilot : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelWounded().values())
        {
            Date woundrecoveryDate = determinePlayerWoundedTime(SquadronMemberStatus.STATUS_WOUNDED);
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_WOUNDED, campaign.getDate(), woundrecoveryDate);
        }
    }

    private void squadronMembersMaimed() throws PWCGException
    {
        for (SquadronMember pilot : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelMaimed().values())
        {
            Date woundrecoveryDate = determinePlayerWoundedTime(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), woundrecoveryDate);
        }
    }

    private void squadronMembersTransferredHome() throws PWCGException
    {
        for (SquadronMember pilot : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelTransferredHome().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_TRANSFERRED, campaign.getDate(), null);
        }
    }

    private void squadronMembersTransfers() throws PWCGException
    {
        for (TransferRecord transferRecord : aarContext.getCampaignUpdateData().getResupplyData().getSquadronTransferData().getSquadronMembersTransferred())
        {
            SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(transferRecord.getTransferTo());
            transferRecord.getSquadronMember().setSquadronId(transferRecord.getTransferTo());
            squadronPersonnel.addSquadronMember(transferRecord.getSquadronMember());
            
            Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(transferRecord.getTransferTo());
            ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
            PersonnelReplacementsService replacementService = campaign.getPersonnelManager().getPersonnelReplacementsService(service.getServiceId());
            replacementService.transferFromFeservesToActive(transferRecord.getSquadronMember().getSerialNumber());
        }
    }

    private void personnelAceAdditions() throws PWCGException
    {
        for (TransferRecord transferRecord : aarContext.getCampaignUpdateData().getResupplyData().getAcesTransferred().getSquadronMembersTransferred())
        {
            transferRecord.getSquadronMember().setSquadronId(transferRecord.getTransferTo());
        }
    }
    
    private void personnelHealWoundedPilots() throws PWCGException
    {
        CampaignWoundUpdater woundUpdater = new CampaignWoundUpdater(campaign);
        woundUpdater.healWoundedPilots(aarContext.getNewDate());
    }
    
    private Date determinePlayerWoundedTime(int pilotStatus) throws PWCGException
    {
        WoundRecovery woundTimeCalculator = new WoundRecovery(campaign);
        Date woundedDate = woundTimeCalculator.calcDateOfRecovery(pilotStatus);
        return woundedDate;
    }
 }
