package pwcg.mission.flight.escort;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightInformationFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortBuilder
{
    public Flight createEscortForPlayerFlight(Flight playerFlight) throws PWCGException 
    {
        FlightInformation playerFlightInformation = playerFlight.getFlightInformation();
        
        Squadron friendlyFighterSquadron = PWCGContextManager.getInstance().getSquadronManager().getSquadronByProximityAndRoleAndSide(
                        playerFlightInformation.getCampaign(), 
                        playerFlightInformation.getSquadron().determineCurrentPosition(playerFlightInformation.getCampaign().getDate()), 
                        Role.ROLE_FIGHTER, 
                        playerFlightInformation.getSquadron().determineSquadronCountry(playerFlightInformation.getCampaign().getDate()).getSide());

        if (friendlyFighterSquadron != null)
        {
            MissionBeginUnit missionBeginUnitEscort = new MissionBeginUnit(playerFlightInformation.getDepartureAirfield().getPosition());
            
            McuWaypoint rendezvousWP = WaypointGeneratorUtils.findWaypointByType(playerFlight.getAllWaypoints(), 
                    WaypointType.INGRESS_WAYPOINT.getName());

            if (rendezvousWP != null)
            {
                Coordinate rendezvous = rendezvousWP.getPosition().copy();
                FlightInformation escortFlightInformation = FlightInformationFactory.buildEscortForPlayerFlightInformation(playerFlight.getFlightInformation(), 
                        friendlyFighterSquadron, rendezvous);
                EscortForPlayerFlight escortForPlayerFlight = new EscortForPlayerFlight(escortFlightInformation, missionBeginUnitEscort, playerFlight);
                escortForPlayerFlight.createUnitMission();       
                return escortForPlayerFlight;
            }
        }
        
        return null;
    }
}
