package pwcg.campaign.tank;

import java.util.Date;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.RandomNumberGenerator;

public class TankEquipmentFactory
{
    public static EquippedTank makeTankForSquadron (Campaign campaign, String tankTypeName, int squadronId) throws PWCGException
    {
        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        TankType tankType = tankTypeFactory.createTankTypeByType(tankTypeName);        
        EquippedTank equippedPlane = new EquippedTank(tankType, campaign.getSerialNumber().getNextPlaneSerialNumber(), squadronId, TankStatus.STATUS_DEPLOYED);

        return equippedPlane;
    }

    public static EquippedTank makePlaneForDepot (Campaign campaign, String tankTypeName) throws PWCGException
    {
        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        TankType tankType = tankTypeFactory.createTankTypeByType(tankTypeName);        
        EquippedTank equippedPlane = new EquippedTank(tankType, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, TankStatus.STATUS_DEPOT);

        return equippedPlane;
    }

    public static EquippedTank makePlaneForBeforeCampaign (Campaign campaign, Side side, Date date) throws PWCGException
    {
        TankTypeFactory tankTypeFactory = PWCGContext.getInstance().getTankTypeFactory();
        List<TankType> tankTypes = tankTypeFactory.createActiveTankTypesForDateAndSide(side, date);
        int index = RandomNumberGenerator.getRandom(tankTypes.size());
        TankType tankType = tankTypes.get(index);
        
        EquippedTank equippedPlane = new EquippedTank(tankType, campaign.getSerialNumber().getNextPlaneSerialNumber(), -1, TankStatus.STATUS_DEPOT);
        return equippedPlane;
    }

}
