package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pwcg.aar.inmission.phase1.parse.AARLogEventData;
import pwcg.aar.inmission.phase1.parse.event.IAType17;
import pwcg.aar.inmission.phase1.parse.event.rof.AType17;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogWaypoint;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class AARWaypointBuilderTest
{
    @Mock
    private AARLogEventData logEventData;

    @Mock
    AType17 wpEvent;
    
    @Before
    public void setup() throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.ROF);
    }

    @Test
    public void testWaypointListCreation() throws PWCGException
    {
        Coordinate coordinate = new Coordinate();
        coordinate.setXPos(100000.0);
        coordinate.setZPos(90000.0);
        coordinate.setYPos(3000.0);
        
        Mockito.when(wpEvent.getLocation()).thenReturn(coordinate);
        Mockito.when(wpEvent.getSequenceNum()).thenReturn(10);

        List<IAType17> waypointEvents = new ArrayList<>();
        waypointEvents.add(wpEvent);
        waypointEvents.add(wpEvent);
        waypointEvents.add(wpEvent);
        waypointEvents.add(wpEvent);
        waypointEvents.add(wpEvent);
        Mockito.when(logEventData.getWaypointEvents()).thenReturn(waypointEvents);

        AARWaypointBuilder aarWaypointBuilder = new AARWaypointBuilder(logEventData);
        List<LogWaypoint> waypointEventList = aarWaypointBuilder.buildWaypointEvents();
        
        assert(waypointEventList.size() == 5);
        for (LogWaypoint waypointEvent : waypointEventList)
        {
            Coordinate wpCoord = waypointEvent.getLocation();
            assert(wpCoord.getXPos() == 100000.0);
            assert(wpCoord.getZPos() == 90000.0);
            assert(wpCoord.getYPos() == 3000.0);
            assert(waypointEvent.getSequenceNum() == 10);
        }
    }

}
