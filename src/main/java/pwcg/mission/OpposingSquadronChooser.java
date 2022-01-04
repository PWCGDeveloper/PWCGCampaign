package pwcg.mission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.PwcgRole;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class OpposingSquadronChooser
{
    private Campaign campaign;
    private List<PwcgRole> opposingRoles = new ArrayList<>();
    private Side opposingSide;
    private int numberOfOpposingFlights = 1;

    public OpposingSquadronChooser(Campaign campaign, List<PwcgRole> opposingRoles, Side opposingSide, int numberOfOpposingFlights)
    {
        this.campaign = campaign;
        this.opposingRoles = opposingRoles;
        this.opposingSide = opposingSide;
        this.numberOfOpposingFlights = numberOfOpposingFlights;
    }

    public List<Company> getOpposingSquadrons() throws PWCGException
    {
        List<Company> viableOpposingSquads = getViableOpposingSquadrons();        
        if (viableOpposingSquads.size() <= numberOfOpposingFlights)
        {
            return viableOpposingSquads;
        }
        else
        {
            return selectOpposingSquadrons(viableOpposingSquads);            
        }
    }

    private List<Company> selectOpposingSquadrons(List<Company> viableOpposingSquads)
    {
        Map<Integer, Company> selectedOpposingSquads = new HashMap<>();
        HashSet<Integer> alreadyPicked = new HashSet<>();
        while (selectedOpposingSquads.size() < numberOfOpposingFlights)
        {
            int index= RandomNumberGenerator.getRandom(viableOpposingSquads.size());
            Company opposingSquadron = viableOpposingSquads.get(index);
            if (!selectedOpposingSquads.containsKey(opposingSquadron.getCompanyId()))
            {
                selectedOpposingSquads.put(opposingSquadron.getCompanyId(), opposingSquadron);
                alreadyPicked.add(index);
            }
        }
        return new ArrayList<>(selectedOpposingSquads.values());
    }

    private List<Company> getViableOpposingSquadrons() throws PWCGException
    {        
        List<Company> viableOpposingSquads = PWCGContext.getInstance().getCompanyManager().getViableAiCompaniesForCurrentMapAndSideAndRole(campaign, opposingRoles, opposingSide);
        return viableOpposingSquads;
    }
}
