package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.IFlight;

public class NecessaryFlightKeeper
{
    private Mission mission;
    private Campaign campaign;

    private List<IFlight> alliedProximitySortedFlights;
    private List<IFlight> axisProximitySortedFlights;
    private KeptFlightsRecorder keptFlightsRecorder;

    public NecessaryFlightKeeper(Mission mission, List<IFlight> alliedProximitySortedFlights, List<IFlight> axisProximitySortedFlights, KeptFlightsRecorder keptFlights)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
        this.alliedProximitySortedFlights = alliedProximitySortedFlights;
        this.axisProximitySortedFlights = axisProximitySortedFlights;
        this.keptFlightsRecorder = keptFlights;
    }

    public void keepNecessaryFlights() throws PWCGException
    {
        PWCGLogger.log(LogLevel.DEBUG, "*** Necessary Flight Keeper Started ***: ");

        keepPlayerFlights(mission.getMissionFlightBuilder().getPlayerFlights());
        keepOpposingPlayerFlights(mission.getMissionFlightBuilder().getAiFlights());
        keepRequiredAlliedFlights();
        keepRequiredAxisFlights();
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

    private void keepRequiredAlliedFlights() throws PWCGException
    {
        keepRequiredFlights(alliedProximitySortedFlights);
    }

    private void keepRequiredAxisFlights() throws PWCGException
    {
        keepRequiredFlights(axisProximitySortedFlights);
    }

    private void keepRequiredFlights(List<IFlight> flights) throws PWCGException
    {
        for (IFlight flight : flights)
        {
            if (isNecessaryFlight(flight))
            {
                keptFlightsRecorder.keepFlight(flight);
            }
        }
    }

    private boolean isNecessaryFlight(IFlight flight) throws PWCGException
    {
        if (mission.getMissionSquadronRecorder().isSquadronInUse(flight.getSquadron().getSquadronId()))
        {
            return false;
        }
        
        if (flight.getFlightInformation().isPlayerFlight())
        {
            PWCGLogger.log(LogLevel.DEBUG, "necessary flight because player: " + flight.getSquadron().determineDisplayName(campaign.getDate()));
            return true;
        }

        if (flight.getFlightInformation().isOpposingFlight())
        {
            PWCGLogger.log(LogLevel.DEBUG, "necessary flight because opposing: " + flight.getSquadron().determineDisplayName(campaign.getDate()));
            return true;
        }

        if (keepForSkirmish(flight) == true)
        {
            PWCGLogger.log(LogLevel.DEBUG, "necessary flight because iconic: " + flight.getSquadron().determineDisplayName(campaign.getDate()));
            return true;
        }

        PWCGLogger.log(LogLevel.DEBUG, "Not necessary flight: " + flight.getSquadron().determineDisplayName(campaign.getDate()));
        return false;
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
