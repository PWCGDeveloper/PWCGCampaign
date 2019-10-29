package pwcg.mission.flight;

import java.util.List;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.MissionSkinGenerator;
import pwcg.mission.Unit;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.ActualWaypointPackage;
import pwcg.mission.flight.waypoint.VirtualWaypointPackage;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuMessage;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class FlightFinalizer 
{
    protected int flightId = -1;

     protected Flight flight;

    public FlightFinalizer(Flight flight)
    {
        this.flight = flight;
    }

    public void finalizeFlight() throws PWCGException 
    {
        setWaypointOrientation();
        createWaypointTargetAssociations();
        setFlightLeaderLinks();

        // Special finalization logic for player flight
        if (flight.isPlayerFlight())
        {
            finalizePlayerFlight();
        }
        else if (flight.isVirtual())
        {
            finalizeVirtualFlight();
        }
        else
        {
            finalizeNonVirtualAiFlight();
        }

        // Finalize linked flights
        for (Unit unit : flight.getLinkedUnits())
        {
            if (unit instanceof Flight)
            {
                Flight flight = (Flight) unit;
                flight.finalizeFlight();
            }
        }

        // Initialize the attack entity to make the AI do something useful
        linkAttackEntity();

        createTargetAssociationsMissionBegin();

        assignSkinsForFlight();
    }

    private void assignSkinsForFlight() throws PWCGException
    {
        MissionSkinGenerator skinGenerator = new MissionSkinGenerator();
        if (flight.isPlayerFlight())
        {
            for (PlaneMCU plane : flight.getPlanes())
            {
                SquadronMember squadronMember = plane.getPilot();
                skinGenerator.setSkinForPlayerSquadron(squadronMember, flight.getSquadron(), plane, flight.getCampaign().getDate());
            }
        }
        else
        {
            for (PlaneMCU plane : flight.getPlanes())
            {
                skinGenerator.setAISkin(flight.getSquadron(), plane, flight.getCampaign().getDate());
            }
        }
    }

    private void finalizePlayerFlight() throws PWCGException 
    {
        // Add take off notification.
        // This is needed to trigger the first WP
        if (!flight.isAirStart())
        {
            if (flight.getTakeoff() != null)
            {
                BaseFlightMcu wpEntryMcu = flight.getWaypointPackage().getEntryMcu();

                flight.getLeadPlane().getEntity().setOnMessages(
                                McuMessage.ONTAKEOFF,
                                flight.getTakeoff().getIndex(),
                                wpEntryMcu.getIndex());
            }
        }
        else
        {
            // The mission begin timer triggers the formation timer.
            // For airstart, link the formation timer to the WP timer
            BaseFlightMcu wpEntryMcu = flight.getWaypointPackage().getEntryMcu();
            if (wpEntryMcu != null)
            {
                flight.getFormationTimer().setTarget(wpEntryMcu.getIndex());
            }
        }

        // Reset the player flight for air starts
        if (flight.isAirStart())
        {
            resetPlaneInitialPositionForAirStarts();
        }
        
        flight.enableNonVirtualFlight();
    }

    private void finalizeNonVirtualAiFlight() throws PWCGException 
    {
        // The mission begin timer triggers the formation timer.
        // For airstart, link the formation timer to the WP timer
    	if (flight.getFlightInformation().isEscortedByPlayerFlight() || flight.getFlightInformation().isEscortForPlayerFlight())
        {
            // Flights escorted by the player or escorts for the player circle until rendezvous
        }
    	else
    	{
	        BaseFlightMcu wpEntryMcu = flight.getWaypointPackage().getEntryMcu();
	        if (wpEntryMcu != null)
	        {
	            flight.getFormationTimer().setTarget(wpEntryMcu.getIndex());
	        }
    	}

        resetPlaneInitialPositionForAirStarts();
        flight.enableNonVirtualFlight();
    }

    private void finalizeVirtualFlight() throws PWCGException 
    {
        if (flight.getWaypointPackage() instanceof VirtualWaypointPackage)
        {
            buildVirtualWaypoints();            
            linkVirtualWaypoints();
        }
        else
        {
            throw new PWCGMissionGenerationException("Non virtual AI flight");
        }
    }

    private void buildVirtualWaypoints() throws PWCGException
    {
        VirtualWaypointGenerator virtualWaypointGenerator = new VirtualWaypointGenerator(flight);
        List<VirtualWayPoint> virtualWaypoints = virtualWaypointGenerator.createVirtualWaypoints();

        VirtualWaypointPackage virtualWaypointPackage = (VirtualWaypointPackage) flight.getWaypointPackage();
        virtualWaypointPackage.setVirtualWaypoints(virtualWaypoints);
    }

    private void linkVirtualWaypoints()
    {
        BaseFlightMcu wpEntryMcu = flight.getWaypointPackage().getEntryMcu();
        if (wpEntryMcu != null)
        {
            flight.getMissionBeginUnit().linkToMissionBegin(wpEntryMcu.getIndex());
        }
    }

    private void linkAttackEntity()
    {
        for (int index = 0; index < flight.getPlanes().size(); ++index)
        {
            PlaneMCU plane = flight.getPlanes().get(index);
            
            plane.initializeAttackEntity(index);
            if (flight.getWaypointPackage() instanceof VirtualWaypointPackage)
            {
                VirtualWaypointPackage virtualWaypointPackage = (VirtualWaypointPackage)flight.getWaypointPackage();
                for (VirtualWayPoint vwp : virtualWaypointPackage.getVirtualWaypoints())
                {
                    vwp.onTriggerAddTarget(plane, plane.getOnSpawnTimer().getIndex());
                }
            }
            else
            {
                ActualWaypointPackage actualWaypointPackage = (ActualWaypointPackage)flight.getWaypointPackage();
                actualWaypointPackage.onTriggerAddTarget(plane.getOnSpawnTimer().getIndex());
            }
        }
    }

    private void resetPlaneInitialPositionForAirStarts() throws PWCGException 
    {
        PlaneMCU flightLeader = flight.getFlightLeader();

        int i = 0;
        Coordinate flightLeaderPos = null;
        Orientation flightLeaderOrient = null;
        for (PlaneMCU plane : flight.getPlanes())
        {
            if (i == 0)
            {
                flightLeaderPos = flightLeader.getPosition().copy();
                flightLeaderOrient = flightLeader.getOrientation().copy();
                ++i;
                continue;
            }

            Coordinate planeCoords = new Coordinate();

            // Since we always face east, subtract from z to get your mates
            // behind you
            IProductSpecificConfiguration productSpecificConfiguration = ProductSpecificConfigurationFactory.createProductSpecificConfiguration();
            int AircraftSpacingHorizontal = productSpecificConfiguration.getAircraftSpacingHorizontal();
            planeCoords.setXPos(flightLeaderPos.getXPos() - (i * AircraftSpacingHorizontal));
            planeCoords.setZPos(flightLeaderPos.getZPos() - (i * AircraftSpacingHorizontal));

            int AircraftSpacingVertical = productSpecificConfiguration.getAircraftSpacingVertical();
            planeCoords.setYPos(flightLeaderPos.getYPos() + (i * AircraftSpacingVertical));
            plane.setPosition(planeCoords);

            plane.setOrientation(flightLeaderOrient.copy());

            ++i;
        }
    }

    private void setFlightLeaderLinks()
    {
        int flightLeaderIndex = flight.getFlightLeader().getEntity().getIndex();
        if (flight.getActivationEntity() != null)
        {
            flight.getActivationEntity().setObject(flightLeaderIndex);
        }

        // only the player takes off
        if (flight.getTakeoff() != null)
        {
            flight.getTakeoff().setObject(flightLeaderIndex);
        }

        // Landing
        if (flight.getLanding() != null)
        {
            flight.getLanding().setObject(flightLeaderIndex);
        }

        // Formation
        if (flight.getFormationEntity() != null)
        {
            flight.getFormationEntity().setObject(flightLeaderIndex);
        }
    }

    private void setWaypointOrientation() throws PWCGException 
    {
        // TL each waypoint to the next one
        McuWaypoint prevWP = null;
        for (McuWaypoint nextWP : flight.getWaypointPackage().getWaypointsForLeadPlane())
        {
            if (prevWP != null)
            {
                Coordinate prevPos = prevWP.getPosition().copy();
                Coordinate nextPos = nextWP.getPosition().copy();
    
                Orientation orient = new Orientation();
                orient.setyOri(MathUtils.calcAngle(prevPos, nextPos));
    
                prevWP.setOrientation(orient);
            }
            
            prevWP = nextWP;
        }
    }

    private void createWaypointTargetAssociations() throws PWCGException
    {
        flight.createTargetAssociationsForFlight();
    }

    private void createTargetAssociationsMissionBegin()
    {
        if (flight.isPlayerFlight())
        {
            flight.getMissionBeginUnit().linkToMissionBegin(flight.getFormationTimer().getIndex());
        }
        else if (flight.isVirtual())
        {
            flight.getMissionBeginUnit().linkToMissionBegin(flight.getActivationTimer().getIndex());
            flight.getActivationTimer().setTarget(flight.getFormationTimer().getIndex());
        }
        else
        {
            flight.getMissionBeginUnit().linkToMissionBegin(flight.getActivationTimer().getIndex());
            flight.getActivationTimer().setTarget(flight.getFormationTimer().getIndex());
        }
    }
}
