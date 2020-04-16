package pwcg.campaign;

import java.io.File;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGDirectoryManager;
import pwcg.campaign.io.json.CoopPersonaOldIOJson;
import pwcg.campaign.io.json.CoopUserIOJson;
import pwcg.campaign.squadmember.SquadronMember;
import pwcg.coop.CoopUserManager;
import pwcg.coop.model.CoopPersona;
import pwcg.coop.model.CoopPersonaOld;
import pwcg.coop.model.CoopUser;
import pwcg.core.utils.PWCGLogger;

public class CampaignCoopConverter
{
    private Campaign campaign;
    
    public CampaignCoopConverter(Campaign campaign)
    {
        this.campaign = campaign;
    }
    
    public void convertToV8Coop()
    {
        try
        {            
            if (campaign.isCoop())
            {
                // Does pilots dir exist
                PWCGDirectoryManager directoryManager = PWCGContext.getInstance().getDirectoryManager();
                String oldCoopPilotDir = directoryManager.getPwcgCoopDir() + "Pilots\\";
                File oldCoopPilotDirFile = new File(oldCoopPilotDir);
                if (!oldCoopPilotDirFile.exists())
                {
                    return;
                }
                
                if (oldCoopPilotDirFile.listFiles().length == 0)
                {
                    oldCoopPilotDirFile.delete();
                }
                
                // read pilots
                List<CoopPersonaOld> oldCoopPersonas= CoopPersonaOldIOJson.readCoopPersonas();
                for (CoopPersonaOld oldCoopPersona : oldCoopPersonas)
                {
                    // map pilots to new pilot format
                    CoopPersona newCoopPersona = new CoopPersona();
                    newCoopPersona.setCampaignName(oldCoopPersona.getCampaignName());
                    newCoopPersona.setCoopUsername(oldCoopPersona.getUsername());
                    newCoopPersona.setSerialNumber(oldCoopPersona.getSerialNumber());
                    
                    // Is campaign this campaign
                    if (!newCoopPersona.getCampaignName().equals(campaign.getCampaignData().getName()))
                    {
                        continue;
                    }
                    
                    // Does pilot serial number exist in campaign
                    SquadronMember player = campaign.getPersonnelManager().getAnyCampaignMember(oldCoopPersona.getSerialNumber());
                    if (player == null)
                    {
                        continue;
                    }
                    
                    // Does user exist
                    CoopUser coopUser = CoopUserManager.getIntance().getCoopUser(oldCoopPersona.getUsername());
                    if (coopUser == null)
                    {
                        continue;
                    }

                    // map pilot to user
                    coopUser.addPersona(newCoopPersona);
                    CoopUserIOJson.writeJson(coopUser);

                    // delete pilot
                    String oldCoopPilotFilename = oldCoopPilotDir + oldCoopPersona.getPilotName() + ".json";
                    File oldCoopPilotFile = new File(oldCoopPilotFilename);
                    if (oldCoopPilotFile.exists())
                    {
                        oldCoopPilotFile.delete();
                    }
                }
            }
        }
        catch(Exception e)
        {
            PWCGLogger.logException(e);
        }
    }
 }
