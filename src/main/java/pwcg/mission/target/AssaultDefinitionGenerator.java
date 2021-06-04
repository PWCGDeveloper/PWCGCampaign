package pwcg.mission.target;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.battle.Battle;
import pwcg.campaign.battle.BattleManager;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.FrontLinesForMap;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.CountryFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.Mission;
import pwcg.mission.ground.BattleSize;

public class AssaultDefinitionGenerator
{
    public static final int DISTANCE_BETWEEN_COMBATANTS = 200;

    private Campaign campaign;
    private Mission mission;
    private List<AssaultDefinition> assaultInformationElements = new ArrayList<>();
    private Coordinate assaultPosition;

    public AssaultDefinitionGenerator(Mission mission, Coordinate assaultPosition)
    {
        this.mission = mission;
        this.assaultPosition = assaultPosition;
        this.campaign = mission.getCampaign();
    }

    public List<AssaultDefinition> generateAssaultDefinition() throws PWCGException
    {
        generateMiniAssaultOnEachIndex();
        return assaultInformationElements;
    }

    private void generateMiniAssaultOnEachIndex() throws PWCGException
    {
        BattleSize battleSize = AssaultBattleSizeGenerator.createAssaultBattleSize(campaign);
        ICountry defendingCountry = getDefendingCountry();
        ICountry assaultingCountry = getAssaultingCountry(defendingCountry);

        List<Coordinate> assaultDefensePositions = getDefensePositions(defendingCountry, battleSize);
        for (Coordinate assaultDefensePosition : assaultDefensePositions)
        {
            completeBattleDefinition(defendingCountry, assaultingCountry, battleSize, assaultDefensePosition);
        }
    }

    private ICountry getDefendingCountry() throws PWCGException
    {
        ICountry defendingCountry = getDefendingCountryFromSkirmish();
        if (defendingCountry.getCountry() == Country.NEUTRAL)
        {
            defendingCountry = getDefendingCountryByMapCircumstances(assaultPosition);
        }
        return defendingCountry;
    }
    
    private ICountry getDefendingCountryFromSkirmish()
    {
        if (mission.getSkirmish() != null)
        {
            Side defendingSide =  mission.getSkirmish().getAttackerGround().getOppositeSide();
            ICountry defendingCountry = CountryFactory.makeMapReferenceCountry(defendingSide);
            return defendingCountry;
        }
        
        return CountryFactory.makeCountryByCountry(Country.NEUTRAL);
    }

    private ICountry getDefendingCountryByMapCircumstances(Coordinate battleLocation) throws PWCGException
    {
        BattleManager battleManager = PWCGContext.getInstance().getCurrentMap().getBattleManager();
        Battle battle = battleManager.getBattleForCampaign(PWCGContext.getInstance().getCurrentMap().getMapIdentifier(), battleLocation,
                campaign.getDate());
        if (battle != null)
        {
            int roll = RandomNumberGenerator.getRandom(100);
            if (roll < 80)
            {
                return CountryFactory.makeCountryByCountry(battle.getDefendercountry());
            }
            else
            {
                return CountryFactory.makeCountryByCountry(battle.getAggressorcountry());
            }
        }
        
        return chooseSidesRandom();
    }

    private ICountry chooseSidesRandom() throws PWCGException
    {
        ICountry alliedCountry = CountryFactory.makeAssaultProximityCountry(Side.ALLIED, assaultPosition, campaign.getDate());
        ICountry axisCountry = CountryFactory.makeAssaultProximityCountry(Side.AXIS, assaultPosition, campaign.getDate());

        int roll = RandomNumberGenerator.getRandom(100);
        if (roll < 50)
        {
            return alliedCountry;
        }
        else
        {
            return axisCountry;
        }
    }

    private ICountry getAssaultingCountry(ICountry defendingCountry) throws PWCGException
    {
        if (defendingCountry.getSide() == Side.ALLIED)
        {
            ICountry axisCountry = CountryFactory.makeAssaultProximityCountry(Side.AXIS, assaultPosition, campaign.getDate());
            return axisCountry;
        }
        else
        {
            ICountry alliedCountry = CountryFactory.makeAssaultProximityCountry(Side.ALLIED, assaultPosition, campaign.getDate());
            return alliedCountry;
        }
    }


    private void completeBattleDefinition(ICountry defendingCountry, ICountry assaultingCountry, BattleSize battleSize, Coordinate defenseCoordinate) throws PWCGException
    {
        Coordinate assaultCoordinate = getAssaultPositionAcrossFromAssaultingUnit(assaultingCountry.getSide(), defenseCoordinate);

        AssaultDefinition assaultDefinition = new AssaultDefinition();
        assaultDefinition.setAssaultPosition(assaultCoordinate);
        assaultDefinition.setDefensePosition(defenseCoordinate);
        assaultDefinition.setBattleSize(battleSize);
        assaultDefinition.setAssaultingCountry(assaultingCountry);
        assaultDefinition.setDefendingCountry(defendingCountry);

        assaultInformationElements.add(assaultDefinition);
    }

    private List<Coordinate> getDefensePositions(ICountry defendingCountry, BattleSize battleSize) throws PWCGException
    {
        FrontLinesForMap frontLines = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        int centerIndex = frontLines.findIndexForClosestPosition(assaultPosition, defendingCountry.getSide());

        int numAssaultSegments = AssaultDefinitionRange.determineNumberOfAssaultSegments(battleSize);

        List<Coordinate> assaultSegmentDefensePositions = new ArrayList<>();
        if (numAssaultSegments == 1)
        {
            Coordinate assaultSegmentDefensePosition = frontLines.getCoordinates(centerIndex, defendingCountry.getSide());
            assaultSegmentDefensePositions.add(assaultSegmentDefensePosition);
        }
        else
        {
            int startFrontIndex = centerIndex - numAssaultSegments;
            for (int i = 0; i < numAssaultSegments; ++i)
            {
                int assaultSegmentIndex = startFrontIndex + (i * 2);
                Coordinate assaultSegmentDefensePosition = frontLines.getCoordinates(assaultSegmentIndex, defendingCountry.getSide());
                assaultSegmentDefensePositions.add(assaultSegmentDefensePosition);
            }
        }

        return assaultSegmentDefensePositions;
    }

    private Coordinate getAssaultPositionAcrossFromAssaultingUnit(Side assaultingSide, Coordinate defensePosition) throws PWCGException
    {
        FrontLinesForMap frontLines = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate assaultPosition = frontLines.findClosestFrontCoordinateForSide(defensePosition, assaultingSide);

        double angleFromDefensePosition = MathUtils.calcAngle(defensePosition, assaultPosition);
        Coordinate assaultStartPosition = MathUtils.calcNextCoord(defensePosition, angleFromDefensePosition, DISTANCE_BETWEEN_COMBATANTS);
        return assaultStartPosition;
    }
}
