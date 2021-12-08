package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType2;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PositionFinder;

public class AARPlayerLocator
{
    private AARVehicleBuilder aarVehicleBuilder = null;
    private LogEventData logEventData = null;
    private List<LogPlane> playerPlanes = new ArrayList<>();
    private List<Coordinate> playerLocations = new ArrayList<>();

    public AARPlayerLocator(LogEventData logEventData, AARVehicleBuilder aarVehicleBuilder)
    {
        this.logEventData = logEventData;        
        this.aarVehicleBuilder = aarVehicleBuilder; 
    }

    public void evaluatePlayerLocation()
    {
        playerPlanes = aarVehicleBuilder.getPlayerLogPlanes();
        
        for (IAType2 atype2 : logEventData.getDamageEvents())
        {
            if (isPlayerPlane(atype2.getVictim()) || isPlayerPlane(atype2.getVictor()))
            {
                Coordinate playerLocation = atype2.getLocation();
                playerLocations.add(playerLocation);
            }
        }
    }
    
    public double closestPlayerDistance(Coordinate location)
    {
        double closest = PositionFinder.ABSURDLY_LARGE_DISTANCE;
        for (Coordinate playerLocation : playerLocations)
        {
            double distance = MathUtils.calcDist(location, playerLocation);
            if (distance < closest)
            {
                closest = distance;
            }
        }
        return closest;
    }

    private boolean isPlayerPlane(String planeId)
    {
        for (LogPlane playerLogPlane : playerPlanes)
        {
            if (playerLogPlane.getId().equals(planeId))
            {
                return true;
            }
        }
        return false;
    }
}
