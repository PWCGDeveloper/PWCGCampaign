package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.org.IGroundUnit;

public class MissionGroundUnitResourceManager
{
    private List<IGroundUnit> balloonsInMission = new ArrayList<>();

    public MissionGroundUnitResourceManager ()
    {
    }

    public void registerBalloon(IGroundUnit balloonUnit)
    {
        balloonsInMission.add(balloonUnit) ;
    }

    public boolean isBalloonPositionInUse(Coordinate requestedBalloonPosition) throws PWCGException
    {
        for (IGroundUnit balloon : balloonsInMission)
        {
            double distance = MathUtils.calcDist(requestedBalloonPosition, balloon.getPosition());
            if (distance < 2000)
            {
                return true;
            }
        }
        return false;
    }
    
    public List<IGroundUnit> getBalloonsForSide(Side side) throws PWCGException
    {
        List<IGroundUnit> balloonsForSide = new ArrayList<>();
        for (IGroundUnit balloon : balloonsInMission)
        {
            if (balloon.getCountry().getSide() == side)
            {
                balloonsForSide.add(balloon);
            }
        }
        return balloonsForSide;
    }
}
