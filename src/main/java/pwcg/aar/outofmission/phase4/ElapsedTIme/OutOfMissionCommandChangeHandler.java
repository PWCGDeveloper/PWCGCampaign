package pwcg.aar.outofmission.phase4.ElapsedTIme;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
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
        
        for (SquadronMember player : campaign.getPersonnelManager().getAllActivePlayers().getSquadronMemberList())
        {
            if (player.determineIsSquadronMemberCommander(campaign))
            {
            	SquadronPersonnel playerPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(player.getSquadronId());
                for (SquadronMember squadronMember : playerPersonnel.getSquadronMembers().getSquadronMemberList())
                {
                    if (!squadronMember.isPlayer() && squadronMember.determineIsSquadronMemberCommander(campaign))
                    {
                        squadronMember.setPilotActiveStatus(SquadronMemberStatus.STATUS_TRANSFERRED, campaign.getDate(), null);
                        personnelLosses.addPersonnelTransferredHome(squadronMember);
                    }
                }
            }
        }
        
        return personnelLosses;
    }
}
