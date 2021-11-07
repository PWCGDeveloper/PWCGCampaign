package pwcg.mission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.virtual.IVirtualWaypointPackage;
import pwcg.mission.mcu.group.virtual.IVirtualWaypoint;
import pwcg.mission.utils.AiAdjuster;

public class MissionFlightFinalizer
{
    private Campaign campaign;
    private Mission mission;
    
    public MissionFlightFinalizer (Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }
    
    public void finalizeMissionFlights() throws PWCGException 
    {
        convertForCoop();
        finalizeFlights();        
        addVirtualEscorts();        
        adjustAI();        
        setCzTriggers();
    }

    private void convertForCoop() throws PWCGException, PWCGException
    {
        if (campaign.isCoop())
        {
            MissionCoopConverter coopConverter = new MissionCoopConverter();
            coopConverter.convertToCoop(mission);
        }
    }

    private void finalizeFlights() throws PWCGException
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            flight.finalizeFlight();
        }
    }

    private void addVirtualEscorts() throws PWCGException
    {
        List<IFlight> shuffledFlights = new ArrayList<>();
        if (mission.getFlights().hasPlayerFlightForSide(Side.AXIS))
        {
            shuffledFlights.addAll(mission.getFlights().getAiFlightsForSide(Side.ALLIED));
        }
        
        if (mission.getFlights().hasPlayerFlightForSide(Side.ALLIED))
        {
            shuffledFlights.addAll(mission.getFlights().getAiFlightsForSide(Side.AXIS));
        }
        
        Collections.shuffle(shuffledFlights);
        for (IFlight flight : shuffledFlights)
        {
            boolean needsVirtualEscort = flight.getMission().getVirtualEscortHandler().needsEscort(flight);
            if (needsVirtualEscort)
            {
                flight.addVirtualEscort();
            }
        }        
    }

    private void adjustAI() throws PWCGException
    {
        AiAdjuster aiAdjuster = new AiAdjuster(campaign);
        aiAdjuster.adjustAI(mission);
    }

    private void setCzTriggers() throws PWCGException  
    {
        for (IFlight flight : mission.getFlights().getAllAerialFlights())
        {
            triggerOtherFlightCZFromPlayerFlight(flight);
        }
    }

    private void triggerOtherFlightCZFromPlayerFlight(IFlight flight) throws PWCGException 
    {
        if (flight.getFlightInformation().isVirtual())
        {
            IVirtualWaypointPackage virtualWaypointPackage = flight.getVirtualWaypointPackage();
            for (IVirtualWaypoint vwp : virtualWaypointPackage.getVirtualWaypoints())
            {
                if (vwp != null)
                {
                    for (int planeIndex : flight.getMission().getFlights().determinePlayerPlaneIds())
                    {
                        vwp.setVwpTriggerObject(planeIndex);
                    }
                    
                    for (int truckIndex : mission.getMissionAAATrucks().getPlayerVehicleIds())
                    {
                        vwp.setVwpTriggerObject(truckIndex);
                    }
                }
            }
        }
    }
}
