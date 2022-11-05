package pwcg.mission.flight.initialposition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class RunwayPlacerStaggered implements IRunwayPlacer
{
    private IFlight flight;
    private Campaign campaign;
    private Airfield airfield;
    private int takeoffSpacing = 40;

    public RunwayPlacerStaggered (IFlight flight, Airfield airfield, int takeoffSpacing)
    {
        this.flight = flight;
        this.campaign = flight.getCampaign();
        this.airfield = airfield;
        this.takeoffSpacing = takeoffSpacing;
    }


    public List<Coordinate> getFlightTakeoffPositions() throws PWCGException
    {
        List<Coordinate> takeOffPositions = new ArrayList<>();
        Coordinate initialPlacement = calculateInitialPlacement();
        
        Coordinate lastPlacement = initialPlacement.copy();
        for (int i = 0; i < flight.getFlightPlanes().getFlightSize(); ++i)
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
        Mission mission = flight.getMission();
        double takeoffAngle = airfield.getTakeoffLocation(mission).getOrientation().getyOri();
        double initialPlacementAngleAngle = MathUtils.adjustAngle(takeoffAngle, 270);
        
        // Move initial placement directly left of the start point
        double offsetLeftDistance = (takeoffSpacing / 2) * Math.cos(45);

        Coordinate fieldPlanePosition = airfield.getTakeoffLocation(mission).getPosition().copy();
        Coordinate initialCoord = MathUtils.calcNextCoord(campaign.getCampaignMap(), fieldPlanePosition, initialPlacementAngleAngle, (offsetLeftDistance));
        
        initialCoord = moveFlightForwardToEnsureTakeoff(initialCoord, takeoffAngle);
        
        return initialCoord;
    }
    
    private Coordinate moveFlightForwardToEnsureTakeoff(Coordinate initialCoord, double takeoffAngle) throws PWCGException
    {
        return MathUtils.calcNextCoord(campaign.getCampaignMap(), initialCoord, takeoffAngle, 50.0);
    }

    private Coordinate calculateNextRight(Coordinate lastPosition) throws PWCGException
    {
        Mission mission = flight.getMission();
        double takeoffAngle = airfield.getTakeoffLocation(mission).getOrientation().getyOri();
        double nextlacementAngleAngle = MathUtils.adjustAngle(takeoffAngle, 45);

        Coordinate nextTakeoffCoord = MathUtils.calcNextCoord(campaign.getCampaignMap(), lastPosition, nextlacementAngleAngle, (takeoffSpacing));
        return nextTakeoffCoord;
    }
    
    private Coordinate calculateNextLeft(Coordinate lastPosition) throws PWCGException
    {
        Mission mission = flight.getMission();
        double takeoffAngle = airfield.getTakeoffLocation(mission).getOrientation().getyOri();
        double nextlacementAngleAngle = MathUtils.adjustAngle(takeoffAngle, 315);

        Coordinate nextTakeoffCoord = MathUtils.calcNextCoord(campaign.getCampaignMap(), lastPosition, nextlacementAngleAngle, (takeoffSpacing));
        return nextTakeoffCoord;
    }
}
