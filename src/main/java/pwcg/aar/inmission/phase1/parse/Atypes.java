package pwcg.aar.inmission.phase1.parse;

public class Atypes
{
    private static String[] ATypes = 
        {
                "AType:0 ",     // "Mission Start", 
                "AType:1 ",     // "Hit", /
                "AType:2 ",     // "Damage ",
                "AType:3 ",     // "Kill ", 
                "AType:4 ",     // "Finished Flight ", 
                "AType:5 ",     // "Take Off ",
                "AType:6 ",     // "Landing ", 
                "AType:7 ",     // "Mission End ",
                "AType:8 ",     // "Mission Complete ", 
                "AType:9 ",     // "Airfield Info ",
                "AType:10 ",    // "Player Spawn ", 
                "AType:11 ",    // "Formation ",
                "AType:12 ",    // "Object Spawn ", 
                "AType:13 ",    // "Influence Area ",
                "AType:14 ",    // "Influence Area Boundary", 
                "AType:15 ",    // "Unknown", 
                "AType:16 ",    // "Unknown", 
                "AType:17 ",    // "Waypoint", 
                "AType:99 "     // "Message"
        };


    public static String getAType(int atypeIndex)
    {
        return ATypes[atypeIndex];
    }
}
