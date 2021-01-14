package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.ground.org.GroundUnitCollection;
import pwcg.mission.ground.org.IGroundUnit;

public class TargetDefinitionBuilderInfantry
{
    private FlightInformation flightInformation;
    private List<TargetDefinition> targetDefinitions = new ArrayList<>();

    public TargetDefinitionBuilderInfantry(FlightInformation flightInformation) throws PWCGException
    {
        this.flightInformation = flightInformation;
    }

    public List<TargetDefinition> findInfantryGroundUnits() throws PWCGException
    {
        for (GroundUnitCollection groundUnitCollection : flightInformation.getMission().getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            List<TargetDefinition> targetDefinitionsForCollection = createTargetDefinitionFromGroundUnit(groundUnitCollection);
            targetDefinitions.addAll(targetDefinitionsForCollection);
        }
        return targetDefinitions;
    }

    private List<TargetDefinition> createTargetDefinitionFromGroundUnit(GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        List<TargetDefinition> targetDefinitionsForCollection = new ArrayList<>();
        for (IGroundUnit enemyGroundUnit : groundUnitCollection.getInterestingGroundUnitsForSide(flightInformation.getSquadron().determineEnemySide()))
        {
            TargetDefinition targetDefinition = new TargetDefinition(enemyGroundUnit.getTargetType(), enemyGroundUnit.getPosition().copy(), enemyGroundUnit.getCountry());
            targetDefinitionsForCollection.add(targetDefinition);
        }

        return targetDefinitionsForCollection;
    }
}
