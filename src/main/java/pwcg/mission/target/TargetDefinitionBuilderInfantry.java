package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
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
        for (GroundUnitCollection groundUnitCollection : flightInformation.getMission().getGroundUnitBuilder().getAllMissionGroundUnits())
        {
            List<TargetDefinition> targetDefinitionsForCollection = createTargetDefinitionFromGroundUnit(groundUnitCollection);
            targetDefinitions.addAll(targetDefinitionsForCollection);
        }
        return targetDefinitions;
    }

    private List<TargetDefinition> createTargetDefinitionFromGroundUnit(GroundUnitCollection groundUnitCollection) throws PWCGException
    {
        List<TargetDefinition> targetDefinitionsForCollection = new ArrayList<>();
        for (IGroundUnit enemyGroundUnit : groundUnitCollection.getInterestingGroundUnitsForSide(flightInformation.getCountry().getSide().getOppositeSide()))
        {
            String targetDescription = buildTargetDescription(enemyGroundUnit);
            TargetDefinition targetDefinition = new TargetDefinition(enemyGroundUnit.getTargetType(), enemyGroundUnit.getPosition().copy(), enemyGroundUnit.getCountry(), targetDescription);
            if (targetDefinition.getTargetType() == TargetType.TARGET_AIRFIELD)
            {
                if (isCloseToAirfield(targetDefinition.getPosition()))
                {
                    targetDefinitionsForCollection.add(targetDefinition);
                }
            }
            else
            {
                targetDefinitionsForCollection.add(targetDefinition);
            }
        }

        return targetDefinitionsForCollection;
    }
    
    private String buildTargetDescription(IGroundUnit enemyGroundUnit)
    {
        if (enemyGroundUnit.getVehicles() != null && enemyGroundUnit.getVehicles().size() > 0)
        {
            return enemyGroundUnit.getVehicles().get(0).getVehicleName();
        }
        return "Infantry";
    }

    private boolean isCloseToAirfield(Coordinate blockPosition) throws PWCGException
    {
        List<Airfield> closeAirfields = PWCGContext.getInstance().getCurrentMap().getAirfieldManager().getAirfieldFinder().getAirfieldsWithinRadiusBySide(
                blockPosition, flightInformation.getCampaign().getDate(), 5000, flightInformation.getCountry().getSide().getOppositeSide());
        if (closeAirfields.isEmpty())
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
