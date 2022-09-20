package pwcg.dev.jsonconvert;

import java.io.BufferedReader;
import java.io.FileReader;

import pwcg.campaign.battle.AmphibiousAssault;
import pwcg.campaign.battle.AmphibiousAssaultShipDefinition;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.io.json.AmphibiousAssaultIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.dev.jsonconvert.orig.io.DevIOConstants;
import pwcg.dev.jsonconvert.orig.io.VehicleIO;
import pwcg.mission.ground.vehicle.Vehicle;

public class NormandyAmphibiousAssaultBuilder 
{    
    AmphibiousAssault dieppe = new AmphibiousAssault("Dieppe");
    AmphibiousAssault sword = new AmphibiousAssault("Sword");
    AmphibiousAssault juno = new AmphibiousAssault("Juno");
    AmphibiousAssault gold = new AmphibiousAssault("Gold");
    AmphibiousAssault omaha = new AmphibiousAssault("Omaha");
    AmphibiousAssault utah = new AmphibiousAssault("Utah");

    public static void main(String[] args) throws Exception
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        NormandyAmphibiousAssaultBuilder jsonConverter = new NormandyAmphibiousAssaultBuilder();
        jsonConverter.buildAmphibiousAssaults("Normandy");
    }

    private void buildAmphibiousAssaults (String mapName) throws Exception 
    {
        String filename = "D:\\PWCG\\NormandyData\\NormandyPWCGData.Mission";     
        initializeAmphibiousAssaults();
        readAmphibiousAssaults(filename);
        writeAmphibiousAssaults(mapName);
    }

    private void initializeAmphibiousAssaults() throws PWCGException
    {
        dieppe.setLandingStartDate(DateUtils.getDateYYYYMMDD("19420819"));
        dieppe.setLandingStopDate(DateUtils.getDateYYYYMMDD("19420819"));
        dieppe.setAggressorCountry(Country.BRITAIN);
        dieppe.setDefendingCountry(Country.GERMANY);
        
        sword.setLandingStartDate(DateUtils.getDateYYYYMMDD("19440606"));
        sword.setLandingStopDate(DateUtils.getDateYYYYMMDD("19440608"));
        sword.setAggressorCountry(Country.BRITAIN);
        sword.setDefendingCountry(Country.GERMANY);
        
        juno.setLandingStartDate(DateUtils.getDateYYYYMMDD("19440606"));
        juno.setLandingStopDate(DateUtils.getDateYYYYMMDD("19440608"));
        juno.setAggressorCountry(Country.BRITAIN);
        juno.setDefendingCountry(Country.GERMANY);
        
        gold.setLandingStartDate(DateUtils.getDateYYYYMMDD("19440606"));
        gold.setLandingStopDate(DateUtils.getDateYYYYMMDD("19440608"));
        gold.setAggressorCountry(Country.BRITAIN);
        gold.setDefendingCountry(Country.GERMANY);
        
        omaha.setLandingStartDate(DateUtils.getDateYYYYMMDD("19440606"));
        omaha.setLandingStopDate(DateUtils.getDateYYYYMMDD("19440608"));
        omaha.setAggressorCountry(Country.USA);
        omaha.setDefendingCountry(Country.GERMANY);

        utah.setLandingStartDate(DateUtils.getDateYYYYMMDD("19440606"));
        utah.setLandingStopDate(DateUtils.getDateYYYYMMDD("19440608"));
        utah.setAggressorCountry(Country.USA);
        utah.setDefendingCountry(Country.GERMANY);
    }

    private void readAmphibiousAssaults (String fileName) throws PWCGException 
    {        
        try 
        {           
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (line.startsWith(DevIOConstants.SHIP))
                {
                    Vehicle vehicle = VehicleIO.readBlock(reader);
                    AmphibiousAssault amphibiousAssault = getNormandyAmphibiousAssault(vehicle);
                    if (amphibiousAssault != null)
                    {
                        String landingCraftName = parseLandingCraftName(vehicle.getScript());

                        double angleToBeach = MathUtils.adjustAngle(vehicle.getOrientation().getyOri(), 180);
                        vehicle.getOrientation().setyOri(angleToBeach);
                        
                        AmphibiousAssaultShipDefinition amphibiousAssaultShipDefinition = new AmphibiousAssaultShipDefinition();
                        amphibiousAssaultShipDefinition.setShipType(landingCraftName);
                        amphibiousAssaultShipDefinition.setDestination(vehicle.getPosition());
                        amphibiousAssaultShipDefinition.setOrientation(vehicle.getOrientation());
                        
                        amphibiousAssault.addAssaultShip(amphibiousAssaultShipDefinition);
                     }
                }
            }
            
            reader.close();
        } 
        catch (Exception e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
    }

    private String parseLandingCraftName(String vehicleName)
    {
        int startIndex = vehicleName.lastIndexOf("\\");
        String fullName = vehicleName.substring(startIndex+1);
        int endIndex = fullName.indexOf(".");
        String landingCraftName = fullName.substring(0, endIndex);
        return landingCraftName;
    }

    private AmphibiousAssault getNormandyAmphibiousAssault(Vehicle vehicle)
    {
        if (vehicle.getVehicleName().contains("Dieppe"))
        {
            return dieppe;
        }
        
        if (vehicle.getVehicleName().contains("Gold"))
        {
            return gold;
        }

        if (vehicle.getVehicleName().contains("Juno"))
        {
            return juno;
        }

        if (vehicle.getVehicleName().contains("Sword"))
        {
            return sword;
        }

        if (vehicle.getVehicleName().contains("Omaha"))
        {
            return omaha;
        }
        if (vehicle.getVehicleName().contains("Utah"))
        {
            return utah;
        }
        
        return null;
    }

    private void writeAmphibiousAssaults(String mapName) throws PWCGException
    {
        AmphibiousAssaultIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\Amphibious\\", "Dieppe.json", dieppe);
        AmphibiousAssaultIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\Amphibious\\", "Sword.json", sword);
        AmphibiousAssaultIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\Amphibious\\", "Juno.json", juno);
        AmphibiousAssaultIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\Amphibious\\", "Gold.json", gold);
        AmphibiousAssaultIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\Amphibious\\", "Omaha.json", omaha);
        AmphibiousAssaultIOJson.writeJson(PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\Amphibious\\", "Utah.json", utah);
    }
    
}
