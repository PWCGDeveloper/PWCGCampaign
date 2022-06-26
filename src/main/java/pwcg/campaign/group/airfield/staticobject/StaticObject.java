package pwcg.campaign.group.airfield.staticobject;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.BlockDefinition;
import pwcg.campaign.group.BlockDefinitionManager;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.VehicleDefinition;
import pwcg.mission.mcu.McuTREntity;

public class StaticObject extends Block implements IVehicle
{
    protected VehicleDefinition vehicleDefinition;
    protected ICountry country;

    private StaticObject()
    {
    }

    public StaticObject(VehicleDefinition vehicleDefinition)
    {
        super();
        this.vehicleDefinition = vehicleDefinition;
    }

    public void makeVehicleFromDefinition(ICountry vehicleCountry)
    {
        script = "LuaScripts\\WorldObjects\\" + vehicleDefinition.getScriptDir() + vehicleDefinition.getVehicleType() + ".txt";
        model = "graphics\\" + vehicleDefinition.getModelDir() + vehicleDefinition.getVehicleType() + ".mgm";
        setPosition(new Coordinate());
        setOrientation(new Orientation());
        BlockDefinition blockDefinition = BlockDefinitionManager.getInstance().getBlockDefinition(vehicleDefinition.getVehicleType());
        if (blockDefinition != null)
        {
            setDurability(blockDefinition.getDurability());
        }
    }

    @Override
    public IVehicle clone()
    {
        StaticObject clone = new StaticObject();
        clone(clone);
        clone.vehicleDefinition = this.vehicleDefinition;
        return clone;
    }

    @Override
    public void populateEntity() throws PWCGException
    {
    }

    @Override
    public McuTREntity getEntity()
    {
        return null;
    }

    @Override
    public boolean vehicleExists()
    {
        return false;
    }

    @Override
    public void setAiLevel(AiSkillLevel aiLevel)
    {
    }

    @Override
    public void setSpotterRange(int spotterRange)
    {
    }

    @Override
    public void setBeaconChannel(int beaconChannel)
    {
    }

    @Override
    public String getDescription()
    {
        return (vehicleDefinition.getVehicleType() + " / " + script + " / " + model);
    }

    @Override
    public int getBeaconChannel()
    {
        return 0;
    }

    @Override
    public void setEngageable(int engageable)
    {
    }

    @Override
    public void setCountry(ICountry country)
    {
        this.country = country;
    }

    @Override
    public String getVehicleType()
    {
        return desc;
    }

    @Override
    public String getVehicleName()
    {
        return desc;
    }
}
