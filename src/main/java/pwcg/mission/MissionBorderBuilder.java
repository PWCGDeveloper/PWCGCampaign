package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.flight.Flight;
import pwcg.mission.mcu.McuWaypoint;

public class MissionBorderBuilder 
{
	private static final double MIN_BOX_DIAMETER = 25000.0;
	
	private Flight playerFlight;
	private CoordinateBox coordinateBox;

    public static CoordinateBox buildCoordinateBox (Flight playerFlight, int additionalSpread) throws PWCGException
    {
        MissionBorderBuilder missionBorders = new MissionBorderBuilder(playerFlight);
        return missionBorders.makeCoordinateBox(additionalSpread);
    }

    private MissionBorderBuilder (Flight playerFlight)
    {
        this.playerFlight = playerFlight;
    }
    
    private CoordinateBox makeCoordinateBox (int additionalSpread) throws PWCGException
    {
        setMissionBorderToWaypointBorder();
        addToBordersForNarrowMissions();
        expandMissionBorders(additionalSpread);
        keepMissionBordersWithinMap();
        return coordinateBox;

    }

	private void setMissionBorderToWaypointBorder() throws PWCGException
	{
        List<Coordinate> coordinates = new ArrayList<>();
        for (McuWaypoint waypoint : playerFlight.getAllWaypoints())
        {
            coordinates.add(waypoint.getPosition());
        }
        
        coordinateBox =  CoordinateBox.coordinateBoxFromCoordinateList(coordinates);
 	}

	private void addToBordersForNarrowMissions() throws PWCGException
	{
		if (coordinateBox.getBoxHeight() < MIN_BOX_DIAMETER) 
		{
			double extraHeight = MIN_BOX_DIAMETER - coordinateBox.getBoxHeight();
			coordinateBox.addHeight(extraHeight / 2);
		}
				
		if (coordinateBox.getBoxWidth() < MIN_BOX_DIAMETER) 
		{
			double extraWidth = MIN_BOX_DIAMETER - coordinateBox.getBoxWidth();
            coordinateBox.addWidth(extraWidth / 2);
		}
	}

    private void expandMissionBorders(int additionalSpread) throws PWCGException
    {
        coordinateBox.expandBox(additionalSpread);
    }

    private void keepMissionBordersWithinMap() throws PWCGException
    {
        coordinateBox.keepWithinMap();
    }
}
