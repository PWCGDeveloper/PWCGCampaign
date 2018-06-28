package pwcg.mission;

import java.util.ArrayList;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IPWCGObject;
import pwcg.campaign.context.Country;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.PWCGLocation;

public abstract class Unit extends PWCGLocation implements IPWCGObject
{
    protected int index = IndexGenerator.getInstance().getNextIndex();;
	protected ICountry country = CountryFactory.makeCountryByCountry(Country.NEUTRAL);

	public static enum UnitTypes
	{
		PLANES,
		GROUND_FORCES,
		SHIPS,
		TRAIN,
		AIRFIELD
	}
	
	protected MissionBeginUnit missionBeginUnit = null;
	protected ArrayList<Unit> linkedUnits = new ArrayList<Unit>();

    abstract public void createUnitMission() throws PWCGException ;

	public Unit()
	{
		super();
	}

	public void initialize(MissionBeginUnit missionBeginUnit, Coordinate unitPosition, String name, ICountry country) 
	{
        this.missionBeginUnit = missionBeginUnit;
        this.country = country;
		super.initialize(unitPosition, name);		
	}

	public ArrayList<Unit> getLinkedUnits() {
		return linkedUnits;
	}

    public void addLinkedUnit (Unit unit) throws PWCGException
    {
        if (unit != null)
        {
            linkedUnits.add(unit);
        }
        else
        {
            throw new PWCGException("Attempt to add NULL unit");
        }
    }

    public MissionBeginUnit getMissionBeginUnit()
    {
        return missionBeginUnit;
    }
    
    public int getIndex()
    {
        return index;
    }

	public void setCountry(ICountry country)
	{
		this.country = country;
	}

	public ICountry getCountry()
	{
		return country;
	}
}	



