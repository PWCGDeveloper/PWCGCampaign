package pwcg.campaign.group.airfield.staticobject;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IStaticPlane;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.ground.vehicle.IVehicle;

public class AirfieldObjects
{
	private List<IVehicle> airfieldObjects = new ArrayList<IVehicle>();
	private List<IStaticPlane> staticPlanes = new ArrayList<IStaticPlane>();
    private List<IGroundUnitCollection> aaaForAirfield = new ArrayList<>();
    private List<IGroundUnitCollection> searchLightsForAirfield = new ArrayList<>();

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

    public List<IGroundUnitCollection> getAaaForAirfield()
    {
        return aaaForAirfield;
    }

    public void addAaaForAirfield(IGroundUnitCollection aaaMg)
    {
        this.aaaForAirfield.add(aaaMg);
    }

    public List<IGroundUnitCollection> getSearchLightsForAirfield()
    {
        return searchLightsForAirfield;
    }

    public void addSearchlightsForAirfield(IGroundUnitCollection aaa)
    {
        this.searchLightsForAirfield.add(aaa);
    }
}
