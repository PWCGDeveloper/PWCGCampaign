package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.ITargetDefinitionBuilder;
import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.target.TargetDefinitionBuilderFactory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.plane.FlightPlaneBuilder;
import pwcg.mission.flight.plane.PlaneMCU;

public class FlightInformationFactory
{

    public static FlightInformation buildPlayerFlightInformation(Squadron squadron, Mission mission, FlightTypes flightType) throws PWCGException
    {    	
        FlightInformation playerFlightInformation = new FlightInformation(mission);
        playerFlightInformation.setFlightType(flightType);
        playerFlightInformation.setMission(mission);
        playerFlightInformation.setSquadron(squadron);
        playerFlightInformation.setPlayerFlight(true);
        playerFlightInformation.setEscortForPlayerFlight(false);
        playerFlightInformation.setEscortedByPlayerFlight(false);
        buildTargetDefinition (playerFlightInformation);
        buildPlanes (playerFlightInformation);
        
        return playerFlightInformation;
    }

    public static FlightInformation buildAiFlightInformation(Squadron squadron, Mission mission, FlightTypes flightType) throws PWCGException
    {
        FlightInformation aiFlightInformation = new FlightInformation(mission);
        aiFlightInformation.setFlightType(flightType);
        aiFlightInformation.setMission(mission);
        aiFlightInformation.setSquadron(squadron);
        aiFlightInformation.setPlayerFlight(false);
        aiFlightInformation.setEscortForPlayerFlight(false);
        aiFlightInformation.setEscortedByPlayerFlight(false);
        buildTargetDefinition (aiFlightInformation);
        buildPlanes (aiFlightInformation);

        return aiFlightInformation;
    }

    public static FlightInformation buildEscortForPlayerFlight(FlightInformation playerFlightInformation, Squadron friendlyFighterSquadron) throws PWCGException
    {
        FlightInformation escortFlightInformation = new FlightInformation(playerFlightInformation.getMission());
        escortFlightInformation.setFlightType(FlightTypes.ESCORT);
        escortFlightInformation.setMission(escortFlightInformation.getMission());
        escortFlightInformation.setSquadron(friendlyFighterSquadron);
        escortFlightInformation.setPlayerFlight(false);
        escortFlightInformation.setEscortForPlayerFlight(true);
        escortFlightInformation.setEscortedByPlayerFlight(false);
        buildPlanes (escortFlightInformation);

        return escortFlightInformation;
    }
    
    public static void buildTargetDefinition (FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = TargetDefinitionBuilderFactory.createFlightTargetDefinitionBuilder(flightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
        flightInformation.setTargetDefinition(targetDefinition);
    }
    

    private static void buildPlanes(FlightInformation playerFlightInformation) throws PWCGException
    {
        FlightPlaneBuilder flightPlaneBuilder = new FlightPlaneBuilder(playerFlightInformation);
        List<PlaneMCU> planes = flightPlaneBuilder.createPlanesForFlight();
        playerFlightInformation.setPlanes(planes);
    }

}
