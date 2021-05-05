package pwcg.mission.mcu;

public class McuEvent
{
    static public int ONPILOTWOUNDED = 1;
    static public int ONPLANECRASHED = 2;
    static public int ONPLANECRITICALDAMAGED = 3;
    static public int ONPLANEBINGOFUEL = 7;
    static public int ONPLANEBINGOAMMO = 8;
    static public int ONPLANEBINGOBOMBS = 9;
    static public int ONPLANEDAMAGED = 12;
    static public int ONKILLED = 13;
    static public int ONPHOTO = 14;

    private int type = 0;
    private int tarId = 0;

    public McuEvent(int type, int tarId)
    {
        this.type = type;
        this.tarId = tarId;
    }

    public int getType()
    {
        return type;
    }

    public int getTarId()
    {
        return tarId;
    }
}
