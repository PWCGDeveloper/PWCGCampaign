package pwcg.mission.flight.escort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.api.Side;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.IFlightInformation;
import pwcg.mission.flight.waypoint.WaypointGeneratorUtils;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.target.TargetDefinition;

public class EscortForPlayerFlightBuilder
{
    public void addEscort(Mission mission, IFlight escortedFlight) throws PWCGException 
    {
        if (mission.isNightMission())
        {
            return;
        }
        
        if (!escortedFlight.getFlightInformation().isPlayerFlight())
        {
            return;
        }
        
        IFlight escortFlight = createEscortForPlayerFlight(escortedFlight);
        if (escortFlight != null)
        {
            escortedFlight.getLinkedFlights().addLinkedFlight(escortFlight);
        }
    }

    private IFlight createEscortForPlayerFlight(IFlight playerFlight) throws PWCGException 
    {
        IFlightInformation playerFlightInformation = playerFlight.getFlightInformation();
        List<Role> escortRole = new ArrayList<Role>(Arrays.asList(Role.ROLE_FIGHTER));
        Side friendlySide = playerFlightInformation.getSquadron().determineSquadronCountry(playerFlightInformation.getCampaign().getDate()).getSide();
        Squadron friendlyFighterSquadron = playerFlight.getMission().getMissionSquadronChooser().getEscortSquadron(playerFlightInformation.getCampaign(), playerFlightInformation, escortRole, friendlySide);

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
