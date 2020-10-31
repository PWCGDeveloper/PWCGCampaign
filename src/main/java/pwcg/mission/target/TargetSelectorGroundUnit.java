package pwcg.mission.target;

import java.util.Collections;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class TargetSelectorGroundUnit
{
    private IFlightInformation flightInformation;

    public TargetSelectorGroundUnit(IFlightInformation flightInformation) throws PWCGException 
    { 
        this.flightInformation = flightInformation;
    }

	public IGroundUnitCollection findTarget() throws PWCGException 
	{
	    FlightTypes flightType = flightInformation.getFlightType();
	    if (flightType == FlightTypes.CONTACT_PATROL || 
	    	flightType == FlightTypes.CARGO_DROP || 
	    	flightType == FlightTypes.PARATROOP_DROP)
        {
            return findInfantryGroundUnits();        
        }
        else
        {
            return findAnyGroundUnits();
        }
	}

    private IGroundUnitCollection findInfantryGroundUnits() throws PWCGException
    {
        for (IGroundUnitCollection groundUnitCollection : flightInformation.getMission().getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            if (groundUnitCollection.getTargetType() == TargetType.TARGET_INFANTRY)
            {
                int enemyGroundUnitCount = groundUnitCollection.getGroundUnitsForSide(flightInformation.getSquadron().determineEnemySide()).size();
                if (enemyGroundUnitCount > 0)
                {
                    return groundUnitCollection;
                }
            }
        }
        
        IGroundUnitCollection targetGroundUnit = findGroundUnitsOfType(TargetType.TARGET_ASSAULT);
        if (targetGroundUnit != null)
        {
            return targetGroundUnit;
        }
        
        targetGroundUnit = findGroundUnitsOfType(TargetType.TARGET_INFANTRY);
        if (targetGroundUnit != null)
        {
            return targetGroundUnit;
        }
        
        targetGroundUnit = findGroundUnitsOfType(TargetType.TARGET_ARTILLERY);
        if (targetGroundUnit != null)
        {
            return targetGroundUnit;
        }
        
        targetGroundUnit = findGroundUnitsOfType(TargetType.TARGET_ARMOR);
        if (targetGroundUnit != null)
        {
            return targetGroundUnit;
        }
        
        return findAnyGroundUnits();
    }

    private IGroundUnitCollection findGroundUnitsOfType(TargetType targetType) throws PWCGException
    {
        for (IGroundUnitCollection groundUnitCollection : flightInformation.getMission().getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            if (groundUnitCollection.getTargetType() == targetType)
            {
                int enemyGroundUnitCount = groundUnitCollection.getGroundUnitsForSide(flightInformation.getSquadron().determineEnemySide()).size();
                if (enemyGroundUnitCount > 0)
                {
                    return groundUnitCollection;
                }
            }
        }
        return null;
    }

    private IGroundUnitCollection findAnyGroundUnits() throws PWCGException
    {
        List<TargetType> shuffledTargetTypes = TargetPriorityGeneratorGroundUnit.getTargetTypePriorities(flightInformation.getCampaign(), flightInformation.getSquadron());
        List<IGroundUnitCollection> shuffledGroundUnits = flightInformation.getMission().getMissionGroundUnitBuilder().getAllInterestingMissionGroundUnits();
        Collections.shuffle(shuffledGroundUnits);

        for (TargetType desiredTargetType : shuffledTargetTypes)
        {
            for (IGroundUnitCollection groundUnitCollection : shuffledGroundUnits)
            {
                TargetType unitTargetType = groundUnitCollection.getTargetType();
                if (desiredTargetType == unitTargetType)
                {
                    int enemyGroundUnitCount = groundUnitCollection.getGroundUnitsForSide(flightInformation.getSquadron().determineEnemySide()).size();
                    if (enemyGroundUnitCount > 0)
                    {
                        return groundUnitCollection;
                    }
                }
            }
        }
        throw new PWCGException ("No targets available in mission");
    }
 }
