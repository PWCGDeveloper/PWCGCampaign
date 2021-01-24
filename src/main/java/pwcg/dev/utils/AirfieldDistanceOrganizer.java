package pwcg.dev.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pwcg.campaign.api.Side;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.context.PWCGMap.FrontMapIdentifier;
import pwcg.campaign.context.PWCGProduct;
import pwcg.campaign.group.AirfieldManager;
import pwcg.campaign.group.airfield.Airfield;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;

public class AirfieldDistanceOrganizer
{

    AirfieldSet alliedAirfieldSet;
    AirfieldSet axisAirfieldSet;

    public static void main(String[] args)
    {
		System.setProperty( "user.dir", "E:\\PWCG\\workspacePWCGGradle\\PwcgCampaign" );

        AirfieldDistanceOrganizer airfieldReporter = new AirfieldDistanceOrganizer();
        
        Date startDate;
        try
        {
            //startDate = DateUtils.getDateYYYYMMDD("19420701");
            //startDate = DateUtils.getDateYYYYMMDD("19420725");
            //startDate = DateUtils.getDateYYYYMMDD("19420810");
            //startDate = DateUtils.getDateYYYYMMDD("19420821");
            //startDate = DateUtils.getDateYYYYMMDD("19430120");
            startDate = DateUtils.getDateYYYYMMDD("19430131");
            //startDate = DateUtils.getDateYYYYMMDD("19430918");
            //startDate = DateUtils.getDateYYYYMMDD("19430927");
            //startDate = DateUtils.getDateYYYYMMDD("19431004");
 
            airfieldReporter.process(startDate, FrontMapIdentifier.KUBAN_MAP);
         }
        catch (PWCGException e)
        {
            e.printStackTrace();
        }
    }
    
    

    /**
     * 
     */
    public void process(Date startDate, FrontMapIdentifier frontMapIdentifier)
    {

        try
        {
        	PWCGContext.setProduct(PWCGProduct.BOS);
        	            
            PWCGContext.getInstance();
            PWCGContext.getInstance().changeContext(frontMapIdentifier);;
                                 
            AirfieldManager manager = PWCGContext.getInstance().getCurrentMap().getAirfieldManager();

            alliedAirfieldSet = sortAirfieldsByDistance(manager.getAirFieldsForSide(startDate, Side.ALLIED), startDate, Side.ALLIED);
            System.out.println("\n\n\nAllied");
            alliedAirfieldSet.side = Side.ALLIED;
            alliedAirfieldSet.date = startDate;
            alliedAirfieldSet.dump();

            axisAirfieldSet = sortAirfieldsByDistance(manager.getAirFieldsForSide(startDate, Side.AXIS), startDate, Side.AXIS);
            System.out.println("\n\n\nAxis");
            axisAirfieldSet.side = Side.AXIS;
            axisAirfieldSet.date = startDate;
            axisAirfieldSet.dump();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private AirfieldSet sortAirfieldsByDistance(List<Airfield> fields, Date date, Side side) throws PWCGException
    {
        AirfieldSet airfieldSet = new AirfieldSet();

        System.out.println("\n\n\n" + side + "\n");

        // Sort the fields 
        for (Airfield field: fields)
        {
            if (field.getName().contains("DUPLICATE"))
            {
                continue;
            }
            
            int distance = AirfieldReporter.getDistanceToFront(field, side, date);
            if (distance < 15)
            {
                airfieldSet.fieldsTooClose.put(field.getName(), field);
            }
            else if (distance < 50)
            {
                airfieldSet.fieldsFighter.put(field.getName(), field);
            }
            else if (distance < 70)
            {
                airfieldSet.fieldsEither.put(field.getName(), field);
            }
            else if (distance < 100)
            {
                airfieldSet.fieldsBomber.put(field.getName(), field);
            }
            else 
            {
                airfieldSet.fieldsTooFar.put(field.getName(), field);
            }
        }
        
        return airfieldSet;
    }
    
    
    
    public class AirfieldSet
    {
        private Map <String, Airfield> fieldsFighter = new TreeMap <String, Airfield>();
        private Map <String, Airfield> fieldsBomber = new TreeMap <String, Airfield>();
        private Map <String, Airfield> fieldsEither = new TreeMap <String, Airfield>();
        private Map <String, Airfield> fieldsTooClose = new TreeMap <String, Airfield>();  
        private Map <String, Airfield> fieldsTooFar = new TreeMap <String, Airfield>();  
        private Side side;
        private Date date;
        
        public void dump() throws PWCGException
        {
        	dumpList ("Close", new ArrayList<Airfield>(fieldsTooClose.values()));
        	dumpList ("Fighter", new ArrayList<Airfield>(fieldsFighter.values()));
        	dumpList ("Attack", new ArrayList<Airfield>(fieldsEither.values()));
        	dumpList ("Bomber", new ArrayList<Airfield>(fieldsBomber.values()));
        	dumpList ("Far", new ArrayList<Airfield>(fieldsTooFar.values()));
        }
        
        private void dumpList(String description, List<Airfield> fields) throws PWCGException
        {
            System.out.println(description);
        	for (Airfield field : fields)
        	{
                int distance = AirfieldReporter.getDistanceToFront(field, side, date);

                System.out.println("    " + field.getName() + ".  Distance: " + distance);
        	}
        }
        
        public Map <String, Airfield> getFighterFields()
        {
            Map <String, Airfield> fighterFields = new HashMap <String, Airfield>();
            
            fighterFields.putAll(fieldsFighter);
            fighterFields.putAll(fieldsEither);
            
            return fighterFields;
        }
        
        public Map <String, Airfield> getBomberFields()
        {
            Map <String, Airfield> bomberFields = new HashMap <String, Airfield>();
            
            bomberFields.putAll(fieldsBomber);
            bomberFields.putAll(fieldsEither);
            
            return bomberFields;
        }

        public Map<String, Airfield> getFieldsFighter()
        {
            return fieldsFighter;
        }

        public Map<String, Airfield> getFieldsBomber()
        {
            return fieldsBomber;
        }

        public Map<String, Airfield> getFieldsEither()
        {
            return fieldsEither;
        }

        public Map<String, Airfield> getFieldsTooClose()
        {
            return fieldsTooClose;
        }

        public Map<String, Airfield> getFieldsTooFar()
        {
            return fieldsTooFar;
        }
        
        
    }
}
