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

public class RunwayPlacerLineAbreast implements IRunwayPlacer
{
    private IFlight flight = null;
    private Airfield airfield = null;
    private Campaign campaign;
    private int takeoffSpacing = 40;

    public RunwayPlacerLineAbreast (IFlight flight, Airfield airfield, int takeoffSpacing)
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
            else
            {
                Coordinate nextTakeoffCoord = calculateNextAbreast(lastPlacement, takeoffSpacing);
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

        Coordinate fieldPlanePosition = airfield.getTakeoffLocation(mission).getPosition().copy();
        Coordinate initialCoord = MathUtils.calcNextCoord(campaign.getCampaignMap(), fieldPlanePosition, initialPlacementAngleAngle, (25.0));
        return initialCoord;
    }
    
    private Coordinate calculateNextAbreast(Coordinate lastPosition, int takeoffSpacing) throws PWCGException
    {
        Mission mission = flight.getMission();
        double takeoffAngle = airfield.getTakeoffLocation(mission).getOrientation().getyOri();
        double nextlacementAngleAngle = MathUtils.adjustAngle(takeoffAngle, 90);

        Coordinate nextTakeoffCoord = MathUtils.calcNextCoord(campaign.getCampaignMap(), lastPosition, nextlacementAngleAngle, (takeoffSpacing));
        return nextTakeoffCoord;
    }
}
