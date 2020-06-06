package pwcg.campaign.group.airfield.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IStaticPlane;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.GroundUnitCollectionData;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.mcu.Coalition;
import pwcg.mission.target.TargetType;

public class AirfieldObjects
{
    private List<IVehicle> airfieldObjects = new ArrayList<IVehicle>();
    private List<IStaticPlane> staticPlanes = new ArrayList<IStaticPlane>();
    private IGroundUnitCollection vehiclesForAirfield = new GroundUnitCollection(null, null);
    private List<IGroundUnitCollection> airfieldApproachAA = new ArrayList<>();

    public AirfieldObjects(Side airfieldSide)
    {
        GroundUnitCollectionData groundUnitCollectionData = new GroundUnitCollectionData(GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION,
                "Airfield vehicles", TargetType.TARGET_INFANTRY, Coalition.getCoalitionsForSide(airfieldSide.getOppositeSide()));
        vehiclesForAirfield = new GroundUnitCollection("Airfield vehicles", groundUnitCollectionData);
    }

    public List<IVehicle> getAirfieldObjects()
    {
        return airfieldObjects;
    }

    public void addAirfieldObject(IVehicle airfieldObject)
    {
        this.airfieldObjects.add(airfieldObject);
    }

    public List<IStaticPlane> getStaticPlanes()
    {
        return staticPlanes;
    }

    public void addStaticPlane(IStaticPlane staticPlane)
    {
        this.staticPlanes.add(staticPlane);
    }

    public IGroundUnitCollection getVehiclesForAirfield()
    {
        return vehiclesForAirfield;
    }

    public void addVehiclesForAirfield(IGroundUnitCollection aaaMg)
    {
        vehiclesForAirfield.merge(aaaMg);
    }

    public void addVehiclesForAirfield(IGroundUnit airfieldGroup)
    {
        vehiclesForAirfield.addGroundUnit(airfieldGroup);
    }

    public void finish(Mission mission) throws PWCGException
    {
        vehiclesForAirfield.finishGroundUnitCollection();
        vehiclesForAirfield.triggerOnPlayerOrCoalitionProximity(mission);
    }

    public int getUnitCount()
    {
        int airfieldUnitCount = vehiclesForAirfield.getUnitCount();

        for (IGroundUnitCollection airfieldApproachAAGun : airfieldApproachAA)
        {
            airfieldUnitCount += airfieldApproachAAGun.getUnitCount();
        }

        return airfieldUnitCount;
    }

    public List<IGroundUnitCollection> getAirfieldApproachAA()
    {
        return airfieldApproachAA;
    }

    public void setAirfieldApproachAA(List<IGroundUnitCollection> airfieldApproachAA)
    {
        this.airfieldApproachAA = airfieldApproachAA;
    }
}
