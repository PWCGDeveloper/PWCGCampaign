package pwcg.campaign.squadmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.api.IGroundUnitNames;
import pwcg.campaign.factory.GroundUnitNameFactory;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

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
        victoryDesc +=  "A " + getGroundUnitName(victory.getVictim().getType()) + " was destroyed by ";
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
            victoryDesc +=  "A " + victimPlaneType + " of " + victory.getVictim().getSquadronName() + " was brought down by a " + getGroundUnitName(victory.getVictor().getType()) + ".";

            // Line 3
            victoryDesc +=  "\n";
            String pilotFate = getPilotFate();
            victoryDesc += pilotFate;
        }
        // If we do not have all of the information
        else
        {
            victoryDesc +=  victimPlaneType + " shot down by " + getGroundUnitName(victory.getVictor().getType()) + "";
        }
        
        return victoryDesc;
    }

    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() ground unit> was destroyed by a <ground unit name> 
    public String createVictoryDescriptionGroundToGround()
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
        victoryDesc +=  "A " + getGroundUnitName(victory.getVictim().getType()) + " was destroyed by a " + getGroundUnitName(victory.getVictor().getType()) + ".";
        
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
    

    private String getGroundUnitName(String groundUnitType)
    {
        IGroundUnitNames groundUnitNames = GroundUnitNameFactory.createGroundUnitNames();
        String groundUnitName = groundUnitNames.getGroundUnitDisplayName(groundUnitType);
        
        if (groundUnitName.isEmpty())
        {
            groundUnitName = groundUnitType;
        }
        
        return groundUnitName;
    }
}
