package pwcg.mission.mcu;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.IFlight;

public class McuFactory
{
    public static McuTimer createTimer(IFlight flight, String descriptor, int delayTime) throws PWCGException
    {
        FlightInformation flightInformation = flight.getFlightInformation();

        McuTimer formationTimer = new McuTimer();
        formationTimer.setName(flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()) + ": " + descriptor + " Timer");
        formationTimer.setDesc(descriptor + " timer for " + flightInformation.getSquadron().determineDisplayName(flightInformation.getCampaign().getDate()));
        formationTimer.setPosition(flightInformation.getDepartureAirfield().getPosition().copy());
        
        formationTimer.setTime(delayTime);
        return formationTimer;
    }
}
