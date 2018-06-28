package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightProximityAnalyzer;
import pwcg.mission.flight.FlightTypes;

public class MissionFlightBuilder
{
    private Campaign campaign;
    private Mission mission;
    private Flight playerFlight;
    private List<Flight> missionFlights = new ArrayList<Flight>();
    
    public MissionFlightBuilder(Campaign campaign, Mission mission)
    {
    	this.campaign = campaign;
    	this.mission = mission;
    }
    
    public void generateFlights(FlightTypes flightType) throws PWCGException 
    {
        createPlayerFlight(flightType);
        createAiFlights();
        moveAiFlightsToStartPositions();

        FlightProximityAnalyzer flightAnalyzer = new FlightProximityAnalyzer(this);
        flightAnalyzer.plotFlightEncounters();        
    }

    public void finalizeMissionFlights() throws PWCGException 
    {
        MissionFlightFinalizer flightFinalizer = new MissionFlightFinalizer(campaign, this);
        missionFlights = flightFinalizer.finalizeMissionFlights();
    }
    
    private void createPlayerFlight(FlightTypes flightType) throws PWCGException
    {
        PlayerFlightBuilder playerFlightBuilder = new PlayerFlightBuilder(campaign, mission);   
        playerFlight = playerFlightBuilder.createPlayerFlight(flightType);
    }

    private void createAiFlights() throws PWCGException
    {
        AiFlightBuilder aiFlightBuilder = new AiFlightBuilder(campaign, mission);
        missionFlights = aiFlightBuilder.createAiFlights();
    }


    private void moveAiFlightsToStartPositions() throws PWCGException
    {
        for (Flight flight : missionFlights)
        {
            flight.moveToStartPosition();
        }
    }
    
    public boolean isInFlightPath(Coordinate position) throws PWCGException 
    {
        FlightPathProximityCalculator flightPathProximityCalculator = new FlightPathProximityCalculator(playerFlight);
        return flightPathProximityCalculator.isInFlightPath(position);
    }

    public List<Flight> getAllAlliedFlights() throws PWCGException
    {
        List<Flight> alliedFlights = new ArrayList<Flight>();
        for (Flight flight : this.getAllAerialFlights())
        {
            if (flight.getCountry().getSide() == Side.ALLIED)
            {
                alliedFlights.add(flight);
            }
        }
        
        return alliedFlights;
    }

    public List<Flight> getAllAxisFlights() throws PWCGException
    {
        List<Flight> axisFlights = new ArrayList<Flight>();
        for (Flight flight : this.getAllAerialFlights())
        {
            if (flight.getCountry().getSide() == Side.AXIS)
            {
                axisFlights.add(flight);
            }
        }
        
        return axisFlights;
    }


    public List<Flight> getAlliedAiFlights() throws PWCGException
    {
        List<Flight> alliedFlights = new ArrayList<Flight>();
        for (Flight flight : missionFlights)
        {
            if (flight.getCountry().getSide() == Side.ALLIED)
            {
                alliedFlights.add(flight);
            }
        }
        
        return alliedFlights;
    }

    public List<Flight> getAxisAiFlights() throws PWCGException
    {
        List<Flight> axisFlights = new ArrayList<Flight>();
        for (Flight flight : missionFlights)
        {
            if (flight.getCountry().getSide() == Side.AXIS)
            {
                axisFlights.add(flight);
            }
        }
        
        return axisFlights;
    }


    public List<Flight> getAllAerialFlights()
    {
        ArrayList<Flight> allFlights = new ArrayList<Flight>();
        allFlights.add(playerFlight);
        for (Unit linkedUnit : playerFlight.getLinkedUnits())
        {
            if (linkedUnit instanceof Flight)
            {
                allFlights.add((Flight) linkedUnit);
            }
        }

        for (Flight flight : missionFlights)
        {
            if (flight.getPlanes().size() > 0)
            {
                allFlights.add(flight);
            }

            for (Unit linkedUnit : flight.getLinkedUnits())
            {
                if (linkedUnit instanceof Flight)
                {
                    allFlights.add((Flight) linkedUnit);
                }
            }
        }

        return allFlights;
    }

    public CoordinateBox getMissionBorders(Integer additionalSpread) throws PWCGException 
    {
        return MissionBorderBuilder.buildCoordinateBox(playerFlight, additionalSpread);
    }

    public Flight getPlayerFlight()
    {
        return playerFlight;
    }

    public void setPlayerFlight(Flight myFlight) 
    {
		this.playerFlight = myFlight;
	}

	public List<Flight> getMissionFlights()
    {
        return missionFlights;
    }


}
