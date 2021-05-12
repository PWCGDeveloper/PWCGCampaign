package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.plot.FlightProximityAnalyzer;

public class MissionFlightKeeper
{
    private Mission mission;
    private MissionFlightProximitySorter proximitySorter;
    private List<IFlight> allGeneratedPlayerFlights = new ArrayList<>();
    private List<IFlight> allGeneratedAiFlights = new ArrayList<>();

    private KeptFlightsRecorder keptFlightsRecorder;

    public MissionFlightKeeper(Mission mission, List<IFlight> allGeneratedFlights)
    {
        this.mission = mission;
        this.proximitySorter = new MissionFlightProximitySorter();
        this.keptFlightsRecorder = new KeptFlightsRecorder();
        
        sortFlights(allGeneratedFlights);
    }

    public List<IFlight> keepLimitedFlights() throws PWCGException
    {
        PWCGLogger.log(LogLevel.DEBUG, "*** Flight Keeper Started ***: ");
        
        sortFlightsByProximity();

        keepRequiredFlights();
        keepOptionalFlights();
        
        for (IFlight flight : keptFlightsRecorder.getKeptFlights())
        {
            mission.getMissionSquadronRegistry().registerSquadronForUse(flight.getSquadron());
        }

        return keptFlightsRecorder.getKeptFlights();
    }

    private void sortFlightsByProximity() throws PWCGException
    {
        FlightProximityAnalyzer proximityAnalyzer = new FlightProximityAnalyzer(allGeneratedPlayerFlights, allGeneratedAiFlights);
        proximityAnalyzer.plotFlightEncounters();
        proximitySorter.mapEnemyDistanceToPlayerFlights(allGeneratedAiFlights);
    }

    private void keepRequiredFlights() throws PWCGException
    {
        NecessaryFlightKeeper necessaryFlightKeeper = new NecessaryFlightKeeper(mission, allGeneratedPlayerFlights, proximitySorter.getFlightsByProximity(Side.ALLIED), proximitySorter.getFlightsByProximity(Side.AXIS), keptFlightsRecorder);   
        necessaryFlightKeeper.keepNecessaryFlights();
    }

    private void keepOptionalFlights() throws PWCGException
    {
        OptionalFlightKeeper optionalFlightKeeper = new OptionalFlightKeeper(mission, proximitySorter.getFlightsByProximity(Side.ALLIED), proximitySorter.getFlightsByProximity(Side.AXIS), keptFlightsRecorder);   
        optionalFlightKeeper.keepLimitedFlights();
    }
    
    
    public void sortFlights(List<IFlight> allGeneratedFlights)
    {
        for (IFlight flight : allGeneratedFlights)
        {
            if (flight.getFlightInformation().getNecessaryFlightType() == NecessaryFlightType.PLAYER_FLIGHT)
            {
                allGeneratedPlayerFlights.add(flight);
            }
            else
            {
                allGeneratedAiFlights.add(flight);
            }
        }
    }
}
