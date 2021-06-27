package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pwcg.aar.outofmission.phase4.ElapsedTIme.SquadronMoveHandler;
import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@RunWith(MockitoJUnitRunner.class)
public class SquadronMoveHandlerTest
{
    @Mock private Campaign campaign;
    @Mock Squadron squadron;
    @Mock Airfield currentAirfield;
    @Mock Airfield newAirfield;

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
        Mockito.when(squadron.determineCurrentAirfieldAnyMap(campaignDate)).thenReturn(currentAirfield);
        Mockito.when(squadron.determineCurrentAirfieldAnyMap(newDate)).thenReturn(newAirfield);
    }

    @Test
    public void noSquadronMove () throws PWCGException
    {             
        Mockito.when(squadron.determineCurrentAirfieldName(campaignDate)).thenReturn("Ivanskoe");
        Mockito.when(squadron.determineCurrentAirfieldName(newDate)).thenReturn("Ivanskoe");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate, squadron);
        assert (squadronMoveEvent == null);
        
    }

    @Test
    public void squadronMoveNoFerryBecuaseSameMap () throws PWCGException
    {             
        Mockito.when(squadron.determineCurrentAirfieldName(campaignDate)).thenReturn("Ivanskoe");
        Mockito.when(squadron.determineCurrentAirfieldName(newDate)).thenReturn("Mozhaysk");
        Mockito.when(currentAirfield.getName()).thenReturn("Ivanskoe");
        Mockito.when(newAirfield.getName()).thenReturn("Mozhaysk");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate, squadron);
        assert (squadronMoveEvent.getLastAirfield().equals("Ivanskoe"));
        assert (squadronMoveEvent.getNewAirfield().equals("Mozhaysk"));
        assert (squadronMoveEvent.getDate().equals(newDate));
        assert (squadronMoveEvent.isNeedsFerryMission() == true);
        
    }

    @Test
    public void squadronMoveNoFerryBecuaseDifferentMap () throws PWCGException
    {             
        Mockito.when(squadron.determineCurrentAirfieldName(campaignDate)).thenReturn("Ivanskoe");
        Mockito.when(squadron.determineCurrentAirfieldName(newDate)).thenReturn("Surovikino");
        Mockito.when(currentAirfield.getName()).thenReturn("Ivanskoe");
        Mockito.when(newAirfield.getName()).thenReturn("Surovikino");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate, squadron);
        assert (squadronMoveEvent.getLastAirfield().equals("Ivanskoe"));
        assert (squadronMoveEvent.getNewAirfield().equals("Surovikino"));
        assert (squadronMoveEvent.getDate().equals(newDate));
        assert (squadronMoveEvent.isNeedsFerryMission() == false);
    }
}