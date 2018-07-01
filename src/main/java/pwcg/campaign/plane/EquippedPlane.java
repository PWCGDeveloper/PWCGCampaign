package pwcg.campaign.plane;

import pwcg.campaign.squadmember.SerialNumber;

public class EquippedPlane extends PlaneType
{
    protected int serialNumber = SerialNumber.NO_SERIAL_NUMBER;

    public EquippedPlane()
    {
        super();
    }

    public EquippedPlane(PlaneType planeType, int serialNumber)
    {
        super();
        planeType.copyTemplate(this);
        this.serialNumber = serialNumber;
    }

    public int getSerialNumber()
    {
        return serialNumber;
    }
}
