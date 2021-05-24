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

    protected Campaign campaign;
    protected Mission mission;
    protected List<AssaultDefinition> assaultInformationElements = new ArrayList<>();

    public AssaultDefinitionGenerator(Mission mission)
    {
        this.mission = mission;
        this.campaign = mission.getCampaign();
    }

    public List<AssaultDefinition> generateAssaultDefinition(Coordinate battleLocation) throws PWCGException
    {
        generateMiniAssaultOnEachIndex(battleLocation);
        return assaultInformationElements;
    }

    private void generateMiniAssaultOnEachIndex(Coordinate battleLocation) throws PWCGException
    {
        BattleSize battleSize = AssaultBattleSizeGenerator.createAssaultBattleSize(campaign);
        ICountry defendingCountry = getDefendingCountry(battleLocation);
        ICountry assaultingCountry = getAssaultingCountry(defendingCountry);

        List<Integer> battleFrontIndeces = getFrontBattleIndeces(defendingCountry, battleLocation, battleSize);
        FrontLinesForMap frontLineMarker = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        for (int battleIndex : battleFrontIndeces)
        {
            completeBattleDefinition(defendingCountry, assaultingCountry, battleSize, battleIndex, frontLineMarker);
        }
    }

    private ICountry getDefendingCountry(Coordinate battleLocation)
    {
        ICountry defendingCountry = getDefendingCountryFromSkirmish();
        if (defendingCountry.getCountry() == Country.NEUTRAL)
        {
            defendingCountry = getDefendingCountryByMapCircumstances(battleLocation);
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

    private ICountry getDefendingCountryByMapCircumstances(Coordinate battleLocation)
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

    private ICountry chooseSidesRandom()
    {
        ICountry alliedCountry = CountryFactory.makeMapReferenceCountry(Side.ALLIED);
        ICountry axisCountry = CountryFactory.makeMapReferenceCountry(Side.AXIS);

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
    

    private ICountry getAssaultingCountry(ICountry defendingCountry)
    {
        if (defendingCountry.getSide() == Side.ALLIED)
        {
            ICountry axisCountry = CountryFactory.makeMapReferenceCountry(Side.AXIS);
            return axisCountry;
        }
        else
        {
            ICountry alliedCountry = CountryFactory.makeMapReferenceCountry(Side.ALLIED);
            return alliedCountry;
        }
    }


    private void completeBattleDefinition(ICountry defendingCountry, ICountry assaultingCountry, BattleSize battleSize, int battleIndex,
            FrontLinesForMap frontLineMarker) throws PWCGException
    {
        Coordinate defenseCoordinate = frontLineMarker.getCoordinates(battleIndex, defendingCountry.getSide());
        Coordinate assaultCoordinate = getAssaultPositionAcrossFromAssaultingUnit(assaultingCountry.getSide(), defenseCoordinate);

        AssaultDefinition assaultDefinition = new AssaultDefinition();
        assaultDefinition.setAssaultPosition(assaultCoordinate);
        assaultDefinition.setDefensePosition(defenseCoordinate);
        assaultDefinition.setBattleSize(battleSize);
        assaultDefinition.setAssaultingCountry(assaultingCountry);
        assaultDefinition.setDefendingCountry(defendingCountry);

        assaultInformationElements.add(assaultDefinition);
    }

    private List<Integer> getFrontBattleIndeces(ICountry defendingCountry, Coordinate battleLocation, BattleSize battleSize) throws PWCGException
    {
        List<Integer> battleFrontIndeces = new ArrayList<>();
        int centerFrontIndex = AssaultDefinitionRange.determineCenterOfBattle(campaign, defendingCountry, battleLocation);
        int numAssaults = AssaultDefinitionRange.determineNumberOfAssaultSegments(battleSize, centerFrontIndex);

        int startFrontIndex = centerFrontIndex;
        int finishFrontIndex = centerFrontIndex;
        if (numAssaults > 1)
        {
            startFrontIndex = centerFrontIndex - (numAssaults * 3);
            finishFrontIndex = startFrontIndex + (numAssaults * 3);
        }
        
        for (int battleIndex = startFrontIndex; battleIndex <= finishFrontIndex; ++battleIndex)
        {
            battleFrontIndeces.add(battleIndex);
        }

        return battleFrontIndeces;
    }

    private Coordinate getAssaultPositionAcrossFromAssaultingUnit(Side assaultingSide, Coordinate defensePosition) throws PWCGException
    {
        FrontLinesForMap frontLineMarker = PWCGContext.getInstance().getCurrentMap().getFrontLinesForMap(campaign.getDate());
        Coordinate assaultPosition = frontLineMarker.findClosestFrontCoordinateForSide(defensePosition, assaultingSide);

        double angleFromDefensePosition = MathUtils.calcAngle(defensePosition, assaultPosition);
        Coordinate assaultStartPosition = MathUtils.calcNextCoord(defensePosition, angleFromDefensePosition, DISTANCE_BETWEEN_COMBATANTS);
        return assaultStartPosition;
    }
}
