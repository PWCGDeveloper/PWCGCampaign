package pwcg.campaign.io.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class SquadronAirfieldAssignmentTest
{
    private List<String> validMoveDates = new ArrayList<>();
    
    @Before
    public void setup()
    {
        validMoveDates.clear();
        validMoveDates.add("19411001");
        validMoveDates.add("19411020");
        validMoveDates.add("19411110");
        validMoveDates.add("19411120");
        validMoveDates.add("19411215");
        validMoveDates.add("19420110");
        
        validMoveDates.add("19420301");
        
        validMoveDates.add("19420601");
        validMoveDates.add("19420624");
        validMoveDates.add("19420709");
        validMoveDates.add("19420721");
        
        validMoveDates.add("19420801");
        validMoveDates.add("19420906");
        validMoveDates.add("19421011");
        validMoveDates.add("19421123");
        validMoveDates.add("19421223");
        validMoveDates.add("19430120");
        
        validMoveDates.add("19430301");
        validMoveDates.add("19430330");
        validMoveDates.add("19430418");
        validMoveDates.add("19430918");
        validMoveDates.add("19430927");
        validMoveDates.add("19431004");
        validMoveDates.add("19431008");
        validMoveDates.add("19440801");
    }
    
    @Test
    public void verifyValidBoSAirfieldMoveDatesTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        List<Squadron> squadrons = SquadronIOJson.readJson();
        assert (squadrons.size() > 0);
        
        boolean success = true;
        for (Squadron squadron : squadrons)
        {
            if (!verifyValidBoSAirfieldMoveDatesForSquadron(squadron))
            {
                success = false;
            }
        }
        
        assert(success);
    }
    
    
    @Test
    public void verifyCompleteBoSAirfieldMoveDatesTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        List<Squadron> squadrons = SquadronIOJson.readJson();
        assert (squadrons.size() > 0);
        
        boolean success = true;
        for (Squadron squadron : squadrons)
        {
            if (!verifyCompleteBoSAirfieldMoveDatesForSquadron(squadron))
            {
                success = false;
            }
        }
        
        assert(success);
    }

    private boolean verifyValidBoSAirfieldMoveDatesForSquadron(Squadron squadron) throws PWCGException
    {
        Map <Date, String> airfields = squadron.getAirfields();
        boolean success = true;
        try 
        {
            for (Date airfieldDate : airfields.keySet())
            {
                String airfieldDateString = DateUtils.getDateStringYYYYMMDD(airfieldDate);
                validateBoSSquadAirfieldDate(squadron, airfieldDateString);
            }
        }
        catch (PWCGException e)
        {
            System.out.println(e.getMessage());
            success = false;
        }
        
        return success;
    }

    private void validateBoSSquadAirfieldDate(Squadron squadron, String date) throws PWCGException
    {
        for (String validMoveDate : validMoveDates)
        {
            if (validMoveDate.equals(date))
            {
                return;
            }
        }
        
        String errorMsg = "invalid airfield move date " + date + " for squadron " + squadron.getSquadronId(); 
        throw new PWCGException(errorMsg);
    }
    
    private boolean verifyCompleteBoSAirfieldMoveDatesForSquadron(Squadron squadron) throws PWCGException
    {
        Map <Date, String> airfields = squadron.getAirfields();
        boolean success = true;
        try 
        {
            List<Date> airfieldDatesObjectsForSquadron = new ArrayList<>(airfields.keySet());
            List<String> airfieldDatesForSquadron = new ArrayList<>();
            for (Date airfieldDate : airfieldDatesObjectsForSquadron)
            {
                airfieldDatesForSquadron.add(DateUtils.getDateStringYYYYMMDD(airfieldDate));
            }
            
            int firstTransitionDatePosition = findFirstTransitionDatePosition(airfieldDatesForSquadron.get(0));
            int easternFrontTransitionDates = validMoveDates.size() - 1;
            int westernFrontTransitionDates = validMoveDates.size();
            
            if ((airfieldDatesForSquadron.size() != (easternFrontTransitionDates - firstTransitionDatePosition)) && 
                (airfieldDatesForSquadron.size() != (westernFrontTransitionDates - firstTransitionDatePosition)))
            {
                String errorMsg = "incomplete airfield move date for squadron " + squadron.getSquadronId(); 
                System.out.println(errorMsg);
                success = false;
            }
            
            for (int i = 0; i < airfieldDatesForSquadron.size(); ++i)
            {
                String airfieldDate = airfieldDatesForSquadron.get(i);
                String moveDate = validMoveDates.get(firstTransitionDatePosition + i);
                if (!airfieldDate.equals(moveDate))
                {
                    String errorMsg = "unmatched airfield move date " + airfieldDate + " for squadron " + squadron.getSquadronId(); 
                    System.out.println(errorMsg);
                    success = false;
                }
            }
        }
        catch (PWCGException e)
        {
            System.out.println(e.getMessage());
            success = false;
        }
        
        return success;
    }

    private int findFirstTransitionDatePosition(String date) throws PWCGException
    {
        for (int i = 0; i < validMoveDates.size(); ++i)
        {
            String validMoveDate = validMoveDates.get(i);
            if (validMoveDate.equals(date))
            {
                return i;
            }
        }
        
        String errorMsg = "invalid airfield move date " + date; 
        throw new PWCGException(errorMsg);
    }
}
