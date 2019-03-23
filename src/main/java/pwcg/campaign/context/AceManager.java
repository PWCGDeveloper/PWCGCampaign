package pwcg.campaign.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignAces;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.io.json.HistoricalAceIOJson;
import pwcg.campaign.medals.Medal;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.HistoricalAce;
import pwcg.campaign.squadmember.HistoricalAceRank;
import pwcg.campaign.squadmember.HistoricalAceSquadron;
import pwcg.campaign.squadmember.SquadronMemberStatus;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.Logger;

public class AceManager
{
    public String HISTORICAL_ACE_LIST = "HISTORICAL";

    private List<HistoricalAce> historicalAces = new ArrayList<HistoricalAce>();

    public AceManager()
    {
    }

    public void configure() throws PWCGException 
    {
        historicalAces = HistoricalAceIOJson.readJson(); 
    }

    public CampaignAces loadFromHistoricalAces(Date date) throws PWCGException 
    {
        Map<Integer, Ace> historicalAcesAtDate = new HashMap<>();

        for (int i = 0; i < historicalAces.size(); ++i)
        {
            HistoricalAce historicalAce = historicalAces.get(i);
            Ace adjustedAce = historicalAce.getAtDate(date);

            historicalAcesAtDate.put(adjustedAce.getSerialNumber(), adjustedAce);
        }
        
        CampaignAces campaignAces = new CampaignAces();
        campaignAces.setCampaignAces(historicalAcesAtDate);

        return campaignAces;
    }

    public List<Ace> getAllAcesForSquadron(Collection<Ace> aces, int squadronId)
    {
        List<Ace> retAces = new ArrayList<Ace>();

        for (Ace ace : aces)
        {
            int aceSquadronId = ace.getSquadronId();
            if (aceSquadronId == squadronId)
            {
                Ace aceClone = ace.copy();
                retAces.add(aceClone);
            }
        }

        return retAces;
    }

