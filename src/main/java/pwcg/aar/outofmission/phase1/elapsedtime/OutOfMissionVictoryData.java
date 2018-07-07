package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;

public class OutOfMissionVictoryData
{
    private Map<Integer, List<Victory>> victoryAwardsBySquadronMember = new HashMap<>();
    private Map<Integer, SquadronMember> shotDownPilots = new HashMap<>();
    private Map<Integer, EquippedPlane> shotDownPlanes = new HashMap<>();
    
    public void merge(OutOfMissionVictoryData victoryData)
    {
        for (Integer pilotSerialNumber : victoryData.getVictoryAwardsBySquadronMember().keySet())
        {
            List<Victory> victories = victoryData.getVictoryAwardsBySquadronMember().get(pilotSerialNumber);
            for (Victory victory : victories)
            {
                addVictoryAwards(pilotSerialNumber, victory);
            }
        }
        
        for (SquadronMember shotDownPilot : victoryData.getShotDownPilots().values())
        {
            addShotDownPilot(shotDownPilot);
        }
        
        for (EquippedPlane shotDownPlane : victoryData.getShotDownPlanes().values())
        {
            addShotDownPlane(shotDownPlane);
        }
    }
    
    public void addVictoryAwards(Integer pilotSerialNumber, Victory victory)
    {
        if (!victoryAwardsBySquadronMember.containsKey(pilotSerialNumber))
        {
            List<Victory> victoriesForSquadronMember = new ArrayList<>();
            victoryAwardsBySquadronMember.put(pilotSerialNumber, victoriesForSquadronMember);
        }
        
        List<Victory> victoriesForSquadronMember = victoryAwardsBySquadronMember.get(pilotSerialNumber);
        victoriesForSquadronMember.add(victory);
    }

    public void addVictoryEvents(OutOfMissionVictoryData victoryEvents)
    {
        victoryAwardsBySquadronMember.putAll(victoryEvents.getVictoryAwardsBySquadronMember());
    }

    public void addShotDownPilot(SquadronMember shotDownPilot)
    {
        shotDownPilots.put(shotDownPilot.getSerialNumber(), shotDownPilot);
    }

    public void addShotDownPlane(EquippedPlane shotDownPlane)
    {
        shotDownPlanes.put(shotDownPlane.getSerialNumber(), shotDownPlane);
    }
    
    public Map<Integer, SquadronMember> getShotDownPilots()
    {
        return shotDownPilots;
    }

    public Map<Integer, List<Victory>> getVictoryAwardsBySquadronMember()
    {
        return victoryAwardsBySquadronMember;
    }

    public Map<Integer, EquippedPlane> getShotDownPlanes()
    {
        return shotDownPlanes;
    }
}
