package pwcg.mission;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.CampaignMode;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
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
import pwcg.mission.ground.org.IGroundUnitCollection;

public class MissionBalloons
{
    private List<IGroundUnitCollection> ambientBalloons = new ArrayList<>();

    public void createAmbientBalloons(Mission mission) throws PWCGException 
    {
        if (!shouldMakeAmbientBalloons(mission))
        {
            return;
        }

        List<Coordinate> balloonPositions = generateBalloonPositions(mission);
        makeDefinedNumberOfAmbientBalloons(mission, balloonPositions);
    }

    private boolean shouldMakeAmbientBalloons(Mission mission) throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            return false;
        }

        if (!(mission.getCampaign().getCampaignData().getCampaignMode() == CampaignMode.CAMPAIGN_MODE_SINGLE))
        {
            return false;
        }

        Squadron squad =  mission.getMissionFlightBuilder().getReferencePlayerFlight().getSquadron();
        if (squad.isSquadronThisRole(mission.getCampaign().getDate(), Role.ROLE_STRAT_BOMB) || 
            squad.isSquadronThisRole(mission.getCampaign().getDate(), Role.ROLE_SEA_PLANE) || 
            squad.isHomeDefense(mission.getCampaign().getDate()))
        {
            return false;
        }
        
        return true;
    }

    private List<Coordinate> generateBalloonPositions(Mission mission) throws PWCGException
    {
        List<Coordinate> balloonPositions = new ArrayList<Coordinate>();
        for(Flight flight : mission.getMissionFlightBuilder().getAllAerialFlights())
        {
            if (flight instanceof AiBalloonDefenseFlight)
            {
                AiBalloonDefenseFlight balloonDefenseFlight = (AiBalloonDefenseFlight)flight;
                balloonPositions.add(balloonDefenseFlight.getBalloonPosition());
            } 
        }
        return balloonPositions;
    }
    
    private void makeDefinedNumberOfAmbientBalloons(Mission mission, List<Coordinate> balloonPositions) throws PWCGException
    {
        ConfigManager configManager = mission.getCampaign().getCampaignConfigManager();
        int maxAmbientBalloons = configManager.getIntConfigParam(ConfigItemKeys.MaxAmbientBalloonsKey);
        int numAmbientBalloons = 1 + RandomNumberGenerator.getRandom(maxAmbientBalloons);
        for (int i = 0; i < numAmbientBalloons; ++i)
        {
            try
            {
                Side balloonSide = Side.ALLIED;
                int roll = RandomNumberGenerator.getRandom(100);
                if (roll < 50)
                {
                	balloonSide = Side.AXIS;
                }

                AmbientBalloonDefensePackage ambientBalloonPackage = new AmbientBalloonDefensePackage(mission);
            	Coordinate ambientBalloonReferencePosition = determineAmbientBalloonReferencePosition(mission);
            	IGroundUnitCollection balloonGroup = ambientBalloonPackage.createPackage(balloonSide, ambientBalloonReferencePosition);

                boolean alreadyTaken = isBalloonPositionTaken(balloonPositions, balloonGroup);
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
    
    private Coordinate determineAmbientBalloonReferencePosition(Mission mission)
    {
    	List<Flight> playerFlights = mission.getMissionFlightBuilder().getPlayerFlights();
    	int index = RandomNumberGenerator.getRandom(playerFlights.size());
    	Flight referenceFlight = playerFlights.get(index);
    	Coordinate ambientBalloonReferencePosition = referenceFlight.getPlanes().get(0).getPosition();
    	return ambientBalloonReferencePosition;
    }

	private boolean isBalloonPositionTaken(List<Coordinate> balloonPositions, IGroundUnitCollection balloonGroup) throws PWCGException 
	{
		boolean alreadyTaken = false;
		for (Coordinate balloonPosition : balloonPositions)
		{
		    if (MathUtils.calcDist(balloonGroup.getPosition(), balloonPosition) < 2000.0)
		    {
		        alreadyTaken = true;
		    }
		}
		return alreadyTaken;
	}

    public List<IGroundUnitCollection> getAmbientBalloons()
    {
        return ambientBalloons;
    }
}
