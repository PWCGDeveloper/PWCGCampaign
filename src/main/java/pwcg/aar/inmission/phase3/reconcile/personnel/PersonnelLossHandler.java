package pwcg.aar.inmission.phase3.reconcile.personnel;

import java.util.List;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogCrewMember;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
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


    public AARPersonnelLosses crewMembersShotDown(List<LogCrewMember> crewMemberStatusList) throws PWCGException
    {
        for (LogCrewMember crewMemberStatus : crewMemberStatusList)
        {
            CampaignPersonnelManager campaignPersonnelManager = campaign.getPersonnelManager();            
            CrewMember crewMember = campaignPersonnelManager.getAnyCampaignMember(crewMemberStatus.getSerialNumber());

            if (crewMember.isPlayer())
            {
                setStatus(crewMemberStatus, crewMember);
            }
            else
            {
                setStatus(crewMemberStatus, crewMember);
            }
        }
        
        return personnelLosses;
    }


    private void setStatus(LogCrewMember logCrewMember, CrewMember crewMember) throws PWCGException
    {
        if (logCrewMember.getStatus() == CrewMemberStatus.STATUS_WOUNDED)
        {
            personnelLosses.addPersonnelWounded(crewMember);
        }

        if (logCrewMember.getStatus() == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            personnelLosses.addPersonnelMaimed(crewMember);
        }

        if (logCrewMember.getStatus() == CrewMemberStatus.STATUS_CAPTURED)
        {
            personnelLosses.addPersonnelCaptured(crewMember);
        }

        if (logCrewMember.getStatus() == CrewMemberStatus.STATUS_KIA)
        {
            personnelLosses.addPersonnelKilled(crewMember);
        }
    }
}
