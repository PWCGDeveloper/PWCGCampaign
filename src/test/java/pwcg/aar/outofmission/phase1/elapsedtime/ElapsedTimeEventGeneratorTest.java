package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.aar.outofmission.phase4.ElapsedTIme.ElapsedTimeEventGenerator;
import pwcg.aar.outofmission.phase4.ElapsedTIme.ElapsedTimeEvents;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.campaign.squadmember.SquadronMembers;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class ElapsedTimeEventGeneratorTest
{
    @Mock private Campaign campaign;
    @Mock private CampaignPersonnelManager personnelManager;
    @Mock private SquadronMembers playerMembers;
    @Mock private SquadronMember player;
    @Mock private AARContext aarContext;
    @Mock private CampaignUpdateData campaignUpdateData;
    @Mock private Squadron squad;
    @Mock private Airfield currentAirfield;
    @Mock private Airfield newAirfield;

    private Date campaignDate;
    private Date newDate;

    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        campaignDate = DateUtils.getDateYYYYMMDD("19420901");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(squad.determineCurrentAirfieldAnyMap(campaignDate)).thenReturn(currentAirfield);
       
        Mockito.when(campaign.getPersonnelManager()).thenReturn(personnelManager);
        Mockito.when(personnelManager.getAllActivePlayers()).thenReturn(playerMembers);
        List<SquadronMember> players = new ArrayList<>();
        players.add(player);
        Mockito.when(playerMembers.getSquadronMemberList()).thenReturn(players);   
        Mockito.when(player.determineSquadron()).thenReturn(squad);   
    }

    @Test
    public void noSquadronMoveOrEndOfWar () throws PWCGException
    {             
        newDate = DateUtils.getDateYYYYMMDD("19420902");
        Mockito.when(aarContext.getNewDate()).thenReturn(newDate);
 
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Tuzov");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Tuzov");

        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        assert (elapsedTimeEvents.getEndOfWarEvent() == null);
        assert (elapsedTimeEvents.getSquadronMoveEvents().size() == 0);
        
    }

    @Test
    public void squadronMove () throws PWCGException
    {             
        campaignDate = DateUtils.getDateYYYYMMDD("19420901");
        newDate = DateUtils.getDateYYYYMMDD("19420909");
        Mockito.when(aarContext.getNewDate()).thenReturn(newDate);
        Mockito.when(squad.determineCurrentAirfieldAnyMap(newDate)).thenReturn(newAirfield);

        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Tuzov");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Kalach");

        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        assert (elapsedTimeEvents.getEndOfWarEvent() == null);
        assert (elapsedTimeEvents.getSquadronMoveEvents().size() > 0);
        
    }

    @Test
    public void endOfWar () throws PWCGException
    {    
        campaignDate = DateUtils.getDateYYYYMMDD("19450502");
        newDate = DateUtils.getDateYYYYMMDD("19450508");
        Mockito.when(aarContext.getNewDate()).thenReturn(newDate);


        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        assert (elapsedTimeEvents.getEndOfWarEvent() != null);
        assert (elapsedTimeEvents.getSquadronMoveEvents().size() == 0);
        
    }

}
