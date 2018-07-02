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

    public void copyTemplate(EquippedPlane equippedPlane)
    {
        super.copyTemplate(equippedPlane);
        equippedPlane.serialNumber = this.serialNumber;
    }
    
    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }
}
