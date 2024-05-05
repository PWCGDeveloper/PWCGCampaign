package pwcg.dev.jsonconvert;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.airfield.AirfieldConfiguration;
import pwcg.campaign.group.airfield.AirfieldDescriptor;
import pwcg.campaign.group.airfield.AirfieldDescriptorMapSet;
import pwcg.campaign.group.airfield.Runway;
import pwcg.campaign.io.json.AirfieldDescriptorIOMapJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.MathUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class BoSRunwayFixer
{
    
    public static void main(String[] args) throws PWCGException
    {
        PWCGContext.setProduct(PWCGProduct.BOS);

        BoSRunwayFixer runwayFixer = new BoSRunwayFixer();
        runwayFixer.limitAllMapsToOneRunway("Moscow");
        runwayFixer.limitAllMapsToOneRunway("Stalingrad");
        runwayFixer.limitAllMapsToOneRunway("Kuban");
        runwayFixer.limitAllMapsToOneRunway("East1944");
        runwayFixer.limitAllMapsToOneRunway("East1945");
    }

    public void limitAllMapsToOneRunway (String mapName) throws PWCGException
    {
        String pwcgInputDir = PWCGContext.getInstance().getDirectoryManager().getPwcgInputDir() + mapName + "\\";
        AirfieldDescriptorMapSet airfieldDescriptors = AirfieldDescriptorIOMapJson.readJson(pwcgInputDir, AirfieldConfiguration.AIRFIELD_LOCATION_FILE_NAME);
        limitToOneRunway(airfieldDescriptors);
        // AirfieldDescriptorIOJson.writeJson(pwcgInputDir,  AirfieldConfiguration.AIRFIELD_LOCATION_FILE_NAME, airfieldDescriptors);
    }

    private void limitToOneRunway(AirfieldDescriptorMapSet airfieldDescriptors)
    {
        for (AirfieldDescriptor desc : airfieldDescriptors.getLocations())
        {
            if (desc.getRunways().size() > 1)
            {
                List<Runway> oneRunway = new ArrayList<>();
                oneRunway.add(desc.getRunways().get(0));
                desc.setRunways(oneRunway);
            }
            
            analyzeAirfield(desc);
        }
    }
    
    private void analyzeAirfield(AirfieldDescriptor desc)
    {
        if (desc.getName().equals("Ruza") || desc.getName().equals("Nikolskoye"))
        {
            Coordinate parkingLocation = desc.getRunways().get(0).getParkingLocation().getPosition();
            List<Coordinate> taxiStartLocations = desc.getRunways().get(0).getTaxiToStart();
            Coordinate takeoffLocation = desc.getRunways().get(0).getEndPos();
            String name = desc.getName();
            
            Double distance = MathUtils.calcDist(parkingLocation, takeoffLocation);
            PWCGLogger.log(LogLevel.DEBUG, name + " distance: " + distance.intValue());

            Double taxiInitialDistance = MathUtils.calcDist(parkingLocation, taxiStartLocations.get(0));
            PWCGLogger.log(LogLevel.DEBUG, name + " taxi initial distance: " + taxiInitialDistance.intValue());

            
            Coordinate lasttaxiLoc = null;
            for (Coordinate taxiLoc : taxiStartLocations)
            {
                if (lasttaxiLoc != null)
                {
                    Double taxiDistance = MathUtils.calcDist(lasttaxiLoc, taxiLoc);
                    PWCGLogger.log(LogLevel.DEBUG, name + " taxi distance: " + taxiDistance.intValue());
                }
                
                lasttaxiLoc = taxiLoc;
            }
        }
    }
}
