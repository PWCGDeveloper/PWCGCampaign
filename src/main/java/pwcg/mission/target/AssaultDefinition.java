package pwcg.mission.target;

import pwcg.campaign.api.ICountry;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.core.utils.MathUtils;
import pwcg.mission.ground.BattleSize;

public class AssaultDefinition
{
    private static final Integer CLOSE_TO_BATTLE = 10000;

    private Coordinate assaultPosition = null;
    private Coordinate defensePosition = null;
    private ICountry assaultingCountry = null;
    private ICountry defendingCountry = null;
    private BattleSize battleSize;
    
    public Orientation getTowardsDefenderOrientation() throws PWCGException
    {
        double angleToDefensePosition = MathUtils.calcAngle(assaultPosition, defensePosition);
        return new Orientation(angleToDefensePosition);
    }
    
    public Orientation getTowardsAttackerOrientation() throws PWCGException
    {
        double angleToAssaultPosition = MathUtils.calcAngle(defensePosition, assaultPosition);
        return new Orientation(angleToAssaultPosition);
    }

    public BattleSize getBattleSize()
    {
        return battleSize;
    }

    public void setBattleSize(BattleSize battleSize)
    {
        this.battleSize = battleSize;
    }

    public boolean isNearBattle(Coordinate coordinate)
    {
        double distanceFromAssault = MathUtils.calcDist(coordinate, assaultPosition);
        double distanceFromDefense = MathUtils.calcDist(coordinate, defensePosition);
        
        if (distanceFromAssault < CLOSE_TO_BATTLE || distanceFromDefense < CLOSE_TO_BATTLE)
        {
            return true;
        }

        return false;
    }
    
    public boolean determineIsBattleForPlayer()
    {
        if (battleSize == BattleSize.BATTLE_SIZE_TINY)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static Integer getCloseToBattle()
    {
        return CLOSE_TO_BATTLE;
    }

    public Coordinate getAssaultPosition()
    {
        return assaultPosition;
    }

    public void setAssaultPosition(Coordinate assaultPosition)
    {
        this.assaultPosition = assaultPosition;
    }

    public Coordinate getDefensePosition()
    {
        return defensePosition;
    }

    public void setDefensePosition(Coordinate defensePosition)
    {
        this.defensePosition = defensePosition;
    }    

    public ICountry getAssaultingCountry()
    {
        return assaultingCountry;
    }

    public void setAssaultingCountry(ICountry assaultingCountry)
    {
        this.assaultingCountry = assaultingCountry;
    }

    public ICountry getDefendingCountry()
    {
        return defendingCountry;
    }

    public void setDefendingCountry(ICountry defendingCountry)
    {
        this.defendingCountry = defendingCountry;
    }
}
