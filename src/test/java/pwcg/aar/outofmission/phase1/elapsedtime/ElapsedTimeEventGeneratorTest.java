package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.data.AARContext;
import pwcg.aar.data.CampaignUpdateData;
import pwcg.campaign.Campaign;
import pwcg.campaign.CampaignPersonnelManager;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
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
    @Mock private IAirfield currentAirfield;
    @Mock private IAirfield newAirfield;

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
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Aussonvillers");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Aussonvillers");

        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        assert (elapsedTimeEvents.getEndOfWarEvent() == null);
        assert (elapsedTimeEvents.getSquadronMoveEvents().size() == 0);
        
    }

    @Test
    public void squadronMove () throws PWCGException
    {             
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Aussonvillers");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Bailleul");

        ElapsedTimeEventGenerator elapsedTimeEventGenerator = new ElapsedTimeEventGenerator(campaign, aarContext);
        ElapsedTimeEvents elapsedTimeEvents = elapsedTimeEventGenerator.createElapsedTimeEvents();
        assert (elapsedTimeEvents.getEndOfWarEvent() == null);
        assert (elapsedTimeEvents.getSquadronMoveEvents().size() > 0);
        
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
        assert (elapsedTimeEvents.getSquadronMoveEvents().size() == 0);
        
    }

}
