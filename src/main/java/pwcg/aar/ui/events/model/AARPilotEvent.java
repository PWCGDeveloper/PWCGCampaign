package pwcg.aar.ui.events.model;

public class AARPilotEvent extends AAREvent
{
    public static final int ALL_SQUADRONS = -1;

    private Integer squadronId = ALL_SQUADRONS;
    private String squadron = "";
    private String pilotName;
    
    public AARPilotEvent(int squadronId)
    {
        this.squadronId = squadronId;
    }

    public Integer getSquadronId()
    {
        return squadronId;
    }

    public void setSquadronId(Integer squadronId)
    {
        this.squadronId = squadronId;
    }

    public String getSquadron()
    {
        return squadron;
    }

    public void setSquadron(String squadron)
    {
        this.squadron = squadron;
    }
    
    public String getPilotName()
    {
        return pilotName;
    }

    public void setPilotName(String pilotName)
    {
        this.pilotName = pilotName;
    }
}
