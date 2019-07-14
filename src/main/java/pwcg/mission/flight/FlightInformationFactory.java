package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.target.ITargetDefinitionBuilder;
import pwcg.campaign.target.TargetDefinition;
import pwcg.campaign.target.TargetDefinitionBuilderFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
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
        playerFlightInformation.setCampaign(mission.getCampaign());
        playerFlightInformation.setSquadron(squadron);
        playerFlightInformation.setPlayerFlight(true);
        playerFlightInformation.setEscortForPlayerFlight(false);
        playerFlightInformation.setEscortedByPlayerFlight(false);
        playerFlightInformation.setTargetSearchStartLocation(mission.getMissionBorders().getCenter());
        buildTargetDefinition (playerFlightInformation);
        buildPlanes (playerFlightInformation);
        
        return playerFlightInformation;
    }

    public static FlightInformation buildAiFlightInformation(Squadron squadron, Mission mission, FlightTypes flightType) throws PWCGException
    {
        FlightInformation aiFlightInformation = new FlightInformation(mission);
        aiFlightInformation.setFlightType(flightType);
        aiFlightInformation.setMission(mission);
        aiFlightInformation.setCampaign(mission.getCampaign());
        aiFlightInformation.setSquadron(squadron);
        aiFlightInformation.setPlayerFlight(false);
        aiFlightInformation.setEscortForPlayerFlight(false);
        aiFlightInformation.setEscortedByPlayerFlight(false);
        aiFlightInformation.setTargetSearchStartLocation(mission.getMissionBorders().getCenter());
        buildTargetDefinition (aiFlightInformation);
        buildPlanes (aiFlightInformation);

        return aiFlightInformation;
    }

    public static FlightInformation buildEscortForPlayerFlightInformation(FlightInformation playerFlightInformation, 
            Squadron friendlyFighterSquadron,
            Coordinate rendezvous) throws PWCGException
    {
        FlightInformation escortFlightInformation = new FlightInformation(playerFlightInformation.getMission());
        escortFlightInformation.setFlightType(FlightTypes.ESCORT);
        escortFlightInformation.setMission(playerFlightInformation.getMission());
        escortFlightInformation.setCampaign(playerFlightInformation.getCampaign());
        escortFlightInformation.setSquadron(friendlyFighterSquadron);
        escortFlightInformation.setPlayerFlight(false);
        escortFlightInformation.setEscortForPlayerFlight(true);
        escortFlightInformation.setEscortedByPlayerFlight(false);
        escortFlightInformation.setTargetSearchStartLocation(playerFlightInformation.getTargetCoords());
        
        buildPlanes (escortFlightInformation);
        buildTargetDefinition (escortFlightInformation);
        rendezvous.setYPos(rendezvous.getYPos() + 500.0);
        escortFlightInformation.getTargetDefinition().setTargetPosition(rendezvous);

        return escortFlightInformation;
    }
    
    public static FlightInformation buildEscortedByPlayerFlightInformation(FlightInformation escortFlightInformation, Squadron friendlyBomberSquadron) throws PWCGException
    {
        FlightInformation escortedFlightInformation = new FlightInformation(escortFlightInformation.getMission());
        escortedFlightInformation.setFlightType(FlightTypes.BOMB);
        escortedFlightInformation.setMission(escortFlightInformation.getMission());
        escortedFlightInformation.setCampaign(escortFlightInformation.getCampaign());
        escortedFlightInformation.setSquadron(friendlyBomberSquadron);
        escortedFlightInformation.setPlayerFlight(false);
        escortedFlightInformation.setEscortForPlayerFlight(false);
        escortedFlightInformation.setEscortedByPlayerFlight(true);
        escortedFlightInformation.setTargetSearchStartLocation(escortFlightInformation.getTargetCoords());
        buildPlanes (escortedFlightInformation);
        buildTargetDefinition (escortedFlightInformation);

        return escortedFlightInformation;
    }

    public static FlightInformation buildInterceptOpposingInformation(Squadron squadron, Mission mission, FlightTypes flightType, TargetDefinition targetDefinition) throws PWCGException
    {
        FlightInformation interceptedFlightInformation = new FlightInformation(mission);
        interceptedFlightInformation.setFlightType(FlightTypes.ESCORT);
        interceptedFlightInformation.setMission(mission);
        interceptedFlightInformation.setCampaign(mission.getCampaign());
        interceptedFlightInformation.setSquadron(squadron);
        interceptedFlightInformation.setPlayerFlight(false);
        interceptedFlightInformation.setEscortForPlayerFlight(false);
        interceptedFlightInformation.setEscortedByPlayerFlight(false);
        interceptedFlightInformation.setTargetDefinition(targetDefinition);
        buildPlanes (interceptedFlightInformation);

        return interceptedFlightInformation;
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
        if (planes.size() == 0)
        {
            throw new PWCGException("No planes for flight");
        }
        playerFlightInformation.setPlanes(planes);
    }

}
