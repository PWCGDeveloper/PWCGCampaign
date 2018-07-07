package pwcg.aar.ui.events.model;

public class PlaneReplacementEvent extends AAREvent
{
    private int planeSerialNumber;
    private int transferTo;

    public PlaneReplacementEvent()
    {
    }

    public int getPlaneSerialNumber()
    {
        return planeSerialNumber;
    }

    public void setPlaneSerialNumber(int planeSerialNumber)
    {
        this.planeSerialNumber = planeSerialNumber;
    }

    public int getTransferTo()
    {
        return transferTo;
    }

    public void setTransferTo(int transferTo)
    {
        this.transferTo = transferTo;
    }
}
