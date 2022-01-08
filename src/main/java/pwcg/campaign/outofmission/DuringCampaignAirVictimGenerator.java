package pwcg.campaign.outofmission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pwcg.campaign.ArmedService;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IRankHelper;
import pwcg.campaign.company.Company;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.crewmember.CrewMembers;
import pwcg.campaign.factory.RankFactory;
import pwcg.campaign.personnel.CompanyPersonnel;
import pwcg.campaign.personnel.CrewMemberFilter;
import pwcg.campaign.tank.EquippedTank;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class DuringCampaignAirVictimGenerator implements IVictimGenerator
{    
    private Campaign campaign;
    private Company victimSquadron;

    private Map<Integer, CrewMember> lowRanks = new HashMap<>();
    private Map<Integer, CrewMember> medRanks = new HashMap<>();
    private Map<Integer, CrewMember> highRanks = new HashMap<>();
    private Map<Integer, CrewMember> all = new HashMap<>();

    public DuringCampaignAirVictimGenerator (Campaign campaign, Company victimSquadron) throws PWCGException
    {
        this.campaign = campaign;
        this.victimSquadron = victimSquadron;
    }

    public CrewMember generateVictimAiCrew() throws PWCGException 
    {        
        CrewMember victimCrewMember = null;
        
        Map<Integer, CrewMember> possibleVictims = getPossibleVictims();
        if (possibleVictims.size() > 0)
        {
            categorizeAICrewMembers(possibleVictims);
            Map<Integer, CrewMember> selectedCategory = selectCrewMemberCategory();
            victimCrewMember = selectVictim(selectedCategory);
        }
        
        return victimCrewMember;
    }

    private void categorizeAICrewMembers(Map<Integer, CrewMember> possibleVictims) throws PWCGException 
    {
        for (CrewMember crewMember : possibleVictims.values())
        {
            IRankHelper rankObj = RankFactory.createRankHelper();
            Company possibleVictimSquadron = crewMember.determineSquadron();
            if (possibleVictimSquadron != null)
            {
                ArmedService victimService = possibleVictimSquadron.determineServiceForSquadron(campaign.getDate());
                int rankPos = rankObj.getRankPosByService(crewMember.getRank(), victimService);
                if (rankPos == 0)
                {
                    // DO not include commanders
                }
                else if (rankPos == 1)
                {
                    highRanks.put(crewMember.getSerialNumber(), crewMember);
                }
                else if (rankPos == 2)
                {
                    medRanks.put(crewMember.getSerialNumber(), crewMember);
                }
                else
                {
                    lowRanks.put(crewMember.getSerialNumber(), crewMember);
                }
            }
            
            all.put(crewMember.getSerialNumber(), crewMember);
        }
    }

    private Map<Integer, CrewMember> getPossibleVictims() throws PWCGException 
    {
        Map<Integer, CrewMember> possibleVictims = new HashMap<>();
        CompanyPersonnel squadronPersonnel = campaign.getPersonnelManager().getCompanyPersonnel(victimSquadron.getCompanyId());
        CrewMembers squadronMembers = CrewMemberFilter.filterActiveAINoWounded(squadronPersonnel.getCrewMembersWithAces().getCrewMemberCollection(), campaign.getDate());
        for (CrewMember crewMember : squadronMembers.getCrewMemberList())
        {
            possibleVictims.put(crewMember.getSerialNumber(), crewMember);
        }

        return possibleVictims;
    }


    private Map<Integer, CrewMember> selectCrewMemberCategory() throws PWCGException 
    {
        Map<Integer, CrewMember> selectedCategory;

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

    private CrewMember selectVictim(Map<Integer, CrewMember> potentialVictims) throws PWCGException 
    {
    	if (!potentialVictims.isEmpty())
    	{
    	    List<CrewMember> potentialVictimsList = new ArrayList<>(potentialVictims.values());
    		int index = RandomNumberGenerator.getRandom(potentialVictimsList.size());
    		CrewMember victim = potentialVictimsList.get(index);
    		return victim;
    	}
    	
    	return null;
    }

    public EquippedTank generateVictimPlane() throws PWCGException 
    {        
        EquippedTank victimPlane = campaign.getEquipmentManager().getAnyActiveTankFromCompany(victimSquadron.getCompanyId());
        return victimPlane;
    }

}
