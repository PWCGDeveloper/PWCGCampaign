package pwcg.mission;

import java.util.HashMap;
import java.util.Map;

import pwcg.campaign.group.Block;
import pwcg.campaign.group.Bridge;

public class MissionGroundUnitResourceManager
{
    private Map<Integer, Block> usedTrainStations = new HashMap<>();
    private Map<Integer, Bridge> usedTruckBridges = new HashMap<>();

    public MissionGroundUnitResourceManager ()
    {
    }

    public void registerBridge(Bridge bridge)
    {
        usedTruckBridges.put(bridge.getIndex(), bridge);
    }

    public void registerTrainStation(Block trainStation)
    {
        usedTrainStations.put(trainStation.getIndex(), trainStation) ;
    }

    public boolean isTrainStationInUse(Integer id)
    {
        return usedTrainStations.containsKey(id);
    }

    public boolean isBridgeInUse(Integer id)
    {
        return usedTrainStations.containsKey(id);
    }
}
