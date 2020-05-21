package pwcg.mission.target;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class TargetDefinitionBuilderAirToGround implements ITargetDefinitionBuilder
{
    private IFlightInformation flightInformation;

    public TargetDefinitionBuilderAirToGround (IFlightInformation flightInformation)
    {
        this.flightInformation = flightInformation;
    }

    @Override
    public TargetDefinition buildTargetDefinition () throws PWCGException
    {        
        TargetSelectorGroundUnit targetSelector = new TargetSelectorGroundUnit(flightInformation);
        IGroundUnitCollection target = targetSelector.findTarget();
        TargetDefinition targetDefinition = createTargetDefinitionFromGroundUnit(flightInformation, target);

        return targetDefinition;
    }
    
    private TargetDefinition createTargetDefinitionFromGroundUnit (IFlightInformation flightInformation, IGroundUnitCollection groundUnitCollection) throws PWCGException
    {
        List<IGroundUnit> enemyGroundUnits = groundUnitCollection.getGroundUnitsForSide(flightInformation.getSquadron().determineEnemySide().getOppositeSide());
        int index = RandomNumberGenerator.getRandom(enemyGroundUnits.size());
        IGroundUnit selectedGroundUnit = enemyGroundUnits.get(index);
        
        TargetDefinition targetDefinition = new TargetDefinition(
                selectedGroundUnit.getTargetType(), selectedGroundUnit.getPosition().copy(),
                selectedGroundUnit.getCountry());
        
        return targetDefinition;
    }
}
