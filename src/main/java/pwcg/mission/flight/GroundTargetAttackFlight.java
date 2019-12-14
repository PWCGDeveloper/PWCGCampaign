package pwcg.mission.flight;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuWaypoint;

public abstract class GroundTargetAttackFlight extends Flight
{
    protected Map<Integer, AirGroundAttackMcuSequence> attackMcuSequences = new HashMap<Integer, AirGroundAttackMcuSequence>();
    
    protected int attackTime = 180;
    
    public GroundTargetAttackFlight(FlightInformation flightInformation, MissionBeginUnit missionBeginUnit, int attackTime)
    {
        super (flightInformation, missionBeginUnit);
        this.attackTime = attackTime;
    }

    @Override
    protected void createFlightSpecificTargetAssociations() throws PWCGException
    {
        if (isVirtual())
        {
            getWaypointPackage().duplicateWaypointsForFlight(this);
           
            for (PlaneMCU plane : getPlanes())
            {
                AirGroundAttackMcuSequence attackMcuSequence = attackMcuSequences.get(plane.getIndex());
                List<McuWaypoint>waypointsToLink = getWaypointPackage().getWaypointsForPlane(plane);
                
                createWaypointTargetAssociationsWithAttack(plane, waypointsToLink, attackMcuSequence);
            }
        }
        else
        {
            AirGroundAttackMcuSequence attackMcuSequence = attackMcuSequences.get(getLeadPlane().getIndex());
            List<McuWaypoint>waypointsToLink =  getWaypointPackage().getWaypointsForLeadPlane();
            
            createWaypointTargetAssociationsWithAttack(getLeadPlane(), waypointsToLink, attackMcuSequence);
        }
    }

    private void createWaypointTargetAssociationsWithAttack(PlaneMCU plane, List<McuWaypoint>waypointsToLink, AirGroundAttackMcuSequence attackMcuSequence)
    {
        linkWPToPlane(plane, waypointsToLink);
        
        if (getWaypointPackage().getWaypointsForLeadPlane().size() > 0)
        {
            McuWaypoint prevWP = null;
    
            for (McuWaypoint waypoint: waypointsToLink)
            {
                if (prevWP != null)
                {
                    if (prevWP.isTargetWaypoint())
                    {
                        attackMcuSequence.linkToAttackDeactivate(waypoint.getIndex());
                    }
                    else
                    {
                        prevWP.setTarget(waypoint.getIndex());
                    }
                }
    
                prevWP = waypoint;
            }
        }
    }
    
    protected void createAttackArea(int altitude) throws PWCGException 
    {
        if (isVirtual())
        {
            for (PlaneMCU plane : planes)
            {
                AirGroundAttackMcuSequence attackMcuSequence = new AirGroundAttackMcuSequence();
                attackMcuSequence.createAttackArea(getSquadron().determineDisplayName(getCampaign().getDate()), this.getFlightType(), getTargetCoords(), altitude, attackTime);
                attackMcuSequence.createTriggerForPlane(plane, getTargetCoords());
                
                attackMcuSequences.put(plane.getIndex(), attackMcuSequence);
            }
        }
        else
        {
            AirGroundAttackMcuSequence attackMcuSequence = new AirGroundAttackMcuSequence();
            attackMcuSequence.createAttackArea(getSquadron().determineDisplayName(getCampaign().getDate()), this.getFlightType(), getTargetCoords(), altitude, attackTime);
            attackMcuSequence.createTriggerForFlight(this, getTargetCoords());
            
            attackMcuSequences.put(getLeadPlane().getIndex(), attackMcuSequence);
        }
    }

    @Override
    public List<BaseFlightMcu> getAllMissionPoints()
    {
        List<BaseFlightMcu> allMissionPoints = new ArrayList<BaseFlightMcu>();
        
        List<McuWaypoint> allWaypoints = this.getAllFlightWaypoints();
        for (McuWaypoint waypoint : allWaypoints)
        {
            allMissionPoints.add(waypoint);
            
            if (waypoint.getWpAction() == WaypointAction.WP_ACTION_TARGET_FINAL)
            {
                allMissionPoints.add(attackMcuSequences.get(getLeadPlane().getIndex()).getAttackAreaMcu());
            }
        }
        
        return allMissionPoints;
    }

    @Override
    public List<BaseFlightMcu> getAllMissionPointsForPlane(PlaneMCU plane)
    {
        List<BaseFlightMcu> allMissionPointsForPlane = new ArrayList<BaseFlightMcu>();
        
        List<McuWaypoint> allWaypoints = this.getAllWaypointsForPlane(plane);
        for (McuWaypoint waypoint : allWaypoints)
        {
            allMissionPointsForPlane.add(waypoint);
            
            if (waypoint.getWpAction() == WaypointAction.WP_ACTION_TARGET_FINAL)
            {
                allMissionPointsForPlane.add(attackMcuSequences.get(plane.getIndex()).getAttackAreaMcu());
            }
        }
        
        return allMissionPointsForPlane;
    }


    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        super.write(writer);

        for (AirGroundAttackMcuSequence attackMcuSequence : attackMcuSequences.values())
        {
            attackMcuSequence.write(writer);
        }
    }

    public void validate() throws PWCGException
    {
        if (isVirtual())
        {
            if (attackMcuSequences.size() != getPlanes().size())
            {
                throw new PWCGException("Incorrect number of attack sequences for virtual flight");
            }
        }
        else
        {
            if (attackMcuSequences.size() != 1)
            {
                throw new PWCGException("Incorrect number of attack sequences for realized flight");
            }

            if (!attackMcuSequences.containsKey(getLeadPlane().getIndex()))
            {
                throw new PWCGException("Incorrect association for attack sequence");
            }
        }
    }
}
