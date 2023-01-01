package pwcg.mission.finalize;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.NecessaryFlightType;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.flight.waypoint.missionpoint.MissionPoint;
import pwcg.mission.mcu.McuIcon;
import pwcg.mission.mcu.McuIconFactory;
import pwcg.mission.mcu.McuIconLineType;
import pwcg.mission.mcu.McuWaypoint;

public class MissionWaypointIconBuilder
{
    private List<McuIcon> waypointIcons = new ArrayList<>();

    public void createWaypointIcons(List<IFlight> playerFlights) throws PWCGException
    {
        for (IFlight playerFlight : playerFlights)
        {
            createWaypointIconsForFlight(playerFlight);
        }
    }

    private void createWaypointIconsForFlight(IFlight playerFlight) throws PWCGException
    {
        List<McuWaypoint> waypoints = playerFlight.getWaypointPackage().getAllWaypoints();

        McuIcon firstIcon = null;
        McuIcon prevIcon = null;

        MissionPoint takeoff = playerFlight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_TAKEOFF);
        if (takeoff != null)
        {
            McuIcon icon = McuIconFactory.buildWaypointActionIcon(WaypointAction.WP_ACTION_TAKEOFF, takeoff, playerFlight.getFlightInformation().getCountry().getSide());
            prevIcon = icon;
            firstIcon = icon;
            waypointIcons.add(icon);
        }

        for (int i = 0; i < waypoints.size(); ++i)
        {
            McuWaypoint waypoint = waypoints.get(i);
            McuIcon icon = McuIconFactory.buildWaypointIcon(waypoint, playerFlight.getFlightInformation().getCountry().getSide());
            if (firstIcon == null)
            {
                firstIcon = icon;
            }
            if (prevIcon != null)
            {
                prevIcon.setIconTarget(icon.getIndex());
            }
            prevIcon = icon;
            waypointIcons.add(icon);

            if (waypoint.getWpAction() == WaypointAction.WP_ACTION_RENDEZVOUS)
            {                
                IFlight escortedByPlayerFlight = playerFlight.getAssociatedFlight();
                if (escortedByPlayerFlight != null && escortedByPlayerFlight.getFlightInformation().getNecessaryFlightType() == NecessaryFlightType.PLAYER_ESCORTED)
                {
                    boolean foundRendezvous = false;

                    for (McuWaypoint escortWaypoint : escortedByPlayerFlight.getWaypointPackage().getAllWaypoints())
                    {
                        if (escortWaypoint.getWpAction() == WaypointAction.WP_ACTION_LANDING_APPROACH)
                        {
                            break;
                        }
                        if (!foundRendezvous)
                        {
                            if (escortWaypoint.getWpAction() == WaypointAction.WP_ACTION_RENDEZVOUS)
                            {
                                foundRendezvous = true;
                            }
                            continue;
                        }

                        icon = new McuIcon(escortWaypoint, playerFlight.getFlightInformation().getCountry().getSide());
                        icon.setName("Escort " + icon.getName());
                        IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
                        if (productSpecificConfiguration.usePosition1()) {
                            prevIcon.setLineType(McuIconLineType.ICON_LINE_TYPE_POSITION2);
                        }
                        prevIcon.setIconTarget(icon.getIndex());
                        prevIcon = icon;
                        waypointIcons.add(icon);

                        if (escortWaypoint.getWpAction() == WaypointAction.WP_ACTION_TARGET_FINAL)
                        {
                            MissionPoint target = escortedByPlayerFlight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_ATTACK);
                            icon = McuIconFactory.buildWaypointActionIcon(WaypointAction.WP_ACTION_ATTACK, target, escortedByPlayerFlight.getFlightInformation().getCountry().getSide());
                            icon.setName("Escort " + icon.getName());
                            if (productSpecificConfiguration.usePosition1()) {
                                prevIcon.setLineType(McuIconLineType.ICON_LINE_TYPE_POSITION2);
                            }
                            prevIcon.setIconTarget(icon.getIndex());
                            prevIcon = icon;
                            waypointIcons.add(icon);
                        }
                    }
                }
            }

            if (waypoint.getWpAction() == WaypointAction.WP_ACTION_TARGET_FINAL)
            {
                MissionPoint target = playerFlight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_ATTACK);
                icon = McuIconFactory.buildWaypointActionIcon(WaypointAction.WP_ACTION_ATTACK, target, playerFlight.getFlightInformation().getCountry().getSide());
                prevIcon.setIconTarget(icon.getIndex());
                prevIcon = icon;
                waypointIcons.add(icon);
            }
        }

        MissionPoint landing = playerFlight.getWaypointPackage().getMissionPointByAction(WaypointAction.WP_ACTION_LANDING);
        if (landing != null)
        {
            McuIcon icon = McuIconFactory.buildWaypointActionIcon(WaypointAction.WP_ACTION_LANDING, landing, playerFlight.getFlightInformation().getCountry().getSide());
            if (prevIcon != null)
                prevIcon.setIconTarget(icon.getIndex());
            icon.setIconTarget(firstIcon.getIndex());
            waypointIcons.add(icon);
        }
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        for (McuIcon icon : waypointIcons)
        {
            icon.write(writer);
        }
    }

    public void removeWaypointIcons() throws PWCGException
    {
        waypointIcons.clear();
    }

}
