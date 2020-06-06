package pwcg.campaign.squadmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.PlaneArchType;
import pwcg.campaign.plane.PlaneType;
import pwcg.core.config.ConfigItemKeys;
import pwcg.core.config.ConfigManagerCampaign;
import pwcg.core.exception.PWCGException;

public abstract class VictoryDescriptionBuilderBase
{
    protected Victory victory;
    protected Campaign campaign;
    
    public VictoryDescriptionBuilderBase (Campaign campaign, Victory victory)
    {
        this.victory = victory;
        this.campaign = campaign;
    }    

    protected String describeVictor() {
        String victorDesc;
        if (victory.getVictor().isGunner())
        {
            victorDesc = "a gunner flying with " + victory.getVictor().getPilotName();
        }
        else
        {
            victorDesc = victory.getVictor().getPilotName();
        }
        if (victory.getVictor().getSquadronName() != null && !(victory.getVictor().getSquadronName().isEmpty()))
        {
            victorDesc += " of " + victory.getVictor().getSquadronName();
        }
        return victorDesc;
    }

    protected String getPlaneDescription(String planeType) throws PWCGException
    {
        String planeName = "Enemy Aircraft";
        PlaneType plane = PWCGContext.getInstance().getPlaneTypeFactory().createPlaneTypeByAnyName(planeType);
        if (plane != null)
        {
            planeName = plane.getDisplayName();
            ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
            int detailedVictoryDescription = configManager.getIntConfigParam(ConfigItemKeys.DetailedVictoryDescriptionKey);
            if (detailedVictoryDescription == 0)
            {
                planeName = PlaneArchType.getArchTypeDescription(plane.getArchType());
            }
        }
        else if (planeType != null && !planeType.isEmpty())
        {
            planeName = planeType;            
        }
        

        return planeName;
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


    protected String getPilotFate() throws PWCGException
    {
        String pilotFate = "";
       
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int detailedVictoryDescription = configManager.getIntConfigParam(ConfigItemKeys.DetailedVictoryDescriptionKey);
        if (detailedVictoryDescription != 0)
        {
            if (victory.getCrashedInSight())
            {
                String pilotName = "The pilot";
                if (victory.getVictim().getPilotName() != null && 
                    !(victory.getVictim().getPilotName().equals("")) && 
                    !(victory.getVictim().getPilotName().equals("Unknown")))
                {
                    pilotName = victory.getVictim().getPilotName();
                }
                else
                {
                    pilotName = "The pilot";
                }
                    
                pilotFate =  "\n" + pilotName + " " + getStringForStatus(victory.getVictim().getPilotStatus());
            }
            else
            {
                pilotFate = "\nThe aircraft went down behind enemy lines.";
            }
        }

        return pilotFate;
    }

}
