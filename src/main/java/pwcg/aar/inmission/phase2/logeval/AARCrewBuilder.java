package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.core.exception.PWCGException;

public class AARCrewBuilder
{
    private Map <String, LogPlane> planeAiEntities = new HashMap <>();

    public AARCrewBuilder(Map <String, LogPlane> planeAiEntities)
    {
        this.planeAiEntities = planeAiEntities;
    }
    
    public List<LogPilot> buildPilotsFromLogPlanes() throws PWCGException
    {
        List<LogPilot> pilotsInMission = new ArrayList<LogPilot>();
        for (LogPlane logPlane : planeAiEntities.values())
        {
            pilotsInMission.add(logPlane.getLogPilot());
        }
        
        if (pilotsInMission.isEmpty())
        {
            throw new PWCGException("No squadron members found for mission");
        }
        
        return pilotsInMission;
     }

    public List<LogPilot> buildAcesFromLogPlanes()
    {
        List<LogPilot> aceCrewsInMission = new ArrayList<LogPilot>();
        for (LogPlane logPlane : planeAiEntities.values())
        {
            if (SerialNumber.getSerialNumberClassification(logPlane.getPilotSerialNumber()) == SerialNumberClassification.ACE)
            {
                aceCrewsInMission.add(logPlane.getLogPilot());
            }
        }
        
        return aceCrewsInMission;
    }
}
