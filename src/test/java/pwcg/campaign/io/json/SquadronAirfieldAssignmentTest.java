package pwcg.campaign.io.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

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
        validMoveDates.add("19431101");
        validMoveDates.add("19431111");

        validMoveDates.add("19440101");
        validMoveDates.add("19440201");
        validMoveDates.add("19440301");
        validMoveDates.add("19440401");
        validMoveDates.add("19440501");
        validMoveDates.add("19440601");
        validMoveDates.add("19440701");
        validMoveDates.add("19440801");
        validMoveDates.add("19440901");
        validMoveDates.add("19440917");
        validMoveDates.add("19440925");
        validMoveDates.add("19440928");
        validMoveDates.add("19441101");
        validMoveDates.add("19441201");

        validMoveDates.add("19450101");
        validMoveDates.add("19450201");
        validMoveDates.add("19450301");
        validMoveDates.add("19450323");
        validMoveDates.add("19450324");
        validMoveDates.add("19450401");
        validMoveDates.add("19450501");

        validMoveDates.add("19440901");
        validMoveDates.add("19441001");
        validMoveDates.add("19441101");
        validMoveDates.add("19441220");
        validMoveDates.add("19441225");
        validMoveDates.add("19441229");
        validMoveDates.add("19450207");
        validMoveDates.add("19450310");
        validMoveDates.add("19450404");
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

    private boolean verifyValidBoSAirfieldMoveDatesForSquadron(Squadron squadron) throws PWCGException
    {
        Map <Date, String> airfields = squadron.getAirfields();
        boolean success = true;
        try 
        {
            boolean firstDate = true;
            for (Date airfieldDate : airfields.keySet())
            {
                if (!firstDate)
                {
                    String airfieldDateString = DateUtils.getDateStringYYYYMMDD(airfieldDate);  
                    validateBoSSquadAirfieldDate(squadron, airfieldDateString);
                }
                else
                {
                    firstDate = false;
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
}
