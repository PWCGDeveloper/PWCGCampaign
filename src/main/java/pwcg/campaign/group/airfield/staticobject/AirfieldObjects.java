package pwcg.campaign.group.airfield.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IStaticPlane;
import pwcg.mission.ground.unittypes.GroundUnitSpawning;
import pwcg.mission.ground.vehicle.IVehicle;

public class AirfieldObjects
{
	private List<IVehicle> airfieldObjects = new ArrayList<IVehicle>();
	private List<IStaticPlane> staticPlanes = new ArrayList<IStaticPlane>();
	private List<GroundUnitSpawning> aaaForAirfield = new ArrayList<GroundUnitSpawning>();

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

	public List<GroundUnitSpawning> getAaaForAirfield()
	{
		return aaaForAirfield;
	}

	public void addAaaForAirfield(GroundUnitSpawning aaa)
	{
		this.aaaForAirfield.add(aaa);
	}
}
