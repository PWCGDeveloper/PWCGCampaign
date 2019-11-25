package pwcg.campaign.group.airfield.staticobject;

import java.io.BufferedWriter;

import pwcg.campaign.api.ICountry;
import pwcg.campaign.factory.CountryFactory;
import pwcg.campaign.group.Block;
import pwcg.campaign.group.BlockDefinition;
import pwcg.campaign.group.BlockDefinitionManager;
import pwcg.core.constants.AiSkillLevel;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.location.Orientation;
import pwcg.mission.ground.vehicle.IVehicle;
import pwcg.mission.ground.vehicle.IVehicleDefinition;
import pwcg.mission.mcu.McuTREntity;

public class StaticObject extends Block implements IVehicle
{
    @Override
    public void write(BufferedWriter writer) throws PWCGException {
        // TODO Auto-generated method stub
        super.write(writer);
    }

    protected IVehicleDefinition vehicleDefinition;

    private StaticObject() {
    }

    public StaticObject(IVehicleDefinition vehicleDefinition)
    {
        super();
        this.vehicleDefinition = vehicleDefinition;
    }

    public void makeVehicleFromDefinition(ICountry vehicleCountry)
    {
        country = vehicleCountry.getCountry();
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
    public IVehicle clone() {
        StaticObject clone = new StaticObject();
        clone(clone);
        clone.vehicleDefinition = this.vehicleDefinition;
        return clone;
    }

    @Override
    public void populateEntity() throws PWCGException {
    }

    @Override
    public McuTREntity getEntity() {
        return null;
    }

    @Override
    public boolean vehicleExists() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setAiLevel(AiSkillLevel aiLevel) {
    }

    @Override
    public void setCountry(ICountry country) {
        setCountry(country.getCountry());
    }

    @Override
    public void setSpotterRange(int spotterRange) {
    }

    @Override
    public void setBeaconChannel(int beaconChannel) {
    }

    @Override
    public String getDescription() {
        return (vehicleDefinition.getVehicleType() + " / " + script + " / " + model);
    }

    @Override
    public ICountry getCountry() {
        return CountryFactory.makeCountryByCountry(country);
    }

    @Override
    public int getBeaconChannel() {
        return 0;
    }

}
