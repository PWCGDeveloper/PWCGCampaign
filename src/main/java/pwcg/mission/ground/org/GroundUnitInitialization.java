package pwcg.mission.ground.org;

import pwcg.campaign.api.ICountry;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.target.TacticalTarget;

public class GroundUnitInitialization
{
    private TacticalTarget targetType = TacticalTarget.TARGET_NONE;
    private MissionBeginUnit missionBeginUnit;
    private String name;
    private Coordinate position;
    private Coordinate destinationCoords;
    private Orientation orientation;
    private ICountry country;
    private int minimumNumberUnits = 1;
    private int maximumNumberUnits = 1;

    public int calculateNumberOfUnits()
    {
        return minimumNumberUnits + (RandomNumberGenerator.getRandom(maximumNumberUnits - minimumNumberUnits));
    }
    
    public MissionBeginUnit getMissionBeginUnit()
    {
        return missionBeginUnit;
    }

    public void setMissionBeginUnit(MissionBeginUnit missionBeginUnit)
    {
        this.missionBeginUnit = missionBeginUnit;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Coordinate getPosition()
    {
        return position;
    }

    public void setPosition(Coordinate position)
    {
        this.position = position;
    }

    public Coordinate getDestinationCoords()
    {
        return destinationCoords;
    }

    public void setDestinationCoords(Coordinate destinationCoords)
    {
        this.destinationCoords = destinationCoords;
    }
    
    public Orientation getOrientation()
    {
        return orientation;
    }

    public void setOrientation(Orientation orientation)
    {
        this.orientation = orientation;
    }

    public ICountry getCountry()
    {
        return country;
    }

    public void setCountry(ICountry country)
    {
        this.country = country;
    }

    public int getMinimumNumberUnits()
    {
        return minimumNumberUnits;
    }

    public void setMinimumNumberUnits(int minimumNumberUnits)
    {
        this.minimumNumberUnits = minimumNumberUnits;
    }

    public int getMaximumNumberUnits()
    {
        return maximumNumberUnits;
    }

    public void setMaximumNumberUnits(int maximumNumberUnits)
    {
        this.maximumNumberUnits = maximumNumberUnits;
    }

    public TacticalTarget getTargetType()
    {
        return targetType;
    }

    public void setTargetType(TacticalTarget targetType)
    {
        this.targetType = targetType;
    }
}