    public List<Ace> getActiveAcesForCampaign(CampaignAces aces, Date date) throws PWCGException 
    {
        List<Ace> retAces = new ArrayList<Ace>();

        for (Ace ace : aces.getCampaignAces().values())
        {
            HistoricalAce historicalAce = getHistoricalAceBySerialNumber(ace.getSerialNumber());
            if (historicalAce != null)
            {
                Ace aceClone = getAceNoCampaignAdjustment(aces, ace.getSerialNumber(), date);                
                if (aceClone != null && aceClone.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
                {
                    retAces.add(aceClone);
                }
            }
        }

        return retAces;
    }

    public List<Ace> getActiveAcesForSquadron(CampaignAces aces, Date date, int squadronId) throws PWCGException 
    {
        List<Ace> retAces = new ArrayList<Ace>();

        for (Ace ace : aces.getCampaignAces().values())
        {
            HistoricalAce historicalAce = getHistoricalAceBySerialNumber(ace.getSerialNumber());
            if (historicalAce != null)
            {
                int squadAtThisDate = historicalAce.getCurrentSquadron(date, true);
    
                if (squadAtThisDate == squadronId)
                {                    
                    Ace aceClone = getAceNoCampaignAdjustment(aces, ace.getSerialNumber(), date);
    
                    if (aceClone != null && aceClone.getPilotActiveStatus() == SquadronMemberStatus.STATUS_ACTIVE)
                    {
                        retAces.add(aceClone);
                    }
                }
            }
        }

        return retAces;
    }

    public Ace getAceWithCampaignAdjustment(Campaign campaign, CampaignAces aces, Integer aceSerialNumber, Date date) throws PWCGException 
	{
		Ace aceClone = getAceNoCampaignAdjustment(aces, aceSerialNumber, date);
		if (aceClone != null)
		{
			setRankToCommanderIfAceCommandsSquadron(campaign, date, aceClone);
		}

		return aceClone;
	}

    public HistoricalAce getHistoricalAceBySerialNumber(Integer serialNumber)
    {
        HistoricalAce retAce = null;
        retAce = lookForAce(serialNumber);
        return retAce;
    }

    public List<Ace> acesKilledHistoricallyInTimePeriod(Date beforeDate, Date afterDate) throws PWCGException 
    {
        List<Ace> deadAces = new ArrayList<Ace>();

        for (HistoricalAce ha : historicalAces)
        {
            Ace aceBefore = ha.getAtDate(beforeDate);
            if (aceBefore.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_CAPTURED)
            {
                continue;
            }

            Ace aceAfter = ha.getAtDate(afterDate);
            if (aceAfter.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_CAPTURED)
            {
                deadAces.add(aceAfter);
            }
        }

        return deadAces;
    }

    public List<HistoricalAce> getHistoricalAces()
    {
        return historicalAces;
    }

    public Set<Integer> getAceCommandedSquadrons()
    {
        Set<Integer> aceCommandedSquadrons = new HashSet<Integer>();
        
        try
        {            
            for (HistoricalAce ace : historicalAces)
            {            	
                Date commandRankDate = getAceCommandRankDate(ace);
                if (commandRankDate == null)
                {
                	continue;
                }
                
                determineCurrentSquadronCommandedByAce(aceCommandedSquadrons, ace, commandRankDate);
             }
            
        }
        catch (Exception e)
        {
             Logger.logException(e);
        }
        
        return aceCommandedSquadrons;
    }
    
    private void determineCurrentSquadronCommandedByAce(Set<Integer> aceCommandedSquadrons, HistoricalAce ace, Date commandRankDate)
    {
        List<HistoricalAceSquadron> aceSquadrons = ace.getSquadrons();
        HistoricalAceSquadron previousAceSquadron = null;
        for (HistoricalAceSquadron aceSquadron : aceSquadrons)
        {
            if (aceSquadron.date.after(commandRankDate))
            {
                if (aceCommandedSquadrons.isEmpty())
                {
                    if (previousAceSquadron != null)
                    {
                    	if (previousAceSquadron.squadron > 0)
                    	{
                    		aceCommandedSquadrons.add(previousAceSquadron.squadron);
                    	}
                    }
                }

            	if (aceSquadron.squadron > 0)
            	{
                    aceCommandedSquadrons.add(aceSquadron.squadron);
            	}
            }
            
            previousAceSquadron = aceSquadron;
        }

    }

    private Ace getAceNoCampaignAdjustment(CampaignAces aces, Integer aceSerialNumber, Date date) throws PWCGException 
	{
		Ace aceClone = null;
		
		Ace ace = aces.retrieveAceBySerialNumber(aceSerialNumber);
		if (ace != null)
		{
			aceClone = ace.copy();
			if (!(aceClone.getPilotActiveStatus() <= SquadronMemberStatus.STATUS_CAPTURED))
			{
				HistoricalAce historicalAce = getHistoricalAceBySerialNumber(ace.getSerialNumber());
	            setAceCloneRank(date, aceClone, historicalAce);
				setAceCloneSquadron(date, aceClone, historicalAce);
				
				setAceCloneMedals(date, aceClone, historicalAce);
			}
		}
		
		return aceClone;
	}


	private void setAceCloneSquadron(Date date, Ace aceClone, HistoricalAce historicalAce)
	{
		int squadAtThisDate = historicalAce.getCurrentSquadron(date, true) ;
		aceClone.setSquadronId(squadAtThisDate);
		if (squadAtThisDate > 0)
		{
			aceClone.setPilotActiveStatus(SquadronMemberStatus.STATUS_ACTIVE, null, null);
		}
	}

	private void setAceCloneRank(Date date, Ace aceClone, HistoricalAce historicalAce) throws PWCGException
	{
		String rankAtThisDate = historicalAce.getCurrentRank(date);
		if (rankAtThisDate != null && rankAtThisDate.length() > 0)
		{
		    aceClone.setRank(rankAtThisDate);
		}
	}

	private void setAceCloneMedals(Date date, Ace aceClone, HistoricalAce historicalAce)
	{
		List<Medal> medals = historicalAce.getMedals(date);
		aceClone.setMedals(medals);
	}

	private void setRankToCommanderIfAceCommandsSquadron(Campaign campaign, Date date, Ace aceClone)
	        throws PWCGException
	{
		if (campaign != null)
		{
            if (Squadron.isPlayerSquadron(campaign, aceClone.getSquadronId()))
			{
				if (campaign.getPersonnelManager().getSquadronPersonnel(aceClone.getSquadronId()).isPlayerCommander())
				{
				    IRankHelper rankObj = RankFactory.createRankHelper();
                    String commandRankRank = rankObj.getRankByService(0, aceClone.determineService(date));
					if (aceClone.getRank().equalsIgnoreCase(commandRankRank))
					{
						String newRank = rankObj.getRankByService(1, aceClone.determineService(date));
						aceClone.setRank(newRank);
					}
				}
			}
		}
	}

    private HistoricalAce lookForAce(Integer serialNumber)
    {
        HistoricalAce retAce = null;
        
        for (int i = 0; i < historicalAces.size(); ++i)
        {
            HistoricalAce ace = historicalAces.get(i);
            if (ace.getSerialNumber() == serialNumber)
            {
                retAce = ace;
                break;
            }
        }
        return retAce;
    }

    private Date getAceCommandRankDate(HistoricalAce ace)
    {
        Date commandRankDate = null;
        List<HistoricalAceRank> aceRanks = ace.getRanks();
        for (HistoricalAceRank aceRank : aceRanks)
        {
            if (aceRank.rank == 0)
            {
                commandRankDate =  aceRank.date;
            }
        }
        
        return commandRankDate;
    }

}
