package pwcg.mission.flight.waypoint.patterns;

import java.util.Date;

import pwcg.campaign.api.Side;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;

public class PathAlongFrontData
{
    private Mission mission;
    private Coordinate targetGeneralLocation;
    private Date date;
    private Side side = Side.ALLIED;
    private int pathDistance = 15000;
    private int randomDistanceMax = 15000;
    private int offsetTowardsEnemy = 0;
    private boolean returnAlongRoute = false;
    
    public Mission getMission()
    {
        return mission;
    }

    public void setMission(Mission mission)
    {
        this.mission = mission;
    }

    public Coordinate getTargetGeneralLocation()
    {
        return targetGeneralLocation;
    }

    public void setTargetGeneralLocation(Coordinate targetGeneralLocation)
    {
        this.targetGeneralLocation = targetGeneralLocation;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Side getSide()
    {
        return side;
    }

    public void setSide(Side side)
    {
        this.side = side;
    }

    public int getPathDistance()
    {
        return pathDistance;
    }

    public void setPathDistance(int pathDistance)
    {
        this.pathDistance = pathDistance;
    }

    public int getRandomDistanceMax()
    {
        return randomDistanceMax;
    }

    public void setRandomDistanceMax(int randomDistanceMax)
    {
        this.randomDistanceMax = randomDistanceMax;
    }

    public int getOffsetTowardsEnemy()
    {
        return offsetTowardsEnemy;
    }

    public void setOffsetTowardsEnemy(int offsetTowardsEnemy)
    {
        this.offsetTowardsEnemy = offsetTowardsEnemy;
    }

    public boolean isReturnAlongRoute()
    {
        return returnAlongRoute;
    }

    public void setReturnAlongRoute(boolean returnAlongRoute)
    {
        this.returnAlongRoute = returnAlongRoute;
    }

}
