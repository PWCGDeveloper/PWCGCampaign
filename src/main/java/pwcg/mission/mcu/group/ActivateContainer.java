package pwcg.mission.mcu.group;

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

public class ActivateContainer
{
    private List<PlaneMcu> planesAtActivate = new ArrayList<>();
    private McuTimer activateTimer = new McuTimer();
    private McuActivate activate = new McuActivate();
    private McuTimer wpActivateTimer = new McuTimer();
    private McuTimer planeAttackTimer = new McuTimer();
    private VirtualWayPointCoordinate vwpCoordinate;
    private McuTimer formationTimer = new McuTimer();
    private McuFormation formation = new McuFormation();

    public ActivateContainer(VirtualWayPointCoordinate vwpPosition)
    {
        this.vwpCoordinate = vwpPosition;
    }

    public void create(IFlight flight, Coordinate flightCoordinate) throws PWCGException
    {
        buildPlanesAtActivate(flight, flightCoordinate);
        buildMcus(flightCoordinate);
        createTargetAssociations();
        createObjectAssociations();
    }

    private void buildPlanesAtActivate(IFlight flight, Coordinate flightCoordinate) throws PWCGException
    {
        for (int i = 0; i < flight.getFlightPlanes().getFlightSize(); ++i)
        {
            PlaneMcu plane = flight.getFlightPlanes().getPlanes().get(i).copy();
            double planeAltitude = flightCoordinate.getYPos() + (30 * i);
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

    private void buildMcus(Coordinate flightCoordinate)
    {
        activate.setPosition(flightCoordinate.copy());
        activate.setOrientation(vwpCoordinate.getOrientation().copy());
        activate.setName("Activate");
        activate.setDesc("Activate");
        
        activateTimer.setTimer(1);
        activateTimer.setPosition(flightCoordinate.copy());
        activateTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        activateTimer.setName("Activate Timer");
        activateTimer.setDesc("Activate Timer");

        wpActivateTimer.setTimer(1);
        wpActivateTimer.setPosition(flightCoordinate.copy());
        wpActivateTimer.setOrientation(vwpCoordinate.getOrientation().copy());
        wpActivateTimer.setName("Activate WP Timer");
        wpActivateTimer.setDesc("Activate WP Timer");
        
        formationTimer.setPosition(flightCoordinate.copy());
        formationTimer.setName("Formation Timer");
        formationTimer.setDesc("Formation Timer");
        
        planeAttackTimer.setPosition(flightCoordinate.copy());
        planeAttackTimer.setName("Plane Attack Timer");
        planeAttackTimer.setDesc("Plane Attack Timer");

        formation.setPosition(flightCoordinate.copy());
    }

    private void createTargetAssociations()
    {
        activateTimer.setTarget(activate.getIndex());
        formationTimer.setTarget(formation.getIndex());
        wpActivateTimer.setTarget(vwpCoordinate.getWaypointindex());

        for (PlaneMcu plane : planesAtActivate)
        {
            planeAttackTimer.setTarget(plane.getAttackTimer().getIndex());
        }

        activateTimer.setTarget(formationTimer.getIndex());
        formationTimer.setTarget(wpActivateTimer.getIndex());
        wpActivateTimer.setTarget(planeAttackTimer.getIndex());
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
        wpActivateTimer.write(writer);
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
    
    public int getEntryPoint()
    {
        return activateTimer.getIndex();
    }
    
    public void linkToNextActivateContainer(int nextAActivateContainerIndex)
    {
        activateTimer.setTarget(nextAActivateContainerIndex);
    }
    
    public void registerPlaneCounter(int counterIndex)
    {
        activateTimer.setTarget(counterIndex);
    }

    public McuTimer getActivateTimer()
    {
        return activateTimer;
    }

    public McuTimer getWpActivateTimer()
    {
        return wpActivateTimer;
    }
    
    public McuTimer getFormationTimer()
    {
        return formationTimer;
    }

    public McuTimer getPlaneAttackTimer()
    {
        return planeAttackTimer;
    }

    public McuActivate getActivate()
    {
        return activate;
    }

    public McuFormation getFormation()
    {
        return formation;
    }

    public VirtualWayPointCoordinate getVwpPosition()
    {
        return vwpCoordinate;
    }
}
