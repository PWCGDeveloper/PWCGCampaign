package pwcg.gui.rofmap.brief.updater;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.utils.AutoStart;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.PlayerFlightEditor;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingFlight;
import pwcg.mission.Mission;
import pwcg.mission.flight.IFlight;
import pwcg.mission.io.MissionFileWriter;

public class BriefingMissionUpdater
{

    public static void finalizeMission(BriefingData briefingContext) throws PWCGException
    {
        Mission mission = briefingContext.getMission();
        if (!mission.isFinalized())
        {
            updateMissionBriefingParameters();
            mission.finalizeMission();
            mission.write();

            Campaign campaign = PWCGContext.getInstance().getCampaign();
            campaign.setCurrentMission(mission);
        }
    }

    public static void updateMissionBriefingParameters(BriefingData briefingContext) throws PWCGException 
    {
        Mission mission = briefingContext.getMission();
        if (!mission.isFinalized())
        {
            for (int squadronIdForFlight : briefingMissionFlights.keySet())
            {
                IFlight playerFlight = mission.getMissionFlightBuilder().getPlayerFlightForSquadron(squadronIdForFlight);
                        
                BriefingFlight briefingMissionHandler = briefingMissionFlights.get(squadronIdForFlight);
                        
                playerFlight.getWaypointPackage().updateWaypoints(briefingMissionHandler.getBriefingFlightParameters().getWaypointsInBriefing());
                playerFlight.getFlightPlanes().setFuel(briefingMissionHandler.getBriefingFlightParameters().getSelectedFuel());
            }
            
            PWCGContext.getInstance().getCurrentMap().getMissionOptions().getMissionTime().setMissionTime(selectedTime);
        }
    }
}
