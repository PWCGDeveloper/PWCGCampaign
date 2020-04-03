package pwcg.mission.flight.strategicintercept;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightSpotterBuilder;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.mcu.McuWaypoint;

public class StrategicInterceptPackage implements IFlightPackage
{
    private IFlightInformation flightInformation;

    public StrategicInterceptPackage()
    {
    }

    @Override
    public IFlight createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.STRATEGIC_INTERCEPT);

        List<IFlight> opposingFlights = makeLinkedStrategicInterceptFlights();

        if (opposingFlights.isEmpty())
        {
            throw new PWCGException("COuld not find opposition for intercept");
        }
        
        resetFlightInformationAltitudeToMatchTargetFlightList(opposingFlights);
        StrategicInterceptFlight interceptFlight = new StrategicInterceptFlight (flightInformation, opposingFlights.get(0));
        interceptFlight.createFlight();
        
        if (flightInformation.isPlayerFlight())
        {
            FlightSpotterBuilder.createSpotters(interceptFlight, flightInformation);
        }
        
        for (IFlight opposingFlight: opposingFlights)
        {
            interceptFlight.getLinkedFlights().addLinkedFlight(opposingFlight);
        }

        return interceptFlight;
    }

    private List<IFlight> makeLinkedStrategicInterceptFlights() throws PWCGException
    {
        StrategicInterceptOpposingFlightBuilder opposingFlightBuilder = new StrategicInterceptOpposingFlightBuilder(flightInformation);
        List<IFlight> opposingFlights = opposingFlightBuilder.buildOpposingFlights();
        return opposingFlights;
    }

    private void resetFlightInformationAltitudeToMatchTargetFlightList(List<IFlight> opposingFlights)
    {
        IFlight bomberFlight = opposingFlights.get(0);
        double maxAltitude = 0.0;
        for (McuWaypoint bomberWaypoint : bomberFlight.getWaypointPackage().getAllWaypoints())
        {
            if (bomberWaypoint.getPosition().getYPos() > maxAltitude)
            {
                maxAltitude = bomberWaypoint.getPosition().getYPos();
            }
        }
                
        int altitudeAboveBombers = Double.valueOf(maxAltitude).intValue() + 300;
        flightInformation.setAltitude(altitudeAboveBombers);
    }
}
