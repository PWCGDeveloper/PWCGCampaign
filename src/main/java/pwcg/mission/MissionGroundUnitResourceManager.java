package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;
import pwcg.campaign.group.GroupManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.org.IGroundUnit;

public class MissionGroundUnitResourceManager
{
    private Map<Integer, Block> usedTrainStations = new HashMap<>();
    private Map<Integer, Bridge> usedTruckBridges = new HashMap<>();
    private List<IGroundUnit> balloonsInMission = new ArrayList<>();

    public MissionGroundUnitResourceManager ()
    {
    }

    public void registerBridge(Bridge bridge)
    {
        usedTruckBridges.put(bridge.getIndex(), bridge);
    }

    public void registerTrainStation(Coordinate trainCoordinate) throws PWCGException
    {
        GroupManager groupManager = PWCGContext.getInstance().getCurrentMap().getGroupManager();
        Block trainStation = groupManager.getRailroadStationFinder().getClosestTrainPosition(trainCoordinate);
        usedTrainStations.put(trainStation.getIndex(), trainStation) ;
    }

    public void registerBalloon(IGroundUnit balloonUnit)
    {
        balloonsInMission.add(balloonUnit) ;
    }

    public boolean isTrainStationInUse(Integer id)
    {
        return usedTrainStations.containsKey(id);
    }

    public boolean isBridgeInUse(Integer id)
    {
        return usedTrainStations.containsKey(id);
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
