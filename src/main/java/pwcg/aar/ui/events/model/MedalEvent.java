package pwcg.aar.ui.events.model;

public class MedalEvent  extends AARPilotEvent
{
	private String medal = "";
    private int serialNumber = 0;

    public MedalEvent(int squadronId, int serialNumber)
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

	public String getMedal() {
		return medal;
	}

	public void setMedal(String medal) {
		this.medal = medal;
	}
}
