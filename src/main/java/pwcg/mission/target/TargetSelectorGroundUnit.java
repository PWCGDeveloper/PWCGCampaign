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
	    if (flightType == FlightTypes.CONTACT_PATROL)
        {
            return findAssaultGroundUnits();        
        }
        else if (flightType == FlightTypes.CARGO_DROP)
        {
            return findAssaultGroundUnits();        
        }
        else if (flightType == FlightTypes.PARATROOP_DROP)
        {
            return findAssaultGroundUnits();        
        }   
        else if (flightType == FlightTypes.SPY_EXTRACT)
        {
            return findAssaultGroundUnits();        
        }   
        else
        {
            return findAnyGroundUnits();
        }
	}

    private IGroundUnitCollection findAssaultGroundUnits() throws PWCGException
    {
        for (IGroundUnitCollection groundUnitCollection : flightInformation.getMission().getMissionGroundUnitBuilder().getAllMissionGroundUnits())
        {
            if (groundUnitCollection.getTargetType() == TargetType.TARGET_INFANTRY)
            {
                return groundUnitCollection;
            }
        }
        
        return findAnyGroundUnits();
    }

    private IGroundUnitCollection findAnyGroundUnits() throws PWCGException
    {
        List<TargetType> shuffledTargetTypes = TargetPriorityGeneratorGroundUnit.getTargetTypePriorities(flightInformation.getCampaign(), flightInformation.getSquadron());
        List<IGroundUnitCollection> shuffledGroundUnits = flightInformation.getMission().getMissionGroundUnitBuilder().getAllMissionGroundUnits();
        Collections.shuffle(shuffledGroundUnits);

        for (TargetType desiredTargetType : shuffledTargetTypes)
        {
            for (IGroundUnitCollection groundUnitCollection : shuffledGroundUnits)
            {
                if (desiredTargetType == groundUnitCollection.getTargetType())
                {
                    return groundUnitCollection;
                }
            }
        }
        throw new PWCGException ("No targets available inm mission");
    }
 }
