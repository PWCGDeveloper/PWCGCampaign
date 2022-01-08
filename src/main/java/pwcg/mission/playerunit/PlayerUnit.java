package pwcg.mission.playerunit;

import java.io.BufferedWriter;
import java.util.List;

import pwcg.campaign.company.Company;
import pwcg.core.exception.PWCGException;
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

    public List<TankMcu> getTanks()
    {
        return unitVehicles.getTanks();
    }

    public TankMcu getLeadVehicle()
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

    public UnitVehicles getUnitTanks()
    {
        return unitVehicles;
    }

    public void preparePlaneForCoop() throws PWCGException
    {
        unitVehicles.prepareTankForCoop();        
    }

    public void write(BufferedWriter writer)
    {        
    }

}
