package pwcg.mission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.CoordinateBox;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionSquadronFinder
{
    private List<Squadron> alliedSquads = new ArrayList<Squadron>();
    private List<Squadron> axisSquads = new ArrayList<Squadron>();
    private Campaign campaign;
    private Mission mission;

    public MissionSquadronFinder(Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }

    public void findAiSquadronsForSinglePlayer() throws PWCGException
    {
        if (mission.getMissionProfile() == MissionProfile.DAY_TACTICAL_MISSION)
        {
            findSquadronsFoDayTacticalMission();
        }
        if (mission.getMissionProfile() == MissionProfile.DAY_STRATEGIC_MISSION)
        {
            // TODO make night tactical
            findSquadronsFoDayTacticalMission();
        }
        else if (mission.getMissionProfile() == MissionProfile.DAY_STRATEGIC_MISSION)
        {
            // TODO make day strategic
            findSquadronsForStrategicMission();
        }
        else if (mission.getMissionProfile() == MissionProfile.NIGHT_STRATEGIC_MISSION)
        {
            // TODO make night strategic
            findSquadronsForNightStrategicMission();
        }
        else if (mission.getMissionProfile() == MissionProfile.SEA_PLANE_MISSION)
        {
            findSquadronsForSeaPlaneMission(campaign.getDate());
        }
        else
        {
            throw new PWCGException("No profile for mission");
        }
    }

    private void findSquadronsFoDayTacticalMission() throws PWCGException
    {
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();

        int initialSquadronSearchRadiusKey = configManager.getIntConfigParam(ConfigItemKeys.InitialSquadronSearchRadiusKey);

        PWCGMap map = PWCGContextManager.getInstance().getCurrentMap();
        FrontLinesForMap frontLinesForMap = map.getFrontLinesForMap(campaign.getDate());

        CoordinateBox missionBorders = mission.getMissionFlightBuilder().getMissionBorders(5000);
        Coordinate closestAlliedFrontPosition = frontLinesForMap.findClosestFrontCoordinateForSide(missionBorders.getCenter(), Side.ALLIED);
        alliedSquads = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsBySide(campaign, closestAlliedFrontPosition, 5,
                        initialSquadronSearchRadiusKey, Side.ALLIED, campaign.getDate());

        Coordinate closestAxisFrontPosition = frontLinesForMap.findClosestFrontCoordinateForSide(missionBorders.getCenter(), Side.AXIS);
        axisSquads = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsBySide(campaign, closestAxisFrontPosition, 5,
                        initialSquadronSearchRadiusKey, Side.AXIS, campaign.getDate());
        determineSquadronAvailability();
    }

    private void findSquadronsForSeaPlaneMission(Date date) throws PWCGException
    {
        alliedSquads.clear();
        axisSquads.clear();

        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();

        // Get the Allied lines and pick the northern most position
        Coordinate seaFrontPosition = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate()).getCoordinates(0,
                        Side.ALLIED);

        // Add each sea plane squadron multiple times to increase the number of
        // sea plane missions
        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(Role.ROLE_SEA_PLANE);

        List<Squadron> alliedSeaPlaneSquads = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(campaign,
                        seaFrontPosition.copy(), 1, 400000.0, acceptableRoles, Side.ALLIED, date);
        List<Squadron> axisSeaPlaneSquads = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(campaign,
                        seaFrontPosition.copy(), 1, 400000.0, acceptableRoles, Side.AXIS, date);

        for (Squadron alliedSeaPlaneSquad : alliedSeaPlaneSquads)
        {
            for (int i = 0; i < 3; ++i)
            {
                alliedSquads.add(alliedSeaPlaneSquad);
            }
        }

        for (Squadron axisSeaPlaneSquad : axisSeaPlaneSquads)
        {
            for (int i = 0; i < 3; ++i)
            {
                axisSquads.add(axisSeaPlaneSquad);
            }
        }

        int initialSquadronSearchRadiusKey = configManager.getIntConfigParam(ConfigItemKeys.InitialSquadronSearchRadiusKey);

        // All missions are available for sea planes although most will be
        // rejected due to lack of proximity
        acceptableRoles = Role.getAllRoles();

        List<Squadron> otherAlliedSquads = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(campaign, seaFrontPosition,
                        3, initialSquadronSearchRadiusKey, acceptableRoles, Side.ALLIED, date);

        List<Squadron> otherAxisSquads = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(campaign, seaFrontPosition,
                        3, initialSquadronSearchRadiusKey, acceptableRoles, Side.AXIS, date);

        alliedSquads.addAll(otherAlliedSquads);
        axisSquads.addAll(otherAxisSquads);
        determineSquadronAvailability();
    }

    private void findSquadronsForStrategicMission() throws PWCGException
    {
        alliedSquads.clear();
        axisSquads.clear();

        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();

        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.clear();
        acceptableRoles.add(Role.ROLE_FIGHTER);

        FrontLinesForMap frontLinesForMap = PWCGContextManager.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        int initialSquadronSearchRadiusKey = configManager.getIntConfigParam(ConfigItemKeys.InitialSquadronSearchRadiusKey);

        CoordinateBox missionBorders = mission.getMissionFlightBuilder().getMissionBorders(5000);
        Coordinate closestAlliedFrontPosition = frontLinesForMap.findClosestFrontCoordinateForSide(missionBorders.getCenter(), Side.ALLIED);
        List<Squadron> otherAlliedSquads = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(campaign,
                        closestAlliedFrontPosition, 3, initialSquadronSearchRadiusKey, acceptableRoles, Side.ALLIED, campaign.getDate());

        Coordinate closestAxisFrontPosition = frontLinesForMap.findClosestFrontCoordinateForSide(missionBorders.getCenter(), Side.AXIS);
        List<Squadron> otherAxisSquads = PWCGContextManager.getInstance().getSquadronManager().getNearestSquadronsByRole(campaign,
                        closestAxisFrontPosition, 3, initialSquadronSearchRadiusKey, acceptableRoles, Side.AXIS, campaign.getDate());

        alliedSquads.addAll(otherAlliedSquads);
        axisSquads.addAll(otherAxisSquads);
        determineSquadronAvailability();
    }

    private void findSquadronsForNightStrategicMission()
    {
        alliedSquads.clear();
        axisSquads.clear();
    }

    private void determineSquadronAvailability() throws PWCGException
    {
        alliedSquads = filterSquadrons(alliedSquads);
        axisSquads = filterSquadrons(axisSquads);
    }

    private List<Squadron> filterSquadrons(List<Squadron> squadronsToBeEvaluated) throws PWCGException
    {
        List<Squadron> acceptedSquadrons = new ArrayList<>();
        for (Squadron squadron : squadronsToBeEvaluated)
        {
            if (Squadron.isPlayerSquadron(campaign, squadron.getSquadronId()))
            {
                continue;
            }

            if (isSquadronPersonnelDepleted(squadron))
            {
                continue;
            }

            acceptedSquadrons.add(squadron);
        }

        return acceptedSquadrons;
    }

    private boolean isSquadronPersonnelDepleted(Squadron squadron) throws PWCGException
    {
        SquadronMembers squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAcesNoWounded(campaign.getPersonnelManager()
                        .getSquadronPersonnel(squadron.getSquadronId()).getSquadronMembersWithAces().getSquadronMemberCollection(),
                        campaign.getDate());
        int numSquadronMembers = squadronMembers.getSquadronMemberList().size();
        if (numSquadronMembers < 6)
        {
            return true;
        }

        if (numSquadronMembers > 10)
        {
            return false;
        }

        int diceRoll = RandomNumberGenerator.getRandom(10);
        if (numSquadronMembers < diceRoll)
        {
            return true;
        }

        return false;
    }

    public List<Squadron> getAlliedSquads()
    {
        return alliedSquads;
    }

    public List<Squadron> getAxisSquads()
    {
        return axisSquads;
    }
}
