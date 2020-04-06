package pwcg.mission.flight.escort;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetDefinition;

public class EscortForPlayerBuilder
{
    public IFlight createEscortForPlayerFlight(IFlight playerFlight) throws PWCGException 
    {
        IFlightInformation playerFlightInformation = playerFlight.getFlightInformation();
        
        Squadron friendlyFighterSquadron = PWCGContext.getInstance().getSquadronManager().getSquadronByProximityAndRoleAndSide(
                        playerFlightInformation.getCampaign(), 
                        playerFlightInformation.getSquadron().determineCurrentPosition(playerFlightInformation.getCampaign().getDate()), 
                        Role.ROLE_FIGHTER, 
                        playerFlightInformation.getSquadron().determineSquadronCountry(playerFlightInformation.getCampaign().getDate()).getSide());

        if (friendlyFighterSquadron != null)
        {          
            McuWaypoint rendezvousWP = WaypointGeneratorUtils.findWaypointByType(playerFlight.getWaypointPackage().getAllWaypoints(), 
                    WaypointType.RENDEZVOUS_WAYPOINT.getName());

            if (rendezvousWP != null)
            {
                Coordinate rendezvous = rendezvousWP.getPosition().copy();
                IFlightInformation escortFlightInformation = EscortForPlayerFlightInformationBuilder.buildEscortForPlayerFlightInformation(playerFlight.getFlightInformation(), 
                        friendlyFighterSquadron, rendezvous);
                TargetDefinition targetDefinition = EscortForPlayerTargetDefinitionBuilder.buildEscortForPlayerTargetDefinition(escortFlightInformation, rendezvous);
                EscortForPlayerFlight escortForPlayerFlight = new EscortForPlayerFlight(escortFlightInformation, targetDefinition, playerFlight);
                escortForPlayerFlight.createFlight();   
                                
                EscortForPlayerFlightConnector connector = new EscortForPlayerFlightConnector(escortForPlayerFlight, playerFlight);
                connector.connectEscortAndEscortedFlight();

                escortForPlayerFlight.overrideFlightCruisingSpeedForEscort(playerFlight.getFlightCruisingSpeed());
                
                return escortForPlayerFlight;
            }
        }
        
        return null;
    }
}
