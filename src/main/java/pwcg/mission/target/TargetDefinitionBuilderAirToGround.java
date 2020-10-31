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
        List<IGroundUnit> enemyGroundUnits = groundUnitCollection.getInterestingGroundUnitsForSide(flightInformation.getSquadron().determineEnemySide());
        IGroundUnit selectedGroundUnit = findTarget(enemyGroundUnits);
        
        TargetDefinition targetDefinition = new TargetDefinition(
                selectedGroundUnit.getTargetType(), selectedGroundUnit.getPosition().copy(),
                selectedGroundUnit.getCountry());
        
        return targetDefinition;
    }

    private IGroundUnit findTarget(List<IGroundUnit> enemyGroundUnits)
    {
        IGroundUnit selectedGroundUnit = null;
        if (preferArmor())
        {
            selectedGroundUnit = findArmoredTarget(enemyGroundUnits);
        }
        
        if (selectedGroundUnit == null)
        {
            int index = RandomNumberGenerator.getRandom(enemyGroundUnits.size());
            selectedGroundUnit = enemyGroundUnits.get(index);
        }
        
        return selectedGroundUnit;
    }

    private boolean preferArmor()
    {
        int preferArmorTarget = RandomNumberGenerator.getRandom(100);
        if (preferArmorTarget < 50)
        {
            return true;
        }
        return false;
    }
    

    private IGroundUnit findArmoredTarget(List<IGroundUnit> enemyGroundUnits)
    {
        for (IGroundUnit enemyGroundUnit : enemyGroundUnits)
        {
            if (enemyGroundUnit.getTargetType() == TargetType.TARGET_ARMOR)
            {
                return enemyGroundUnit;
            }
        }
        return null;
    }

}
