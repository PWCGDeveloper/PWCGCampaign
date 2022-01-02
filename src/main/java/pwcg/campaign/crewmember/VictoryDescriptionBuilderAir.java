package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class VictoryDescriptionBuilderAir extends VictoryDescriptionBuilderBase
{
    public VictoryDescriptionBuilderAir (Campaign campaign, Victory victory)
    {
        super(campaign, victory);
    }    
    
    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() plane Name> of <victory.getVictim() squadron name> was brought down by <victory.getVictor() name> of <victory.getVictor() squadron name> 
    // <victory.getVictim() crew 1> was <killed/captured>.  <victory.getVictim() crew 2> was <killed/captured>
    // <victory.getVictor() crew 1> and <victory.getVictor() crew 1> were flying a <victory.getVictor() plane Name>.
    /**
     * @return
     * @throws PWCGException 
     */
    public String getVictoryDescriptionAirToAirFull() throws PWCGException
    {
        // Create the victory description
        String victoryDesc = "";
        
        String victimPlaneType = getPlaneDescription(victory.getVictim().getType());
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
        victoryDesc +=  "A " + victimPlaneType + " of " + victory.getVictim().getSquadronName() + " was brought down by ";
        victoryDesc += describeVictor();
        victoryDesc +=  ".";

        // Line 3
        String crewMemberFate = getCrewMemberFate();
        victoryDesc += crewMemberFate;

        // Line 4
        victoryDesc +=  "\n";
        victoryDesc +=  victory.getVictor().getCrewMemberName();
        victoryDesc +=  " was flying a " + victorPlaneType + ".";          
        
        return victoryDesc;
    }

    
    public String getVictoryDescriptionBalloonFull() throws PWCGException
    {
        // Create the victory description
        String victoryDesc = "";
        
        String victimBalloonType = victory.getVictim().getType();
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
        victoryDesc +=  "A " + victimBalloonType + " was brought down by ";
        victoryDesc += describeVictor();
        victoryDesc +=  ".";

        // Line 3
        victoryDesc +=  "\n";
        victoryDesc +=  victory.getVictor().getCrewMemberName();
        victoryDesc +=  " was flying a " + victorPlaneType + ".";          
        
        return victoryDesc;
    }


    public String getVictoryDescriptionAirToAirSimple() throws PWCGException
    {
        String victimPlaneType = "Enemy Aircraft";
        if (victory.getVictim().getType() != null && !victory.getVictim().getType().isEmpty())
        {
            victimPlaneType = getPlaneDescription(victory.getVictim().getType());
        }
     
        String victoryDesc = "";
        if (victory.getDate() != null)
        {
            victoryDesc +=  "On " + DateUtils.getDateString(victory.getDate()) + " a ";
        }
        
        victoryDesc += victimPlaneType + " was shot down.";
        
        return victoryDesc;
    }

    String createVictoryDescriptionUnknownToAir() throws PWCGException
    {
        String victoryDesc = "";
        
        String victimPlaneType = getPlaneDescription(victory.getVictim().getType());

        if (victory.getCrashedInSight() && !(victory.getVictim().getCrewMemberName().isEmpty()))
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
            victoryDesc +=  "A " + victimPlaneType + " of " + victory.getVictim().getSquadronName() + " crashed.";

            // Line 3
            String crewMemberFate = getCrewMemberFate();
            victoryDesc += crewMemberFate;
        }
        // Backwards compatibility - we do not have the information for the latest victory logs
        else
        {
            victoryDesc +=  victimPlaneType + " crashed.";
        }
        
        return victoryDesc;
    }
}
