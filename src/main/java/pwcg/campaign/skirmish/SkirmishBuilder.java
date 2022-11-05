package pwcg.campaign.skirmish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.MissionHumanParticipants;

public class SkirmishBuilder
{
    private Campaign campaign;
    private MissionHumanParticipants participatingPlayers;

    public SkirmishBuilder(Campaign campaign, MissionHumanParticipants participatingPlayers)
    {
        this.campaign = campaign;
        this.participatingPlayers = participatingPlayers;
    }

    public Skirmish chooseBestSkirmish() throws PWCGException
    {
        SkirmishManager skirmishManager = PWCGContext.getInstance().getMap(campaign.getCampaignMap()).getSkirmishManager();
        List<Skirmish> skirmishes = skirmishManager.getSkirmishesForDate(campaign, participatingPlayers);
        if (skirmishes.isEmpty())
        {
            return null;
        }

        return chooseBestAvailableSkirmish(skirmishes);
    }

    private Skirmish chooseBestAvailableSkirmish(List<Skirmish> skirmishes) throws PWCGException
    {
        List<Skirmish> candidateSkirmishes = new ArrayList<>();
        if (campaign.getDate().equals(DateUtils.getDateYYYYMMDD("19450101")))
        {
            candidateSkirmishes = getClosestAirfieldsForBodenplatte(skirmishes);
        }
        else
        {
            candidateSkirmishes = getInRangeSkirmishes(skirmishes);
        }

        if (!candidateSkirmishes.isEmpty())
        {
            int selected = RandomNumberGenerator.getRandom(candidateSkirmishes.size());
            return candidateSkirmishes.get(selected);
        }

        return null;
    }

    private List<Skirmish> getClosestAirfieldsForBodenplatte(List<Skirmish> skirmishes) throws PWCGException
    {
        Skirmish closestSkirmish = null;
        double closestDistance = 0;
        for (Skirmish skirmish : skirmishes)
        {
            double distance = TargetDistance.calculateDistance(campaign, participatingPlayers.getAllParticipatingPlayers().get(0), skirmish.getCenter());
            if ((closestSkirmish == null) || (distance <  closestDistance))
            {
                closestSkirmish = skirmish;
                closestDistance = distance;
            }

        }
        
        if (closestSkirmish == null)
        {
            throw new PWCGException("Could not find bodenplatte skirmish on Jan 1 1945");
        }
        
        return Arrays.asList(closestSkirmish);
    }

    private List<Skirmish> getInRangeSkirmishes(List<Skirmish> skirmishes) throws PWCGException
    {
        List<Skirmish> candidateSkirmishes = new ArrayList<>();
        for (Skirmish skirmish : skirmishes)
        {
            if(TargetDistance.isWithinRange(campaign, participatingPlayers, skirmish.getCenter()))
            {
                candidateSkirmishes.add(skirmish);
            }
        }
        return candidateSkirmishes;
    }
}
