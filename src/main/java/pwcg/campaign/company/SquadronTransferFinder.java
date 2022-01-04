package pwcg.campaign.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.AceManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.CrewMember;
import pwcg.campaign.tank.PwcgRoleCategory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class SquadronTransferFinder
{
    private Campaign campaign;
    private CrewMember crewMember;
    private List<Integer> bestFitSquadrons = new ArrayList<Integer>();
    private List<Integer> anySquadrons = new ArrayList<Integer>();
    
    
    public SquadronTransferFinder(Campaign campaign, CrewMember crewMember)
    {
        this.campaign = campaign;
        this.crewMember = crewMember;
    }

    public int chooseSquadronForTransfer() throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
       
        for (Company possibleSquadron : squadronManager.getActiveCompanies(campaign.getDate()))
        {
            // Exclude squadrons commanded by an ace
            if (canTransferToThisSquadron(possibleSquadron))
            {
                anySquadrons.add(possibleSquadron.getCompanyId());
            }
            
            if (isBestFitSquadron(possibleSquadron))
            {
                bestFitSquadrons.add(possibleSquadron.getCompanyId());
            }
        }
        
        int selectedSquadronId = selectSquadronToTransferTo();
        return selectedSquadronId;
    }

    private boolean canTransferToThisSquadron(Company possibleSquadron) throws PWCGException
    {
        if (!squadronMemberHasCurrentSquadron())
        {
            return true;
        }

        AceManager aceManager = PWCGContext.getInstance().getAceManager();
        Set<Integer> aceCommandedSquadrons = aceManager.getAceCommandedSquadrons();

        if (aceCommandedSquadrons.contains(possibleSquadron.getCompanyId()))
        {
            return false;
        }
        
        if (possibleSquadron.getCompanyId() == crewMember.getCompanyId())
        {
            return false;
        }
        
        if (crewMember.determineCountry(campaign.getDate()).getCountryCode() != possibleSquadron.getCountry().getCountryCode())
        {
            return false;
        }
        
        return true;
    }

    private boolean squadronMemberHasCurrentSquadron() throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        Company squadronMemberCurrentSquadron = squadronManager.getCompany(crewMember.getCompanyId());
        if (squadronMemberCurrentSquadron == null)
        {
            return true;
        }
        
        return false;
    }

    private boolean isBestFitSquadron(Company possibleSquadron) throws PWCGException
    {
        CompanyManager squadronManager = PWCGContext.getInstance().getCompanyManager();
        Company squadronMemberCurrentSquadron = squadronManager.getCompany(crewMember.getCompanyId());
        if (squadronMemberCurrentSquadron != null)
        {
            PwcgRoleCategory bestRoleForThisSquadron = squadronMemberCurrentSquadron.determineSquadronPrimaryRoleCategory(campaign.getDate());
            PwcgRoleCategory bestRoleForNewSquadron = possibleSquadron.determineSquadronPrimaryRoleCategory(campaign.getDate());
        
            if (bestRoleForThisSquadron == bestRoleForNewSquadron)
            {
                return true;
            }
        }
        
        return false;
    }


    private int selectSquadronToTransferTo()
    {
        int selectedSquadronId = -1;
        if (bestFitSquadrons.size() > 0)
        {
            int size = bestFitSquadrons.size();
            int index = RandomNumberGenerator.getRandom(size);
            selectedSquadronId = bestFitSquadrons.get(index);
        }
        else
        {
            int size = anySquadrons.size();
            int index = RandomNumberGenerator.getRandom(size);
            selectedSquadronId = anySquadrons.get(index);
        }
        return selectedSquadronId;
    }

}
