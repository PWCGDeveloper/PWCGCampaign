package pwcg.aar.ui.events.model;

public class AceKilledEvent extends AARPilotEvent
{
    private String status = "";
    private int serialNumber = 0;

    public AceKilledEvent(int squadronId, int serialNumber)
    {
        super(squadronId);
        this.serialNumber = serialNumber;
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
