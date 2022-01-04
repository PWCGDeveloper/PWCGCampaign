package pwcg.aar.outofmission.phase1.elapsedtime;

import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.outofmission.phase4.ElapsedTIme.SquadronMoveHandler;
import pwcg.aar.ui.events.model.SquadronMoveEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
public class SquadronMoveHandlerTest
{
    @Mock private Campaign campaign;
    @Mock Company squadron;
    @Mock Airfield currentAirfield;
    @Mock Airfield newAirfield;

    private Date campaignDate;
    private Date newDate;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.MOSCOW_MAP);
                
        campaignDate = DateUtils.getDateYYYYMMDD("19411120");
        newDate = DateUtils.getDateYYYYMMDD("19411215");
        Mockito.when(campaign.getDate()).thenReturn(campaignDate);
    }

    @Test
    public void noSquadronMove () throws PWCGException
    {             
        Mockito.when(squadron.determineCurrentAirfieldName(campaignDate)).thenReturn("Ivanskoe");
        Mockito.when(squadron.determineCurrentAirfieldName(newDate)).thenReturn("Ivanskoe");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate, squadron);
        Assertions.assertTrue (squadronMoveEvent == null);
        
    }

    @Test
    public void squadronMoveNoFerryBecuaseSameMap () throws PWCGException
    {             
        Mockito.when(squadron.determineCurrentAirfieldAnyMap(newDate)).thenReturn(newAirfield);
        Mockito.when(squadron.determineCurrentAirfieldAnyMap(campaignDate)).thenReturn(currentAirfield);
        Mockito.when(squadron.determineCurrentAirfieldName(campaignDate)).thenReturn("Ivanskoe");
        Mockito.when(squadron.determineCurrentAirfieldName(newDate)).thenReturn("Mozhaysk");
        Mockito.when(currentAirfield.getName()).thenReturn("Ivanskoe");
        Mockito.when(newAirfield.getName()).thenReturn("Mozhaysk");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate, squadron);
        Assertions.assertTrue (squadronMoveEvent.getLastAirfield().equals("Ivanskoe"));
        Assertions.assertTrue (squadronMoveEvent.getNewAirfield().equals("Mozhaysk"));
        Assertions.assertTrue (squadronMoveEvent.getDate().equals(newDate));
        Assertions.assertTrue (squadronMoveEvent.isNeedsFerryMission() == true);
        
    }

    @Test
    public void squadronMoveNoFerryBecuaseDifferentMap () throws PWCGException
    {             
        Mockito.when(squadron.determineCurrentAirfieldAnyMap(newDate)).thenReturn(newAirfield);
        Mockito.when(squadron.determineCurrentAirfieldAnyMap(campaignDate)).thenReturn(currentAirfield);
        Mockito.when(squadron.determineCurrentAirfieldName(campaignDate)).thenReturn("Ivanskoe");
        Mockito.when(squadron.determineCurrentAirfieldName(newDate)).thenReturn("Surovikino");
        Mockito.when(currentAirfield.getName()).thenReturn("Ivanskoe");
        Mockito.when(newAirfield.getName()).thenReturn("Surovikino");

        SquadronMoveHandler squadronMoveHandler = new SquadronMoveHandler(campaign);
        SquadronMoveEvent squadronMoveEvent = squadronMoveHandler.squadronMoves(newDate, squadron);
        Assertions.assertTrue (squadronMoveEvent.getLastAirfield().equals("Ivanskoe"));
        Assertions.assertTrue (squadronMoveEvent.getNewAirfield().equals("Surovikino"));
        Assertions.assertTrue (squadronMoveEvent.getDate().equals(newDate));
        Assertions.assertTrue (squadronMoveEvent.isNeedsFerryMission() == false);
    }
}