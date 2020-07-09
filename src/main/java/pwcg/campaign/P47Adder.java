package pwcg.campaign;

import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.PlaneType;
import pwcg.campaign.plane.PlaneTypeFactory;
import pwcg.campaign.squadron.Squadron;
import pwcg.campaign.squadron.SquadronManager;
import pwcg.core.exception.PWCGException;

public class P47Adder
{
    private Campaign campaign;
    
    public P47Adder(Campaign campaign)
    {
        this.campaign = campaign;
    }

    public void addP47D22() throws PWCGException
    {
        if (!hasBeenConverted())
        {
            if (hasP47s())
            {
                SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
                List<Squadron> squadronsToCheck = squadronManager.getActiveSquadrons(campaign.getDate());
                for (Squadron squadron : squadronsToCheck)
                {
                    if (squadron.getActiveArchTypes(campaign.getDate()).get(0).equals("p47"))
                    {
                        Equipment equipmentManager = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
                        convertSomeToEarly(equipmentManager);
                    }
                }
                
                campaign.write();
            }
        }
    }

    private boolean hasBeenConverted() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsToCheck = squadronManager.getActiveSquadrons(campaign.getDate());
        for (Squadron squadron : squadronsToCheck)
        {
            Equipment equipmentManager = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
            for (EquippedPlane plane : equipmentManager.getActiveEquippedPlanes().values())
            {
                if (plane.getType().equals("p47d22"))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasP47s() throws PWCGException
    {
        SquadronManager squadronManager = PWCGContext.getInstance().getSquadronManager();
        List<Squadron> squadronsToCheck = squadronManager.getActiveSquadrons(campaign.getDate());
        for (Squadron squadron : squadronsToCheck)
        {
            Equipment equipmentManager = campaign.getEquipmentManager().getEquipmentForSquadron(squadron.getSquadronId());
            for (EquippedPlane plane : equipmentManager.getActiveEquippedPlanes().values())
            {
                if (plane.getType().equals("p47d28"))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean convertSomeToEarly(Equipment equipmentManager) throws PWCGException
    {
        double allPlanes = equipmentManager.getActiveEquippedPlanes().size();
        int numToConvert = Double.valueOf((allPlanes * .4)).intValue();
        int numConverted = 0;
        for (EquippedPlane plane : equipmentManager.getActiveEquippedPlanes().values())
        {
            if (plane.getType().equals("p47d28"))
            {
                PlaneTypeFactory planeTypeFactory = PWCGContext.getInstance().getPlaneTypeFactory();
                PlaneType earlyP47PlaneType =  planeTypeFactory.getPlaneById("p47d22");
                plane.setDesc(earlyP47PlaneType.getDesc());
                plane.setDisplayName(earlyP47PlaneType.getDisplayName());
                plane.setGoodness(earlyP47PlaneType.getGoodness());
                plane.setIntroduction(earlyP47PlaneType.getIntroduction());
                plane.setModel(earlyP47PlaneType.getModel());
                plane.setScript(earlyP47PlaneType.getScript());
                plane.setType(earlyP47PlaneType.getType());
                plane.setWithdrawal(earlyP47PlaneType.getWithdrawal());

                ++numConverted;
            }
            if (numConverted == numToConvert)
            {
                break;
            }
        }
        return false;
    }
}
