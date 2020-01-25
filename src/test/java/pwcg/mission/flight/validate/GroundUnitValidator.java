package pwcg.mission.flight.validate;

import java.util.List;

import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.ground.org.GroundUnitCollectionType;
import pwcg.mission.ground.org.IGroundUnit;
import pwcg.mission.ground.org.IGroundUnitCollection;

public class GroundUnitValidator
{
    public void validateGroundUnitsForMission(Mission mission)
    {
        List<IFlight> flights = mission.getMissionFlightBuilder().getAllAerialFlights();
        for (IFlight flight : flights)
        {
            validateGroundUnitsForFlight(flight);
        }
    }

    public void validateGroundUnitsForFlight(IFlight flight)
    {
        if (flight.getFlightInformation().isPlayerFlight() || flight.getFlightInformation().isEscortedByPlayerFlight() || flight.getFlightInformation().isEscortForPlayerFlight())
        {
            validatePlayerGroundUnits(flight);
        }
        else
        {
            validateAiGroundUnits(flight);
        }
    }
    
    private void validatePlayerGroundUnits(IFlight flight)
    {
        for (IGroundUnitCollection linkedUnit : flight.getLinkedGroundUnits().getLinkedGroundUnits())
        {
            if ((linkedUnit).getGroundUnitCollectionType() == GroundUnitCollectionType.BALLOON_GROUND_UNIT_COLLECTION)
            {
                assert(linkedUnit.getGroundUnits().size() == 3);
            }
            else if ((linkedUnit).getGroundUnitCollectionType() == GroundUnitCollectionType.INFANTRY_GROUND_UNIT_COLLECTION)
            {
                assert(linkedUnit.getGroundUnits().size() > 0);
            }
            else if ((linkedUnit).getGroundUnitCollectionType() == GroundUnitCollectionType.STATIC_GROUND_UNIT_COLLECTION)
            {
                assert(linkedUnit.getGroundUnits().size() > 0);
            }
            else if (linkedUnit.getGroundUnitCollectionType() == GroundUnitCollectionType.TRANSPORT_GROUND_UNIT_COLLECTION)
            {
                assert(linkedUnit.getGroundUnits().size() > 0);
            }
        }
    }

    private void validateAiGroundUnits(IFlight flight)
    {
        for (IGroundUnitCollection linkedUnit : flight.getLinkedGroundUnits().getLinkedGroundUnits())
        {
            for (IGroundUnit groundUnit : linkedUnit.getGroundUnits())
            {
                assert(groundUnit.getSpawners().size() == 1);
            }
        }
    }

}
