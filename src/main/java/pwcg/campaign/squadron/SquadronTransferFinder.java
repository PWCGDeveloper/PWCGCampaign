package pwcg.campaign.squadron;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.AceManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.SquadronManager;
import pwcg.campaign.plane.Role;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class SquadronTransferFinder
{
    private Campaign campaign;
    private SquadronMember squadronMember;
    private List<Integer> bestFitSquadrons = new ArrayList<Integer>();
    private List<Integer> anySquadrons = new ArrayList<Integer>();
    
    
    // TODO COOP TEST THIS!!!
    public SquadronTransferFinder(Campaign campaign, SquadronMember squadronMember)
    {
        this.campaign = campaign;
        this.squadronMember = squadronMember;
    }

    public int chooseSquadronForTransfer() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
       
        for (Squadron possibleSquadron : squadronManager.getActiveSquadrons(campaign.getDate()))
        {
            // Exclude squadrons commanded by an ace
            if (canTransferToThisSquadron(possibleSquadron))
            {
                anySquadrons.add(possibleSquadron.getSquadronId());
            }
            
            if (isBestFitSquadron(possibleSquadron))
            {
                bestFitSquadrons.add(possibleSquadron.getSquadronId());
            }
        }
        
        int selectedSquadronId = selectSquadronToTransferTo();
        return selectedSquadronId;
    }

    private boolean canTransferToThisSquadron(Squadron possibleSquadron) throws PWCGException
    {
        if (!squadronMemberHasCurrentSquadron())
        {
            return true;
        }

        AceManager aceManager = PWCGContextManager.getInstance().getAceManager();
        Set<Integer> aceCommandedSquadrons = aceManager.getAceCommandedSquadrons();

        if (aceCommandedSquadrons.contains(possibleSquadron.getSquadronId()))
        {
            return false;
        }
        
        if (possibleSquadron.getSquadronId() == squadronMember.getSquadronId())
        {
            return false;
        }
        
        if (squadronMember.determineCountry(campaign.getDate()).getCountryCode() != possibleSquadron.getCountry().getCountryCode())
        {
            return false;
        }
        
        return true;
    }

    private boolean squadronMemberHasCurrentSquadron() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        Squadron squadronMemberCurrentSquadron = squadronManager.getSquadron(squadronMember.getSquadronId());
        if (squadronMemberCurrentSquadron == null)
        {
            return true;
        }
        
        return false;
    }

    private boolean isBestFitSquadron(Squadron possibleSquadron) throws PWCGException
    {
        SquadronManager squadronManager = PWCGContextManager.getInstance().getSquadronManager();
        Squadron squadronMemberCurrentSquadron = squadronManager.getSquadron(squadronMember.getSquadronId());
        if (squadronMemberCurrentSquadron != null)
        {
            Role bestRoleForThisSquadron = squadronMemberCurrentSquadron.determineSquadronPrimaryRole(campaign.getDate());
            Role bestRoleForNewSquadron = possibleSquadron.determineSquadronPrimaryRole(campaign.getDate());
        
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
