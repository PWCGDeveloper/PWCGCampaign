package pwcg.aar.outofmission.phase4.ElapsedTIme;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.core.exception.PWCGException;

public class OutOfMissionCommandChangeHandler
{
    private Campaign campaign;

    public OutOfMissionCommandChangeHandler(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public AARPersonnelLosses replaceCommanderWithPlayer() throws PWCGException
    {
        AARPersonnelLosses personnelLosses = new AARPersonnelLosses();
        
        for (CrewMember player : campaign.getPersonnelManager().getAllActivePlayers().getCrewMemberList())
        {
            if (player.determineIsCrewMemberCommander())
            {
            	CompanyPersonnel playerPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(player.getCompanyId());
                for (CrewMember crewMember : playerPersonnel.getCrewMembers().getCrewMemberList())
                {
                    if (!crewMember.isPlayer() && crewMember.determineIsCrewMemberCommander())
                    {
                        crewMember.setCrewMemberActiveStatus(CrewMemberStatus.STATUS_TRANSFERRED, campaign.getDate(), null);
                        personnelLosses.addPersonnelTransferredHome(crewMember);
                    }
                }
            }
        }
        
        return personnelLosses;
    }
}
