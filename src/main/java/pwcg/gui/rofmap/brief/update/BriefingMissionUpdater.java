package pwcg.gui.rofmap.brief.update;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;

public class BriefingMissionUpdater
{

    public static void finalizeMission(BriefingData briefingContext) throws PWCGException
    {
        Mission mission = briefingContext.getMission();
        if (!mission.isFinalized())
        {
            pushEditsToMission(briefingContext);
            mission.finalizeMission();
            mission.write();

            Campaign campaign = PWCGContext.getInstance().getCampaign();
            campaign.setCurrentMission(mission);
        }
    }

    public static void pushEditsToMission(BriefingData briefingData) throws PWCGException 
    {
        Mission mission = briefingData.getMission();
        if (!mission.isFinalized())
        {
            pushFlightParametersToMission(briefingData);
            pushCrewAndPayloadToMission(briefingData);
            
        }
    }

    private static void pushFlightParametersToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        mission.getMissionOptions().getMissionTime().setMissionTime(briefingData.getSelectedTime());

        for (BriefingFlight briefingFlight : briefingData.getBriefingFlights())
        {
            IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlightForSquadron(briefingFlight.getSquadronId());
            playerFlight.getFlightPlanes().setFuelForFlight(briefingFlight.getSelectedFuel());
            playerFlight.getWaypointPackage().updateWaypointsFromBriefing(briefingFlight.getBriefingFlightParameters().getBriefingMapMapPoints());
        }
    }

    private static void pushCrewAndPayloadToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        for (BriefingFlight briefingFlight : briefingData.getBriefingFlights())
        {
            IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlightForSquadron(briefingFlight.getSquadronId());
            BriefingFlightCrewPlaneUpdater crewePlaneUpdater = new BriefingFlightCrewPlaneUpdater(mission.getCampaign(), playerFlight);
            crewePlaneUpdater.updatePlayerPlanes(briefingFlight.getBriefingAssignmentData().getCrews());
        }
    }
}
