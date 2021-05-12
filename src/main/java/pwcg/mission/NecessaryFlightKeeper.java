package pwcg.mission;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.IFlight;

public class NecessaryFlightKeeper
{
    private Mission mission;

    private List<IFlight> playerFlights;
    private List<IFlight> alliedAiFlightsByProximityToPlayer;
    private List<IFlight> axisAiFlightsByProximityToPlayer;
    private KeptFlightsRecorder keptFlightsRecorder;

    public NecessaryFlightKeeper(Mission mission, List<IFlight> playerFlights, List<IFlight> alliedAiFlightsByProximityToPlayer, List<IFlight> axisAiFlightsByProximityToPlayer, KeptFlightsRecorder keptFlights)
    {
        this.mission = mission;
        this.playerFlights = playerFlights;
        this.alliedAiFlightsByProximityToPlayer = alliedAiFlightsByProximityToPlayer;
        this.axisAiFlightsByProximityToPlayer = axisAiFlightsByProximityToPlayer;
        this.keptFlightsRecorder = keptFlights;
    }

    public void keepNecessaryFlights() throws PWCGException
    {
        PWCGLogger.log(LogLevel.DEBUG, "*** Necessary Flight Keeper Started ***: ");

        keepNecessaryFlights(playerFlights);
        keepNecessaryFlights(alliedAiFlightsByProximityToPlayer);
        keepNecessaryFlights(axisAiFlightsByProximityToPlayer);
        keepSkirmishAlliedFlights();
        keepSkirmishAxisFlights();
    }

    private void keepNecessaryFlights(List<IFlight> flights) throws PWCGException
    {
        for (IFlight flight : flights)
        {
            if (flight.getFlightInformation().isNecessaryFlight())
            {
                PWCGLogger.log(LogLevel.DEBUG, "Keep Player Flight: " + flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()));
                keptFlightsRecorder.keepFlight(flight);
            }
        }
    }

    private void keepSkirmishAlliedFlights() throws PWCGException
    {
        keepForSkirmish(alliedAiFlightsByProximityToPlayer);
    }

    private void keepSkirmishAxisFlights() throws PWCGException
    {
        keepForSkirmish(axisAiFlightsByProximityToPlayer);
    }

    private void keepForSkirmish(List<IFlight> flights) throws PWCGException
    {
        if (mission.getSkirmish() == null)
        {
            return;
        }

        for (IFlight flight : flights)
        {
            int numFlightsOfTypeKept = keptFlightsRecorder.getNumKeptFlightType(flight);
            
            if (keptFlightsRecorder.isSquadronInKept(flight))
            {
                PWCGLogger.log(LogLevel.DEBUG, "Reject Skirmish because squadron already kept: " + flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()));
                continue;
            }

            if (!mission.getSkirmish().needsMoreIconicFlightType(flight.getFlightInformation(), numFlightsOfTypeKept))
            {
                PWCGLogger.log(LogLevel.DEBUG, "Reject Skirmish because not iconic: " + flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()));
                continue;
            }
            
            if (!keptFlightsRecorder.needsMoreFlights(flight))
            {
                PWCGLogger.log(LogLevel.DEBUG, "Reject Skirmish because has enough flights: " + flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()));
                continue;
            }
            
            if (keptFlightsRecorder.airfieldInUseForTakeoff(flight))
            {
                PWCGLogger.log(LogLevel.DEBUG, "Reject Skirmish because airfield in use: " + flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()));
                continue;
            }
            
            PWCGLogger.log(LogLevel.DEBUG, "Keep Skirmish Flight: " + flight.getSquadron().determineDisplayName(flight.getCampaign().getDate()));
            keptFlightsRecorder.keepFlight(flight);
        }
    }
}
