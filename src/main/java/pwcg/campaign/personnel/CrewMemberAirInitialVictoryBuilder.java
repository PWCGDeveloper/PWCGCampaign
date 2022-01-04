package pwcg.campaign.personnel;

import java.util.Calendar;
import java.util.Date;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.Victory;
import pwcg.campaign.outofmission.UnknownSquadronVictoryGenerator;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.product.bos.country.BoSServiceManager;

public class CrewMemberAirInitialVictoryBuilder
{
    private Campaign campaign;
    private Company victorSquadron;
    private int minVictories;
    private int maxVictories;

    public CrewMemberAirInitialVictoryBuilder(Campaign campaign, Company squadron)
    {
        this.campaign = campaign;
        this.victorSquadron = squadron;
    }
    
    public void createCrewMemberVictories(CrewMember newCrewMember, int rankPos) throws PWCGException
    {
        initializeVictoriesFromRank(rankPos);
        factorServiceQuality(rankPos);
        factorSquadronQuality(rankPos);
        factorLuftwaffe(rankPos);
        resetForEarlyWWI(rankPos);

        int victories = calcNumberOfVictories(minVictories, maxVictories);
        addVictories(newCrewMember, victories);
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

        if (service.getServiceId() == BoSServiceManager.WEHRMACHT)
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

    private void addVictories(CrewMember newCrewMember, int victories) throws PWCGException
    {
        for (int i = victories; i > 0; --i)
        {
            Date victoryDate = generateVictoryDate(i);
            Victory victory = generateVictoryWithoutSquadron(victoryDate, newCrewMember);
            if (victory != null)
            {
                newCrewMember.addVictory(victory);
            }
        }
    }

    private Victory generateVictoryWithoutSquadron(Date victoryDate, CrewMember newCrewMember) throws PWCGException
    {
        UnknownSquadronVictoryGenerator unknownSquadronVictoryGenerator = new UnknownSquadronVictoryGenerator(newCrewMember);
        return unknownSquadronVictoryGenerator.generateOutOfMissionVictory(victoryDate);
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
