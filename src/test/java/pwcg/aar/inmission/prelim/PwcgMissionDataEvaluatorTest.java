package pwcg.aar.inmission.prelim;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class PwcgMissionDataEvaluatorTest
{
    @Mock
    private Campaign campaign;

    @Mock
    private PwcgMissionData pwcgMissionData;

    @Mock
    CampaignPersonnelManager personnelManager;

    @Mock
    Company squadron;
    
    static int thisSquadronId = 20111052; // JG52

    public PwcgMissionDataEvaluatorTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void determineCrewMembersInMissionTest () throws PWCGException
    {             
        
    }
    
    @Test
    public void determineCrewMembersNotInMissionTest () throws PWCGException
    {             
    }
    
    @Test
    public void determineCrewsInMissionFromPlayerSquadronTest () throws PWCGException
    {             
    }
    
    @Test
    public void determineAxisPlanesInMissionTest () throws PWCGException
    {             
    }
    
    @Test
    public void determineAlliedPlanesInMissionTest () throws PWCGException
    {             
    }

    
}
