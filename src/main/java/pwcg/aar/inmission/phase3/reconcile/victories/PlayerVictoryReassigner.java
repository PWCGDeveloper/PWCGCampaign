package pwcg.aar.inmission.phase3.reconcile.victories;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogUnknown;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class PlayerVictoryReassigner
{
    private Campaign campaign;
    
    public PlayerVictoryReassigner (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public void resetUnclamedPlayerVictoriesForAssignmentToOthers(VictorySorter victorySorter) throws PWCGException
    {
        for (LogVictory resultVictory : victorySorter.getFirmAirVictories())
        {
            forRandomAssignment(resultVictory);
        }
        
        for (LogVictory resultVictory : victorySorter.getFirmBalloonVictories())
        {
            forRandomAssignment(resultVictory);
        }
    }

    private void forRandomAssignment(LogVictory resultVictory) throws PWCGException
    {
        if (!resultVictory.isConfirmed())
        {
            if (resultVictory.getVictor() instanceof LogPlane)
            {
                LogPlane victorPlanePlane = (LogPlane)resultVictory.getVictor();
                SquadronMember squadronMember = campaign.getPersonnelManager().getAnyCampaignMember(victorPlanePlane.getPilotSerialNumber());
                if (PlayerVictoryResolver.isPlayerVictory(squadronMember, victorPlanePlane.getPilotSerialNumber()))
                {
                    LogUnknown markedForAssignment = new LogUnknown();
                    markedForAssignment.setUnknownVictoryAssignment(UnknownVictoryAssignments.RANDOM_ASSIGNMENT);
                    resultVictory.setVictor(markedForAssignment);
                }
            }
        }
    }

}
