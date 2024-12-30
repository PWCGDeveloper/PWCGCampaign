package pwcg.dev.jsonconvert.orig.io;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import pwcg.campaign.group.airfield.AirfieldDescriptor;
import pwcg.campaign.group.airfield.AirfieldDescriptorListSet;
import pwcg.campaign.group.airfield.AirfieldDescriptorMapSet;
import pwcg.campaign.group.airfield.Runway;
import pwcg.campaign.io.json.AirfieldDescriptorIOListJson;
import pwcg.campaign.io.json.AirfieldDescriptorIOMapJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.PWCGLocation;

public class AirfieldConverterBoS 
{    
    TreeMap<String, PWCGLocation> airfieldLocations = new TreeMap<>();
    List<Runway> airfieldRunways = new ArrayList<>();
    TreeMap<String, AirfieldDescriptor> airfields = new TreeMap<>();
    
    public static void main(String[] args) throws Exception
    {
        AirfieldConverterBoS jsonConverter = new AirfieldConverterBoS();
        jsonConverter.convertFile("Stalingrad");
        jsonConverter.convertFile("Kuban");
        jsonConverter.convertFile("East1944");
        jsonConverter.convertFile("East1945");
        jsonConverter.convertFile("Bodenplatte");
        jsonConverter.convertFile("Normandy");
    }


    private void convertFile(String mapName) throws PWCGException
    {
        String filePath = "C:\\PWCG\\workspacePWCG2023\\PWCGCampaign\\BoSData\\Input\\" + mapName + "\\";
        AirfieldDescriptorListSet listAirfields = AirfieldDescriptorIOListJson.readJson(filePath, "AirfieldLocations");
        
        AirfieldDescriptorMapSet mapSet = new AirfieldDescriptorMapSet();
        mapSet.setLocationSetName(listAirfields.getLocationSetName());
        mapSet.setLocations(listAirfields.getLocations());
        
        AirfieldDescriptorIOMapJson.writeJson(filePath, "AirfieldLocations.json", mapSet);
    }
}
