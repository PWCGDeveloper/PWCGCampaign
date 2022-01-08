package pwcg.gui.rofmap.brief.update;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.mission.Mission;
import pwcg.mission.playerunit.PlayerUnit;

public class BriefingUnitUpdater
{

    public static void finalizeMission(BriefingData briefingContext) throws PWCGException
    {
        Mission mission = briefingContext.getMission();
        if (!mission.getFinalizer().isFinalized())
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
        if (!mission.getFinalizer().isFinalized())
        {
            pushUnitParametersToMission(briefingData);
            pushCrewAndPayloadToMission(briefingData);
            pushFuelToMission(briefingData);
            
        }
    }

    private static void pushUnitParametersToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        mission.getMissionOptions().getMissionTime().setMissionTime(briefingData.getMissionTime());

        for (BriefingUnit briefingUnit : briefingData.getBriefingUnits())
        {
            PlayerUnit playerUnit = mission.getUnits().getPlayerUnitForCompany(briefingUnit.getCompanyId());
            playerUnit.getWaypointPackage().updateWaypointsFromBriefing(briefingUnit.getBriefingUnitParameters().getBriefingMapMapPoints());
        }
    }

    private static void pushCrewAndPayloadToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        for (BriefingUnit briefingUnit : briefingData.getBriefingUnits())
        {
            PlayerUnit playerUnit = mission.getUnits().getPlayerUnitForCompany(briefingUnit.getCompanyId());
            BriefingCrewTankUpdater crewePlaneUpdater = new BriefingCrewTankUpdater(mission.getCampaign(), playerUnit);
            crewePlaneUpdater.updatePlayerTanks(briefingUnit.getBriefingAssignmentData().getCrews());
        }
    }
    

    private static void pushFuelToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        mission.getMissionOptions().getMissionTime().setMissionTime(briefingData.getMissionTime());

        for (BriefingUnit briefingUnit : briefingData.getBriefingUnits())
        {
            PlayerUnit playerUnit = mission.getUnits().getPlayerUnitForCompany(briefingUnit.getCompanyId());
            playerUnit.getUnitTanks().setFuelForUnit(briefingUnit.getSelectedFuel());
        }
    }

}
