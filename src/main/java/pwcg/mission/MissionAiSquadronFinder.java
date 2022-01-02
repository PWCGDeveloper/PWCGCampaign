package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PwcgRole;
import pwcg.campaign.squadron.Company;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class MissionAiSquadronFinder
{
    private List<Company> alliedSquads = new ArrayList<Company>();
    private List<Company> axisSquads = new ArrayList<Company>();
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
        List<PwcgRole> acceptableRoles = new ArrayList<PwcgRole>();
        acceptableRoles.add(PwcgRole.ROLE_FIGHTER);
        acceptableRoles.add(PwcgRole.ROLE_BOMB);
        acceptableRoles.add(PwcgRole.ROLE_ATTACK);
        acceptableRoles.add(PwcgRole.ROLE_TRANSPORT);

        List<Company> otherAlliedSquads = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, acceptableRoles, Side.ALLIED);
        List<Company> otherAxisSquads = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, acceptableRoles, Side.AXIS);

        alliedSquads.addAll(otherAlliedSquads);
        axisSquads.addAll(otherAxisSquads);
    }

    private void determineSquadronAvailability() throws PWCGException
    {
        alliedSquads = filterSquadrons(alliedSquads);
        axisSquads = filterSquadrons(axisSquads);
    }

    private List<Company> filterSquadrons(List<Company> squadronsToBeEvaluated) throws PWCGException
    {
        List<Company> acceptedSquadrons = new ArrayList<>();
        for (Company squadron : squadronsToBeEvaluated)
        {
            if (Company.isPlayerSquadron(campaign, squadron.getSquadronId()))
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

    private boolean shouldFlyDayNight(Company squadron) throws PWCGException
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
            if (squadron.getNightOdds(campaign.getDate()) >= 10)
            {
                return true;
            }
            
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll >= squadron.getNightOdds(campaign.getDate()))
            {
                return false;
            }
        }

        return true;
    }

    public List<Company> getAlliedSquads()
    {
        return alliedSquads;
    }

    public List<Company> getAxisSquads()
    {
        return axisSquads;
    }
}
