package pwcg.mission;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.flight.Flight;

public class MissionBorderBuilder 
{
	private static final double MIN_BOX_DIAMETER = 25000.0;
	
	private List<Flight> playerFlights;
	private CoordinateBox coordinateBox;

    public static CoordinateBox buildCoordinateBox (List<Flight> playerFlights, int minimumSize, int additionalSpread) throws PWCGException
    {
        MissionBorderBuilder missionBorders = new MissionBorderBuilder(playerFlights);
        return missionBorders.makeCoordinateBox(additionalSpread);
    }

    private MissionBorderBuilder (List<Flight> playerFlights)
    {
        this.playerFlights = playerFlights;
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
        coordinateBox =  CoordinateBox.coordinateBoxFromFlights(playerFlights);
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
