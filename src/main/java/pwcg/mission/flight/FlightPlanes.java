package pwcg.mission.flight;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.Role;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.ground.org.IGroundUnit;

public class FlightPlanes implements IFlightPlanes
{
    private List<PlaneMcu> planes = new ArrayList<PlaneMcu>();
    private IFlight flight;

    public FlightPlanes(IFlight flight)
    {
        this.flight = flight;
        this.planes = flight.getFlightInformation().getPlanes();
    }
    
    @Override
    public void enableNonVirtualFlight()
    {
        if (!flight.getFlightInformation().isVirtual())
        {
            for (PlaneMcu plane : planes)
            {
                plane.getEntity().setEnabled(1);
            }
        }
    }

    @Override
    public List<PlaneMcu> getAiPlanes() throws PWCGException 
    {
        List<PlaneMcu> aiPlanes = new ArrayList<>();
        for (PlaneMcu plane : planes)
        {
            if (!plane.getPilot().isPlayer())
            {
                aiPlanes.add(plane);
            }
        }

        return aiPlanes;
    }

    @Override
    public List<PlaneMcu> getPlayerPlanes() throws PWCGException 
    {
        List<PlaneMcu> playerPlanes = new ArrayList<>();
        for (PlaneMcu plane : planes)
        {
            if (plane.getPilot().isPlayer())
            {
                playerPlanes.add(plane);
            }
        }

        return playerPlanes;
    }

    @Override
    public PlaneMcu getPlaneForPilot(Integer pilotSerialNumber)
    {
        PlaneMcu pilotPlane = null;
        for (PlaneMcu plane : planes)
        {
            if (plane.getPilot().getSerialNumber() == pilotSerialNumber)
            {
                pilotPlane = plane;
                break;
            }
        }

        return pilotPlane;
    }

    @Override
    public PlaneMcu getPlaneByLinkTrId(Integer planeLinkTrId)
    {
        PlaneMcu pilotPlane = null;
        for (PlaneMcu plane : planes)
        {
            if (plane.getLinkTrId() == planeLinkTrId)
            {
                pilotPlane = plane;
                break;
            }
        }

        return pilotPlane;
    }

    @Override
    public PlaneMcu getFlightLeader()
    {
        return planes.get(0);
    }

    @Override
    public List<PlaneMcu> getPlanes()
    {
        return planes;
    }

    @Override
    public void setFuel(double myFuel) 
    {
        for (PlaneMcu plane : getPlanes())
        {
            plane.setFuel(myFuel);
        }
    }

    @Override
    public void addFlightTarget(IFlight targetFlight)
    {
        for (PlaneMcu plane : planes)
        {
            if (isAggressivePlane(plane))
            {
                for (PlaneMcu targetPlane : targetFlight.getFlightPlanes().getPlanes())
                {
                    plane.addPlaneTarget(targetPlane.getEntity().getIndex());
                }
            }
        }
    }

    @Override
    public void addGroundUnitTarget(IGroundUnit targetGroundUnit)
    {
        for (PlaneMcu plane : planes)
        {
            if (isAggressivePlane(plane))
            {
                plane.addPlaneTarget(targetGroundUnit.getVehicle().getEntity().getIndex());
            }
        }
    }

    private boolean isAggressivePlane(PlaneType plane)
    {
        if (plane.isPrimaryRole(Role.ROLE_FIGHTER))
        {
            return true;
        }

        if (flight.getFlightInformation().getFlightType().isCategory(FlightTypeCategory.FIGHTER))
        {
            return true;
        }

        return false;
    }

    @Override
    public int getFlightCruisingSpeed()
    {
        int cruisingSpeed = planes.get(0).getCruisingSpeed();
        for (PlaneMcu plane : planes)
        {
            if (plane.getCruisingSpeed() < cruisingSpeed)
            {
                cruisingSpeed = plane.getCruisingSpeed();
            }
        }
        
        return cruisingSpeed;
    }

    @Override
    public boolean isFlightHasFighterPlanes()
    {
        return getPlanes().get(0).isPrimaryRole(Role.ROLE_FIGHTER);
    }

    @Override
    public void setPlanes(List<PlaneMcu> planes) throws PWCGException
    {
        this.planes = planes;        
    }
    
    @Override
    public List<Integer> getPlaneLinkTrIds()
    {
        List<Integer> planeLinkIds = new ArrayList<>();
        for (PlaneMcu plane : planes)
        {
            planeLinkIds.add(plane.getLinkTrId());
        }
        return planeLinkIds;        
    }
    
    @Override
    public void setFlightPayload() throws PWCGException
    {
        FlightPayloadBuilder payloadBuilder = new FlightPayloadBuilder(flight);
        payloadBuilder.setFlightPayload();
    }
    
    @Override
    public void setPlanePosition(Integer planeLinkTrId, Coordinate planeCoords, Orientation planeOrientation, int startingPoint)
    {
        PlaneMcu plane = this.getPlaneByLinkTrId(planeLinkTrId);
        plane.setPosition(planeCoords);
        plane.setOrientation(planeOrientation);
        plane.setStartInAir(startingPoint);
        plane.populateEntity(flight, getFlightLeader());
    }


    @Override
    public void preparePlaneForCoop(IFlight flight) throws PWCGException
    {
        for (PlaneMcu plane : planes)
        {
            if (plane.isActivePlayerPlane())
            {
                plane.setCoopStart(1);
                plane.setAiLevel(AiSkillLevel.ACE);
            }
            else
            {
                plane.setCoopStart(0);
            }
        }
    }
    

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        for (int i = 0; i < planes.size(); ++i)
        {
            PlaneMcu plane = planes.get(i);
            plane.write(writer);
        }
    }
    

    @Override
    public int getFlightSize()
    {
        return planes.size();
    }
  
    @Override
    public void finalize() throws PWCGException
    {
        
    }
}
