package pwcg.mission.flight;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.plane.FlightPlaneBuilder;
import pwcg.mission.target.TargetDefinitionBuilderFactory;

public class FlightInformationFactory
{
    public static IFlightInformation buildFlightInformation(FlightBuildInformation flightBuildInformation, FlightTypes flightType) throws PWCGException
    {
        if (flightBuildInformation.isPlayerFlight())
        {
            return buildPlayerFlightInformation(flightBuildInformation.getMission(), flightBuildInformation.getSquadron(), flightType);
        }
        else
        {
            return buildAiFlightInformation(flightBuildInformation.getMission(), flightBuildInformation.getSquadron(), flightType);
        }
    }
    
    private static IFlightInformation buildPlayerFlightInformation(Mission mission, Squadron squadron, FlightTypes flightType) throws PWCGException
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
        TargetDefinitionBuilderFactory.buildTargetDefinition (playerFlightInformation);
        FlightPlaneBuilder.buildPlanes (playerFlightInformation);
        playerFlightInformation.calculateAltitude();
        
        return playerFlightInformation;
    }

    private static IFlightInformation buildAiFlightInformation(Mission mission, Squadron squadron, FlightTypes flightType) throws PWCGException
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
        TargetDefinitionBuilderFactory.buildTargetDefinition (aiFlightInformation);
        FlightPlaneBuilder.buildPlanes (aiFlightInformation);
        aiFlightInformation.calculateAltitude();

        return aiFlightInformation;
    }
}
