package pwcg.mission.flight.plot;

import java.util.List;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plane.FlightPlaneBuilder;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.target.ITargetDefinitionBuilder;
import pwcg.mission.target.TargetType;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetDefinitionBuilderFactory;

public class FlightInformationFactory
{

    public static IFlightInformation buildPlayerFlightInformation(Squadron squadron, Mission mission, FlightTypes flightType) throws PWCGException
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
        setAltitude (playerFlightInformation);
        
        return playerFlightInformation;
    }

    public static IFlightInformation buildAiFlightInformation(Squadron squadron, Mission mission, FlightTypes flightType) throws PWCGException
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
        setAltitude (aiFlightInformation);

        return aiFlightInformation;
    }

    public static IFlightInformation buildEscortForPlayerFlightInformation(IFlightInformation playerFlightInformation, 
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
        escortFlightInformation.setTargetSearchStartLocation(playerFlightInformation.getTargetPosition());
        
        buildPlanes (escortFlightInformation);
        setAltitude (escortFlightInformation);
        buildTargetDefinition (escortFlightInformation);
        rendezvous.setYPos(rendezvous.getYPos() + 500.0);
        escortFlightInformation.getTargetDefinition().setTargetPosition(rendezvous);

        return escortFlightInformation;
    }

    public static IFlightInformation buildEscortedByPlayerFlightInformation(IFlightInformation escortFlightInformation, Squadron friendlyBomberSquadron) throws PWCGException
    {
        FlightInformation escortedFlightInformation = new FlightInformation(escortFlightInformation.getMission());
        escortedFlightInformation.setFlightType(FlightTypes.BOMB);
        escortedFlightInformation.setMission(escortFlightInformation.getMission());
        escortedFlightInformation.setCampaign(escortFlightInformation.getCampaign());
        escortedFlightInformation.setSquadron(friendlyBomberSquadron);
        escortedFlightInformation.setPlayerFlight(false);
        escortedFlightInformation.setEscortForPlayerFlight(false);
        escortedFlightInformation.setEscortedByPlayerFlight(true);
        escortedFlightInformation.setTargetSearchStartLocation(escortFlightInformation.getTargetPosition());
        buildPlanes (escortedFlightInformation);
        buildTargetDefinition (escortedFlightInformation);
        escortedFlightInformation.setAltitude(escortFlightInformation.getAltitude() - 500);


        return escortedFlightInformation;
    }


    public static IFlightInformation buildAiScrambleOpposingFlightInformation(Squadron opposingSquadron, IFlightInformation playerFlightInformation, FlightTypes opposingFlightType) throws PWCGException
    {
        FlightInformation scrambleOpposingFlightInformation = new FlightInformation(playerFlightInformation.getMission());
        scrambleOpposingFlightInformation.setFlightType(opposingFlightType);
        scrambleOpposingFlightInformation.setMission(playerFlightInformation.getMission());
        scrambleOpposingFlightInformation.setCampaign(playerFlightInformation.getCampaign());
        scrambleOpposingFlightInformation.setSquadron(opposingSquadron);
        scrambleOpposingFlightInformation.setPlayerFlight(false);
        scrambleOpposingFlightInformation.setEscortForPlayerFlight(false);
        scrambleOpposingFlightInformation.setEscortedByPlayerFlight(false);
        scrambleOpposingFlightInformation.setTargetSearchStartLocation(playerFlightInformation.getFlightHomePosition());
        buildPlanes (scrambleOpposingFlightInformation);
        setAltitude (scrambleOpposingFlightInformation);
        buildSpecificTargetDefinition (scrambleOpposingFlightInformation, TargetType.TARGET_AIRFIELD);

        return scrambleOpposingFlightInformation;
    }
    
    private static void setAltitude(IFlightInformation flightInformation) throws PWCGException
    {
        flightInformation.calculateAltitude();
    }

    private static void buildTargetDefinition (FlightInformation flightInformation) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = TargetDefinitionBuilderFactory.createFlightTargetDefinitionBuilder(flightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildTargetDefinition();
        flightInformation.setTargetDefinition(targetDefinition);
    }

    private static void buildSpecificTargetDefinition (FlightInformation scrambleOpposingFlightInformation, TargetType targetType) throws PWCGException
    {
        ITargetDefinitionBuilder targetDefinitionBuilder = TargetDefinitionBuilderFactory.createFlightTargetDefinitionBuilder(scrambleOpposingFlightInformation);
        TargetDefinition targetDefinition = targetDefinitionBuilder.buildScrambleOpposeTargetDefinition(scrambleOpposingFlightInformation, targetType);
        scrambleOpposingFlightInformation.setTargetDefinition(targetDefinition);
    }

    private static void buildPlanes(FlightInformation playerFlightInformation) throws PWCGException
    {
        FlightPlaneBuilder flightPlaneBuilder = new FlightPlaneBuilder(playerFlightInformation);
        List<PlaneMcu> planes = flightPlaneBuilder.createPlanesForFlight();
        if (planes.size() == 0)
        {
            throw new PWCGException("No planes for flight");
        }
        playerFlightInformation.setPlanes(planes);
    }
}
