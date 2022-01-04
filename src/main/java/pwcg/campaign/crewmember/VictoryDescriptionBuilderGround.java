package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IStaticPlaneSelector;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.factory.StaticPlaneSelectorFactory;
import pwcg.campaign.group.BlockDefinition;
import pwcg.campaign.group.BlockDefinitionManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;
import pwcg.mission.ground.building.PwcgBuildingIdentifier;
import pwcg.mission.ground.building.PwcgStructure;
import pwcg.mission.ground.vehicle.VehicleDefinition;
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
        
        String victorTankType = getPlaneDescription(victory.getVictor().getType());

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
        victoryDesc +=  victory.getVictor().getCrewMemberName();
        victoryDesc +=  " was flying a " + victorTankType + ".";        
        
        return victoryDesc;
    }
    

    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() plane Name> of <victory.getVictim() squadron name> was brought down by a <ground unit name> 
    // <victory.getVictim() crew 1> was <killed/captured>.  <victory.getVictim() crew 2> was <killed/captured>
    String createVictoryDescriptionGroundToAir() throws PWCGException
    {        
        String victoryDesc = "";
        
        String victimTankType = getPlaneDescription(victory.getVictim().getType());

        if (!(victory.getVictim().getCrewMemberName().isEmpty()))
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
            victoryDesc +=  "A " + victimTankType + " of " + victory.getVictim().getSquadronName() + " was brought down by a " + getGroundUnitName(victory.getVictor()) + ".";

            // Line 3
            String crewMemberFate = getCrewMemberFate();
            victoryDesc += crewMemberFate;
        }
        // If we do not have all of the information
        else
        {
            victoryDesc +=  victimTankType + " shot down by " + getGroundUnitName(victory.getVictor()) + "";
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
        PwcgStructure building = PwcgBuildingIdentifier.identifyBuilding(victoryEntity.getType());
        if (building != PwcgStructure.UNKNOWN)
        {
            if (building == PwcgStructure.STATIC_VEHICLE)
            {
                return identifyStaticVehicle(victoryEntity);
            }
            else
            {
                return building.getDescription();
            }
        }
        
        String vehicleNameFromType = getVehicleName(victoryEntity.getType());
        if (vehicleNameFromType != null)
        {
            return vehicleNameFromType;
        }

        String vehicleNameFromName = getVehicleName(victoryEntity.getName());
        if (vehicleNameFromName != null)
        {
            return vehicleNameFromName;
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

        PWCGLogger.log(LogLevel.ERROR, "No vehicle match found for " + victoryEntity.getName() + " or " + victoryEntity.getType());
        return "vehicle";
    }

    private String identifyStaticVehicle(VictoryEntity victoryEntity) throws PWCGException
    {
        VehicleDefinition vehicle = PWCGContext.getInstance().getStaticObjectDefinitionManager().findStaticVehicle(victoryEntity.getType());
        if (vehicle != null)
        {
            return vehicle.getDisplayName();
        }
        
        IStaticPlaneSelector staticPlaneFactory = StaticPlaneSelectorFactory.createStaticPlaneSelector();
        if (staticPlaneFactory.isStaticPlane(victoryEntity.getType()))
        {
            return "parked plane";
        }

        return "vehicle";
    }

    private String getVehicleName(String vehicleDescriptor) throws PWCGException
    {
        VehicleDefinition vehicleDefinitionByName = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionByVehicleName(vehicleDescriptor);
        if (vehicleDefinitionByName != null)
        {
            return vehicleDefinitionByName.getDisplayName();
        }

        VehicleDefinition vehicleDefinitionByDisplayName = PWCGContext.getInstance().getVehicleDefinitionManager().getVehicleDefinitionByVehicleDisplayName(vehicleDescriptor);
        if (vehicleDefinitionByDisplayName != null)
        {
            return vehicleDefinitionByDisplayName.getDisplayName();
        }
        
        return null;
    }
}
