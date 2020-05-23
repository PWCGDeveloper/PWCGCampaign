package pwcg.mission;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.config.ConfigSimple;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.ground.builder.BalloonUnitBuilder;
import pwcg.mission.ground.org.IGroundUnitCollection;
import pwcg.mission.target.TargetDefinition;
import pwcg.mission.target.TargetType;
import pwcg.mission.target.locator.TargetLocationFinder;

public class MissionBalloonBuilder
{
    private List<IGroundUnitCollection> missionBalloons = new ArrayList<>();
    private Mission mission;

    public MissionBalloonBuilder(Mission mission)
    {
        this.mission = mission;
    }
    
    public List<IGroundUnitCollection> createMissionBalloons() throws PWCGException 
    {
        if (shouldMakeBalloons())
        {
            makeBalloons();
        }
        return missionBalloons;
    }

    public int getUnitCount() 
    {
        int missionBalloonUnitCount = 0;
        for (IGroundUnitCollection groundUnitCollection : missionBalloons)
        {
            missionBalloonUnitCount += groundUnitCollection.getUnitCount();
            System.out.println("Unit count balloon : " + groundUnitCollection.getUnitCount());
        }
        return missionBalloonUnitCount;
    }

    private boolean shouldMakeBalloons() throws PWCGException
    {
        if (PWCGContext.getProduct() == PWCGProduct.BOS)
        {
            return false;
        }

        return true;
    }

    private void makeBalloons() throws PWCGException
    {
        makeBalloonsForSide(Side.ALLIED);
        makeBalloonsForSide(Side.AXIS);
    }
    
    private void makeBalloonsForSide(Side balloonSide) throws PWCGException
    {
        int numBalloons = detemineNumberBalloonsPerSide();
        ICountry balloonCountry = PWCGContext.getInstance().getCurrentMap().getGroundCountryForMapBySide(balloonSide);
        for (int i = 0; i < numBalloons; ++i)
        {
            try
            {
                Coordinate balloonPosition = determineBalloonPosition(balloonSide);
                boolean alreadyTaken = isBalloonPositionTaken(balloonPosition);
                if (!alreadyTaken)
                {
                    TargetDefinition targetDefinition = new TargetDefinition(TargetType.TARGET_BALLOON, balloonPosition, balloonCountry);
                    
                    BalloonUnitBuilder groundUnitBuilderBalloonDefense = new BalloonUnitBuilder(mission, targetDefinition);
                    IGroundUnitCollection balloonGroup = groundUnitBuilderBalloonDefense.createBalloonUnit();
                    mission.getMissionGroundUnitManager().registerBalloon(balloonGroup.getPrimaryGroundUnit());
                    missionBalloons.add(balloonGroup);
                }
            }
            catch (Exception e)
            {
                PWCGLogger.logException(e);
            }
        }
    }
    
    public int detemineNumberBalloonsPerSide() throws PWCGException 
    {
        int numBalloonsPerSide = 1;      
        ConfigManagerCampaign configManager = mission.getCampaign().getCampaignConfigManager();
        String currentGroundSetting = configManager.getStringConfigParam(ConfigItemKeys.SimpleConfigGroundKey);
        if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_LOW))
        {
            numBalloonsPerSide = 1;      
        }
        else if (currentGroundSetting.equals(ConfigSimple.CONFIG_LEVEL_MED))
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 60)
            {
                numBalloonsPerSide = 1;
            }
            else
            {
                numBalloonsPerSide = 2;
            }
        }
        else
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 30)
            {
                numBalloonsPerSide = 1;
            }
            else if (roll < 70)
            {
                numBalloonsPerSide = 2;
            }
            else
            {
                numBalloonsPerSide = 3;
            }
        }

        return numBalloonsPerSide;
    }

    
    private Coordinate determineBalloonPosition(Side balloonSide) throws PWCGException
    {
        int attemptCount = 0;
        Coordinate balloonCoordinate;
        
        do
        {
            Coordinate frontCoordinate = getFrontCoordinate(balloonSide);
            FrontLinesForMap frontLines = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(mission.getCampaign().getDate());
            balloonCoordinate = frontLines.findPositionBehindLinesForSide(frontCoordinate, 5000, 3000, 6000, balloonSide);
            
            ++attemptCount;
        } 
        while (isBalloonPositionTaken(balloonCoordinate) && attemptCount < 10);
        
        return balloonCoordinate;
    }
    
    private Coordinate getFrontCoordinate(Side balloonSide) throws PWCGException
    {
        Coordinate missionCenter = mission.getMissionBorders().getCenter();
        double missionRadius = mission.getMissionBorders().getAreaRadius();
        TargetLocationFinder targetLocationFinder = new TargetLocationFinder(mission.getCampaign(), balloonSide, missionCenter, missionRadius);
        Coordinate targetWaypoint = targetLocationFinder.findLocationAtFront();
        return targetWaypoint;
    }

	private boolean isBalloonPositionTaken(Coordinate requestedBalloonPosition) throws PWCGException 
	{
		return mission.getMissionGroundUnitManager().isBalloonPositionInUse(requestedBalloonPosition);
	}

    public void write(BufferedWriter writer) throws PWCGException
    {
        for (IGroundUnitCollection missionBalloon : missionBalloons)
        {
            missionBalloon.write(writer);
        }
    }
}
