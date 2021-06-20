package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Map;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class PersonnelOutOfMissionStatusHandler
{
    private AARPersonnelLosses outOfMissionPersonnelLosses = new AARPersonnelLosses();

    public AARPersonnelLosses determineFateOfShotDownPilots(Map<Integer, SquadronMember> shotDownPilots) throws PWCGException
    {
        for (SquadronMember squadronMember : shotDownPilots.values())
        {
            int pilotStatus = fateOfShotDownPilot();
            sortByStatus(squadronMember, pilotStatus);
        }
        return outOfMissionPersonnelLosses;        
    }
    
    private int fateOfShotDownPilot() throws PWCGException
    {
        int fateDiceRoll = RandomNumberGenerator.getRandom(100);
        if (fateDiceRoll < 20)
        {
            return SquadronMemberStatus.STATUS_KIA;
        }
        else if (fateDiceRoll < 30)
        {
            return SquadronMemberStatus.STATUS_CAPTURED;
        }
        else if (fateDiceRoll < 45)
        {
            return SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED;
        }
        else if (fateDiceRoll < 65)
        {
            return SquadronMemberStatus.STATUS_WOUNDED;
        }
        else
        {
            return SquadronMemberStatus.STATUS_ACTIVE;
        }
    }

    private void sortByStatus(SquadronMember squadronMember, int pilotStatus)
    {
        if (pilotStatus == SquadronMemberStatus.STATUS_KIA)
        {
            outOfMissionPersonnelLosses.addPersonnelKilled(squadronMember);
        }
        else if (pilotStatus == SquadronMemberStatus.STATUS_CAPTURED)
        {
            outOfMissionPersonnelLosses.addPersonnelCaptured(squadronMember);
        }
        else if (pilotStatus == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            outOfMissionPersonnelLosses.addPersonnelMaimed(squadronMember);
        }
        else if (pilotStatus == SquadronMemberStatus.STATUS_WOUNDED)
        {
            outOfMissionPersonnelLosses.addPersonnelWounded(squadronMember);
        }
    }
}
