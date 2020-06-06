package pwcg.campaign.personnel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class EnemySquadronFinder
{
    private Campaign campaign;

    public EnemySquadronFinder (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Squadron getEnemyForInitialSquadronBuild(Squadron squadron, Date date) throws PWCGException
    {
        boolean useViableSquadrons = false;
        List<Squadron> enemySquadrons = getEnemySquadronsForVictory(squadron, date, useViableSquadrons);
        Squadron enemySquadron = null;
        if (enemySquadrons.size() > 0)
        {
            enemySquadron = getEnemySquadron(enemySquadrons);
        }
        
        return enemySquadron;
    }

    public Squadron getEnemyForOutOfMission(Squadron squadron, Date date) throws PWCGException
    {
        boolean useViableSquadrons = true;
        List<Squadron> enemySquadrons = getEnemySquadronsForVictory(squadron, date, useViableSquadrons);
        Squadron enemySquadron = null;
        if (enemySquadrons.size() > 0)
        {
            enemySquadron = getEnemySquadron(enemySquadrons);
        }

        return enemySquadron;
    }

    private Squadron getEnemySquadron(List<Squadron> possibleSquadrons) throws PWCGException
    {
        int index = RandomNumberGenerator.getRandom(possibleSquadrons.size());
        Squadron enemySquadron = possibleSquadrons.get(index);
        return enemySquadron;
    }

    private List<Squadron> getEnemySquadronsForVictory(Squadron squadron, Date date, boolean useViableSquadrons) throws PWCGException
    {
        List<Squadron> enemySquadrons = new ArrayList<>();
                
        ICountry country = squadron.getCountry();
        Side enemySide = country.getSide().getOppositeSide();
        List<Role> acceptableRoles = createAcceptableRoledForVictory();
        if (useViableSquadrons)
        {
            enemySquadrons = PWCGContext.getInstance().getSquadronManager().getViableAiSquadronsForCurrentMapAndSideAndRole(campaign, acceptableRoles, enemySide);
        }
        else
        {
            enemySquadrons = PWCGContext.getInstance().getSquadronManager().getActiveSquadronsForSide(date, enemySide);
        }
        
        return enemySquadrons;
    }

    private static List<Role> createAcceptableRoledForVictory()
    {
        List<Role> acceptableRoles = new ArrayList<Role>();
        acceptableRoles.add(Role.ROLE_FIGHTER);
        acceptableRoles.add(Role.ROLE_ATTACK);
        acceptableRoles.add(Role.ROLE_DIVE_BOMB);
        acceptableRoles.add(Role.ROLE_BOMB);
        acceptableRoles.add(Role.ROLE_RECON);
        return acceptableRoles;
    }

}
