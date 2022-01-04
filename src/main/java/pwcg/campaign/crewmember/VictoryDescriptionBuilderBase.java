package pwcg.campaign.crewmember;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.tank.TankArchType;
import pwcg.campaign.tank.TankType;
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
            victorDesc = "a gunner flying with " + victory.getVictor().getCrewMemberName();
        }
        else
        {
            victorDesc = victory.getVictor().getCrewMemberName();
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
        TankType plane = PWCGContext.getInstance().getTankTypeFactory().createTankTypeByAnyName(planeType);
        if (plane != null)
        {
            planeName = plane.getDisplayName();
            ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
            int detailedVictoryDescription = configManager.getIntConfigParam(ConfigItemKeys.DetailedVictoryDescriptionKey);
            if (detailedVictoryDescription == 0)
            {
                planeName = TankArchType.getArchTypeDescription(plane.getArchType());
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
        if (crewmemberStatus == CrewMemberStatus.STATUS_KIA)
        {
            statusDesc = "was killed in action";
        }
        else if (crewmemberStatus == CrewMemberStatus.STATUS_CAPTURED)
        {
            statusDesc = "was made a prisoner of war";
        }
        else if (crewmemberStatus == CrewMemberStatus.STATUS_SERIOUSLY_WOUNDED)
        {
            statusDesc = "was seriously wounded";
        }
        else if (crewmemberStatus == CrewMemberStatus.STATUS_WOUNDED)
        {
            statusDesc = "was lightly wounded";
        }
        else 
        {
            statusDesc = "was not injured";
        }
        
        return statusDesc;
    }


    protected String getCrewMemberFate() throws PWCGException
    {
        String crewMemberFate = "";
       
        ConfigManagerCampaign configManager = campaign.getCampaignConfigManager();
        int detailedVictoryDescription = configManager.getIntConfigParam(ConfigItemKeys.DetailedVictoryDescriptionKey);
        if (detailedVictoryDescription != 0)
        {
            if (victory.getCrashedInSight())
            {
                String crewMemberName = "The crewMember";
                if (victory.getVictim().getCrewMemberName() != null && 
                    !(victory.getVictim().getCrewMemberName().equals("")) && 
                    !(victory.getVictim().getCrewMemberName().equals("Unknown")))
                {
                    crewMemberName = victory.getVictim().getCrewMemberName();
                }
                else
                {
                    crewMemberName = "The crewMember";
                }
                    
                crewMemberFate =  "\n" + crewMemberName + " " + getStringForStatus(victory.getVictim().getCrewMemberStatus());
            }
            else
            {
                crewMemberFate = "\nThe aircraft went down behind enemy lines.";
            }
        }

        return crewMemberFate;
    }

}
