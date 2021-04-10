package pwcg.mission;

import java.util.ArrayList;
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

        keepPlayerFlights(playerFlights);
        keepOpposingPlayerFlights(getAllAiFights());
        keepLinkedPlayerFlights(playerFlights);
        keepRequiredAlliedFlights();
        keepRequiredAxisFlights();
    }

    private List<IFlight> getAllAiFights()
    {
        List<IFlight> aiFlights = new ArrayList<>();
        aiFlights.addAll(alliedAiFlightsByProximityToPlayer);
        aiFlights.addAll(axisAiFlightsByProximityToPlayer);
        return aiFlights;
    }

    private void keepPlayerFlights(List<IFlight> flights) throws PWCGException
    {
        for (IFlight flight : flights)
        {
            if (flight.isPlayerFlight())
            {
                keptFlightsRecorder.keepFlight(flight);
            }
        }
    }

    private void keepOpposingPlayerFlights(List<IFlight> flights) throws PWCGException
    {
        for (IFlight flight : flights)
        {
            if (flight.getFlightInformation().isOpposingFlight())
            {
                keptFlightsRecorder.keepFlight(flight);
            }
        }
    }

    private void keepLinkedPlayerFlights(List<IFlight> playerFlights) throws PWCGException
    {
        for (IFlight playerFlight : playerFlights)
        {
            if (playerFlight.isPlayerFlight())
            {
                for (IFlight linkedFlight : playerFlight.getLinkedFlights().getLinkedFlights())
                {
                    keptFlightsRecorder.keepFlight(linkedFlight);
                }
            }
        }
    }

    private void keepRequiredAlliedFlights() throws PWCGException
    {
        keepRequiredFlights(alliedAiFlightsByProximityToPlayer);
    }

    private void keepRequiredAxisFlights() throws PWCGException
    {
        keepRequiredFlights(axisAiFlightsByProximityToPlayer);
    }

    private void keepRequiredFlights(List<IFlight> flights) throws PWCGException
    {
        for (IFlight flight : flights)
        {
            if (keepForSkirmish(flight))
            {
                keptFlightsRecorder.keepFlight(flight);
            }
        }
    }

    private boolean keepForSkirmish(IFlight flight) throws PWCGException
    {
        
        int numFlightsOfTypeKept = keptFlightsRecorder.getNumKeptFlightType(flight);
        if (mission.getSkirmish() != null)
        {
            if (mission.getSkirmish().needsMoreIconicFlightType(flight.getFlightInformation().getFlightType(), numFlightsOfTypeKept))
            {
                if (keptFlightsRecorder.needsMoreFlights(flight))
                {
                    if (!keptFlightsRecorder.airfieldInUse(flight))
                    {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
}
