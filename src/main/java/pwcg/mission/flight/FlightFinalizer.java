package pwcg.mission.flight;

import pwcg.campaign.api.IProductSpecificConfiguration;
import pwcg.campaign.factory.ProductSpecificConfigurationFactory;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.exception.PWCGMissionGenerationException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.IUnit;
import pwcg.mission.MissionSkinGenerator;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.IVirtualWaypointPackage;
import pwcg.mission.flight.waypoint.IWaypointPackage;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuMessage;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.mcu.group.VirtualWayPoint;

public class FlightFinalizer 
{
    protected int flightId = -1;

     protected IFlight flight;

    public FlightFinalizer(IFlight flight)
    {
        this.flight = flight;
    }

    public void finalizeFlight() throws PWCGException 
    {
        setWaypointOrientation();
        createWaypointTargetAssociations();
        setFlightLeaderLinks();

        // Special finalization logic for player flight
        if (flight.getFlightData().getFlightInformation().isPlayerFlight())
        {
            finalizePlayerFlight();
        }
        else if (flight.getFlightData().getFlightInformation().isVirtual())
        {
            finalizeVirtualFlight();
        }
        else
        {
            finalizeNonVirtualAiFlight();
        }

        // Finalize linked flights
        for (IFlight linkedFlight : flight.getFlightData().getLinkedFlights().getLinkedFlights())
        {
            linkedFlight.finalizeFlight();
        }

        // Initialize the attack entity to make the AI do something useful
        linkAttackEntity();

        createTargetAssociationsMissionBegin();

        assignSkinsForFlight();
    }

    private void assignSkinsForFlight() throws PWCGException
    {
        MissionSkinGenerator skinGenerator = new MissionSkinGenerator();
        if (flight.getFlightData().getFlightInformation().isPlayerFlight())
        {
            for (PlaneMcu plane : flight.getFlightData().getFlightPlanes().getPlanes())
            {
                SquadronMember squadronMember = plane.getPilot();
                skinGenerator.setSkinForPlayerSquadron(squadronMember, flight.getFlightData().getFlightInformation().getSquadron(), plane, flight.getCampaign().getDate());
            }
        }
        else
        {
            for (PlaneMcu plane : flight.getFlightData().getFlightPlanes().getPlanes())
            {
                skinGenerator.setAISkin(flight.getFlightData().getFlightInformation().getSquadron(), plane, flight.getCampaign().getDate());
            }
        }
    }

    private void finalizePlayerFlight() throws PWCGException 
    {
        // Add take off notification.
        // This is needed to trigger the first WP
        if (!flight.getFlightData().getFlightInformation().isAirStart())
        {
            if (flight.getFlightData().getTakeoff() != null)
            {
                BaseFlightMcu wpEntryMcu = flight.getFlightData().getWaypointPackage().getEntryMcu();

                flight.getFlightData().getFlightPlanes().getFlightLeader().getEntity().setOnMessages(
                                McuMessage.ONTAKEOFF,
                                flight.getFlightData().getTakeoff().getTakeoffIndex(),
                                wpEntryMcu.getIndex());
            }
        }
        else
        {
            // The mission begin timer triggers the formation timer.
            // For airstart, link the formation timer to the WP timer
            BaseFlightMcu wpEntryMcu = flight.getFlightData().getWaypointPackage().getEntryMcu();
            if (wpEntryMcu != null)
            {
                flight.getFormationTimer().setTarget(wpEntryMcu.getIndex());
            }
            resetPlaneInitialPositionForAirStarts();
        }
        
        flight.getFlightData().getFlightPlanes().enableNonVirtualFlight();
    }

    private void finalizeNonVirtualAiFlight() throws PWCGException 
    {
        // The mission begin timer triggers the formation timer.
        // For airstart, link the formation timer to the WP timer
    	if (flight.getFlightData().getFlightInformation().isEscortedByPlayerFlight() || flight.getFlightData().getFlightInformation().isEscortForPlayerFlight())
        {
            // Flights escorted by the player or escorts for the player circle until rendezvous
        }
    	else
    	{
	        BaseFlightMcu wpEntryMcu = flight.getFlightData().getWaypointPackage().getEntryMcu();
	        if (wpEntryMcu != null)
	        {
	            flight.getFormationTimer().setTarget(wpEntryMcu.getIndex());
	        }
    	}

        resetPlaneInitialPositionForAirStarts();
        flight.getFlightData().getFlightPlanes().enableNonVirtualFlight();
    }

    private void finalizeVirtualFlight() throws PWCGException 
    {
        if (flight.getFlightData().getFlightInformation().isVirtual())
        {
            IVirtualWaypointPackage virtualWaypointPackage = flight.getFlightData().getVirtualWaypointPackage();
            virtualWaypointPackage.buildVirtualWaypoints();            
        }
        else
        {
            throw new PWCGMissionGenerationException("Non virtual AI flight");
        }
    }

    private void linkAttackEntity()
    {
        for (int index = 0; index < flight.getFlightData().getFlightPlanes().getFlightSize(); ++index)
        {
            PlaneMcu plane = flight.getFlightData().getFlightPlanes().get(index);
            
            plane.initializeAttackEntity(index);
            if (flight.getFlightData().getFlightInformation().isVirtual())
            {
                IVirtualWaypointPackage virtualWaypointPackage = flight.getFlightData().getVirtualWaypointPackage();
                for (VirtualWayPoint vwp : virtualWaypointPackage.getVirtualWaypoints())
                {
                    vwp.onTriggerAddTarget(plane, plane.getOnSpawnTimer().getIndex());
                }
            }
            else
            {
                IWaypointPackage waypointPackage = flight.getFlightData().getWaypointPackage();
                waypointPackage.onTriggerAddTarget(plane.getOnSpawnTimer().getIndex());
            }
        }
    }

    private void setFlightLeaderLinks()
    {
        int flightLeaderIndex = flight.getFlightData().getFlightPlanes().getFlightLeader().getEntity().getIndex();
        if (flight.getFlightData().getActivation().getActivationEntity() != null)
        {
            flight.getFlightData().getActivation().getActivationEntity().setObject(flightLeaderIndex);
        }

        // only the player takes off
        if (flight.getFlightData().getTakeoff() != null)
        {
            flight.getFlightData().getTakeoff().setObject(flightLeaderIndex);
        }

        // Landing
        if (flight.getFlightData().getLanding() != null)
        {
            flight.getFlightData().getLanding().setObject(flightLeaderIndex);
        }

        // Formation
        if (flight.getFlightData().getFormation().getFormationEntity() != null)
        {
            flight.getFlightData().getFormation().getFormationEntity().setObject(flightLeaderIndex);
        }
    }

    private void setWaypointOrientation() throws PWCGException 
    {
        // TL each waypoint to the next one
        McuWaypoint prevWP = null;
        for (McuWaypoint nextWP : flight.getFlightData().getWaypointPackage().getWaypointsForLeadPlane())
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

    // TODO make series of classes that hook things together ... patterns?
    // runway -> takeoff -> initial -> mission ingress -> mission (patrol) -> mission egress -> land
    // runway -> takeoff -> initial -> mission ingress -> attack -> mission egress -> land
    private void createWaypointTargetAssociations() throws PWCGException
    {
        flight.createTargetAssociationsForFlight();
    }

    private void createTargetAssociationsMissionBegin()
    {
        if (flight.getFlightData().getFlightInformation().isPlayerFlight())
        {
            flight.getMissionBeginUnit().linkToMissionBegin(flight.getFormationTimer().getIndex());
        }
        else if (flight.getFlightData().getFlightInformation().isVirtual())
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
