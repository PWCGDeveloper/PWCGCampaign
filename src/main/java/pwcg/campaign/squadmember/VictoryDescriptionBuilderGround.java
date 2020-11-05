package pwcg.campaign.squadmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.group.BlockDefinition;
import pwcg.campaign.group.BlockDefinitionManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.mission.ground.vehicle.IVehicleDefinition;
import pwcg.mission.ground.vehicle.VehicleDefinitionManager;

public class VictoryDescriptionBuilderGround extends VictoryDescriptionBuilderBase
{    
    VictoryDescriptionBuilderGround (Campaign campaign, Victory victory)
    {
        super(campaign, victory);
    }    

    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() ground unit> was destroyed by by <victory.getVictor() name> of <victory.getVictor() squadron name> 
    // <victory.getVictor() crew 1> and <victory.getVictor() crew 1> were flying a <victory.getVictor() plane Name>.
    public String createVictoryDescriptionAirToGround() throws PWCGException
    {
        String victoryDesc = "";
        
        String victorPlaneType = getPlaneDescription(victory.getVictor().getType());

        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate());
        if (!victory.getLocation().isEmpty())
        {
            victoryDesc +=  " near " + victory.getLocation();
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "\n";
        victoryDesc +=  "A " + getGroundUnitName(victory.getVictim()) + " was destroyed by ";
        victoryDesc += describeVictor();
        victoryDesc += ".";

        // Line 3
        victoryDesc +=  "\n";
        victoryDesc +=  victory.getVictor().getPilotName();
        victoryDesc +=  " was flying a " + victorPlaneType + ".";        
        
        return victoryDesc;
    }
    

    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() plane Name> of <victory.getVictim() squadron name> was brought down by a <ground unit name> 
    // <victory.getVictim() crew 1> was <killed/captured>.  <victory.getVictim() crew 2> was <killed/captured>
    String createVictoryDescriptionGroundToAir() throws PWCGException
    {        
        String victoryDesc = "";
        
        String victimPlaneType = getPlaneDescription(victory.getVictim().getType());

        if (!(victory.getVictim().getPilotName().isEmpty()))
        {
            // Line 1
            victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate());
            if (!victory.getLocation().isEmpty())
            {
                victoryDesc +=  " near " + victory.getLocation();
            }
            victoryDesc +=  ".";

            // Line 2
            victoryDesc +=  "\n";
            victoryDesc +=  "A " + victimPlaneType + " of " + victory.getVictim().getSquadronName() + " was brought down by a " + getGroundUnitName(victory.getVictor()) + ".";

            // Line 3
            String pilotFate = getPilotFate();
            victoryDesc += pilotFate;
        }
        // If we do not have all of the information
        else
        {
            victoryDesc +=  victimPlaneType + " shot down by " + getGroundUnitName(victory.getVictor()) + "";
        }
        
        return victoryDesc;
    }

    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() ground unit> was destroyed by a <ground unit name> 
    public String createVictoryDescriptionGroundToGround() throws PWCGException
    {
        String victoryDesc = "";
        
        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate());
        if (!victory.getLocation().isEmpty())
        {
            victoryDesc +=  " near " + victory.getLocation();
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "A " + getGroundUnitName(victory.getVictim()) + " was destroyed by a " + getGroundUnitName(victory.getVictor()) + ".";
        
        return victoryDesc;
    }

    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() ground unit> was destroyed by a <ground unit name> 
    public String createVictoryDescriptionUnknownToGround()
    {
        String victoryDesc = "";
        
        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate());
        if (!victory.getLocation().isEmpty())
        {
            victoryDesc +=  " near " + victory.getLocation();
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "A " + victory.getVictim().getType() + " was destroyed.";
        
        return victoryDesc;
    }
    

    private String getGroundUnitName(VictoryEntity victoryEntity) throws PWCGException
    {
        IVehicleDefinition vehicleDefinitionType = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionByVehicleName(victoryEntity.getType());
        if (vehicleDefinitionType != null)
        {
            return vehicleDefinitionType.getDisplayName();
        }

        IVehicleDefinition vehicleDefinitionByName = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionByVehicleName(victoryEntity.getName());
        if (vehicleDefinitionByName != null)
        {
            return vehicleDefinitionByName.getDisplayName();
        }
        
        BlockDefinition blockDefinition = BlockDefinitionManager.getInstance().getBlockDefinition(victoryEntity.getType());
        if (blockDefinition != null)
        {
            return blockDefinition.getDesc();
        }

        if (VehicleDefinitionManager.isLocomotive(victoryEntity.getName()) || VehicleDefinitionManager.isLocomotive(victoryEntity.getType()))
        {
            return "Locomotive";
        }
        
        if (VehicleDefinitionManager.isTrainCar(victoryEntity.getName()) || VehicleDefinitionManager.isTrainCar(victoryEntity.getType()))
        {
            return "Train Car";
        }

        return "vehicle";
    }
}
