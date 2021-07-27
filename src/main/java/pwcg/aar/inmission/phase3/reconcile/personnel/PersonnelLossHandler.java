package pwcg.aar.inmission.phase3.reconcile.personnel;

import java.util.List;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;

public class PersonnelLossHandler
{
    private Campaign campaign;
    private AARPersonnelLosses personnelLosses;

    public PersonnelLossHandler(Campaign campaign)
    {
        this.campaign = campaign;
        personnelLosses = new AARPersonnelLosses();
    }


    public AARPersonnelLosses pilotsShotDown(List<LogPilot> pilotStatusList) throws PWCGException
    {
        for (LogPilot pilotStatus : pilotStatusList)
        {
            CampaignPersonnelManager campaignPersonnelManager = campaign.getPersonnelManager();            
            SquadronMember pilot = campaignPersonnelManager.getAnyCampaignMember(pilotStatus.getSerialNumber());

            if (pilot.isPlayer())
            {
                setStatus(pilotStatus, pilot);
            }
            else
            {
                setStatus(pilotStatus, pilot);
            }
        }
        
        return personnelLosses;
    }


    private void setStatus(LogPilot logPilot, SquadronMember pilot) throws PWCGException
    {
        if (logPilot.getStatus() == SquadronMemberStatus.STATUS_WOUNDED)
        {
            personnelLosses.addPersonnelWounded(pilot);
        }

        if (logPilot.getStatus() == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            personnelLosses.addPersonnelMaimed(pilot);
        }

        if (logPilot.getStatus() == SquadronMemberStatus.STATUS_CAPTURED)
        {
            personnelLosses.addPersonnelCaptured(pilot);
        }

        if (logPilot.getStatus() == SquadronMemberStatus.STATUS_KIA)
        {
            personnelLosses.addPersonnelKilled(pilot);
        }
    }
}
