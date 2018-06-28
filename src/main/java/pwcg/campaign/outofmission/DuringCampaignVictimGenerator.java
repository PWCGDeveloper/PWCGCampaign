package pwcg.campaign.outofmission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.SquadronPersonnel;
import pwcg.campaign.squadmember.Ace;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class DuringCampaignVictimGenerator implements IVictimGenerator
{    
    private Campaign campaign;
    private Squadron victimSquadron;

    private Map<Integer, SquadronMember> lowRanks = new HashMap<>();
    private Map<Integer, SquadronMember> medRanks = new HashMap<>();
    private Map<Integer, SquadronMember> highRanks = new HashMap<>();
    private Map<Integer, SquadronMember> all = new HashMap<>();

    public DuringCampaignVictimGenerator (Campaign campaign, Squadron victorSquadron) throws PWCGException
    {
        this.campaign = campaign;
        this.victimSquadron = victorSquadron;
    }

    public SquadronMember generateVictimAiCrew() throws PWCGException 
    {        
        SquadronMember victimPilot = null;
        
        Map<Integer, SquadronMember> possibleVictims = getPossibleVictims();
        if (possibleVictims.size() > 0)
        {
            categorizeAIPilots(possibleVictims);
            Map<Integer, SquadronMember> selectedCategory = selectPilotCategory();
            victimPilot = selectVictim(selectedCategory);
        }
        
        return victimPilot;
    }

    private void categorizeAIPilots(Map<Integer, SquadronMember> possibleVictims) throws PWCGException 
    {
        for (SquadronMember squadronMember : possibleVictims.values())
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            Squadron possibleVictimSquadron = squadronMember.determineSquadron();
            if (possibleVictimSquadron != null)
            {
                ArmedService victimService = possibleVictimSquadron.determineServiceForSquadron(campaign.getDate());
                int rankPos = rankObj.getRankPosByService(squadronMember.getRank(), victimService);
                if (rankPos == 0)
                {
                    // DO not include commanders
                }
                else if (rankPos == 1)
                {
                    highRanks.put(squadronMember.getSerialNumber(), squadronMember);
                }
                else if (rankPos == 2)
                {
                    medRanks.put(squadronMember.getSerialNumber(), squadronMember);
                }
                else
                {
                    lowRanks.put(squadronMember.getSerialNumber(), squadronMember);
                }
            }
            
            all.put(squadronMember.getSerialNumber(), squadronMember);
        }
    }

    private Map<Integer, SquadronMember> getPossibleVictims() throws PWCGException 
    {
        Map<Integer, SquadronMember> possibleVictims = new HashMap<>();
        SquadronPersonnel squadronPersonnel = campaign.getPersonnelManager().getSquadronPersonnel(victimSquadron.getSquadronId());
        for (SquadronMember squadronMember : squadronPersonnel.getActiveSquadronMembers().getSquadronMembers().values())
        {
            if (squadronMember.isPlayer())
            {
                continue;
            }
            
            if (squadronMember instanceof Ace)
            {
                continue;
            }
            
            if (squadronMember.getSquadronId() == campaign.getSquadronId())
            {
                continue;
            }
            
            possibleVictims.put(squadronMember.getSerialNumber(), squadronMember);
        }

        return possibleVictims;
    }


    private Map<Integer, SquadronMember> selectPilotCategory() throws PWCGException 
    {
        Map<Integer, SquadronMember> selectedCategory;

        int diceRoll = RandomNumberGenerator.getRandom(100);
        if (diceRoll < 8)
        {
            selectedCategory = highRanks;
        }
        else if (diceRoll < 40)
        {
            selectedCategory = medRanks;
        }
        else
        {
            selectedCategory = lowRanks;
        }
        
        if (selectedCategory.isEmpty())
        {
        	selectedCategory = all;
        }
        
        return selectedCategory;
    }

    private SquadronMember selectVictim(Map<Integer, SquadronMember> potentialVictims) throws PWCGException 
    {
    	if (!potentialVictims.isEmpty())
    	{
    	    List<SquadronMember> potentialVictimsList = new ArrayList<>(potentialVictims.values());
    		int index = RandomNumberGenerator.getRandom(potentialVictimsList.size());
    		SquadronMember victim = potentialVictimsList.get(index);
    		return victim;
    	}
    	
    	return null;
    }

}
