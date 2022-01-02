package pwcg.aar.outofmission.phase3.resupply;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.crewmember.HistoricalAce;
import pwcg.campaign.crewmember.TankAce;
import pwcg.campaign.resupply.personnel.SquadronTransferData;
import pwcg.campaign.resupply.personnel.TransferRecord;
import pwcg.core.exception.PWCGException;

public class HistoricalAceTransferHandler
{
    private Campaign campaign;
    private Date newDate;

    private SquadronTransferData acesTransferred = new SquadronTransferData();

    public HistoricalAceTransferHandler(Campaign campaign, Date newDate)
    {
        this.campaign = campaign;
        this.newDate = newDate;
    }

    public SquadronTransferData determineAceTransfers() throws PWCGException
    {
        CampaignPersonnelManager campaignPersonnelManager = campaign.getPersonnelManager();            
        List<TankAce> acesBefore = PWCGContext.getInstance().getAceManager().getActiveAcesForCampaign(campaignPersonnelManager.getCampaignAces(), campaign.getDate());
        for (TankAce aceBefore : acesBefore)
        {
            if (aceBefore.getCrewMemberActiveStatus() > CrewMemberStatus.STATUS_CAPTURED)
            {
                HistoricalAce ha = PWCGContext.getInstance().getAceManager().getHistoricalAceBySerialNumber(aceBefore.getSerialNumber());
                TankAce aceAfter = ha.getAtDate(newDate);

                if (aceAfter.getCrewMemberActiveStatus() > CrewMemberStatus.STATUS_CAPTURED)
                {
                    if (!(aceBefore.getCompanyId() == aceAfter.getCompanyId()))
                    {
                        TransferRecord aceTransferRecord = new TransferRecord(aceAfter, aceBefore.getCompanyId(), aceAfter.getCompanyId());
                        acesTransferred.addTransferRecord(aceTransferRecord);
                    }
                }
            }
        }

        return acesTransferred;
    }
}
