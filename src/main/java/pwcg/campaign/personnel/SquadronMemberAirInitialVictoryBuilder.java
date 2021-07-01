package pwcg.campaign.personnel;

import java.util.Calendar;
import java.util.Date;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.outofmission.BeforeCampaignVictimGenerator;
import pwcg.campaign.outofmission.OutOfMissionAirVictoryBuilder;
import pwcg.campaign.outofmission.UnknownSquadronVictoryGenerator;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.Victory;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.product.bos.country.BoSServiceManager;

public class SquadronMemberAirInitialVictoryBuilder
{
    private Campaign campaign;
    private Squadron victorSquadron;
    private int minVictories;
    private int maxVictories;

    public SquadronMemberAirInitialVictoryBuilder(Campaign campaign, Squadron squadron)
    {
        this.campaign = campaign;
        this.victorSquadron = squadron;
    }
    
    public void createPilotVictories(SquadronMember newPilot, int rankPos) throws PWCGException
    {
        initializeVictoriesFromRank(rankPos);
        factorServiceQuality(rankPos);
        factorSquadronQuality(rankPos);
        factorLuftwaffe(rankPos);
        resetForEarlyWWI(rankPos);

        int victories = calcNumberOfVictories(minVictories, maxVictories);
        addVictories(newPilot, victories);
    }

    private void initializeVictoriesFromRank(int rankPos) throws PWCGException
    {
        if (rankPos == 3)
        {
            minVictories = 0;
            maxVictories = 0;
        }
        else if (rankPos == 2)
        {
            minVictories = 0;
            maxVictories = 2;
        }
        else if (rankPos == 1)
        {
            minVictories = 2;
            maxVictories = 5;
        }
        else if (rankPos == 0)
        {
            minVictories = 3;
            maxVictories = 8;
        }
    }

    private void factorServiceQuality(int rankPos) throws PWCGException
    {
        ArmedService service = victorSquadron.determineServiceForSquadron(campaign.getDate());
        int serviceQuality = service.getServiceQuality().getQuality(campaign.getDate()).getQualityValue();

        int minAdjustment = 0;
        int maxAdjustment = 0;
        if (rankPos == 2)
        {
            minAdjustment = (serviceQuality / 10) - 10;
            maxAdjustment = (serviceQuality / 10) - 7;
        }
        else if (rankPos == 1)
        {
            minAdjustment = (serviceQuality / 10) - 8;
            maxAdjustment = (serviceQuality / 10) - 5;
        }
        else if (rankPos == 0)
        {
            minAdjustment = (serviceQuality / 10) - 6;
            maxAdjustment = (serviceQuality / 10) - 4;
        }

        minVictories += minAdjustment;
        maxVictories += maxAdjustment;
    }

    private void factorSquadronQuality(int rankPos) throws PWCGException
    {
        int squadronQuality = victorSquadron.determineSquadronSkill(campaign.getDate());
        
        int minAdjustment = 0;
        int maxAdjustment = 0;
        if (rankPos == 2)
        {
            minAdjustment = (squadronQuality / 10) - 10;
            maxAdjustment = (squadronQuality / 10) - 6;
        }
        else if (rankPos == 1)
        {
            minAdjustment = (squadronQuality / 10) - 8;
            maxAdjustment = (squadronQuality / 10) - 5;
        }
        else if (rankPos == 0)
        {
            minAdjustment = (squadronQuality / 10) - 6;
            maxAdjustment = (squadronQuality / 10) - 4;
        }

        minVictories += minAdjustment;
        maxVictories += maxAdjustment;
    }


    private void factorLuftwaffe(int rankPos) throws PWCGException
    {
        ArmedService service = victorSquadron.determineServiceForSquadron(campaign.getDate());
        int serviceQuality = service.getServiceQuality().getQuality(campaign.getDate()).getQualityValue();

        if (service.getServiceId() == BoSServiceManager.LUFTWAFFE)
        {
            if (serviceQuality > 80)
            {
                int extraLuftwaffeVictories = (serviceQuality - 80);
                int minAdjustment = 0;
                int maxAdjustment = 0;

                if (rankPos == 1)
                {
                    minAdjustment = extraLuftwaffeVictories - 8;
                    maxAdjustment = extraLuftwaffeVictories - 3;
                }
                else if (rankPos == 0)
                {
                    minAdjustment = extraLuftwaffeVictories - 6;
                    maxAdjustment = extraLuftwaffeVictories - 2;
                }
                                
                minVictories += minAdjustment;
                maxVictories += maxAdjustment;
            }
        }
    }

    private void resetForEarlyWWI(int rankPos) throws PWCGException
    {
        Date startOfRealScoring = DateUtils.getDateWithValidityCheck("01/03/1917");
        if (campaign.getDate().before(startOfRealScoring))
        {
            if (rankPos >= 2)
            {
                minVictories = 0;
                maxVictories = 0;
            }
            else if (rankPos == 1)
            {
                minVictories = 0;
                maxVictories = 3;
            }
            else if (rankPos == 0)
            {
                minVictories = 1;
                maxVictories = 5;
            }
        }
    }

    private int calcNumberOfVictories(int minPossibleVictories, int maxAdditionalVictories)
    {
        int victories = RandomNumberGenerator.getRandom(maxVictories - minVictories) + minVictories;
        
        if (victories < 0)
        {
            victories = 0;
        }
        
        return victories;
    }

    private void addVictories(SquadronMember newPilot, int victories) throws PWCGException
    {
        for (int i = victories; i > 0; --i)
        {
            Date victoryDate = generateVictoryDate(i);
            EnemySquadronFinder enemySquadronFinder = new EnemySquadronFinder(campaign);
            
            Squadron victimSquadron = enemySquadronFinder.getEnemyForInitialSquadronBuild(victorSquadron, victoryDate);
            if (victimSquadron != null)
            {
                Victory victory = generateVictoryFromActualSquadrons(victoryDate, victimSquadron, newPilot);
                if (victory != null)
                {
                    newPilot.addVictory(victory);
                }
                else
                {
                    victory = generateVictoryWithoutSquadron(victoryDate, newPilot);
                    if (victory != null)
                    {
                        newPilot.addVictory(victory);
                    }
                }
            }
        }
    }

    private Victory generateVictoryWithoutSquadron(Date victoryDate, SquadronMember newPilot) throws PWCGException
    {
        UnknownSquadronVictoryGenerator unknownSquadronVictoryGenerator = new UnknownSquadronVictoryGenerator(newPilot);
        return unknownSquadronVictoryGenerator.generateOutOfMissionVictory(victoryDate);
    }

    private Victory generateVictoryFromActualSquadrons(Date victoryDate, Squadron victimSquadron, SquadronMember newPilot) throws PWCGException
    {
        Victory victory = null;
        if (victimSquadron != null)
        {
            BeforeCampaignVictimGenerator beforeCampaignVictimGenerator = new BeforeCampaignVictimGenerator(campaign, victimSquadron, victoryDate);
            
            OutOfMissionAirVictoryBuilder outOfMissionVictoryGenerator = new OutOfMissionAirVictoryBuilder(campaign, victimSquadron, beforeCampaignVictimGenerator, newPilot);
            victory = outOfMissionVictoryGenerator.generateOutOfMissionVictory(victoryDate);
        }
        return victory;
    }

    private Date generateVictoryDate(int i) throws PWCGException
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(campaign.getDate());
        calendar.add(Calendar.DAY_OF_YEAR, (3 * i * -1));
        Date victoryDate = calendar.getTime();

        victoryDate = BeforeCampaignDateFinder.useEarliestPossibleDate(victoryDate);
        return victoryDate;
    }
}
