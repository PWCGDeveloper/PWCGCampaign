package pwcg.mission.ground.vehicle;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.api.IPWCGObject;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.location.Orientation;
import pwcg.mission.mcu.McuTREntity;

public interface IVehicle extends IPWCGObject
{
    public void populateEntity();
    public McuTREntity getEntity();
    public void setOrientation(Orientation orientation);
    public boolean vehicleExists();
    public void setAiLevel(AiSkillLevel aiLevel);
    public void setCountry(ICountry country);
}
