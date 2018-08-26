package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.Campaign;
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
        
        for (SquadronMember player : campaign.getPlayers())
        {
            if (player.determineIsSquadronMemberCommander())
            {
                for (SquadronMember squadronMember : campaign.getPersonnelManager().getPlayerPersonnel().getSquadronMembers().getSquadronMemberList())
                {
                    if (!squadronMember.isPlayer() && squadronMember.determineIsSquadronMemberCommander())
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
