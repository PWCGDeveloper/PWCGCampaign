package pwcg.campaign.target.locator.targettype;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PositionFinder;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.target.TargetType;
import pwcg.mission.target.TargetRadius;
import pwcg.mission.target.locator.StrategicTargetLocator;

public class StrategicTargetTypeGenerator
{
    private Side side;
    private Date date;
    private Coordinate targetLocation;
    
    public StrategicTargetTypeGenerator(Side side, Date date, Coordinate referenceLocation)
    {
        this.side = side;
        this.date = date;
        this.targetLocation = referenceLocation;
    }
    
    public TargetType createTargetType(double missionRadius) throws PWCGException
    {
        List<TargetType> targetTypes = createAvailableTargetTypes(missionRadius);
        int index = RandomNumberGenerator.getRandom(targetTypes.size());
        return targetTypes.get(index);
    }

    private List <TargetType> createAvailableTargetTypes(double missionRadius) throws PWCGException
    {
        TargetRadius targetRadius = new TargetRadius();
        targetRadius.calculateTargetRadius(FlightTypes.STRATEGIC_BOMB, missionRadius);
        int strategicBombingRadius = Double.valueOf(targetRadius.getInitialTargetRadius()).intValue();

        Map<TargetType, List<IFixedPosition>> targetAvailability = new HashMap<>();
        while (targetAvailability.size() == 0 && strategicBombingRadius < PositionFinder.ABSURDLY_LARGE_DISTANCE)
        {
            StrategicTargetLocator strategicTargetLocator = new StrategicTargetLocator(strategicBombingRadius, side, date, targetLocation);
            targetAvailability = strategicTargetLocator.getStrategicTargetAvailability();
            strategicBombingRadius += 20000;
        }
        
        List<TargetType> availbleTargetTypes = new ArrayList<TargetType>();

        if (targetAvailability.containsKey(TargetType.TARGET_FACTORY))
        {
            availbleTargetTypes.add(TargetType.TARGET_FACTORY);
            availbleTargetTypes.add(TargetType.TARGET_FACTORY);
            availbleTargetTypes.add(TargetType.TARGET_FACTORY);
            availbleTargetTypes.add(TargetType.TARGET_FACTORY);
            availbleTargetTypes.add(TargetType.TARGET_FACTORY);
        }
        
        if (targetAvailability.containsKey(TargetType.TARGET_RAIL))
        {
            availbleTargetTypes.add(TargetType.TARGET_RAIL);
            availbleTargetTypes.add(TargetType.TARGET_RAIL);
            availbleTargetTypes.add(TargetType.TARGET_RAIL);
        }
        
        if (targetAvailability.containsKey(TargetType.TARGET_PORT))
        {
            availbleTargetTypes.add(TargetType.TARGET_PORT);
            availbleTargetTypes.add(TargetType.TARGET_PORT);
            availbleTargetTypes.add(TargetType.TARGET_PORT);
        }
        
        if (targetAvailability.containsKey(TargetType.TARGET_AIRFIELD))
        {
            availbleTargetTypes.add(TargetType.TARGET_AIRFIELD);
            availbleTargetTypes.add(TargetType.TARGET_AIRFIELD);
        }
        
        if (availbleTargetTypes.isEmpty())
        {
            availbleTargetTypes.add(TargetType.TARGET_CITY);
        }

        return availbleTargetTypes;
    }
}
