package pwcg.campaign.target;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.campaign.api.IFixedPosition;
import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.api.Side;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.FlightTypes;

public class StrategicTargetTypeGenerator
{
    private Side side;
    private Date date;
    private Coordinate referenceLocation;
    
    public StrategicTargetTypeGenerator(Side side, Date date, Coordinate referenceLocation)
    {
        this.side = side;
        this.date = date;
        this.referenceLocation = referenceLocation;
    }
    
    public TacticalTarget createTargetType() throws PWCGException
    {
        List<TacticalTarget> targetTypes = createAvailableTargetTypes();
        int index = RandomNumberGenerator.getRandom(targetTypes.size());
        return targetTypes.get(index);
    }

    private List <TacticalTarget> createAvailableTargetTypes() throws PWCGException
    {
        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
        int strategicBombingRadius = productSpecificConfiguration.getInitialTargetRadiusFromGeneralTargetLocation(FlightTypes.STRATEGIC_BOMB);

        StrategicTargetLocator strategicTargetLocator = new StrategicTargetLocator(strategicBombingRadius, side, date, referenceLocation);
        Map<TacticalTarget, List<IFixedPosition>> targetAvailability = strategicTargetLocator.getStrategicTargetAvailability();
        
        List<TacticalTarget> availbleTargetTypes = new ArrayList<TacticalTarget>();

        if (targetAvailability.containsKey(TacticalTarget.TARGET_FACTORY))
        {
            availbleTargetTypes.add(TacticalTarget.TARGET_FACTORY);
            availbleTargetTypes.add(TacticalTarget.TARGET_FACTORY);
            availbleTargetTypes.add(TacticalTarget.TARGET_FACTORY);
            availbleTargetTypes.add(TacticalTarget.TARGET_FACTORY);
            availbleTargetTypes.add(TacticalTarget.TARGET_FACTORY);
        }
        
        if (targetAvailability.containsKey(TacticalTarget.TARGET_RAIL))
        {
            availbleTargetTypes.add(TacticalTarget.TARGET_RAIL);
            availbleTargetTypes.add(TacticalTarget.TARGET_RAIL);
            availbleTargetTypes.add(TacticalTarget.TARGET_RAIL);
        }
        
        if (targetAvailability.containsKey(TacticalTarget.TARGET_PORT))
        {
            availbleTargetTypes.add(TacticalTarget.TARGET_PORT);
            availbleTargetTypes.add(TacticalTarget.TARGET_PORT);
            availbleTargetTypes.add(TacticalTarget.TARGET_PORT);
        }
        
        if (targetAvailability.containsKey(TacticalTarget.TARGET_AIRFIELD))
        {
            availbleTargetTypes.add(TacticalTarget.TARGET_AIRFIELD);
            availbleTargetTypes.add(TacticalTarget.TARGET_AIRFIELD);
        }
        
        if (availbleTargetTypes.isEmpty())
        {
            availbleTargetTypes.add(TacticalTarget.TARGET_CITY);
        }

        return availbleTargetTypes;
    }
}
