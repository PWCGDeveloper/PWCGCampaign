package pwcg.campaign.io.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.SquadHistory;
import pwcg.campaign.SquadHistoryEntry;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class SquadronHistoryValidationTest
{
    @Test
    public void readJsonBoSSquadronsTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        List<Squadron> squadrons = SquadronIOJson.readJson();
        assert (squadrons.size() > 0);
        
        boolean success = true;
        for (Squadron squadron : squadrons)
        {
            verifyVVSTransition(squadron);
            if (!verifyBoSTransitionDates(squadron))
            {
                success = false;
            }
        }
        
        assert(success);
    }
    
    private void verifyVVSTransition(Squadron squadron) throws PWCGException
    {
        if (squadron.getSquadronId() == 10131136)
        {
            SquadHistory squadronHistory = squadron.getSquadHistory();
            assert (squadronHistory != null);
            
            SquadHistoryEntry  squadHistoryEntry = squadronHistory.getSquadHistoryEntry(DateUtils.getDateYYYYMMDD("19420301"));
            assert (squadHistoryEntry != null);
            assert (squadHistoryEntry.getArmedServiceName().equals("Voyenno-Vozdushnye Sily"));
            assert (squadHistoryEntry.getSquadName().equals("45th Bomber Air Regiment"));
            assert (squadHistoryEntry.getSkill() == 40);
            
            assert(squadron.determineSquadronSkill(DateUtils.getDateYYYYMMDD("19420228")) == 30);
            assert(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19420228")).equals("136th Bomber Air Regiment"));

            assert(squadron.determineSquadronSkill(DateUtils.getDateYYYYMMDD("19420301")) == 40);
            assert(squadron.determineDisplayName(DateUtils.getDateYYYYMMDD("19420301")).equals("45th Bomber Air Regiment"));
        }
    }
    
    private boolean verifyBoSTransitionDates(Squadron squadron) throws PWCGException
    {
        SquadHistory squadronHistory = squadron.getSquadHistory();
        boolean success = true;
        try 
        {
            if (squadronHistory != null)
            {
                validateBoSSquadHistoryEntries(squadronHistory);
            }
        }
        catch (PWCGException e)
        {
            System.out.println(e.getMessage());
            success = false;
        }
        
        return success;
    }

    private void validateBoSSquadHistoryEntries(SquadHistory squadronHistory) throws PWCGException
    {
        validateNoDuplicateEntries(squadronHistory);
        validateNoOutOfOrderEntries(squadronHistory);
    }
    
    private void validateNoDuplicateEntries(SquadHistory squadronHistory) throws PWCGException
    {
        List<SquadHistoryEntry> validatedEntries = new ArrayList<>();
        for (SquadHistoryEntry  squadHistoryEntry : squadronHistory.getSquadHistoryEntries())
        {
            for (SquadHistoryEntry validatedSquadHistoryEntry : validatedEntries)
            {
                if (squadHistoryEntry.getDate().equals(validatedSquadHistoryEntry.getDate()))
                {
                    String errorMsg = squadHistoryEntry.getSquadName() + " duplicate transition date " + squadHistoryEntry.getDate(); 
                    throw new PWCGException(errorMsg);
                }
            }
            
            validatedEntries.add(squadHistoryEntry);
        }
    }
    
    private void validateNoOutOfOrderEntries(SquadHistory squadronHistory) throws PWCGException
    {
        SquadHistoryEntry lastEntry = null;
        for (SquadHistoryEntry  squadHistoryEntry : squadronHistory.getSquadHistoryEntries())
        {
            if (lastEntry != null)
            {
                Date thisEntryDate = DateUtils.getDateYYYYMMDD(squadHistoryEntry.getDate());
                Date lastEntryDate = DateUtils.getDateYYYYMMDD(lastEntry.getDate());
                if (!thisEntryDate.after(lastEntryDate))
                {
                    String errorMsg = squadHistoryEntry.getSquadName() + " out of sequence transition date " + squadHistoryEntry.getDate(); 
                    throw new PWCGException(errorMsg);
                }
            }
            
            lastEntry = squadHistoryEntry;
        }
    }
}
