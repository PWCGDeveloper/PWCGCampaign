package pwcg.aar.inmission.phase1.parse.event;

public enum AType
{
    ATYPE0("AType:0 ", "Mission Start"),
    ATYPE1("AType:1 ", "Hit"),
    ATYPE2("AType:2 ", "Damage"),
    ATYPE3("AType:3 ", "Kill"),
    ATYPE4("AType:4 ", "Finished Flight"),
    ATYPE5("AType:5 ", "Take Off"),
    ATYPE6("AType:6 ", "Landing"),
    ATYPE7("AType:7 ", "Mission End"),
    ATYPE8("AType:8 ", "Mission Complete"),
    ATYPE9("AType:9 ", "Airfield Info"),
    ATYPE10("AType:10 ", "Player Spawn"),
    ATYPE11("AType:11 ", "Formation"),
    ATYPE12("AType:12 ", "Object Spawn"),
    ATYPE13("AType:13 ", "Influence Area"),
    ATYPE14("AType:14 ", "Influence Area Boundary"),
    ATYPE15("AType:15 ", "Version"),
    ATYPE16("AType:16 ", "Last Player Position"),
    ATYPE17("AType:17 ", "Waypoint"),
    ATYPE18("AType:18 ", "Bail out"),
    ATYPE99("AType:99 ", "Message");


    private String atypeLogIdentifier;
    private String atypeDesc;

    private AType(String atypeLogIdentifier, String atypeDesc)
    {
        this.atypeLogIdentifier = atypeLogIdentifier;
        this.atypeDesc = atypeDesc;
    }

    public String getAtypeLogIdentifier()
    {
        return atypeLogIdentifier;
    }

    public String getAtypeDesc()
    {
        return atypeDesc;
    }
}
