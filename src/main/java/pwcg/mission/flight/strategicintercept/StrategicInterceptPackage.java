package pwcg.mission.flight.strategicintercept;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightBuildInformation;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.FlightSpotterBuilder;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightPackage;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderAirToAir;

public class StrategicInterceptPackage implements IFlightPackage
{
    private FlightInformation flightInformation;
    private TargetDefinition targetDefinition;
    private List<IFlight> packageFlights = new ArrayList<>();

    @Override
    public List<IFlight> createPackage (FlightBuildInformation flightBuildInformation) throws PWCGException 
    {
        this.flightInformation = FlightInformationFactory.buildFlightInformation(flightBuildInformation, FlightTypes.STRATEGIC_INTERCEPT);
        this.targetDefinition = buildTargetDefintion();

        if (!flightInformation.isPlayerFlight())
        {
            return packageFlights;
        }

        List<IFlight> opposingFlights = makeOpposingFlights();
        packageFlights.addAll(opposingFlights);

        resetFlightInformationAltitudeToMatchTargetFlightList(opposingFlights);
        StrategicInterceptFlight interceptFlight = createPlayerFlight(opposingFlights);
        FlightSpotterBuilder.createSpottersForStrategicIntercept(interceptFlight, opposingFlights);

        packageFlights.add(interceptFlight);
        return packageFlights;
    }

    private StrategicInterceptFlight createPlayerFlight(List<IFlight> opposingFlights) throws PWCGException
    {
        StrategicInterceptFlight interceptFlight = new StrategicInterceptFlight (flightInformation, targetDefinition, opposingFlights.get(0));
        interceptFlight.createFlight();
        return interceptFlight;
    }

    private List<IFlight> makeOpposingFlights() throws PWCGException
    {
        StrategicInterceptOpposingFlightBuilder opposingFlightBuilder = new StrategicInterceptOpposingFlightBuilder(flightInformation, targetDefinition);
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
    
    private TargetDefinition buildTargetDefintion() throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = new TargetDefinitionBuilderAirToAir(flightInformation);
        return  targetDefinitionBuilder.buildTargetDefinition();
    }

}
