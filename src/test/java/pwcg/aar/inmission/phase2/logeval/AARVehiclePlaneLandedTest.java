package pwcg.aar.inmission.phase2.logeval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogPlane;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.AType3;
import pwcg.core.logfiles.event.AType6;
import pwcg.core.logfiles.event.IAType3;
import pwcg.core.logfiles.event.IAType6;

@ExtendWith(MockitoExtension.class)
public class AARVehiclePlaneLandedTest
{
    
    @Mock
    private LogEventData logEventData;
    
    @Mock
    AType3 logDestroyedEvent1;
    
    @Mock
    AType3 logDestroyedEvent2;
    
    @Mock
    AType3 logDestroyedEvent3;
    
    @Mock
    AType6 logLandedEvent1;
    
    @Mock
    AType6 logLandedEvent2;

    private Map <String, LogPlane> planeAiEntities = new HashMap<>();

    @BeforeEach
    public void setup () throws PWCGException
    {        
        PWCGContext.setProduct(PWCGProduct.BOS);

        planeAiEntities = makePlaneEntities();

        Mockito.when(logDestroyedEvent1.getVictim()).thenReturn("100");
        Mockito.when(logDestroyedEvent2.getVictim()).thenReturn("101");
        Mockito.when(logDestroyedEvent3.getVictim()).thenReturn("102");
        Mockito.when(logDestroyedEvent2.getLocation()).thenReturn(new Coordinate(200.0, 0.0, 2000.0));
        Mockito.when(logDestroyedEvent3.getLocation()).thenReturn(new Coordinate(300.0, 0.0, 3000.0));
        List<IAType3> logParserDestroyedEvents = new ArrayList<>();
        logParserDestroyedEvents.add(logDestroyedEvent1);
        logParserDestroyedEvents.add(logDestroyedEvent2);
        logParserDestroyedEvents.add(logDestroyedEvent3);
        Mockito.when(logEventData.getDestroyedEvents()).thenReturn(logParserDestroyedEvents);
        

        Mockito.when(logLandedEvent1.getPid()).thenReturn("103");
        Mockito.when(logLandedEvent2.getPid()).thenReturn("104");
        Mockito.when(logLandedEvent2.getLocation()).thenReturn(new Coordinate(500.0, 0.0, 5000.0));
        List<IAType6> logParserLandedEvents = new ArrayList<>();
        logParserLandedEvents.add(logLandedEvent1);
        logParserLandedEvents.add(logLandedEvent2);
        Mockito.when(logEventData.getLandingEvents()).thenReturn(logParserLandedEvents);
    }

    @Test
    public void testAssignLandingLocation () throws PWCGException
    {  
        AARVehiclePlaneLanded vehiclePlaneLanded = new AARVehiclePlaneLanded(logEventData);
        vehiclePlaneLanded.buildLandedLocations(planeAiEntities);
        
        assert(planeAiEntities.get("11111").getLandAt().getXPos() == 200.0);
        assert(planeAiEntities.get("22222").getLandAt().getXPos() == 300.0);
        assert(planeAiEntities.get("99999").getLandAt() == null);
        assert(planeAiEntities.get("44444").getLandAt().getXPos() == 500.0);
    }


    private Map <String, LogPlane> makePlaneEntities()
    {
        LogPlane resultPlane1 = new LogPlane(1);
        resultPlane1.setId("101");
        planeAiEntities.put("11111", resultPlane1);
        
        LogPlane resultPlane2 = new LogPlane(2);
        resultPlane2.setId("102");
        planeAiEntities.put("22222", resultPlane2);
        
        LogPlane resultPlane3 = new LogPlane(3);
        resultPlane3.setId("999");
        planeAiEntities.put("99999", resultPlane3);
        
        LogPlane resultPlane4 = new LogPlane(4);
        resultPlane4.setId("104");
        planeAiEntities.put("44444", resultPlane4);
                
        return planeAiEntities;
    }

}
