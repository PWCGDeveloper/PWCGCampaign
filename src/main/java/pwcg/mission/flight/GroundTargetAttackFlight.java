package pwcg.mission.flight;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.target.TacticalTarget;
import pwcg.core.exception.PWCGException;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.Unit;
import pwcg.mission.flight.plane.PlaneMCU;
import pwcg.mission.flight.waypoint.WaypointAction;
import pwcg.mission.ground.unittypes.GroundUnit;
import pwcg.mission.ground.unittypes.staticunits.AirfieldStaticGroup;
import pwcg.mission.ground.unittypes.transport.GroundTrainUnit;
import pwcg.mission.mcu.BaseFlightMcu;
import pwcg.mission.mcu.McuWaypoint;

public abstract class GroundTargetAttackFlight extends Flight
{
    protected Map<Integer, AttackMcuSequence> attackMcuSequences = new HashMap<Integer, AttackMcuSequence>();
    
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
                AttackMcuSequence attackMcuSequence = getAttackMcuSequences().get(plane.getIndex());
                List<McuWaypoint>waypointsToLink = getWaypointPackage().getWaypointsForPlane(plane);
                
                createWaypointTargetAssociationsWithAttack(plane, waypointsToLink, attackMcuSequence);
            }
        }
        else
        {
            AttackMcuSequence attackMcuSequence = getAttackMcuSequences().get(getLeadPlane().getIndex());
            List<McuWaypoint>waypointsToLink =  getWaypointPackage().getWaypointsForLeadPlane();
            
            createWaypointTargetAssociationsWithAttack(getLeadPlane(), waypointsToLink, attackMcuSequence);
        }
    }

    protected void createWaypointTargetAssociationsWithAttack(PlaneMCU plane, List<McuWaypoint>waypointsToLink, AttackMcuSequence attackMcuSequence)
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
                AttackMcuSequence attackMcuSequence = new AttackMcuSequence();
                attackMcuSequence.createAttackArea(getSquadron().determineDisplayName(getCampaign().getDate()), getTargetCoords(), altitude, attackTime);
                attackMcuSequence.createTriggerForPlane(plane, getTargetCoords());
                
                attackMcuSequences.put(plane.getIndex(), attackMcuSequence);
            }
        }
        else
        {
            AttackMcuSequence attackMcuSequence = new AttackMcuSequence();
            attackMcuSequence.createAttackArea(getSquadron().determineDisplayName(getCampaign().getDate()), getTargetCoords(), altitude, attackTime);
            attackMcuSequence.createTriggerForFlight(this, getTargetCoords());
            
            attackMcuSequences.put(getLeadPlane().getIndex(), attackMcuSequence);
        }
    }

    @Override
    public List<BaseFlightMcu> getAllMissionPoints()
    {
        List<BaseFlightMcu> allMissionPoints = new ArrayList<BaseFlightMcu>();
        
        List<McuWaypoint> allWaypoints = this.getAllWaypoints();
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

    public String getMissionObjective() throws PWCGException 
    {
        String objective = "Attack the specified objective using all available means.";
        for (Unit linkedUnit : linkedUnits)
        {
            String objectiveLocation =  getMissionObjectiveLocation(flightInformation.getSquadron(), flightInformation.getCampaign().getDate(), linkedUnit);
            
            if (!linkedUnit.getCountry().isSameSide(this.getCountry()))
            {
                if (linkedUnit instanceof AirfieldStaticGroup)
                {
                    AirfieldStaticGroup target = (AirfieldStaticGroup)linkedUnit;
                    objective = "Attack the airfield at " + target.getAirfield().getName();
                    break;
                }
                else if (linkedUnit instanceof GroundTrainUnit)
                {
                    objective = "Attack the trains and rail facilities " + objectiveLocation;
                    break;
                }
                else if (linkedUnit instanceof GroundUnit)
                {
                    GroundUnit groundUnit = (GroundUnit)linkedUnit;             

                    TacticalTarget targetType = groundUnit.getPwcgGroundUnitInformation().getTargetType();
                    if (targetType == TacticalTarget.TARGET_INFANTRY)
                    {
                        objective = "Attack enemy troops " + objectiveLocation; 
                        break;
                    }
                    if (targetType == TacticalTarget.TARGET_ASSAULT)
                    {
                        objective = "Attack assaulting enemy troops " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_DEFENSE)
                    {
                        objective = "Attack defending enemy troops " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_ARTILLERY)
                    {
                        objective = "Attack the artillery battery " + objectiveLocation; 
                    }
                    else if (targetType == TacticalTarget.TARGET_TRANSPORT)
                    {
                        objective = "Attack the transport and road facilities " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_SHIPPING)
                    {
                        objective = "Attack the shipping " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_BALLOON)
                    {
                        objective = "Attack the balloons " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_DRIFTER)
                    {
                        objective = "Attack the light shipping " + objectiveLocation; 
                        break;
                    }
                    else if (targetType == TacticalTarget.TARGET_TROOP_CONCENTRATION)
                    {
                        objective = "Attack troop concentrations " + objectiveLocation; 
                    }
                }
            }
        }
        
        return objective;
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        super.write(writer);

        for (AttackMcuSequence attackMcuSequence : attackMcuSequences.values())
        {
            attackMcuSequence.write(writer);
        }
    }

    public Map<Integer, AttackMcuSequence> getAttackMcuSequences()
    {
        return attackMcuSequences;
    }

    public void setAttackMcuSequences(Map<Integer, AttackMcuSequence> attackMcuSequences)
    {
        this.attackMcuSequences = attackMcuSequences;
    }

    public int getAttackTime()
    {
        return attackTime;
    }

    public void setAttackTime(int attackTime)
    {
        this.attackTime = attackTime;
    } 
}
