package pwcg.mission.flight.escort;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.plot.FlightInformationFactory;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;

public class PlayerEscortBuilder
{
    public IFlight createEscortForPlayerFlight(IFlight playerFlight) throws PWCGException 
    {
        IFlightInformation playerFlightInformation = playerFlight.getFlightData().getFlightInformation();
        
        Squadron friendlyFighterSquadron = PWCGContext.getInstance().getSquadronManager().getSquadronByProximityAndRoleAndSide(
                        playerFlightInformation.getCampaign(), 
                        playerFlightInformation.getSquadron().determineCurrentPosition(playerFlightInformation.getCampaign().getDate()), 
                        Role.ROLE_FIGHTER, 
                        playerFlightInformation.getSquadron().determineSquadronCountry(playerFlightInformation.getCampaign().getDate()).getSide());

        if (friendlyFighterSquadron != null)
        {          
            McuWaypoint rendezvousWP = WaypointGeneratorUtils.findWaypointByType(playerFlight.getFlightData().getWaypointPackage().getAllWaypoints(), 
                    WaypointType.INGRESS_WAYPOINT.getName());

            if (rendezvousWP != null)
            {
                Coordinate rendezvous = rendezvousWP.getPosition().copy();
                IFlightInformation escortFlightInformation = FlightInformationFactory.buildEscortForPlayerFlightInformation(playerFlight.getFlightData().getFlightInformation(), 
                        friendlyFighterSquadron, rendezvous);
                EscortForPlayerFlight escortForPlayerFlight = new EscortForPlayerFlight(escortFlightInformation, playerFlight);
                escortForPlayerFlight.createFlight();       
                return escortForPlayerFlight;
            }
        }
        
        return null;
    }
}
