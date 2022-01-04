package pwcg.campaign.tank;

import java.util.Date;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.crewmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class EquippedTank extends TankType
{
    protected int serialNumber = SerialNumber.NO_SERIAL_NUMBER;
    protected int planeStatus = TankStatus.NO_STATUS;
    protected int squadronId;
    protected Date dateRemovedFromService;
    protected String aircraftIdCode;
    protected boolean isEquipmentRequest = false;

    public EquippedTank()
    {
        super();
    }

    public EquippedTank(TankType planeType, int serialNumber, int squadronId, int planeStatus)
    {
        super();
        planeType.copyTemplate(this);
        this.serialNumber = serialNumber;
        this.squadronId = squadronId;
        this.planeStatus = planeStatus;
    }

    public void copyTemplate(EquippedTank equippedPlane)
    {
        super.copyTemplate(equippedPlane);
        equippedPlane.serialNumber = this.serialNumber;
        equippedPlane.squadronId = this.squadronId;
        equippedPlane.dateRemovedFromService = this.dateRemovedFromService;
        equippedPlane.planeStatus = this.planeStatus;
        equippedPlane.aircraftIdCode = this.aircraftIdCode;
    }

    // Backwards compatibility method for version 13.2.0.  Remove when safe.
    public void updateFromTankType()
    {
        try
        {
            if (this.roleCategories.size() == 0)
            {
                TankTypeFactory planeTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
                TankType planeType = planeTypeFactory.getPlaneById(this.getType());
                planeType.copyTemplate(this);

                if(this.roleCategories.size() == 0)
                {
                    PWCGLogger.log(LogLevel.INFO, "Backwards compatibility for 13.2 and earlier.  Equipped plane update did not work");
                }
            }
        }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
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

    public String getAircraftIdCode()
    {
        return aircraftIdCode;
    }

    public void setAircraftIdCode(String aircraftIdCode)
    {
        this.aircraftIdCode = aircraftIdCode;
    }

    public boolean isEquipmentRequest()
    {
        return isEquipmentRequest;
    }

    public void setEquipmentRequest(boolean isEquipmentRequest)
    {
        this.isEquipmentRequest = isEquipmentRequest;
    }
}
