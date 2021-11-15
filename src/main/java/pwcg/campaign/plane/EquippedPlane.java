package pwcg.campaign.plane;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class EquippedPlane extends PlaneType
{
    protected int serialNumber = SerialNumber.NO_SERIAL_NUMBER;
    protected int planeStatus = PlaneStatus.NO_STATUS;
    protected int squadronId;
    protected Date dateRemovedFromService;
    protected String aircraftIdCode;
    protected boolean isEquipmentRequest = false;

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
        equippedPlane.aircraftIdCode = this.aircraftIdCode;
    }

    // Backwards compatibility method for version 13.2.0.  Remove when safe.
    public void updateFromPlaneType()
    {
        try
        {
            if (this.roleCategories.size() == 0)
            {
                PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
                PlaneType planeType = planeTypeFactory.getPlaneById(this.getType());
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

    public String getDisplayMarkings() throws PWCGException {
        Campaign campaign = PWCGContext.getInstance().getCampaign();
        return campaign.getPlaneMarkingManager().determineDisplayMarkings(campaign, this);
    }
}
