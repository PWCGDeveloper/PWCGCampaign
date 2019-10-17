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
        Coordinate initialPlacement = calculateInitialPlacement();
        
        Coordinate lastPlacement = initialPlacement.copy();
        for (int i = 0; i < flight.getPlanes().size(); ++i)
        {
            if (i == 0)
            {
                takeOffPositions.add(initialPlacement);
                lastPlacement = initialPlacement.copy();
            }
            else
            {
                Coordinate nextTakeoffCoord = calculateNextAstern(lastPlacement, takeoffSpacing);
                takeOffPositions.add(nextTakeoffCoord);
                lastPlacement = nextTakeoffCoord.copy();
            }
        }

        Collections.reverse(takeOffPositions);
        
        return takeOffPositions;
    }
    
    private Coordinate calculateInitialPlacement() throws PWCGException
    {
        double takeoffAngle = airfield.getTakeoffLocation().getOrientation().getyOri();
        double initialPlacementAngleAngle = MathUtils.adjustAngle(takeoffAngle, 270);

        Coordinate fieldPlanePosition = airfield.getTakeoffLocation().getPosition().copy();
        Coordinate initialCoord = MathUtils.calcNextCoord(fieldPlanePosition, initialPlacementAngleAngle, (25.0));
        return initialCoord;
    }
    
    private Coordinate calculateNextAstern(Coordinate lastPosition, int takeoffSpacing) throws PWCGException
    {
        double takeoffAngle = airfield.getTakeoffLocation().getOrientation().getyOri();
        double nextlacementAngleAngle = MathUtils.adjustAngle(takeoffAngle, 180);

        Coordinate nextTakeoffCoord = MathUtils.calcNextCoord(lastPosition, nextlacementAngleAngle, (takeoffSpacing));
        return nextTakeoffCoord;
    }
}
