package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.api.IAirfield;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class SquadronMoveHandlerTest
{
    @Mock
    private Campaign campaign;
    
    @Mock
    Squadron squad;
    
    @Mock
    IAirfield currentAirfield;
    
    @Mock
    IAirfield newAirfield;

    private Date campaignDate;
    private Date newDate;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContextManager.setRoF(true);
        PWCGContextManager.getInstance().changeContext(FrontMapIdentifier.FRANCE_MAP);
                
        campaignDate = DateUtils.getDateYYYYMMDD("19170420");
        newDate = DateUtils.getDateYYYYMMDD("19170430");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(squad.determineCurrentAirfieldAnyMap(campaignDate)).thenReturn(currentAirfield);
        Mockito.when(squad.determineCurrentAirfieldAnyMap(newDate)).thenReturn(newAirfield);
    }

    @Test
    public void noSquadronMove () throws PWCGException
    {             
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Aussonvillers");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Aussonvillers");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate);
        assert (squadronMoveEvent == null);
        
    }

    @Test
    public void squadronMoveNoFerryBecauseTooFar () throws PWCGException
    {             
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Aussonvillers");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Bailleul");
        Mockito.when(currentAirfield.getName()).thenReturn("Aussonvillers");
        Mockito.when(newAirfield.getName()).thenReturn("Bailleul");
        
        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate);
        assert (squadronMoveEvent.getLastAirfield()equals("Aussonvillers"));
        assert (squadronMoveEvent.getNewAirfield().equals("Bailleul"));
        assert (squadronMoveEvent.getDate().equals(campaignDate));
        assert (squadronMoveEvent.isNeedsFerryMission() == false);
        
    }

    @Test
    public void squadronMoveNoFerryBecuaseDifferentMap () throws PWCGException
    {             
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Aussonvillers");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Hoog Huys");
        Mockito.when(currentAirfield.getName()).thenReturn("Aussonvillers");
        Mockito.when(newAirfield.getName()).thenReturn("Hoog Huys");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate);
        assert (squadronMoveEvent.getLastAirfield().equals("Aussonvillers"));
        assert (squadronMoveEvent.getNewAirfield().equals("Hoog Huys"));
        assert (squadronMoveEvent.getDate().equals(campaignDate));
        assert (squadronMoveEvent.isNeedsFerryMission() == false);
        
    }

    @Test
    public void squadronMoveNeedsFerry () throws PWCGException
    {             
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Houplin,S Lille");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Lomme,Lille");
        Mockito.when(currentAirfield.getName()).thenReturn("Houplin,S Lille");
        Mockito.when(newAirfield.getName()).thenReturn("Lomme,Lille");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate);
        assert (squadronMoveEvent.getLastAirfield().equals("Houplin,S Lille"));
        assert (squadronMoveEvent.getNewAirfield().equals("Lomme,Lille"));
        assert (squadronMoveEvent.getDate().equals(campaignDate));
        assert (squadronMoveEvent.isNeedsFerryMission() == true);
        
    }
}
