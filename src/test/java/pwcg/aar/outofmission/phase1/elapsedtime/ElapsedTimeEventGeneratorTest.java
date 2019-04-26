package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class ElapsedTimeEventGeneratorTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    private AARContext aarContext;
    
    @Mock
    private CampaignUpdateData campaignUpdateData;
    
    @Mock
    private Squadron squad;
    
    @Mock
    private IAirfield currentAirfield;
    
    @Mock
    private IAirfield newAirfield;

    private Date campaignDate;
    private Date newDate;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        campaignDate = DateUtils.getDateYYYYMMDD("19170420");
        newDate = DateUtils.getDateYYYYMMDD("19170430");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(squad.determineCurrentAirfieldAnyMap(campaignDate)).thenReturn(currentAirfield);
        Mockito.when(squad.determineCurrentAirfieldAnyMap(newDate)).thenReturn(newAirfield);

        Mockito.when(aarContext.getCampaignUpdateData()).thenReturn(campaignUpdateData);
        Mockito.when(aarContext.getNewDate()).thenReturn(newDate);
    }

    @Test
    public void noSquadronMoveOrEndOfWar () throws PWCGException
    {             
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Aussonvillers");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Aussonvillers");

        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        assert (elapsedTimeEvents.getEndOfWarEvent() == null);
        assert (elapsedTimeEvents.getSquadronMoveEvent() == null);
        
    }

    @Test
    public void squadronMove () throws PWCGException
    {             
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Aussonvillers");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Bailleul");

        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        assert (elapsedTimeEvents.getEndOfWarEvent() == null);
        assert (elapsedTimeEvents.getSquadronMoveEvent() != null);
        
    }

    @Test
    public void endOfWar () throws PWCGException
    {    
        campaignDate = DateUtils.getDateYYYYMMDD("19181020");
        newDate = DateUtils.getDateYYYYMMDD("19181112");
        Mockito.when(aarContext.getNewDate()).thenReturn(newDate);

        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Aussonvillers");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Aussonvillers");
        Mockito.when(squad.determineCurrentAirfieldAnyMap(campaignDate)).thenReturn(currentAirfield);
        Mockito.when(squad.determineCurrentAirfieldAnyMap(newDate)).thenReturn(newAirfield);

        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        assert (elapsedTimeEvents.getEndOfWarEvent() != null);
        assert (elapsedTimeEvents.getSquadronMoveEvent() == null);
        
    }

}
