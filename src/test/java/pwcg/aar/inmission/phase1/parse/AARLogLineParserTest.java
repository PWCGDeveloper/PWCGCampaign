package pwcg.aar.inmission.phase1.parse;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.LogLineParser;
import pwcg.core.logfiles.event.IAType12;
import pwcg.core.logfiles.event.IAType17;
import pwcg.core.logfiles.event.IAType18;
import pwcg.core.logfiles.event.IAType2;
import pwcg.core.logfiles.event.IAType3;
import pwcg.core.logfiles.event.IAType6;

public class AARLogLineParserTest
{
    @Test
    public void testDiscardLinesAfterMissionEnd() throws PWCGException
    {
        List<String> logLines = new ArrayList<String>();
        logLines.add("T:0 AType:15 VER:17");
        logLines.add("T:0 AType:20 USERID:6cfc9723-e261-44cc-b59c-1a1abc71edb5 USERNICKID:beadb889-54ff-4b2c-ba6c-185bc706600b");
        logLines.add("T:0 AType:0 GDate:1942.3.16 GTime:16:30:0 MFile:missions/patrik schorner 1942-03-16.mission MID: GType:0 CNTRS:0:0,101:1,102:1,103:1,201:2,202:2,203:2,301:3,302:3,303:3,304:3,305:3,401:4,402:4 SETTS:11111101111010010000000111001 MODS:0 PRESET:1 AQMID:0 ROUNDS: 1 POINTS: 500");
        
        logLines.add("T:15 AType:12 ID:18431 TYPE:Bf 109 F-4 COUNTRY:201 NAME:Hptm Hubert Romberg PID:-1 POS(121405.1875,42.8003,130806.3281)");
        logLines.add("T:15 AType:12 ID:19455 TYPE:BotCrewMember_Bf109 COUNTRY:201 NAME:BotCrewMember_Bf109 PID:18431 POS(121406.0234,42.6144,130806.5859)");        
        
        logLines.add("T:15 AType:10 PLID:2312191 PID:2313215 BUL:1200 SH:0 BOMB:0 RCT:0 (121387.4766,42.8003,130770.4609) IDS:beadb889-54ff-4b2c-ba6c-185bc706600b LOGIN:6cfc9723-e261-44cc-b59c-1a1abc71edb5 NAME:PatrickAWlson TYPE:Bf 109 F-4 COUNTRY:201 FORM:2 FIELD:2220031 INAIR:1 PARENT:-1 ISPL:1 ISTSTART:1 PAYLOAD:0 FUEL:1.0000 SKIN:bf109f4\\schornerme109f4.dds WM:17");
        logLines.add("T:15 AType:12 ID:2312191 TYPE:Bf 109 F-4 COUNTRY:201 NAME:Maj Patrik Schorner PID:-1 POS(121387.4766,42.8003,130770.4609)");
        logLines.add("T:15 AType:12 ID:2313215 TYPE:BotCrewMember_Bf109 COUNTRY:201 NAME:BotCrewMember_Bf109 PID:2312191 POS(121388.3203,42.6142,130770.7188)");
        
        logLines.add("T:59197 AType:2 DMG:0.0064 AID:2312191 TID:2489343 POS(148986.1250,297.8445,174820.6250)");
        logLines.add("T:59200 AType:2 DMG:0.0007 AID:2312191 TID:2489343 POS(148984.2813,297.3258,174815.6563)");

        logLines.add("T:59880 AType:3 AID:-1 TID:2489343 POS(149364.7813,314.3774,174037.1563)");
        logLines.add("T:44595 AType:18 BOTID:2508799 PARENTID:2507775 POS(150565.6719,165.7078,181386.0781)");
        
        logLines.add("T:44660 AType:6 PID:2507775 POS(150613.3438, 117.9657, 181307.1094)"); 
        
        LogLineParser logLineParser = new LogLineParser();
        LogEventData logEvents = logLineParser.parseLogLinesForMission(logLines);
        
        List<IAType2> damagesEvents = logEvents.getDamageEvents();
        List<IAType3> destroyedEvents = logEvents.getDestroyedEvents();
        List<IAType6> landingEvents = logEvents.getLandingEvents();
        List<IAType12> bots = logEvents.getBots();
        List<IAType12> vehicles = logEvents.getVehicles();
        List<IAType12> turrets = logEvents.getTurrets();
        List<IAType17> waypointEvents = logEvents.getWaypointEvents();
        List<IAType18> bailoutEvents = logEvents.getBailoutEvents();

        assert(damagesEvents.size() == 2);
        assert(destroyedEvents.size() == 1);
        assert(landingEvents.size() == 1);
        assert(bots.size() == 2);
        assert(vehicles.size() == 2);
        assert(turrets.size() == 0);
        assert(waypointEvents.size() == 0);
        assert(bailoutEvents.size() == 1);
    }
 }
