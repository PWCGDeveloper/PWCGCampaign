package pwcg.aar.outofmission.phase1.elapsedtime;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class PilotVictoryRunner
{
    private OutOfMissionVictoryEventHandler outOfMissionVictoryEventHandler;

    private int numVictories = 0;
    
    public PilotVictoryRunner(OutOfMissionVictoryEventHandler outOfMissionVictoryEventHandler)
    {
    	this.outOfMissionVictoryEventHandler = outOfMissionVictoryEventHandler;
    }

    public void testPilotOutOfMissionLostAllPossibleStatus(SquadronMember squadronMember) throws PWCGException
    {
        int numMissionsInWar = 1000;
        for (int j = 0; j < numMissionsInWar; ++j)
        {
        	outOfMissionVictoryEventHandler.getVictoriesOutOMission().getVictoryAwardsBySquadronMember().clear();
           	outOfMissionVictoryEventHandler.outOfMissionVictoriesForSquadronMember(squadronMember);
           	numVictories +=  outOfMissionVictoryEventHandler.getVictoriesOutOMission().getVictoryAwardsBySquadronMember().size();
        }
    }

	public int getNumVictories()
	{
		return numVictories;
	}
}
