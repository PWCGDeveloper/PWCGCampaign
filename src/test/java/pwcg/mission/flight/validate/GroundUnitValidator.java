package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.mission.IUnit;
import pwcg.mission.Mission;
import pwcg.mission.flight.Flight;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class GroundUnitValidator
{
    public void validateGroundUnitsForMission(Mission mission)
    {
        List<Flight> flights = mission.getMissionFlightBuilder().getAllAerialFlights();
        for (Flight flight : flights)
        {
            validateGroundUnitsForFlight(flight);
        }
    }

    public void validateGroundUnitsForFlight(Flight flight)
    {
        if (flight.isPlayerFlight() || flight.getFlightInformation().isEscortedByPlayerFlight() || flight.getFlightInformation().isEscortForPlayerFlight())
        {
            validatePlayerGroundUnits(flight);
        }
        else
        {
            validateAiGroundUnits(flight);
        }
    }
    
    private void validatePlayerGroundUnits(Flight flight)
    {
        for (IUnit linkedUnit : flight.getLinkedUnits())
        {
            if (linkedUnit instanceof IGroundUnitCollection)
            {
                IGroundUnitCollection groundUnitCollection = (IGroundUnitCollection)linkedUnit;
                if (((IGroundUnitCollection) linkedUnit).getGroundUnitCollectionType() == GroundUnitCollectionType.BALLOON_GROUND_UNIT_COLLECTION)
                {
                    assert(groundUnitCollection.getGroundUnits().size() == 3);
                }
                else if (((IGroundUnitCollection) linkedUnit).getGroundUnitCollectionType() == GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION)
                {
                    assert(groundUnitCollection.getGroundUnits().size() > 0);
                }
                else if (((IGroundUnitCollection) linkedUnit).getGroundUnitCollectionType() == GroundUnitCollectionType.STATIC_GROUND_UNIT_COLLECTION)
                {
                    assert(groundUnitCollection.getGroundUnits().size() > 0);
                }
                else if (((IGroundUnitCollection) linkedUnit).getGroundUnitCollectionType() == GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION)
                {
                    assert(groundUnitCollection.getGroundUnits().size() > 0);
                }
            }
        }
    }

    private void validateAiGroundUnits(Flight flight)
    {
        for (IUnit linkedUnit : flight.getLinkedUnits())
        {
            if (linkedUnit instanceof IGroundUnitCollection)
            {
                IGroundUnitCollection groundUnitCollection = (IGroundUnitCollection)linkedUnit;
                assert(groundUnitCollection.getGroundUnits().size() == 1);
            }
        }
    }

}
