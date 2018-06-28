package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.Logger;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.Flight;
import pwcg.mission.flight.balloondefense.AiBalloonDefenseFlight;
import pwcg.mission.flight.balloondefense.AmbientBalloonDefensePackage;
import pwcg.mission.flight.balloondefense.BalloonDefenseGroup;

public class MissionBalloons
{
    private List<BalloonDefenseGroup> ambientBalloons = new ArrayList<BalloonDefenseGroup>();


    /**
     * @throws PWCGException 
     * 
     */
    public void createAmbientBalloons(Mission mission) throws PWCGException 
    {
        Campaign campaign = PWCGContextManager.getInstance().getCampaign();
        ConfigManager configManager = campaign.getCampaignConfigManager();

        List<Coordinate> balloonPositions = new ArrayList<Coordinate>();
        for(Flight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            // Only balloon defense flights have a balloon.
            // Balloon bust flights get their balloon from the linked balloon defense flight.
            if (flight instanceof AiBalloonDefenseFlight)
            {
                AiBalloonDefenseFlight balloonDefenseFlight = (AiBalloonDefenseFlight)flight;
                balloonPositions.add(balloonDefenseFlight.getBalloonPosition());
            } 
        }
        
        Squadron squad =  campaign.determineSquadron();
        if (squad.isSquadronThisRole(campaign.getDate(), Role.ROLE_STRAT_BOMB) || 
            squad.isSquadronThisRole(campaign.getDate(), Role.ROLE_SEA_PLANE) || 
            squad.isHomeDefense(campaign.getDate()))
        {
            return;
        }

        // Number of ambient balloons
        int maxAmbientBalloons = configManager.getIntConfigParam(ConfigItemKeys.MaxAmbientBalloonsKey);
        if (maxAmbientBalloons == 0)
        {
            return;
        }

        int numAmbientBalloons = 1 + RandomNumberGenerator.getRandom(maxAmbientBalloons);

        for (int i = 0; i < numAmbientBalloons; ++i)
        {
            try
            {
                // Balloon is for opposite side by default
                Side balloonSide = campaign.determineCountry().getSide().getOppositeSide();

                // First ambient balloon will always be enemy - others might be
                // friendly
                if (i > 0)
                {
                    int roll = RandomNumberGenerator.getRandom(100);
                    if (roll < 50)
                    {
                        balloonSide = campaign.determineCountry().getSide();
                    }
                }

                // Create the balloon
                AmbientBalloonDefensePackage ambientBalloonPackage = new AmbientBalloonDefensePackage();
                BalloonDefenseGroup balloonGroup = ambientBalloonPackage.createPackage(campaign, mission, balloonSide);

                // Reject in duplicate position
                boolean alreadyTaken = false;
                for (Coordinate balloonPosition : balloonPositions)
                {
                    if (MathUtils.calcDist(balloonGroup.getPosition(), balloonPosition) < 2000.0)
                    {
                        alreadyTaken = true;
                    }
                }

                if (!alreadyTaken)
                {
                    ambientBalloons.add(balloonGroup);
                }
            }
            catch (Exception e)
            {
                Logger.logException(e);
            }
        }
    }

    /**
     * @return
     * @throws PWCGException 
     */
    public boolean hasAlliedBalloon() throws PWCGException
    {
        for (BalloonDefenseGroup balloonGroup : ambientBalloons)
        {
            if (balloonGroup.getCountry().getSide() == Side.ALLIED)
            {
                    return true;
            }
        }
        
        return false;
    }

    /**
     * @return
     * @throws PWCGException 
     */
    public boolean hasAxisBalloon() throws PWCGException
    {
        for (BalloonDefenseGroup balloonGroup : ambientBalloons)
        {
            if (balloonGroup.getCountry().getSide() == Side.AXIS)
            {
                return true;
            }
        }
        
        return false;
    }


    /**
     * @return
     */
    public List<BalloonDefenseGroup> getAmbientBalloons()
    {
        return ambientBalloons;
    }
}
