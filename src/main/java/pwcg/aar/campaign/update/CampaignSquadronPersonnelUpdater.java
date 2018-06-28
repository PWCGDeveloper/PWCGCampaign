package pwcg.aar.campaign.update;

import pwcg.aar.data.AARContext;
import pwcg.aar.outofmission.phase2.transfer.TransferRecord;
import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;

public class CampaignSquadronPersonnelUpdater 
{
	private Campaign campaign;
    private AARContext aarContext;

	public CampaignSquadronPersonnelUpdater (Campaign campaign, AARContext aarContext) 
	{
        this.campaign = campaign;
        this.aarContext = aarContext;
	}
	
    public void personnelUpdates() throws PWCGException 
    {
        personnelAceRemovals();

        personnelPilotLosses();
        
        personnelAceAdditions();
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
        ace.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate());
    }


    private void personnelPilotLosses() throws PWCGException 
    {        
        squadronMembersKilled();
        
        squadronMembersCaptured();

        squadronMembersMaimed();

        squadronMembersTransfers();
    }

    private void squadronMembersKilled()
    {
        for (SquadronMember pilot : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelKilled().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_KIA, campaign.getDate());
        }
    }

    private void squadronMembersCaptured()
    {
        for (SquadronMember pilot : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelCaptured().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_CAPTURED, campaign.getDate());
        }
    }

    private void squadronMembersMaimed()
    {
        for (SquadronMember pilot : aarContext.getCampaignUpdateData().getPersonnelLosses().getPersonnelMaimed().values())
        {
            pilot.setPilotActiveStatus(SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED, campaign.getDate());
        }
    }

    private void squadronMembersTransfers() throws PWCGException
    {
        for (TransferRecord transferRecord : aarContext.getCampaignUpdateData().getTransferData().getSquadronTransferData().getSquadronMembersTransferred())
        {
            SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(transferRecord.getTransferTo());
            transferRecord.getSquadronMember().setSquadronId(transferRecord.getTransferTo());
            squadronPersonnel.addSquadronMember(transferRecord.getSquadronMember());
        }
    }

    private void personnelAceAdditions() throws PWCGException
    {
        for (TransferRecord transferRecord : aarContext.getCampaignUpdateData().getTransferData().getAcesTransferred().getSquadronMembersTransferred())
        {
            transferRecord.getSquadronMember().setSquadronId(transferRecord.getTransferTo());
        }
    }
 }
