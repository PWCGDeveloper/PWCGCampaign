package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Map;

import pwcg.aar.data.AARPersonnelLosses;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class PersonnelOutOfMissionStatusHandler
{
    private AARPersonnelLosses outOfMissionPersonnelLosses = new AARPersonnelLosses();

    public AARPersonnelLosses determineFateOfShotDownCrewMembers(Map<Integer, CrewMember> shotDownCrewMembers) throws PWCGException
    {
        for (CrewMember crewMember : shotDownCrewMembers.values())
        {
            int crewMemberStatus = fateOfShotDownCrewMember();
            sortByStatus(crewMember, crewMemberStatus);
        }
        return outOfMissionPersonnelLosses;        
    }
    
    private int fateOfShotDownCrewMember() throws PWCGException
    {
        int fateDiceRoll = RandomNumberGenerator.getRandom(100);
        if (fateDiceRoll < 20)
        {
            return CrewMemberStatus.STATUS_KIA;
        }
        else if (fateDiceRoll < 30)
        {
            return CrewMemberStatus.STATUS_CAPTURED;
        }
        else if (fateDiceRoll < 45)
        {
            return CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED;
        }
        else if (fateDiceRoll < 65)
        {
            return CrewMemberStatus.STATUS_WOUNDED;
        }
        else
        {
            return CrewMemberStatus.STATUS_ACTIVE;
        }
    }

    private void sortByStatus(CrewMember crewMember, int crewMemberStatus)
    {
        if (crewMemberStatus == CrewMemberStatus.STATUS_KIA)
        {
            outOfMissionPersonnelLosses.addPersonnelKilled(crewMember);
        }
        else if (crewMemberStatus == CrewMemberStatus.STATUS_CAPTURED)
        {
            outOfMissionPersonnelLosses.addPersonnelCaptured(crewMember);
        }
        else if (crewMemberStatus == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            outOfMissionPersonnelLosses.addPersonnelMaimed(crewMember);
        }
        else if (crewMemberStatus == CrewMemberStatus.STATUS_WOUNDED)
        {
            outOfMissionPersonnelLosses.addPersonnelWounded(crewMember);
        }
    }
}
