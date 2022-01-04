package integration.campaign.io.json;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.CompanyIOJson;
import pwcg.campaign.plane.SquadronPlaneAssignment;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class SquadronAircraftAssignmentTest
{    
    @Test
    public void verifyValidBoSAirfieldMoveDatesTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        List<Company> squadrons = CompanyIOJson.readJson();
        Assertions.assertTrue (squadrons.size() > 0);
        
        boolean success = true;
        for (Company squadron : squadrons)
        {
            if (!verifySquadronAircraftTransitions(squadron))
            {
                success = false;
            }
        }
        
        assert(success);
    }
 
    private boolean verifySquadronAircraftTransitions(Company squadron) throws PWCGException
    {
        boolean success = true;
        Date lastEndDate = null;
        List<CompanyTankAssignment> planeAssignments = squadron.getPlaneAssignments();
        for (CompanyTankAssignment planeAssignment : planeAssignments)
        {
            if (lastEndDate == null)
            {
                lastEndDate = planeAssignment.getCompanyWithdrawal();
            }
            else
            {
                Date thisEndDate = planeAssignment.getCompanyWithdrawal();
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
