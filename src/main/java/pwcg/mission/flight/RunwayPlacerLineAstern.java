package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class RunwayPlacerLineAstern implements IRunwayPlacer
{
    private Flight flight = null;
    private IAirfield airfield = null;
    private int takeoffSpacing = 40;

    public RunwayPlacerLineAstern (Flight flight, IAirfield airfield, int takeoffSpacing)
    {
        this.flight = flight;
        this.airfield = airfield;
        this.takeoffSpacing = takeoffSpacing;
    }

    public List<Coordinate> getFlightTakeoffPositions() throws PWCGException
    {
        List<Coordinate> takeOffPositions = new ArrayList<>();
                
        Coordinate fieldPlanePosition = airfield.getPlanePosition().getPosition().copy();
        double fieldPlaneOrientation = airfield.getPlaneOrientation();
        for (int i = 0; i < flight.getPlanes().size(); ++i)
        {
            Coordinate takeoffCoordsForPlane = MathUtils.calcNextCoord(fieldPlanePosition, fieldPlaneOrientation, (takeoffSpacing * i));
            takeOffPositions.add(takeoffCoordsForPlane);
        }

        Collections.reverse(takeOffPositions);
        
        return takeOffPositions;
    }
}
