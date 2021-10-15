package pwcg.mission.flight.factory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightTypes;

@ExtendWith(MockitoExtension.class)
public class NightFlightTypeConverterTest
{
    @Mock Mission mission;

    @Test
    public void testConvertInvalidOnNightFlight() throws PWCGException
    {
        boolean isNightMission = true;
        
        FlightTypes flightType = FlightTypes.ARTILLERY_SPOT;
        FlightTypes convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
        
        flightType = FlightTypes.DIVE_BOMB;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
        
        flightType = FlightTypes.OFFENSIVE;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
        
        flightType = FlightTypes.BALLOON_BUST;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
    }

    @Test
    public void testKeepValidOnNightFlight() throws PWCGException
    {
        boolean isNightMission = true;
        
        FlightTypes flightType = FlightTypes.BOMB;
        FlightTypes convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.BOMB);
        
        flightType = FlightTypes.PATROL;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.PATROL);
        
        flightType = FlightTypes.GROUND_ATTACK;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
        
        flightType = FlightTypes.TRANSPORT;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.TRANSPORT);
    }

    @Test
    public void testKeepOriginalOnDayFlight() throws PWCGException
    {
        boolean isNightMission = false;
        
        FlightTypes flightType = FlightTypes.ARTILLERY_SPOT;
        FlightTypes convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.ARTILLERY_SPOT);
        
        flightType = FlightTypes.DIVE_BOMB;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.DIVE_BOMB);
        
        flightType = FlightTypes.OFFENSIVE;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.OFFENSIVE);
        
        flightType = FlightTypes.BALLOON_BUST;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.BALLOON_BUST);
        
        flightType = FlightTypes.BOMB;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.BOMB);
        
        flightType = FlightTypes.PATROL;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.PATROL);
        
        flightType = FlightTypes.GROUND_ATTACK;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.GROUND_ATTACK);
        
        flightType = FlightTypes.TRANSPORT;
        convertedFlightType = NightFlightTypeConverter.getFlightType(flightType, isNightMission);
        assert(convertedFlightType == FlightTypes.TRANSPORT);
    }
}
