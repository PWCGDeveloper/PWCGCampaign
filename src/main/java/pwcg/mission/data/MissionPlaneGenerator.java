package pwcg.mission.data;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.plane.PlaneMcu;
import pwcg.mission.mcu.group.virtual.VirtualWaypointEscort;

public class MissionPlaneGenerator
{
    private Mission mission;
    private List<PwcgGeneratedMissionPlaneData> missionPlanes = new ArrayList<>();
    
    public MissionPlaneGenerator(Mission mission)
    {
        this.mission = mission;
    }
    
    public List<PwcgGeneratedMissionPlaneData> generateMissionPlaneData() throws PWCGException
    {
        for (IFlight flight : mission.getMissionFlights().getAllAerialFlights())
        {
            makePlaneEntriesForFlight(flight);
            if (flight.getFlightInformation().isVirtual())
            {
                makePlaneEntriesForVirtualEscort(flight);
            }
        }
        
        return missionPlanes;
    }

    private void makePlaneEntriesForVirtualEscort(IFlight flight) throws PWCGException
    {
        VirtualWaypointEscort vwpEscort = flight.getVirtualWaypointPackage().getEscort();
        if (vwpEscort != null)
        {
            makePlaneEntriesForPlanes(vwpEscort.getEscortSquadron(),  vwpEscort.getEscortPlanes());
        }
    }

    private void makePlaneEntriesForFlight(IFlight flight)
    {
        for (PlaneMcu plane : flight.getFlightPlanes().getPlanes())
        {
            makeMissionPlaneEntry(flight.getSquadron(), plane);
        }
    }

    private void makePlaneEntriesForPlanes(Squadron squadron, List<PlaneMcu> planes)
    {
        for (PlaneMcu plane : planes)
        {
            makeMissionPlaneEntry(squadron, plane);
        }
    }

    private void makeMissionPlaneEntry(Squadron squadron, PlaneMcu plane)
    {
        PwcgGeneratedMissionPlaneData missionPlaneData = new PwcgGeneratedMissionPlaneData();
        missionPlaneData.setPilotName(plane.getPilot().getName());
        missionPlaneData.setPilotSerialNumber(plane.getPilot().getSerialNumber());
        missionPlaneData.setPlaneSerialNumber(plane.getSerialNumber());
        missionPlaneData.setSquadronId(squadron.getSquadronId());
        missionPlaneData.setAircraftType(plane.getType());
        
        missionPlanes.add(missionPlaneData);
    }
}
