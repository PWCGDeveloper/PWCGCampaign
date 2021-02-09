package pwcg.campaign.group.airfield.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IStaticPlane;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.vehicle.IVehicle;

public class AirfieldObjects
{
    private List<IVehicle> staticTrucks = new ArrayList<IVehicle>();
    private List<IStaticPlane> staticPlanes = new ArrayList<IStaticPlane>();
    private List<GroundUnitCollection> airfieldVehicles = new ArrayList<>();
    private List<GroundUnitCollection> airfieldAAA = new ArrayList<>();

    public List<IVehicle> getStaticTrucks()
    {
        return staticTrucks;
    }

    public void addAirfieldObject(IVehicle airfieldObject)
    {
        this.staticTrucks.add(airfieldObject);
    }

    public List<IStaticPlane> getStaticPlanes()
    {
        return staticPlanes;
    }

    public void addStaticPlane(IStaticPlane staticPlane)
    {
        this.staticPlanes.add(staticPlane);
    }

    public List<GroundUnitCollection> getAirfieldVehicles()
    {
        return airfieldVehicles;
    }

    public void addAirfieldVehicle(GroundUnitCollection vehicle)
    {
        airfieldVehicles.add(vehicle);
    }

    public List<GroundUnitCollection> getAirfieldAAA()
    {
        return airfieldAAA;
    }

    public void addAirfieldAAA(GroundUnitCollection aaaMg)
    {
        airfieldAAA.add(aaaMg);
    }

    public void finish(Mission mission) throws PWCGException
    {
        for (GroundUnitCollection airfieldAAGun : airfieldAAA)
        {
            airfieldAAGun.finishGroundUnitCollection();
            airfieldAAGun.triggerGroundUnitCollection(mission);
        }

        for (GroundUnitCollection airfieldVehicle : airfieldVehicles)
        {
            airfieldVehicle.finishGroundUnitCollection();
            airfieldVehicle.triggerGroundUnitCollection(mission);
        }
    }

    public int getUnitCount()
    {
        int airfieldUnitCount = 0;

        for (GroundUnitCollection airfieldAAGun : airfieldAAA)
        {
            airfieldUnitCount += airfieldAAGun.getUnitCount();
        }

        for (GroundUnitCollection airfieldVehicle : airfieldVehicles)
        {
            airfieldUnitCount += airfieldVehicle.getUnitCount();
        }

        return airfieldUnitCount;
    }
}
