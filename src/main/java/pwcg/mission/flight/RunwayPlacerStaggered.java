package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.api.IAirfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;

public class RunwayPlacerStaggered implements IRunwayPlacer
{
    private Flight flight = null;
    private IAirfield airfield = null;
    private int takeoffSpacing = 40;

    public RunwayPlacerStaggered (Flight flight, IAirfield airfield, int takeoffSpacing)
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
            else if ((i %2) != 0)
            {
                Coordinate nextTakeoffCoord = calculateNextRight(lastPlacement);
                takeOffPositions.add(nextTakeoffCoord);
                lastPlacement = nextTakeoffCoord.copy();
            }
            else
            {
                Coordinate nextTakeoffCoord = calculateNextLeft(lastPlacement);
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
        
        // Move initial placement directly left of the start point
        double offsetLeftDistance = (takeoffSpacing / 2) * Math.cos(45);

        Coordinate fieldPlanePosition = airfield.getTakeoffLocation().getPosition().copy();
        Coordinate initialCoord = MathUtils.calcNextCoord(fieldPlanePosition, initialPlacementAngleAngle, (offsetLeftDistance));
        return initialCoord;
    }
    
    private Coordinate calculateNextRight(Coordinate lastPosition) throws PWCGException
    {
        double takeoffAngle = airfield.getTakeoffLocation().getOrientation().getyOri();
        double nextlacementAngleAngle = MathUtils.adjustAngle(takeoffAngle, 45);

        Coordinate nextTakeoffCoord = MathUtils.calcNextCoord(lastPosition, nextlacementAngleAngle, (takeoffSpacing));
        return nextTakeoffCoord;
    }
    
    private Coordinate calculateNextLeft(Coordinate lastPosition) throws PWCGException
    {
        double takeoffAngle = airfield.getTakeoffLocation().getOrientation().getyOri();
        double nextlacementAngleAngle = MathUtils.adjustAngle(takeoffAngle, 315);

        Coordinate nextTakeoffCoord = MathUtils.calcNextCoord(lastPosition, nextlacementAngleAngle, (takeoffSpacing));
        return nextTakeoffCoord;
    }
}
