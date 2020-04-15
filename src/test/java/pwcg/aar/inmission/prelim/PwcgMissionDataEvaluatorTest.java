package pwcg.aar.inmission.prelim;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;

@RunWith(MockitoJUnitRunner.class)
public class PwcgMissionDataEvaluatorTest
{
    @Mock
    private Campaign campaign;

    @Mock
    private PwcgMissionData pwcgMissionData;

    @Mock
    CampaignPersonnelManager personnelManager;

    @Mock
    Squadron squadron;
    
    static int thisSquadronId = 20111052; // JG52

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
    }

    @Test
    public void determineSquadronMembersInMissionTest () throws PWCGException
    {             
        
    }
    
    @Test
    public void determineSquadronMembersNotInMissionTest () throws PWCGException
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
