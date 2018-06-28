package pwcg.testutils;

import pwcg.aar.inmission.phase1.parse.AARLogParser;
import pwcg.aar.inmission.phase1.parse.event.IAType12;
import pwcg.aar.inmission.phase1.parse.event.rof.AType12;
import pwcg.campaign.api.ICountry;
import pwcg.campaign.context.Country;
import pwcg.campaign.plane.Balloon;
import pwcg.campaign.ww1.country.RoFCountry;
import pwcg.core.exception.PWCGException;

public class TestATypeFactory
{
    static public int id = 100;
    static public int entitySequenceNumber = 0;
    
    static public AType12 makeFrenchPlane() throws PWCGException
    {
        ++entitySequenceNumber;
        ++id;

        
        
        AType12 aType12 = new AType12("AType:12 ID:1488895 TYPE:spad7 COUNTRY:101 NAME:Lt Pierre Trudeau PID:-1 POS(119648.406,152.195,44274.488)");
        aType12.setId(new Integer(id).toString());
        return aType12;
    }
    
    static public AType12 makeGermanPlane() throws PWCGException
    {
        ++entitySequenceNumber;
        ++id;

        AType12 aType12 = new AType12("AType:12 ID:1488895 TYPE:albatrosd3 COUNTRY:101 NAME:Lt Pierre Trudeau PID:-1 POS(119648.406,152.195,44274.488)");
        aType12.setId(new Integer(id).toString());
        return aType12;
    }
    
    static public AType12 makeBalloon(ICountry country) throws PWCGException
    {
        ++entitySequenceNumber;
        ++id;

        AType12 aType12 = new AType12();
        
        Balloon balloon;

        balloon = new Balloon(country);
        
        aType12.setCountry(country);
        aType12.setName(balloon.getType());
        aType12.setId(new Integer (id).toString());
        aType12.setPid(AARLogParser.UNKNOWN_MISSION_LOG_ENTITY);
        aType12.setSequenceNum(entitySequenceNumber);
        aType12.setType(balloon.getName());
        
        return aType12;
    }
    
    static public AType12 makeTruck(Country countryCode) throws PWCGException
    {
        ++entitySequenceNumber;
        ++id;

        AType12 aType12 = new AType12();
        
        ICountry country;
        
        if (countryCode == Country.FRANCE)
        {
            country = new RoFCountry(Country.FRANCE);            
        }
        else
        {
            country = new RoFCountry(Country.GERMANY);            
        }
        
        aType12.setCountry(country);
        aType12.setName("Truck");
        aType12.setId(new Integer (id).toString());
        aType12.setPid(AARLogParser.UNKNOWN_MISSION_LOG_ENTITY);
        aType12.setSequenceNum(entitySequenceNumber);
        aType12.setType("Truck");
        
        return aType12;
    }
    
    static public AType12 makePilotBot(IAType12 plane) throws PWCGException
    {
        ++entitySequenceNumber;
        ++id;

        AType12 aType12 = new AType12();
        
        aType12.setCountry(plane.getCountry());
        aType12.setName("Bot");
        aType12.setId(new Integer (id).toString());
        aType12.setPid(plane.getId());
        aType12.setSequenceNum(entitySequenceNumber);
        aType12.setType("Common Bot");
        
        return aType12;
    }
}
