package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPilot;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.Campaign;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.campaign.squadmember.SerialNumber.SerialNumberClassification;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;

public class AARCrewBuilder
{
    private Campaign campaign;
    private Map <String, LogPlane> planeAiEntities = new HashMap <>();

    public AARCrewBuilder(Campaign campaign, Map <String, LogPlane> planeAiEntities)
    {
        this.campaign = campaign;
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

    
    public List<LogPilot> buildSquadronMembersFromLogPlanes() throws PWCGException
    {
        List<LogPilot> squadronCrewsInMission = new ArrayList<LogPilot>();
        for (LogPlane logPlane : planeAiEntities.values())
        {
            SquadronMember squadronMember = campaign.getPersonnelManager().getAnyCampaignMember(logPlane.getPilotSerialNumber());            
            if (squadronMember.getSquadronId() == campaign.getSquadronId())
            {
                squadronCrewsInMission.add(logPlane.getLogPilot());
            }
        }
        
        if (squadronCrewsInMission.isEmpty())
        {
            throw new PWCGException("No squadron members found for mission");
        }
        
        return squadronCrewsInMission;
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
