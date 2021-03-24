package pwcg.campaign;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.core.location.Coordinate;
import pwcg.mission.target.TargetType;

public class Skirmish
{
    private String name;
	private Coordinate neCorner;
	private Coordinate swCorner;
    private TargetType alliedTargetType;
    private TargetType axisTargetType;
    private Date startDate;
    private Date stopDate;
    private FrontMapIdentifier map;
    private List<SkirmishMissionPreference> skirmishMissionPreferences = new ArrayList<>();

	public Skirmish()
	{
	}

    public String getName()
    {
        return name;
    }

    public Coordinate getNeCorner()
    {
        return neCorner;
    }

    public Coordinate getSwCorner()
    {
        return swCorner;
    }

    public TargetType getAlliedTargetType()
    {
        return alliedTargetType;
    }

    public TargetType getAxisTargetType()
    {
        return axisTargetType;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getStopDate()
    {
        return stopDate;
    }

    public FrontMapIdentifier getMap()
    {
        return map;
    }
}
