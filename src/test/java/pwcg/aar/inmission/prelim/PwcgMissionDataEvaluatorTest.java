package pwcg.aar.inmission.prelim;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.prelim.PwcgMissionData;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

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
        PWCGContextManager.setRoF(false);
        Date campaignDate = DateUtils.getDateYYYYMMDD("19420420");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(squadron.getSquadronId()).thenReturn(thisSquadronId);
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
