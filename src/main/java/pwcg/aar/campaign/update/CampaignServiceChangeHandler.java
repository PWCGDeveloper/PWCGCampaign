package pwcg.aar.campaign.update;

import java.util.Date;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronMemberFilter;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

public class CampaignServiceChangeHandler
{
    private Campaign campaign = null;
        
    public CampaignServiceChangeHandler (Campaign campaign) 
    {
        this.campaign = campaign;
    }

    public void handleChangeOfService(Date newDate) throws PWCGException 
    {
        for (Squadron squadron : PWCGContextManager.getInstance().getSquadronManager().getActiveSquadrons(campaign.getDate()))
        {
            handleChangeOfServiceForSquadron(squadron, newDate);
        }
    }
    
    private void handleChangeOfServiceForSquadron(Squadron squadron, Date newDate) throws PWCGException 
    {
        ArmedService serviceNow = squadron.determineServiceForSquadron(campaign.getDate());
        ArmedService serviceAfter = squadron.determineServiceForSquadron(newDate);

        if (serviceNow.getServiceId() != serviceAfter.getServiceId())
        {
            changeService(squadron, serviceNow, serviceAfter);
        }
    }

    private void changeService(Squadron squadron, ArmedService serviceNow, ArmedService serviceAfter) throws PWCGException
    {
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(squadron.getSquadronId());
        Map<Integer, SquadronMember> squadronMembers = SquadronMemberFilter.filterActiveAIAndPlayerAndAces(squadronPersonnel.getActiveSquadronMembersWithAces().getSquadronMemberCollection(), campaign.getDate());
        for (SquadronMember pilot : squadronMembers.values())
        {
            setPilotRanksForNewService(pilot, serviceNow, serviceAfter);
            setPilotCountryForNewService(pilot, serviceAfter);
        }
    }


    private void setPilotRanksForNewService(SquadronMember pilot, ArmedService serviceNow, ArmedService serviceAfter) throws PWCGException
    {
        IRankHelper rankObj = RankFactory.createRankHelper();
        List<String> ranksNow = rankObj.getRanksByService(serviceNow);
        List<String> ranksAfter = rankObj.getRanksByService(serviceAfter);

        String rankAfter = getRankAfterChangeOfService(ranksNow, ranksAfter, pilot);
        pilot.setRank(rankAfter);
        setRankOfHistoricalAce(pilot);
    }



    private String getRankAfterChangeOfService(List<String> ranksNow, List<String> ranksAfter, SquadronMember pilot)
    {
        String rankAfter = ranksAfter.get(ranksAfter.size() - 1);
        String pilotRank = pilot.getRank();
        for (int i = 0; i < ranksNow.size(); ++i)
        {
            String rankNow = ranksNow.get(i);
            if (rankNow.equals(pilotRank))
            {
                if (i < ranksAfter.size())
                {
                    rankAfter = ranksAfter.get(i);
                    break;
                }
            }
        }
        return rankAfter;
    }

    private void setRankOfHistoricalAce(SquadronMember pilot)
    {
        if (pilot instanceof Ace)
        {
        	Ace ace = campaign.getPersonnelManager().getCampaignAces().retrieveAceBySerialNumber(pilot.getSerialNumber());
            if (ace != null)
            {
                ace.setRank(pilot.getRank());
            }
        }
    }

    private void setPilotCountryForNewService(SquadronMember pilot, ArmedService serviceAfter) throws PWCGException
    {
        ICountry country = CountryFactory.makeCountryByService(serviceAfter);
        pilot.setCountry(country.getCountry());
    }
}
