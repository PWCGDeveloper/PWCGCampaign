package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.skirmish.Skirmish;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.MathUtils;
import pwcg.mission.flight.FlightInformation;

public class SkirmishTargetDefinitionBuilder
{
    private FlightInformation flightInformation;

    public SkirmishTargetDefinitionBuilder(FlightInformation flightInformation) throws PWCGException
    {
        this.flightInformation = flightInformation;
    }

    public TargetDefinition findIconicTarget() throws PWCGException
    {
        TargetType iconicTargetType = PWCGContext.getInstance().getCurrentMap().getSkirmishManager().getIconicTargetTypes(flightInformation);
        
        if (iconicTargetType != TargetType.TARGET_NONE)
        {
            TargetDefinitionCollector targetDefinitionCollector = new TargetDefinitionCollector(flightInformation);
            List<TargetDefinition> allTargets = targetDefinitionCollector.collectTargetDefinition();

            List<TargetDefinition> iconicTargets = getIconicTargets(allTargets, iconicTargetType);
            return getIconicTargetsNearSkirmish(iconicTargets);
        }
        
        return null;
    }

    private List<TargetDefinition> getIconicTargets(List<TargetDefinition> availableTargets, TargetType iconicTargetType)
    {
        List<TargetDefinition> iconicTargets = new ArrayList<>();
        for (TargetDefinition availableTarget : availableTargets)
        {
            if (availableTarget.getTargetType() == iconicTargetType)
            {
                iconicTargets.add(availableTarget);
            }
        }
        return iconicTargets;
    }

    private TargetDefinition getIconicTargetsNearSkirmish(List<TargetDefinition> iconicTargets) throws PWCGException
    {
        Skirmish skirmish = flightInformation.getMission().getSkirmish();
        TargetDefinition iconicTargetNearSkirmish = null;
        for (TargetDefinition iconicTarget : iconicTargets)
        {
            if (iconicTargetNearSkirmish != null)
            {
                double currentDistance = MathUtils.calcDist(iconicTargetNearSkirmish.getPosition(), skirmish.getCenter());
                double newTargetDistance = MathUtils.calcDist(iconicTarget.getPosition(), skirmish.getCenter());
                if (newTargetDistance < currentDistance)
                {
                    iconicTargetNearSkirmish = iconicTarget;
                }
            }
            else
            {
                iconicTargetNearSkirmish = iconicTarget;
            }
        }
        return iconicTargetNearSkirmish;
    }
}
