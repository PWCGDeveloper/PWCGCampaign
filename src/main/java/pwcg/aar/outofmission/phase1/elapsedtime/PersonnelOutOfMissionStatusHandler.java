package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class PersonnelOutOfMissionStatusHandler
{
    private Map<Integer, SquadronMember> aiKilled = new HashMap<>();
    private Map<Integer, SquadronMember> aiMaimed = new HashMap<>();
    private Map<Integer, SquadronMember> aiCaptured = new HashMap<>();

    public PersonnelOutOfMissionStatusHandler()
    {
    }

    public void determineFateOfShotDownPilots(Map<Integer, SquadronMember> shotDownPilots) throws PWCGException
    {
        for (SquadronMember squadronMember : shotDownPilots.values())
        {
            int pilotStatus = pilotsNotInMissionStatus();
            sortByStatus(squadronMember, pilotStatus);
        }        
    }

    private void sortByStatus(SquadronMember squadronMember, int pilotStatus)
    {
        if (pilotStatus == SquadronMemberStatus.STATUS_CAPTURED)
        {
            aiCaptured.put(squadronMember.getSerialNumber(), squadronMember);
        }
        if (pilotStatus == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            aiMaimed.put(squadronMember.getSerialNumber(), squadronMember);
        }
        if (pilotStatus == SquadronMemberStatus.STATUS_KIA)
        {
            aiKilled.put(squadronMember.getSerialNumber(), squadronMember);
        }
    }
    
    private int pilotsNotInMissionStatus() throws PWCGException
    {
        int fateDiceRoll = RandomNumberGenerator.getRandom(100);
        if (fateDiceRoll < 20)
        {
            return SquadronMemberStatus.STATUS_CAPTURED;
        }
        else if (fateDiceRoll < 40)
        {
            return SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED;
        }
        else if (fateDiceRoll < 70)
        {
            return SquadronMemberStatus.STATUS_KIA;
        }
        else
        {
            return SquadronMemberStatus.STATUS_ACTIVE;
        }
    }

    public Map<Integer, SquadronMember> getAiKilled()
    {
        return aiKilled;
    }

    public Map<Integer, SquadronMember> getAiMaimed()
    {
        return aiMaimed;
    }

    public Map<Integer, SquadronMember> getAiCaptured()
    {
        return aiCaptured;
    }
}
