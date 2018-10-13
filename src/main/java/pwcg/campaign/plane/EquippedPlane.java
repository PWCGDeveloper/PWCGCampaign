package pwcg.campaign.plane;

import java.util.Date;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.squadmember.SerialNumber;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerGlobal;
import pwcg.core.exception.PWCGException;

public class EquippedPlane extends PlaneType
{
    protected int serialNumber = SerialNumber.NO_SERIAL_NUMBER;
    protected int planeStatus = PlaneStatus.NO_STATUS;
    protected int squadronId;
    protected Date dateRemovedFromService;
    protected String aircraftIdCode;

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

    public String getAircraftIdCode() {
        return aircraftIdCode;
    }

    public void setAircraftIdCode(String aircraftIdCode) {
        this.aircraftIdCode = aircraftIdCode;
    }

    public String getDisplayMarkings() throws PWCGException {
        int generateSkins = ConfigManagerGlobal.getInstance().getIntConfigParam(ConfigItemKeys.GenerateSkinsKey);
        if (generateSkins > 0)
        {
            Campaign campaign = PWCGContext.getInstance().getCampaign();
            return PWCGContext.getInstance().getPlaneMarkingManager().determineDisplayMarkings(campaign, this);
        }
        else
        {
            return Integer.toString(getSerialNumber());
        }
    }
}
