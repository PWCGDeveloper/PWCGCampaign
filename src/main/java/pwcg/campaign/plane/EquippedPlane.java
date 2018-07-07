package pwcg.campaign.plane;

import java.util.Date;

import pwcg.campaign.squadmember.SerialNumber;

public class EquippedPlane extends PlaneType
{
    protected int serialNumber = SerialNumber.NO_SERIAL_NUMBER;
    protected int planeStatus = PlaneStatus.NO_STATUS;
    protected int squadronId;
    protected Date dateRemovedFromService;

    public EquippedPlane()
    {
        super();
    }

    public EquippedPlane(PlaneType planeType, int serialNumber, int squadronId, int planeStatus)
    {
        super();
        planeType.copyTemplate(this);
        this.serialNumber = serialNumber;
        this.squadronId = squadronId;
        this.planeStatus = planeStatus;
    }

    public void copyTemplate(EquippedPlane equippedPlane)
    {
        super.copyTemplate(equippedPlane);
        equippedPlane.serialNumber = this.serialNumber;
        equippedPlane.squadronId = this.squadronId;
        equippedPlane.dateRemovedFromService = this.dateRemovedFromService;
        equippedPlane.planeStatus = this.planeStatus;
    }
    
    public int getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber)
    {
        this.serialNumber = serialNumber;
    }
    
    public int getPlaneStatus()
    {
        return planeStatus;
    }

    public void setPlaneStatus(int planeStatus)
    {
        this.planeStatus = planeStatus;
    }

    public Date getDateRemovedFromService()
    {
        return dateRemovedFromService;
    }

    public void setDateRemovedFromService(Date dateRemovedFromService)
    {
        this.dateRemovedFromService = dateRemovedFromService;
    }

    public int getSquadronId()
    {
        return squadronId;
    }

    public void setSquadronId(int squadronId)
    {
        this.squadronId = squadronId;
    }
}
