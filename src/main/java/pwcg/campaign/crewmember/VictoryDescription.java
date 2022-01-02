package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;

public class VictoryDescription
{
    protected Campaign campaign;
    protected Victory victory;
    
    public VictoryDescription (Campaign campaign, Victory victory)
    {
        this.victory = victory;
        this.campaign = campaign;
    }    

    public String createVictoryDescription() throws PWCGException
    {
        VictoryDescriptionBuilderAir descriptionBuilderAir = new VictoryDescriptionBuilderAir(campaign, victory);
        VictoryDescriptionBuilderGround descriptionBuilderGround = new VictoryDescriptionBuilderGround(campaign, victory);
        
        if (victory.getVictim().getAirOrGround() == Victory.AIRCRAFT && victory.getVictor().getAirOrGround() == Victory.AIRCRAFT)
        {
            return createVictoryDescriptionAirToAir();
        }
        // Ace victories will not be full descriptions
        else if (victory.getVictim().getAirOrGround() == Victory.UNSPECIFIED_VICTORY)
        {
            return createVictoryDescriptionAirToAir();
        }
        else if (victory.getVictim().getAirOrGround() == Victory.AIRCRAFT && victory.getVictor().getAirOrGround() == Victory.UNSPECIFIED_VICTORY)
        {
            return descriptionBuilderAir.createVictoryDescriptionUnknownToAir();
        }

        else if (victory.getVictim().getAirOrGround() == Victory.VEHICLE && victory.getVictor().getAirOrGround() == Victory.AIRCRAFT)
        {
            return descriptionBuilderGround.createVictoryDescriptionAirToGround();
        }
        else if (victory.getVictim().getAirOrGround() == Victory.AIRCRAFT && victory.getVictor().getAirOrGround() == Victory.VEHICLE)
        {
            return descriptionBuilderGround.createVictoryDescriptionGroundToAir();
        }
        else if (victory.getVictim().getAirOrGround() == Victory.VEHICLE && victory.getVictor().getAirOrGround() == Victory.VEHICLE)
        {
            return descriptionBuilderGround.createVictoryDescriptionGroundToGround();
        }
        else if (victory.getVictim().getAirOrGround() == Victory.VEHICLE && victory.getVictor().getAirOrGround() == Victory.UNSPECIFIED_VICTORY)
        {
            return descriptionBuilderGround.createVictoryDescriptionUnknownToGround();
        }

        return "";
    }


    // On <victory victory.getDate()> near <location>.
    // A <victory.getVictim() plane Name> of <victory.getVictim() squadron name> was brought down by <victory.getVictor() name> of <victory.getVictor() squadron name> 
    // <victory.getVictim() crew 1> was <killed/captured>.  <victory.getVictim() crew 2> was <killed/captured>
    // <victory.getVictor() crew 1> was flying a <victory.getVictor() plane Name>.
    /**
     * @return
     * @throws PWCGException 
     */
    private String createVictoryDescriptionAirToAir() throws PWCGException
    {
        VictoryDescriptionBuilderAir descriptionBuilder = new VictoryDescriptionBuilderAir(campaign, victory);
        // There might not be any mission plane if this an out of mission victory during a mission evaluation
        if (useFullDescription())
        {
            return descriptionBuilder.getVictoryDescriptionAirToAirFull();
        }
        else if (useBalloonDescription())
        {
            return descriptionBuilder.getVictoryDescriptionBalloonFull();
        }
        
        return descriptionBuilder.getVictoryDescriptionAirToAirSimple();
    }

    private boolean useFullDescription()
    {
        if (victory.getDate() == null)
        {
            return false;
        }
        
        if (victory.getVictim() == null || victory.getVictim().determineCompleteForAir() == false)
        {
            return false;
        }
        
        if (victory.getVictor() == null || victory.getVictor().determineCompleteForAir() == false)
        {
            return false;
        }
                
        return true;
    }

    private boolean useBalloonDescription()
    {
        if (victory.getDate() == null)
        {
            return false;
        }
        
        if (victory.getVictor() == null || victory.getVictor().determineCompleteForAir() == false)
        {
            return false;
        }
                
        return true;
    }

}
