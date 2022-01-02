package pwcg.core.logfiles;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import pwcg.core.exception.PWCGException;

public class AARLogKeeperTest
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
        logLines.add("T:15 AType:10 PLID:18431 PID:19455 BUL:0 SH:0 BOMB:0 RCT:0 (121405.1875,42.8003,130806.3281) IDS:00000000-0000-0000-0000-100000000000 LOGIN:00000000-0000-0000-0000-100000000000 NAME: TYPE:Bf 109 F-4 COUNTRY:201 FORM:3 FIELD:2220031 INAIR:1 PARENT:-1 ISPL:0 ISTSTART:1 PAYLOAD:0 FUEL:1.0000 SKIN:bf109f4\\I_JG51.dds WM:17");
        
        logLines.add("T:61997 AType:4 PLID:2312191 PID:2313215 BUL:396 SH:0 BOMB:0 RCT:0 (149446.6094,1072.7592,170215.6406)");
        logLines.add("T:61998 AType:16 BOTID:2313215 POS(149446.7813,1072.9946,170214.8125)");
        logLines.add("T:61999 AType:7");
        logLines.add("T:61999 AType:4 PLID:2333695 PID:2334719 BUL:0 SH:0 BOMB:0 RCT:0 (148503.6563,359.6019,109556.7734)");
        logLines.add("T:61999 AType:4 PLID:2357247 PID:2358271 BUL:0 SH:0 BOMB:0 RCT:0 (125867.7109,803.4151,96754.4375)");
        logLines.add("T:61999 AType:2 DMG:1.0000 AID:-1 TID:2312191 POS(149446.8906,1073.2948,170213.9844)");
        logLines.add("T:61999 AType:3 AID:-1 TID:2312191 POS(149446.8906,1073.2948,170213.9844)");
        
        List<String> keptLines = LogKeeper.selectLogLinesToKeep(logLines);
        int playerSpawnsFound = 0;
        int spawnsFound = 0;
        int destroyedFound = 0;
        for (String line : keptLines)
        {
            assert(line.contains("AType:3") == false);
            
            if (line.contains("AType:12"))
            {
                ++spawnsFound;
            }

            if (line.contains("AType:10"))
            {
                ++playerSpawnsFound;
            }

            if (line.contains("AType:3"))
            {
                ++destroyedFound;
            }
        }
        
        assert(spawnsFound == 2);
        assert(playerSpawnsFound == 1);
        assert(destroyedFound == 0);
    }
    
    @Test
    public void testKeepDestroyedBeforeMissionEnd() throws PWCGException
    {
        List<String> logLines = new ArrayList<String>();
        logLines.add("T:0 AType:15 VER:17");
        logLines.add("T:0 AType:20 USERID:6cfc9723-e261-44cc-b59c-1a1abc71edb5 USERNICKID:beadb889-54ff-4b2c-ba6c-185bc706600b");
        logLines.add("T:0 AType:0 GDate:1942.3.16 GTime:16:30:0 MFile:missions/patrik schorner 1942-03-16.mission MID: GType:0 CNTRS:0:0,101:1,102:1,103:1,201:2,202:2,203:2,301:3,302:3,303:3,304:3,305:3,401:4,402:4 SETTS:11111101111010010000000111001 MODS:0 PRESET:1 AQMID:0 ROUNDS: 1 POINTS: 500");
        logLines.add("T:15 AType:12 ID:18431 TYPE:Bf 109 F-4 COUNTRY:201 NAME:Hptm Hubert Romberg PID:-1 POS(121405.1875,42.8003,130806.3281)");
        logLines.add("T:15 AType:12 ID:19455 TYPE:BotCrewMember_Bf109 COUNTRY:201 NAME:BotCrewMember_Bf109 PID:18431 POS(121406.0234,42.6144,130806.5859)");
        logLines.add("T:15 AType:10 PLID:18431 PID:19455 BUL:0 SH:0 BOMB:0 RCT:0 (121405.1875,42.8003,130806.3281) IDS:00000000-0000-0000-0000-100000000000 LOGIN:00000000-0000-0000-0000-100000000000 NAME: TYPE:Bf 109 F-4 COUNTRY:201 FORM:3 FIELD:2220031 INAIR:1 PARENT:-1 ISPL:0 ISTSTART:1 PAYLOAD:0 FUEL:1.0000 SKIN:bf109f4\\I_JG51.dds WM:17");
        logLines.add("T:61999 AType:3 AID:-1 TID:2312191 POS(149446.8906,1073.2948,170213.9844)");
        
        logLines.add("T:61997 AType:4 PLID:2312191 PID:2313215 BUL:396 SH:0 BOMB:0 RCT:0 (149446.6094,1072.7592,170215.6406)");
        logLines.add("T:61998 AType:16 BOTID:2313215 POS(149446.7813,1072.9946,170214.8125)");
        logLines.add("T:61999 AType:7");
        logLines.add("T:61999 AType:4 PLID:2333695 PID:2334719 BUL:0 SH:0 BOMB:0 RCT:0 (148503.6563,359.6019,109556.7734)");
        logLines.add("T:61999 AType:4 PLID:2357247 PID:2358271 BUL:0 SH:0 BOMB:0 RCT:0 (125867.7109,803.4151,96754.4375)");
        logLines.add("T:61999 AType:2 DMG:1.0000 AID:-1 TID:2312191 POS(149446.8906,1073.2948,170213.9844)");
        
        List<String> keptLines = LogKeeper.selectLogLinesToKeep(logLines);
        int playerSpawnsFound = 0;
        int spawnsFound = 0;
        int destroyedFound = 0;
        for (String line : keptLines)
        {
            if (line.contains("AType:12"))
            {
                ++spawnsFound;
            }

            if (line.contains("AType:10"))
            {
                ++playerSpawnsFound;
            }

            if (line.contains("AType:3"))
            {
                ++destroyedFound;
            }
        }
        
        assert(spawnsFound == 2);
        assert(playerSpawnsFound == 1);
        assert(destroyedFound == 1);
    }
    
    @Test
    public void testKeepAfternePlayerLeft() throws PWCGException
    {
        List<String> logLines = new ArrayList<String>();
        logLines.add("T:0 AType:15 VER:17");
        logLines.add("T:0 AType:20 USERID:6cfc9723-e261-44cc-b59c-1a1abc71edb5 USERNICKID:beadb889-54ff-4b2c-ba6c-185bc706600b");
        logLines.add("T:0 AType:0 GDate:1942.3.16 GTime:16:30:0 MFile:missions/patrik schorner 1942-03-16.mission MID: GType:0 CNTRS:0:0,101:1,102:1,103:1,201:2,202:2,203:2,301:3,302:3,303:3,304:3,305:3,401:4,402:4 SETTS:11111101111010010000000111001 MODS:0 PRESET:1 AQMID:0 ROUNDS: 1 POINTS: 500");
        logLines.add("T:15 AType:12 ID:18431 TYPE:Bf 109 F-4 COUNTRY:201 NAME:Hptm Hubert Romberg PID:-1 POS(121405.1875,42.8003,130806.3281)");
        logLines.add("T:15 AType:12 ID:19455 TYPE:BotCrewMember_Bf109 COUNTRY:201 NAME:BotCrewMember_Bf109 PID:18431 POS(121406.0234,42.6144,130806.5859)");
        logLines.add("T:15 AType:10 PLID:18431 PID:19455 BUL:0 SH:0 BOMB:0 RCT:0 (121405.1875,42.8003,130806.3281) IDS:00000000-0000-0000-0000-100000000000 LOGIN:00000000-0000-0000-0000-100000000000 NAME: TYPE:Bf 109 F-4 COUNTRY:201 FORM:3 FIELD:2220031 INAIR:1 PARENT:-1 ISPL:0 ISTSTART:1 PAYLOAD:0 FUEL:1.0000 SKIN:bf109f4\\I_JG51.dds WM:17");
        logLines.add("T:61999 AType:4 PLID:2333695 PID:2334719 BUL:0 SH:0 BOMB:0 RCT:0 (148503.6563,359.6019,109556.7734)");
        logLines.add("T:61999 AType:3 AID:-1 TID:2312191 POS(149446.8906,1073.2948,170213.9844)");
        
        logLines.add("T:61997 AType:4 PLID:2312191 PID:2313215 BUL:396 SH:0 BOMB:0 RCT:0 (149446.6094,1072.7592,170215.6406)");
        logLines.add("T:61998 AType:16 BOTID:2313215 POS(149446.7813,1072.9946,170214.8125)");
        logLines.add("T:61999 AType:7");
        logLines.add("T:61999 AType:4 PLID:2357247 PID:2358271 BUL:0 SH:0 BOMB:0 RCT:0 (125867.7109,803.4151,96754.4375)");
        logLines.add("T:61999 AType:2 DMG:1.0000 AID:-1 TID:2312191 POS(149446.8906,1073.2948,170213.9844)");
        
        List<String> keptLines = LogKeeper.selectLogLinesToKeep(logLines);
        int playerSpawnsFound = 0;
        int spawnsFound = 0;
        int destroyedFound = 0;
        for (String line : keptLines)
        {
            if (line.contains("AType:12"))
            {
                ++spawnsFound;
            }

            if (line.contains("AType:10"))
            {
                ++playerSpawnsFound;
            }

            if (line.contains("AType:3"))
            {
                ++destroyedFound;
            }
        }
        
        assert(spawnsFound == 2);
        assert(playerSpawnsFound == 1);
        assert(destroyedFound == 1);
    }

}
