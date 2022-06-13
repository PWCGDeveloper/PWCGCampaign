package pwcg.mission.flight;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.flight.plane.PlaneMcu;

public class FlightPlanes
{
    private List<PlaneMcu> planes = new ArrayList<PlaneMcu>();
    private IFlight flight;

    public FlightPlanes(IFlight flight)
    {
        this.flight = flight;
        this.planes = flight.getFlightInformation().getPlanes();
    }
    
    public void enableNonVirtualFlight()
    {
        if (!flight.getFlightInformation().isVirtual())
        {
            for (PlaneMcu plane : planes)
            {
                plane.enable(true);
            }
        }
    }

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

    public PlaneMcu getFlightLeader()
    {
        return planes.get(0);
    }

    public List<PlaneMcu> getPlanes()
    {
        return planes;
    }

    public void setFuelForFlight(double myFuel) 
    {
        for (PlaneMcu plane : getPlanes())
        {
            plane.setFuel(myFuel);
        }
    }

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

    public void setPlanes(List<PlaneMcu> planes) throws PWCGException
    {
        this.planes = planes;        
    }
    
    public List<Integer> getPlaneLinkTrIds()
    {
        List<Integer> planeLinkIds = new ArrayList<>();
        for (PlaneMcu plane : planes)
        {
            planeLinkIds.add(plane.getLinkTrId());
        }
        return planeLinkIds;        
    }
    
    public void setPlanePosition(Integer planeLinkTrId, Coordinate planeCoords, Orientation planeOrientation, int startingPoint)
    {
        PlaneMcu plane = this.getPlaneByLinkTrId(planeLinkTrId);
        plane.setPosition(planeCoords);
        plane.setOrientation(planeOrientation);
        plane.setStartInAir(startingPoint);
        plane.populateEntity(flight, getFlightLeader());
    }


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
    

    public void write(BufferedWriter writer) throws PWCGException 
    {
        for (int i = 0; i < planes.size(); ++i)
        {
            PlaneMcu plane = planes.get(i);
            plane.write(writer);
        }
    }
    

    public int getFlightSize()
    {
        return planes.size();
    }
  
    @Override
    public void finalize() throws PWCGException
    {
        
    }

    public void setTargetsForPlanes(List<Integer> enemyPlanes)
    {
        for (PlaneMcu planeForVwp : planes)
        {
            planeForVwp.addTargets(enemyPlanes);
        }
    }
}
