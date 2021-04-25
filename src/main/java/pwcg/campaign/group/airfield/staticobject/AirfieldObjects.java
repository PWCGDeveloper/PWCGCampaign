package pwcg.campaign.group.airfield.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IStaticPlane;
import pwcg.mission.ground.vehicle.IVehicle;

public class AirfieldObjects
{
    private List<IVehicle> staticTrucks = new ArrayList<IVehicle>();
    private List<IStaticPlane> staticPlanes = new ArrayList<IStaticPlane>();

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
}
