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
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class SquadronMoveHandlerTest
{
    @Mock private Campaign campaign;
    @Mock Squadron squad;
    @Mock IAirfield currentAirfield;
    @Mock IAirfield newAirfield;

    private Date campaignDate;
    private Date newDate;

    @Before
    public void setupForTestEnvironment() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
                
        campaignDate = DateUtils.getDateYYYYMMDD("19411120");
        newDate = DateUtils.getDateYYYYMMDD("19411215");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
        Mockito.when(squad.determineCurrentAirfieldAnyMap(campaignDate)).thenReturn(currentAirfield);
        Mockito.when(squad.determineCurrentAirfieldAnyMap(newDate)).thenReturn(newAirfield);
    }

    @Test
    public void noSquadronMove () throws PWCGException
    {             
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Ivanskoe");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Ivanskoe");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate, squad);
        assert (squadronMoveEvent == null);
        
    }

    @Test
    public void squadronMoveNoFerryBecuaseSameMap () throws PWCGException
    {             
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Ivanskoe");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Mozhaysk");
        Mockito.when(currentAirfield.getName()).thenReturn("Ivanskoe");
        Mockito.when(newAirfield.getName()).thenReturn("Mozhaysk");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate, squad);
        assert (squadronMoveEvent.getLastAirfield().equals("Ivanskoe"));
        assert (squadronMoveEvent.getNewAirfield().equals("Mozhaysk"));
        assert (squadronMoveEvent.getDate().equals(campaignDate));
        assert (squadronMoveEvent.isNeedsFerryMission() == true);
        
    }

    @Test
    public void squadronMoveNoFerryBecuaseDifferentMap () throws PWCGException
    {             
        Mockito.when(squad.determineCurrentAirfieldName(campaignDate)).thenReturn("Ivanskoe");
        Mockito.when(squad.determineCurrentAirfieldName(newDate)).thenReturn("Surovikino");
        Mockito.when(currentAirfield.getName()).thenReturn("Ivanskoe");
        Mockito.when(newAirfield.getName()).thenReturn("Surovikino");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate, squad);
        assert (squadronMoveEvent.getLastAirfield().equals("Ivanskoe"));
        assert (squadronMoveEvent.getNewAirfield().equals("Surovikino"));
        assert (squadronMoveEvent.getDate().equals(campaignDate));
        assert (squadronMoveEvent.isNeedsFerryMission() == false);
    }
}