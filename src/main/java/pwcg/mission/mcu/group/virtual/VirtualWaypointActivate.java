package pwcg.mission.mcu.group.virtual;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.flight.waypoint.FormationGenerator;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.mcu.McuActivate;
import pwcg.mission.mcu.McuFormation;
import pwcg.mission.mcu.McuTimer;

public class VirtualWaypointActivate
{
    private IFlight flight;
    private VirtualWayPointCoordinate vwpCoordinate;

    private List<PlaneMcu> planesAtActivate = new ArrayList<>();
    private McuFormation formation = new McuFormation();
    private McuActivate activate = new McuActivate();

    private McuTimer activateTimer = new McuTimer();
    private McuTimer formationTimer = new McuTimer();
    private McuTimer planeAttackTimer = new McuTimer();
    private McuTimer waypointTimer = new McuTimer();

    public VirtualWaypointActivate(IFlight flight, VirtualWayPointCoordinate vwpCoordinate)
    {
        this.flight = flight;
        this.vwpCoordinate = vwpCoordinate;
    }

    public void build() throws PWCGException
    {
        buildPlanesAtActivate();
        buildMcus();
        createTargetAssociations();
        createObjectAssociations();
    }

    private void buildPlanesAtActivate() throws PWCGException
    {
        for (int i = 0; i < flight.getFlightPlanes().getFlightSize(); ++i)
        {
            PlaneMcu plane = flight.getFlightPlanes().getPlanes().get(i).copy();
            double planeAltitude = vwpCoordinate.getPosition().getYPos() + (30 * i);
            if (planeAltitude < 800.0)
            {
                planeAltitude = 800.0;
            }

            Coordinate planeCoordinate = FormationGenerator.generatePositionForPlaneInFormation(vwpCoordinate.getOrientation(), vwpCoordinate.getPosition(), i);
            planeCoordinate.setYPos(planeAltitude);
            plane.setPosition(planeCoordinate);
            plane.getEntity().setEnabled(0);
            planesAtActivate.add(plane);
        }
        
        flight.getWaypointPackage().addWaypointObjectFromIndex(planesAtActivate.get(0));
    }

    private void buildMcus()
    {
        activate.setPosition(vwpCoordinate.getPosition().copy());
        activate.setOrientation(vwpCoordinate.getOrientation().copy());
        activate.setName("Activate");
        activate.setDesc("Activate");
        
        activateTimer.setTimer(1);
        activateTimer.setPosition(vwpCoordinate.getPosition().copy());
        activateTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        activateTimer.setName("Activate Timer");
        activateTimer.setDesc("Activate Timer");

        waypointTimer.setTimer(1);
        waypointTimer.setPosition(vwpCoordinate.getPosition().copy());
        waypointTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        waypointTimer.setName("Activate WP Timer");
        waypointTimer.setDesc("Activate WP Timer");
        
        formationTimer.setPosition(vwpCoordinate.getPosition().copy());
        formationTimer.setName("Formation Timer");
        formationTimer.setDesc("Formation Timer");
        
        planeAttackTimer.setPosition(vwpCoordinate.getPosition().copy());
        planeAttackTimer.setName("Plane Attack Timer");
        planeAttackTimer.setDesc("Plane Attack Timer");

        formation.setPosition(vwpCoordinate.getPosition().copy());
    }

    private void createTargetAssociations()
    {
        activateTimer.setTarget(activate.getIndex());
        formationTimer.setTarget(formation.getIndex());
        
        int wpIndex = vwpCoordinate.getWaypointIdentifier(flight);
        waypointTimer.setTarget(wpIndex);

        for (PlaneMcu plane : planesAtActivate)
        {
            planeAttackTimer.setTarget(plane.getAttackTimer().getIndex());
        }

        activateTimer.setTarget(formationTimer.getIndex());
        formationTimer.setTarget(waypointTimer.getIndex());
        waypointTimer.setTarget(planeAttackTimer.getIndex());
    }

    private void createObjectAssociations()
    {
        for (PlaneMcu plane : planesAtActivate)
        {
            activate.setObject(plane.getEntity().getIndex());
        }
        
        formation.setObject(planesAtActivate.get(0).getEntity().getIndex());
    }

    public void write(BufferedWriter writer) throws PWCGException
    {
        activateTimer.write(writer);
        waypointTimer.write(writer);
        formationTimer.write(writer);
        planeAttackTimer.write(writer);
        
        activate.write(writer);
        formation.write(writer);

        for (PlaneMcu plane : planesAtActivate)
        {
            plane.write(writer);
        }
    }
    
    public PlaneMcu getLeadActivatePlane()
    {
        return planesAtActivate.get(0);
    }
    
    public List<PlaneMcu> getAllPlanes()
    {
        return planesAtActivate;
    }
    
    public int getEntryPoint()
    {
        return activateTimer.getIndex();
    }

    public McuFormation getFormation()
    {
        return formation;
    }

    public McuActivate getActivate()
    {
        return activate;
    }

    public McuTimer getActivateTimer()
    {
        return activateTimer;
    }

    public McuTimer getFormationTimer()
    {
        return formationTimer;
    }

    public McuTimer getPlaneAttackTimer()
    {
        return planeAttackTimer;
    }

    public McuTimer getWaypointTimer()
    {
        return waypointTimer;
    }
}
