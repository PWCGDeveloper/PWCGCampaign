package pwcg.aar.campaign.update;

import java.util.Date;

import pwcg.aar.campaigndate.WoundRecovery;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.personnel.PersonnelReplacementsService;
import pwcg.campaign.personnel.SquadronMemberFemaleGenerator;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class PersonnelUpdater 
{
	private Campaign campaign;
    private CampaignUpdateData campaignUpdateData;

	public PersonnelUpdater (Campaign campaign, CampaignUpdateData campaignUpdateData) 
	{
        this.campaign = campaign;
        this.campaignUpdateData = campaignUpdateData;
	}
	
    public void personnelUpdates() throws PWCGException 
    {
        personnelAceRemovals();
        personnelPilotLosses();        
        personnelAceAdditions();
        personnelHealWoundedPilots();
    }

    private void personnelAceRemovals() throws PWCGException
    {
        acesKilled();
        acesTransferredOut();
    }

    private void acesTransferredOut()
	{
	}

	private void acesKilled() throws PWCGException
    {
        for (Integer serialNumber : campaignUpdateData.getPersonnelLosses().getAcesKilled(campaign).keySet())
        {
            setAceKilledInCampaign(serialNumber);
        }
    }

    private void setAceKilledInCampaign(Integer serialNumber) throws PWCGException
    {
        SquadronMember ace = campaign.getPersonnelManager().getAnyCampaignMember(serialNumber);
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
        for (SquadronMember pilot : campaignUpdateData.getPersonnelLosses().getPersonnelKilled().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate(), null);
        }
    }

    private void squadronMembersCaptured()
    {
        for (SquadronMember pilot : campaignUpdateData.getPersonnelLosses().getPersonnelCaptured().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate(), null);
        }
    }

    private void squadronMembersWounded() throws PWCGException
    {
        for (SquadronMember pilot : campaignUpdateData.getPersonnelLosses().getPersonnelWounded().values())
        {
            Date woundrecoveryDate = determinePlayerWoundedTime(SquadronMemberStatus.STATUS_WOUNDED);
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_WOUNDED, campaign.getDate(), woundrecoveryDate);
        }
    }

    private void squadronMembersMaimed() throws PWCGException
    {
        for (SquadronMember pilot : campaignUpdateData.getPersonnelLosses().getPersonnelMaimed().values())
        {
            Date woundrecoveryDate = determinePlayerWoundedTime(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED);
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate(), woundrecoveryDate);
        }
    }

    private void squadronMembersTransferredHome() throws PWCGException
    {
        for (SquadronMember pilot : campaignUpdateData.getPersonnelLosses().getPersonnelTransferredHome().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_TRANSFERRED, campaign.getDate(), null);
        }
    }

    private void squadronMembersTransfers() throws PWCGException
    {
        for (TransferRecord transferRecord : campaignUpdateData.getResupplyData().getSquadronTransferData().getSquadronMembersTransferred())
        {
            addPilotToSquadron(transferRecord);
            removeFromReplacementPool(transferRecord);
        }
    }

    private void addPilotToSquadron(TransferRecord transferRecord) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(transferRecord.getTransferTo());
        transferRecord.getSquadronMember().setSquadronId(transferRecord.getTransferTo());
        SquadronMember converted = SquadronMemberFemaleGenerator.convertToFemale(campaign, transferRecord.getTransferTo(), transferRecord.getSquadronMember());
        transferRecord.setSquadronMember(converted);
        squadronPersonnel.addSquadronMember(transferRecord.getSquadronMember());
    }

    private void removeFromReplacementPool(TransferRecord transferRecord) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(transferRecord.getTransferTo());
        ArmedService service = squadron.determineServiceForSquadron(campaign.getDate());
        PersonnelReplacementsService replacementService = campaign.getPersonnelManager().getPersonnelReplacementsService(service.getServiceId());
        replacementService.transferFromReservesToActive(transferRecord.getSquadronMember().getSerialNumber());
    }

    private void personnelAceAdditions() throws PWCGException
    {
        for (TransferRecord transferRecord : campaignUpdateData.getResupplyData().getAcesTransferred().getSquadronMembersTransferred())
        {
            transferRecord.getSquadronMember().setSquadronId(transferRecord.getTransferTo());
        }
    }
    
    private void personnelHealWoundedPilots() throws PWCGException
    {
        CampaignWoundUpdater woundUpdater = new CampaignWoundUpdater(campaign);
        woundUpdater.healWoundedPilots(campaignUpdateData.getNewDate());
    }
    
    private Date determinePlayerWoundedTime(int pilotStatus) throws PWCGException
    {
        WoundRecovery woundTimeCalculator = new WoundRecovery(campaign);
        Date woundedDate = woundTimeCalculator.calcDateOfRecovery(pilotStatus);
        return woundedDate;
    }
 }
