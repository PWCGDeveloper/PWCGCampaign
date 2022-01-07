package pwcg.mission.playerunit;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointPackage;

public class PlayerUnit
{
    private UnitVehicles unitVehicles;
    
    public PlayerUnit()
    {
        unitVehicles = new UnitVehicles(this);
    }
    
    public PlayerUnitInformation getUnitInformation()
    {
        return null;
    }

    public List<PlayerVehicleMcu> getVehicles()
    {
        return null;
    }

    public PlayerVehicleMcu getLeadVehicle()
    {
        return null;
    }

    public WaypointPackage getWaypointPackage()
    {
        return null;
    }

    public Company getCompany()
    {
        return null;
    }

    public void preparePlaneForCoop() throws PWCGException
    {
        unitVehicles.prepareVehicleForCoop();        
    }

    public void write(BufferedWriter writer)
    {        
    }

}
