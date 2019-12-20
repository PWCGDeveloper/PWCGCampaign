package pwcg.mission.flight.initialposition;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

public interface IRunwayPlacer
{
    public List<Coordinate> getFlightTakeoffPositions() throws PWCGException;
}
