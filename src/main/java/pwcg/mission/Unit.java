package pwcg.mission;

import java.util.ArrayList;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IPWCGObject;
import pwcg.campaign.utils.IndexGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public abstract class Unit  implements IPWCGObject
{
    protected int index = IndexGenerator.getInstance().getNextIndex();	
	protected ArrayList<Unit> linkedUnits = new ArrayList<Unit>();

    abstract public void createUnitMission() throws PWCGException ;
    abstract public ICountry getCountry() throws PWCGException ;
    abstract public Coordinate getPosition() throws PWCGException ;
    abstract public String getName() throws PWCGException ;
    abstract public MissionBeginUnit getMissionBeginUnit();

    public static final Integer NUM_IN_FORMATION_START = 1;
    
	public Unit()
	{
	}

	public ArrayList<Unit> getLinkedUnits() 
	{
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
    
    public int getIndex()
    {
        return index;
    }
}	



