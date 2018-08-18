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
    private Map<Integer, SquadronMember> aiCaptured = new HashMap<>();
    private Map<Integer, SquadronMember> aiMaimed = new HashMap<>();
    private Map<Integer, SquadronMember> aiWounded = new HashMap<>();

    public PersonnelOutOfMissionStatusHandler()
    {
    }

    public void determineFateOfShotDownPilots(Map<Integer, SquadronMember> shotDownPilots) throws PWCGException
    {
        for (SquadronMember squadronMember : shotDownPilots.values())
        {
            int pilotStatus = fateOfShotDownPilot();
            sortByStatus(squadronMember, pilotStatus);
        }        
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
            aiKilled.put(squadronMember.getSerialNumber(), squadronMember);
        }
        else if (pilotStatus == SquadronMemberStatus.STATUS_CAPTURED)
        {
            aiCaptured.put(squadronMember.getSerialNumber(), squadronMember);
        }
        else if (pilotStatus == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            aiMaimed.put(squadronMember.getSerialNumber(), squadronMember);
        }
        else if (pilotStatus == SquadronMemberStatus.STATUS_WOUNDED)
        {
            aiWounded.put(squadronMember.getSerialNumber(), squadronMember);
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

    public Map<Integer, SquadronMember> getAiWounded()
    {
        return aiWounded;
    }
}
