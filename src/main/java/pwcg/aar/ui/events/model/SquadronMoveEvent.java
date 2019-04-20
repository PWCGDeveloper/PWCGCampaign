package pwcg.aar.ui.events.model;

public class SquadronMoveEvent  extends AAREvent
{
    private String newAirfield = null;
    private String lastAirfield = null;
    private String status = "";
    private String squadron = null;
    private boolean needsFerryMission = false;

    public SquadronMoveEvent ()
    {
    }

    public String getNewAirfield()
    {
        return newAirfield;
    }

    public void setNewAirfield(String newAirfield)
    {
        this.newAirfield = newAirfield;
    }

    public String getLastAirfield()
    {
        return lastAirfield;
    }

    public void setLastAirfield(String lastAirfield)
    {
        this.lastAirfield = lastAirfield;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getSquadron()
    {
        return squadron;
    }

    public void setSquadron(String squadron)
    {
        this.squadron = squadron;
    }

    public boolean isNeedsFerryMission()
    {
        return needsFerryMission;
    }

    public void setNeedsFerryMission(boolean needsFerryMission)
    {
        this.needsFerryMission = needsFerryMission;
    }
}
