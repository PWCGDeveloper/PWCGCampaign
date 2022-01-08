package pwcg.mission.flight;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.company.Company;
import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.virtual.VirtualWayPointCoordinate;
import pwcg.mission.flight.waypoint.virtual.VirtualWaypointStartFinder;

@ExtendWith(MockitoExtension.class)
public class VirtualWaypointStartFinderNearFrontTest
{
    @Mock private IFlight flight;
    @Mock private Company squadron;
    @Mock private Campaign campaign;
    @Mock private Mission mission;
    @Mock private VirtualWayPointCoordinate vwpCoordinate1;
    @Mock private VirtualWayPointCoordinate vwpCoordinate2;
    @Mock private VirtualWayPointCoordinate vwpCoordinate3;
    @Mock private VirtualWayPointCoordinate vwpCoordinate4;
    @Mock private VirtualWayPointCoordinate vwpCoordinate5;
    @Mock private VirtualWayPointCoordinate vwpCoordinate6;

    @BeforeEach
    public void setupTest() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);
        Mockito.when(flight.getCompany()).thenReturn(squadron);
        Mockito.when(flight.getCampaign()).thenReturn(campaign);
        Mockito.when(squadron.determineSide()).thenReturn(Side.AXIS);
        Mockito.when(campaign.getDate()).thenReturn(DateUtils.getDateYYYYMMDD("19420801"));
        PWCGContext.getInstance().setCampaign(campaign);
        PWCGContext.getInstance().changeContext(FrontMapIdentifier.STALINGRAD_MAP);
    }

    @Test
    public void nearFrontBeginAndEnd() throws PWCGException
    {
        Coordinate coordinate1 = new Coordinate(170000, 0, 190000);
        Coordinate coordinate2 = new Coordinate(170000, 0, 200000);
        Coordinate coordinate3 = new Coordinate(170000, 0, 210000);
        Coordinate coordinate4 = new Coordinate(170000, 0, 220000);
        Coordinate coordinate5 = new Coordinate(170000, 0, 230000);
        Coordinate coordinate6 = new Coordinate(170000, 0, 270000);
        
        Mockito.when(vwpCoordinate1.getPosition()).thenReturn(coordinate1);        
        Mockito.when(vwpCoordinate2.getPosition()).thenReturn(coordinate2);        
        Mockito.when(vwpCoordinate3.getPosition()).thenReturn(coordinate3);        
        Mockito.when(vwpCoordinate4.getPosition()).thenReturn(coordinate4);        
        Mockito.when(vwpCoordinate5.getPosition()).thenReturn(coordinate5);  
        Mockito.when(vwpCoordinate6.getPosition()).thenReturn(coordinate6);  
        
        List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate3);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate5);
        plotCoordinates.add(vwpCoordinate6);

        int startPositionNearFront = VirtualWaypointStartFinder.findStartVwpProximityToFront(flight, plotCoordinates);
        int endPositionNearFront = VirtualWaypointStartFinder.findEndVwpProximityToFront(flight, plotCoordinates);
        assert(startPositionNearFront == 2);
        assert(endPositionNearFront == 5);
    }

    @Test
    public void notNearFrontBeginAndEnd() throws PWCGException
    {
        Coordinate coordinate1 = new Coordinate(170000, 0, 90000);
        Coordinate coordinate2 = new Coordinate(170000, 0, 100000);
        Coordinate coordinate3 = new Coordinate(170000, 0, 110000);
        Coordinate coordinate4 = new Coordinate(170000, 0, 120000);
        Coordinate coordinate5 = new Coordinate(170000, 0, 130000);
        Coordinate coordinate6 = new Coordinate(170000, 0, 170000);
        
        Mockito.when(vwpCoordinate1.getPosition()).thenReturn(coordinate1);        
        Mockito.when(vwpCoordinate2.getPosition()).thenReturn(coordinate2);        
        Mockito.when(vwpCoordinate3.getPosition()).thenReturn(coordinate3);        
        Mockito.when(vwpCoordinate4.getPosition()).thenReturn(coordinate4);        
        Mockito.when(vwpCoordinate5.getPosition()).thenReturn(coordinate5);  
        Mockito.when(vwpCoordinate6.getPosition()).thenReturn(coordinate6);  
        
        List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate3);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate5);
        plotCoordinates.add(vwpCoordinate6);

        int startPositionNearFront = VirtualWaypointStartFinder.findStartVwpProximityToFront(flight, plotCoordinates);
        assert(startPositionNearFront == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);
        int endPositionNearFront = VirtualWaypointStartFinder.findEndVwpProximityToFront(flight, plotCoordinates);
        assert(endPositionNearFront == VirtualWaypointStartFinder.IS_NOT_NEAR_AREA);
    }

    @Test
    public void nearFrontBeginAndEndAllEndWithinRange() throws PWCGException
    {
        Coordinate coordinate1 = new Coordinate(170000, 0, 190000);
        Coordinate coordinate2 = new Coordinate(170000, 0, 200000);
        Coordinate coordinate3 = new Coordinate(170000, 0, 210000);
        Coordinate coordinate4 = new Coordinate(170000, 0, 220000);
        Coordinate coordinate5 = new Coordinate(170000, 0, 230000);
        
        Mockito.when(vwpCoordinate1.getPosition()).thenReturn(coordinate1);        
        Mockito.when(vwpCoordinate2.getPosition()).thenReturn(coordinate2);        
        Mockito.when(vwpCoordinate3.getPosition()).thenReturn(coordinate3);        
        Mockito.when(vwpCoordinate4.getPosition()).thenReturn(coordinate4);        
        Mockito.when(vwpCoordinate5.getPosition()).thenReturn(coordinate5);  
        
        List<VirtualWayPointCoordinate> plotCoordinates = new ArrayList<>();
        plotCoordinates.add(vwpCoordinate1);
        plotCoordinates.add(vwpCoordinate2);
        plotCoordinates.add(vwpCoordinate3);
        plotCoordinates.add(vwpCoordinate4);
        plotCoordinates.add(vwpCoordinate5);

        int startPositionNearFront = VirtualWaypointStartFinder.findStartVwpProximityToFront(flight, plotCoordinates);
        assert(startPositionNearFront == 2);
        int endPositionNearFront = VirtualWaypointStartFinder.findEndVwpProximityToFront(flight, plotCoordinates);
        assert(endPositionNearFront == 4);
    }

}
