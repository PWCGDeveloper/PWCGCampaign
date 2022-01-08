package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.mission.playerunit.PlayerUnit;

public class MissionPlayerUnits
{
    private List<PlayerUnit> playerUnits = new ArrayList<>();
    private Mission mission;
    private Campaign campaign;

    MissionPlayerUnits()
    {
        this.mission = mission;
        this.campaign = campaign;
    }
    
    public List<Integer> getPlayersInMission()
    {
        return null;
    }

    public List<Integer> determinePlayerVehicleIds()
    {
        return null;
    }

    public List<PlayerUnit> getPlayerUnits()
    {
        return null;
    }

    public List<PlayerUnit> getAiUnits()
    {
        return null;
    }

    public PlayerUnit getReferencePlayerUnit()
    {
        return null;
    }
    

    public void finalizeMissionUnits() throws PWCGException
    {
        MissionUnitFinalizer unitFinalizer = new MissionUnitFinalizer(campaign, mission);
        unitFinalizer.finalizeMissionUnits();
    }

    public PlayerUnit getPlayerUnitForCompany(int companyId)
    {
        return null;
    }

}
