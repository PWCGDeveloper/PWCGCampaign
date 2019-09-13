package pwcg.campaign.io.json;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class SquadronAircraftAssignmentTest
{    
    @Before
    public void setup()
    {
    }
    
    @Test
    public void verifyValidBoSAirfieldMoveDatesTest() throws PWCGException
    {
        PWCGContextManager.setRoF(false);
        List<Squadron> squadrons = SquadronIOJson.readJson();
        assert (squadrons.size() > 0);
        
        boolean success = true;
        for (Squadron squadron : squadrons)
        {
            if (!verifySquadronAircraftTransitions(squadron))
            {
                success = false;
            }
        }
        
        assert(success);
    }
 
    private boolean verifySquadronAircraftTransitions(Squadron squadron) throws PWCGException
    {
        boolean success = true;
        Date lastEndDate = null;
        List<SquadronPlaneAssignment> planeAssignments = squadron.getPlaneAssignments();
        for (SquadronPlaneAssignment planeAssignment : planeAssignments)
        {
            if (lastEndDate == null)
            {
                lastEndDate = planeAssignment.getSquadronWithdrawal();
            }
            else
            {
                Date thisEndDate = planeAssignment.getSquadronWithdrawal();
                thisEndDate = DateUtils.advanceTimeDays(thisEndDate, 1);
                if (thisEndDate.before(lastEndDate))
                {
                    success = false;
                }
                lastEndDate = thisEndDate;
            }
        }
        
        if (lastEndDate.before(DateUtils.getDateYYYYMMDD("19450601")))
        {
            success = false;
        }
        
        return success;
    }
}
