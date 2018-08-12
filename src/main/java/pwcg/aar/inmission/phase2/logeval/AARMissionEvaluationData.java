package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogBase;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogVictory;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;

public class AARMissionEvaluationData
{
    private Map <String, LogPlane> planeAiEntities = new HashMap <>();
    private List<LogVictory> victoryResults = new ArrayList <>();
    private List<LogPilot> pilotsInMission = new ArrayList<>();
    private List<LogPilot> aceCrewsInMission = new ArrayList<>();
    private List<LogBase> chronologicalEvents = new ArrayList<>();
    
    public LogPlane getPlaneInMissionBySerialNumber(Integer serialNumber) throws PWCGException
    {
        for (LogPlane missionPlane : planeAiEntities.values())
        {
            if (missionPlane.isCrewMember(serialNumber))
            {
                return missionPlane;
            }
        }

        return null;
    }

    public List<LogPilot> getPlayerCrewMembers() throws PWCGException
    {
        List<LogPilot> playerCrewMembers = new ArrayList<>();
        for (LogPilot logPlayer : pilotsInMission)
        {
            if (SerialNumber.getSerialNumberClassification(logPlayer.getSerialNumber()) == SerialNumberClassification.PLAYER)
            {
                playerCrewMembers.add(logPlayer);
            }
        }
        
        if (playerCrewMembers.size() == 0)
        {
            throw new PWCGException ("No player crew member found in mission");
        }
        
        return playerCrewMembers;
    }


    public Map<String, LogPlane> getPlaneAiEntities()
    {
        return planeAiEntities;
    }

    public void setPlaneAiEntities(Map<String, LogPlane> planeAiEntities)
    {
        this.planeAiEntities = planeAiEntities;
    }

    public List<LogVictory> getVictoryResults()
    {
        return victoryResults;
    }

    public void setVictoryResults(List<LogVictory> victoryResults)
    {
        this.victoryResults = victoryResults;
    }

    public List<LogPilot> getPilotsInMission()
    {
        return pilotsInMission;
    }

    public void setPilotsInMission(List<LogPilot> squadronCrewsInMission)
    {
        this.pilotsInMission = squadronCrewsInMission;
    }

    public List<LogPilot> getAceCrewsInMission()
    {
        return aceCrewsInMission;
    }

    public void setAceCrewsInMission(List<LogPilot> aceCrewsInMission)
    {
        this.aceCrewsInMission = aceCrewsInMission;
    }

    public List<LogBase> getChronologicalEvents()
    {
        return chronologicalEvents;
    }

    public void setChronologicalEvents(List<LogBase> chronologicalEvents)
    {
        this.chronologicalEvents = chronologicalEvents;
    }
}
