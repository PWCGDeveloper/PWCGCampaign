package pwcg.mission.flight.waypoint.virtual;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.Mission;
import pwcg.mission.flight.FlightInformation;
import pwcg.mission.flight.FlightTypes;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.escort.EscortSquadronSelector;
import pwcg.mission.flight.escort.VirtualEscortFlightInformationBuilder;
import pwcg.mission.flight.waypoint.missionpoint.IVirtualActivate;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointSetType;
import pwcg.mission.mcu.group.virtual.IVirtualWaypoint;
import pwcg.mission.mcu.group.virtual.VirtualWaypoint;
import pwcg.mission.mcu.group.virtual.VirtualWaypointEscort;

public class VirtualWaypointPackage implements IVirtualWaypointPackage
{
    private IFlight flight;
    private List<VirtualWaypoint> virtualWaypoints = new ArrayList<>();

    public VirtualWaypointPackage(IFlight flight)
    {
        this.flight = flight;
    }

    @Override   
    public void buildVirtualWaypoints() throws PWCGException    
    {   
        generateVirtualWaypoints(); 
        linkVirtualWaypointToMissionBegin();
        if (FlightTypes.isFlightWithTargetArea(flight.getFlightType()))
        {
            VirtualWaypointFlightResolver.resolveForAttackFlight(flight, this);
        }
        VirtualWaypointFlightResolver.resolveLanding(flight, this);
    }   

    @Override
    public void addDelayForPlayerDelay(Mission mission) throws PWCGException
    {
        VirtualAdditionalTimeCalculator additionalTimeCalculator = new VirtualAdditionalTimeCalculator();
        int additionalTime = additionalTimeCalculator.addDelayForPlayerDelay(mission, flight);
        if (additionalTime > 30)
        {
            virtualWaypoints.get(0).addAdditionalTime(additionalTime);
        }
    }

    @Override
    public void addEscort() throws PWCGException
    {
        Squadron friendlyFighterSquadron = EscortSquadronSelector.getEscortSquadron(flight.getCampaign(), flight.getSquadron(), flight.getMission().getMissionSquadronRegistry());
        if (friendlyFighterSquadron != null)
        {
            FlightInformation vwpEscortFlightInformation = VirtualEscortFlightInformationBuilder.buildVirtualEscortFlightInformation(flight.getMission(), friendlyFighterSquadron);
            for (VirtualWaypoint virtualWaypoint : virtualWaypoints)
            {
                virtualWaypoint.addEscort(vwpEscortFlightInformation);
            }
            
            flight.getMission().getMissionSquadronRegistry().registerSquadronForUse(friendlyFighterSquadron);
        }
        else
        {
            PWCGLogger.log(LogLevel.DEBUG, "No escort available for virtual flight");
        }
    }

    @Override
    public VirtualWaypointEscort getEscort() throws PWCGException
    {
        if (!virtualWaypoints.isEmpty())
        {
            return virtualWaypoints.get(0).getVwpEscort();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void write(BufferedWriter writer) throws PWCGException 
    {
        for (IVirtualWaypoint virtualWaypoint : virtualWaypoints)
        {
            virtualWaypoint.write(writer);
        }
    }

    @Override
    public List<VirtualWaypoint> getVirtualWaypoints()
    {
        return this.virtualWaypoints;
    }    

    private void generateVirtualWaypoints() throws PWCGException
    {
        VirtualWaypointGenerator virtualWaypointGenerator = new VirtualWaypointGenerator(flight);
        virtualWaypoints = virtualWaypointGenerator.createVirtualWaypoints();
    }

    private void linkVirtualWaypointToMissionBegin() throws PWCGException   
    {   
        VirtualWaypoint firstVirtualWayPoint = virtualWaypoints.get(0);        
        IVirtualActivate activateMissionPointSet = (IVirtualActivate)flight.getWaypointPackage().getMissionPointSet(MissionPointSetType.MISSION_POINT_SET_ACTIVATE);
        activateMissionPointSet.linkMissionBeginToFirstVirtualWaypoint(firstVirtualWayPoint.getEntryPoint());
    }
}
