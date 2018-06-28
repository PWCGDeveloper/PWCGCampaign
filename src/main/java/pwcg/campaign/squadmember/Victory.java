package pwcg.campaign.squadmember;

import java.util.Date;

import pwcg.campaign.api.IGroundUnitNames;
import pwcg.campaign.context.PWCGContextManager;
import pwcg.campaign.factory.GroundUnitNameFactory;
import pwcg.campaign.plane.Balloon;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

/**
 * Victory is was is stored in the campaign file.
 * A victory object can be created from a MissionResultVictory (from the logs)
 * or from straight forward IO of a victory object (See VictoryIO)
 * 
 * @author Patrick Wilson
 *
 */
public class Victory implements Comparable<Victory>
{
    public static final int AIR_VICTORY = 1;
    public static final int GROUND_VICTORY = 2;
    public static final int UNSPECIFIED_VICTORY = 3;

    private Date date = null;
    private String location = "";
    private boolean crashedInSight = false;

    private VictoryEntity victim = new VictoryEntity();
    private VictoryEntity victor = new VictoryEntity();

    public Victory()
    {
    }

    public String createVictoryDescription() throws PWCGException
    {
        if (victim.getAirOrGround() == Victory.AIR_VICTORY && victor.getAirOrGround() == Victory.AIR_VICTORY)
        {
            return createVictoryDescriptionAirToAir();
        }
        // Ace victories will not be full descriptions
        else if (victim.getAirOrGround() == Victory.UNSPECIFIED_VICTORY)
        {
            return createVictoryDescriptionAirToAir();
        }
        else if (victim.getAirOrGround() == Victory.GROUND_VICTORY && victor.getAirOrGround() == Victory.AIR_VICTORY)
        {
            return createVictoryDescriptionAirToGround();
        }
        else if (victim.getAirOrGround() == Victory.AIR_VICTORY && victor.getAirOrGround() == Victory.GROUND_VICTORY)
        {
            return createVictoryDescriptionGroundToAir();
        }
        else if (victim.getAirOrGround() == Victory.GROUND_VICTORY && victor.getAirOrGround() == Victory.GROUND_VICTORY)
        {
            return createVictoryDescriptionGroundToGround();
        }
        else if (victim.getAirOrGround() == Victory.AIR_VICTORY && victor.getAirOrGround() == Victory.UNSPECIFIED_VICTORY)
        {
            return createVictoryDescriptionUnknownToAir();
        }
        else if (victim.getAirOrGround() == Victory.GROUND_VICTORY && victor.getAirOrGround() == Victory.UNSPECIFIED_VICTORY)
        {
            return createVictoryDescriptionUnknownToGround();
        }

        return "";
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


    // On <victory date> near <location>.
    // A <victim plane Name> of <victim squadron name> was brought down by <victor name> of <victor squadron name> 
    // <victim crew 1> was <killed/captured>.  <victim crew 2> was <killed/captured>
    // <victor crew 1> was flying a <victor plane Name>.
    /**
     * @return
     * @throws PWCGException 
     */
    private String createVictoryDescriptionAirToAir() throws PWCGException
    {
        // There might not be any mission plane if this an out of mission victory during a mission evaluation
        if (useFullDescription())
        {
            return getVictoryDescriptionAirToAirFull();
        }
        else if (useBalloonDescription())
        {
            return getVictoryDescriptionBalloonFull();
        }
        
        return getVictoryDescriptionAirToAirSimple();
    }

    private boolean useFullDescription()
    {
        if (date == null)
        {
            return false;
        }
        
        if (victim == null || victim.determineCompleteForAir() == false)
        {
            return false;
        }
        
        if (victor == null || victor.determineCompleteForAir() == false)
        {
            return false;
        }
                
        return true;
    }

    private boolean useBalloonDescription()
    {
        if (date == null)
        {
            return false;
        }
        
        if (victim == null || Balloon.isBalloonName(victim.getType()) == false)
        {
            return false;
        }
        
        if (victor == null || victor.determineCompleteForAir() == false)
        {
            return false;
        }
                
        return true;
    }
    
    // On <victory date> near <location>.
    // A <victim plane Name> of <victim squadron name> was brought down by <victor name> of <victor squadron name> 
    // <victim crew 1> was <killed/captured>.  <victim crew 2> was <killed/captured>
    // <victor crew 1> and <victor crew 1> were flying a <victor plane Name>.
    /**
     * @return
     * @throws PWCGException 
     */
    private String getVictoryDescriptionAirToAirFull() throws PWCGException
    {
        // Create the victory description
        String victoryDesc = "";
        
        String victimPlaneType = getPlaneDescription(victim.getType());
        String victorPlaneType = getPlaneDescription(victor.getType());

        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(date);
        if (!location.isEmpty())
        {
            victoryDesc +=  " near " + location;
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "\n";
        victoryDesc +=  "A " + victimPlaneType + " of " + victim.getSquadronName() + " was brought down by " + victor.getPilotName();
        if (victor.getSquadronName() != null && !(victor.getSquadronName().isEmpty()))
        {
            victoryDesc += " of " + victor.getSquadronName();
        }
        victoryDesc +=  ".";

        // Line 3
        // We only know the fate of the crew if the victim crashed behind our lines
        victoryDesc +=  "\n";
        if (crashedInSight)
        {
            victoryDesc +=  victim.getPilotName() + " " + getStringForStatus(victim.getPilotStatus()) + ".";
        }
        else
        {
            victoryDesc += "The aircraft went down behind enemy lines.\n";
        }

        // Line 4
        victoryDesc +=  "\n";
        victoryDesc +=  victor.getPilotName();
        victoryDesc +=  " was flying a " + victorPlaneType + ".";          
        
        return victoryDesc;
    }

    
    private String getVictoryDescriptionBalloonFull() throws PWCGException
    {
        // Create the victory description
        String victoryDesc = "";
        
        String victimBalloonType = victim.getType();
        String victorPlaneType = getPlaneDescription(victor.getType());

        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(date);
        if (!location.isEmpty())
        {
            victoryDesc +=  " near " + location;
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "\n";
        victoryDesc +=  "A " + victimBalloonType + " was brought down by " + victor.getPilotName();
        if (victor.getSquadronName() != null && !(victor.getSquadronName().isEmpty()))
        {
            victoryDesc += " of " + victor.getSquadronName();
        }
        victoryDesc +=  ".";

        // Line 3
        victoryDesc +=  "\n";
        victoryDesc +=  victor.getPilotName();
        victoryDesc +=  " was flying a " + victorPlaneType + ".";          
        
        return victoryDesc;
    }

    private String getVictoryDescriptionAirToAirSimple() throws PWCGException
    {
        String victimPlaneType = "Enemy Aircraft";
        if (victim.getType() != null && !victim.getType().isEmpty())
        {
            victimPlaneType = getPlaneDescription(victim.getType());
        }
     
        String victoryDesc = "";
        if (date != null)
        {
            victoryDesc +=  "On " + DateUtils.getDateString(date) + " a ";
        }
        
        victoryDesc += victimPlaneType + " was shot down.";
        
        return victoryDesc;
    }

    /**
     * @param planeType
     * @return
     * @throws PWCGException 
     */
    private String getPlaneDescription(String planeType) throws PWCGException
    {
        String planeName = "Enemy Aircraft";
        PlaneType plane = PWCGContextManager.getInstance().getPlaneTypeFactory().getPlaneTypeByAnyName(planeType);
        if (plane != null)
        {
            planeName = plane.getDisplayName();
        }
        
        // Aces shot down planes that are not in the game
        else if (planeType != null && !planeType.isEmpty())
        {
            planeName = planeType;
        }
        
        return planeName;
    }
    

    // On <victory date> near <location>.
    // A <victim ground unit> was destroyed by by <victor name> of <victor squadron name> 
    // <victor crew 1> and <victor crew 1> were flying a <victor plane Name>.
    /**
     * @return
     * @throws PWCGException 
     */
    private String createVictoryDescriptionAirToGround() throws PWCGException
    {
        String victoryDesc = "";
        
        String victorPlaneType = getPlaneDescription(victor.getType());

        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(date);
        if (!location.isEmpty())
        {
            victoryDesc +=  " near " + location;
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "\n";
        victoryDesc +=  "A " + getGroundUnitName(victim.getType()) + " was destroyed by " + victor.getPilotName() + " of " + victor.getSquadronName() + ".";

        // Line 3
        victoryDesc +=  "\n";
        victoryDesc +=  victor.getPilotName();
        victoryDesc +=  " was flying a " + victorPlaneType + ".";        
        
        return victoryDesc;
    }

    
    // On <victory date> near <location>.
    // A <victim plane Name> of <victim squadron name> was brought down by a <ground unit name> 
    // <victim crew 1> was <killed/captured>.  <victim crew 2> was <killed/captured>
    /**
     * @return
     * @throws PWCGException 
     */
    private String createVictoryDescriptionGroundToAir() throws PWCGException
    {        
        String victoryDesc = "";
        
        String victimPlaneType = getPlaneDescription(victim.getType());

        if (!(victim.getPilotName().isEmpty()))
        {
            // Line 1
            victoryDesc +=  "On " + DateUtils.getDateString(date);
            if (!location.isEmpty())
            {
                victoryDesc +=  " near " + location;
            }
            victoryDesc +=  ".";

            // Line 2
            victoryDesc +=  "\n";
            victoryDesc +=  "A " + victimPlaneType + " of " + victim.getSquadronName() + " was brought down by a " + getGroundUnitName(victor.getType()) + ".";

            // Line 3
            // We only know the fate of the crew if the victim crashed behind our lines
            victoryDesc +=  "\n";
            if (crashedInSight)
            {
                victoryDesc +=  victim.getPilotName() + " " + getStringForStatus(victim.getPilotStatus()) + ".";
            }
            else
            {
                victoryDesc += "The aircraft went down behind enemy lines.";
            }
        }
        // If we do not have all of the information
        else
        {
            victoryDesc +=  victimPlaneType + " shot down by " + getGroundUnitName(victor.getType()) + "";
        }
        
        return victoryDesc;
    }

    
    // On <victory date> near <location>.
    // A <victim ground unit> was destroyed by a <ground unit name> 
    /**
     * @return
     */
    private String createVictoryDescriptionGroundToGround()
    {
        String victoryDesc = "";
        
        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(date);
        if (!location.isEmpty())
        {
            victoryDesc +=  " near " + location;
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "A " + getGroundUnitName(victim.getType()) + " was destroyed by a " + getGroundUnitName(victor.getType()) + ".";
        
        return victoryDesc;
    }

    private String createVictoryDescriptionUnknownToAir() throws PWCGException
    {
        String victoryDesc = "";
        
        String victimPlaneType = getPlaneDescription(victim.getType());

        if (crashedInSight && !(victim.getPilotName().isEmpty()))
        {
            // Line 1
            victoryDesc +=  "On " + DateUtils.getDateString(date);
            if (!location.isEmpty())
            {
                victoryDesc +=  " near " + location;
            }
            victoryDesc +=  ".";

            // Line 2
            victoryDesc +=  "\n";
            victoryDesc +=  "A " + victimPlaneType + " of " + victim.getSquadronName() + " crashed.";

            // Line 3
            // We only know the fate of the crew if the victim crashed behind our lines
            victoryDesc +=  "\n";
            if (crashedInSight)
            {
                victoryDesc +=  victim.getPilotName() + " " + getStringForStatus(victim.getPilotStatus());
            }
            else
            {
                victoryDesc += "The aircraft went down behind enemy lines.";
            }
        }
        // Backwards compatibility - we do not have the information for the latest victory logs
        else
        {
            victoryDesc +=  victimPlaneType + " crashed.";
        }
        
        return victoryDesc;
    }
    
    // On <victory date> near <location>.
    // A <victim ground unit> was destroyed by a <ground unit name> 

    private String createVictoryDescriptionUnknownToGround()
    {
        String victoryDesc = "";
        
        // Line 1
        victoryDesc +=  "On " + DateUtils.getDateString(date);
        if (!location.isEmpty())
        {
            victoryDesc +=  " near " + location;
        }
        victoryDesc +=  ".";

        // Line 2
        victoryDesc +=  "A " + victim.getType() + " was destroyed.";
        
        return victoryDesc;
    }

    private String getStringForStatus(int crewmemberStatus)
    {
        String statusDesc = "";
        if (crewmemberStatus == SquadronMemberStatus.STATUS_KIA)
        {
            statusDesc = "was killed in action";
        }
        else if (crewmemberStatus == SquadronMemberStatus.STATUS_CAPTURED)
        {
            statusDesc = "was made a prisoner of war";
        }
        else if (crewmemberStatus == SquadronMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            statusDesc = "was seriously wounded";
        }
        else if (crewmemberStatus == SquadronMemberStatus.STATUS_WOUNDED)
        {
            statusDesc = "was lightly wounded";
        }
        else 
        {
            statusDesc = "was not injured";
        }
        
        return statusDesc;
    }

    @Override
    public int compareTo(Victory otherVictory)
    {
        if (this.date.before(otherVictory.date))
        {
            return -1;
        }
        else if (this.date.after(otherVictory.date))
        {
            return 1;
        }
        return 0;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public VictoryEntity getVictim()
    {
        return victim;
    }

    public void setVictim(VictoryEntity victim)
    {
        this.victim = victim;
    }

    public VictoryEntity getVictor()
    {
        return victor;
    }

    public void setVictor(VictoryEntity victor)
    {
        this.victor = victor;
    }

    public boolean isCrashedInSight()
    {
        return crashedInSight;
    }

   public void setCrashedInSight(boolean crashedInSight)
    {
        this.crashedInSight = crashedInSight;
    }
}
