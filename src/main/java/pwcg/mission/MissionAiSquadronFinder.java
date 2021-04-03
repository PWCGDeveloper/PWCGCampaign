package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class MissionAiSquadronFinder
{
    private List<Squadron> alliedSquads = new ArrayList<Squadron>();
    private List<Squadron> axisSquads = new ArrayList<Squadron>();
    private Campaign campaign;
    private Mission mission;

    public MissionAiSquadronFinder(Campaign campaign, Mission mission)
    {
        this.campaign = campaign;
        this.mission = mission;
    }

    public void findAiSquadronsForMission() throws PWCGException
    {
        alliedSquads.clear();
        axisSquads.clear();

        if (mission.getMissionProfile() == MissionProfile.DAY_TACTICAL_MISSION)
        {
            findSquadronsForTacticalMission();
        }
        else if (mission.getMissionProfile() == MissionProfile.NIGHT_TACTICAL_MISSION)
        {
            findSquadronsForNightTacticalMission();
        }
        else if (mission.getMissionProfile() == MissionProfile.DAY_STRATEGIC_MISSION)
        {
            findSquadronsForStrategicMission();
        }
        else if (mission.getMissionProfile() == MissionProfile.NIGHT_STRATEGIC_MISSION)
        {
            findSquadronsForNightStrategicMission();
        }
        else
        {
            throw new PWCGException("No profile for mission");
        }

        determineSquadronAvailability();
    }

    private void findSquadronsForTacticalMission() throws PWCGException
    {
        alliedSquads = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSide(campaign, Side.ALLIED);
        axisSquads = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSide(campaign, Side.AXIS);
    }

    private void findSquadronsForNightTacticalMission() throws PWCGException
    {
        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(Role.ROLE_FIGHTER);
        acceptableRoles.add(Role.ROLE_BOMB);
        acceptableRoles.add(Role.ROLE_ATTACK);

        List<Squadron> otherAlliedSquads = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, acceptableRoles, Side.ALLIED);
        List<Squadron> otherAxisSquads = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, acceptableRoles, Side.AXIS);

        alliedSquads.addAll(otherAlliedSquads);
        axisSquads.addAll(otherAxisSquads);
    }

    private void findSquadronsForStrategicMission() throws PWCGException
    {
        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(Role.ROLE_FIGHTER);
        acceptableRoles.add(Role.ROLE_BOMB);
        acceptableRoles.add(Role.ROLE_STRAT_BOMB);

        List<Squadron> otherAlliedSquads = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, acceptableRoles, Side.ALLIED);
        List<Squadron> otherAxisSquads = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, acceptableRoles, Side.AXIS);

        alliedSquads.addAll(otherAlliedSquads);
        axisSquads.addAll(otherAxisSquads);
    }

    private void findSquadronsForNightStrategicMission() throws PWCGException
    {
        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(Role.ROLE_BOMB);
        acceptableRoles.add(Role.ROLE_STRAT_BOMB);

        List<Squadron> otherAlliedSquads = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, acceptableRoles, Side.ALLIED);
        List<Squadron> otherAxisSquads = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, acceptableRoles, Side.AXIS);

        alliedSquads.addAll(otherAlliedSquads);
        axisSquads.addAll(otherAxisSquads);
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

            if (!shouldFlyDayNight(squadron))
            {
                continue;
            }

            acceptedSquadrons.add(squadron);
        }

        return acceptedSquadrons;
    }

    private boolean shouldFlyDayNight(Squadron squadron) throws PWCGException
    {

        if ((squadron.getNightOdds(campaign.getDate()) <= 0) && (mission.isNightMission()))
        {
            return false;
        }
        else if ((squadron.getNightOdds(campaign.getDate()) >= 100) && (!mission.isNightMission()))
        {
            return false;
        }
        else if (mission.isNightMission())
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll >= squadron.getNightOdds(campaign.getDate()))
            {
                return false;
            }
        }

        return true;
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
