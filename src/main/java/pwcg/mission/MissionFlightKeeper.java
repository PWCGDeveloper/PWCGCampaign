package pwcg.mission;

import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plot.FlightProximityAnalyzer;

public class MissionFlightKeeper
{
    private Mission mission;
    private MissionFlightProximitySorter proximitySorter;
    private List<IFlight> allPlayerFlights;
    private List<IFlight> allAIFlights;

    private KeptFlightsRecorder keptFlightsRecorder;

    public MissionFlightKeeper(Mission mission, List<IFlight> allPlayerFlights, List<IFlight> allAIFlights)
    {
        this.mission = mission;
        this.allPlayerFlights = allPlayerFlights;
        this.allAIFlights = allAIFlights;
        this.proximitySorter = new MissionFlightProximitySorter();
        this.keptFlightsRecorder = new KeptFlightsRecorder();
    }

    public List<IFlight> keepLimitedFlights() throws PWCGException
    {
        PWCGLogger.log(LogLevel.DEBUG, "*** Flight Keeper Started ***: ");
        
        sortFlightsByProximity();

        keepRequiredFlights();
        keepOptionalFlights();

        return keptFlightsRecorder.getKeptFlights();
    }

    private void sortFlightsByProximity() throws PWCGException
    {
        FlightProximityAnalyzer proximityAnalyzer = new FlightProximityAnalyzer(allPlayerFlights, allAIFlights);
        proximityAnalyzer.plotFlightEncounters();
        proximitySorter.mapEnemyDistanceToPlayerFlights(allAIFlights);
    }

    private void keepRequiredFlights() throws PWCGException
    {
        NecessaryFlightKeeper necessaryFlightKeeper = new NecessaryFlightKeeper(mission, allPlayerFlights, proximitySorter.getFlightsByProximity(Side.ALLIED), proximitySorter.getFlightsByProximity(Side.AXIS), keptFlightsRecorder);   
        necessaryFlightKeeper.keepNecessaryFlights();
    }

    private void keepOptionalFlights() throws PWCGException
    {
        OptionalFlightKeeper optionalFlightKeeper = new OptionalFlightKeeper(mission, proximitySorter.getFlightsByProximity(Side.ALLIED), proximitySorter.getFlightsByProximity(Side.AXIS), keptFlightsRecorder);   
        optionalFlightKeeper.keepLimitedFlights();
    }
}
