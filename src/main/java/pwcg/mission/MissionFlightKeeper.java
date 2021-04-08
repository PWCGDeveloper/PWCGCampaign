package pwcg.mission;

import java.util.List;

import pwcg.campaign.Campaign;
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

    private KeptFlightsRecorder keptFlightsRecorder;

    public MissionFlightKeeper(Campaign campaign, Mission mission)
    {
        this.mission = mission;
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
        FlightProximityAnalyzer proximityAnalyzer = new FlightProximityAnalyzer(mission);
        proximityAnalyzer.plotFlightEncounters();
        proximitySorter.mapEnemyDistanceToPlayerFlights(mission.getMissionFlights().getAiFlights());
    }

    private void keepRequiredFlights() throws PWCGException
    {
        NecessaryFlightKeeper necessaryFlightKeeper = new NecessaryFlightKeeper(mission, proximitySorter.getFlightsByProximity(Side.ALLIED), proximitySorter.getFlightsByProximity(Side.AXIS), keptFlightsRecorder);   
        necessaryFlightKeeper.keepNecessaryFlights();
    }

    private void keepOptionalFlights() throws PWCGException
    {
        OptionalFlightKeeper optionalFlightKeeper = new OptionalFlightKeeper(mission, proximitySorter.getFlightsByProximity(Side.ALLIED), proximitySorter.getFlightsByProximity(Side.AXIS), keptFlightsRecorder);   
        optionalFlightKeeper.keepLimitedFlights();
    }
}
