package pwcg.testutils;

import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.aar.inmission.phase1.parse.event.AType12;
import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.core.exception.PWCGException;
import pwcg.product.fc.country.FCCountry;

public class TestATypeFactory
{
    static public int id = 100;
    
    static public AType12 makeFrenchPlane(String frenchPilotName, String frenchPilotBotId) throws PWCGException
    {
        ++id;
        AType12 aType12Parsed = new AType12("AType:12 ID:1488895 TYPE:spad13 COUNTRY:101 NAME:Lt Pierre Trudeau PID:-1 POS(119648.406,152.195,44274.488)", Integer.valueOf(id).toString());
        AType12 aType12 = new AType12(frenchPilotBotId, aType12Parsed.getType(), frenchPilotName, aType12Parsed.getCountry(), aType12Parsed.getPid());
        return aType12;
    }
    
    static public AType12 makeGermanPlane(String germanPilotName, String germanPilotBotId) throws PWCGException
    {
        ++id;
        AType12 aType12Parsed = new AType12("AType:12 ID:1488895 TYPE:albatrosd5 COUNTRY:101 NAME:Lt Pierre Trudeau PID:-1 POS(119648.406,152.195,44274.488)", Integer.valueOf(id).toString());
        AType12 aType12 = new AType12(germanPilotBotId, aType12Parsed.getType(), germanPilotName, aType12Parsed.getCountry(), aType12Parsed.getPid());
        return aType12;
    }
    
    static public AType12 makeBalloon(ICountry country) throws PWCGException
    {
        ++id;
        AType12 aType12 = new AType12(Integer.valueOf (id).toString(), "Balloon", "Balloon", country, AARLogParser.UNKNOWN_MISSION_LOG_ENTITY);
        return aType12;
    }
    
    static public AType12 makeTruck(Country countryCode) throws PWCGException
    {
        ++id;        
        ICountry country;
        if (countryCode == Country.FRANCE)
        {
            country = new FCCountry(Country.FRANCE);            
        }
        else
        {
            country = new FCCountry(Country.GERMANY);            
        }
        AType12 aType12 = new AType12(Integer.valueOf (id).toString(), "Truck", "Truck", country, AARLogParser.UNKNOWN_MISSION_LOG_ENTITY);
        return aType12;
    }
    
    static public AType12 makePilotBot(IAType12 plane) throws PWCGException
    {
        ++id;
        AType12 aType12 = new AType12(Integer.valueOf (id).toString(), "Common Bot", "Bot", plane.getCountry(), plane.getId());
        return aType12;
    }
}
