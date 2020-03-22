package pwcg.product.bos.plane;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.Country;
import pwcg.campaign.context.PWCGContext;
import pwcg.campaign.plane.Equipment;
import pwcg.campaign.plane.EquippedPlane;
import pwcg.campaign.plane.IPlaneMarkingManager;
import pwcg.campaign.squadron.Squadron;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.product.bos.country.BoSServiceManager;

public class BoSPlaneMarkingManager implements IPlaneMarkingManager {
    private static String RAF_OLD_SERIAL_LETTERS = "KLMNPRTVWXZ";
    private static String RAF_NEW_SERIAL_LETTERS = "ABDEFGHJKLMNPRSTVWXZ";

    private static Map<Integer, Integer> USAAF_ORDERS_BY_YEAR = new HashMap<>();
    static {
    	USAAF_ORDERS_BY_YEAR.put(1940, 3162);
    	USAAF_ORDERS_BY_YEAR.put(1941, 39600);
    	USAAF_ORDERS_BY_YEAR.put(1942, 100000); // Actual value is 110188, but limit to avoid 7-digit tail codes
    	USAAF_ORDERS_BY_YEAR.put(1943, 52437);
    	USAAF_ORDERS_BY_YEAR.put(1944, 92098);
    }
	
    @Override
    public void allocatePlaneIdCode(Campaign campaign, int squadronId, Equipment equipment, EquippedPlane equippedPlane) throws PWCGException
    {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(squadronId);

        Set<String> allocatedCodes = new HashSet<>();
        for (EquippedPlane plane : equipment.getActiveEquippedPlanes().values())
        {
            allocatedCodes.add(plane.getAircraftIdCode());
        }

        if (squadron.getService() == BoSServiceManager.LUFTWAFFE)
        {
            if (squadron.determineDisplayName(campaign.getDate()).contains("JG") ||
                (squadron.determineDisplayName(campaign.getDate()).contains("SG") &&
                 squadron.determineUnitIdCode(campaign.getDate()) == null))
            {
                // Allocate numbers 1-N
                int code = 1;
                while (allocatedCodes.contains(Integer.toString(code)))
                    code++;

                equippedPlane.setAircraftIdCode(Integer.toString(code));
            } else {
                // Allocate letters from A
                // Do this randomly rather than in sequence?
                char code = 'A';
                while (allocatedCodes.contains(Character.toString(code)))
                    code++;
                if (code > 'Z')
                    throw new PWCGException("Unable to allocate plane ID code for squadron " + squadron.getSquadronId());

                equippedPlane.setAircraftIdCode(Character.toString(code));
            }
        }
        else if (squadron.getService() == BoSServiceManager.VVS ||
                 squadron.getService() == BoSServiceManager.NORMANDIE)
        {
            // Random numbers 1-99
            int code = RandomNumberGenerator.getRandom(99);
            while (allocatedCodes.contains(Integer.toString(code + 1)))
                code = (code + 1) % 99;

            equippedPlane.setAircraftIdCode(Integer.toString(code + 1));
        }
        else if (squadron.getService() == BoSServiceManager.REGIA_AERONAUTICA)
        {
            // Allocate numbers 1-N
            int code = 1;
            while (allocatedCodes.contains(Integer.toString(code)))
                code++;

            equippedPlane.setAircraftIdCode(Integer.toString(code));
        }
        else if (squadron.getService() == BoSServiceManager.USAAF ||
                 squadron.getService() == BoSServiceManager.RAF ||
                 squadron.getService() == BoSServiceManager.FREE_FRENCH)
        {
            // Allocate letters from A
            // Do this randomly rather than in sequence?
            char code = 'A';
            while (allocatedCodes.contains(Character.toString(code)))
                code++;
            if (code > 'Z')
                throw new PWCGException("Unable to allocate plane ID code for squadron " + squadron.getSquadronId());

            equippedPlane.setAircraftIdCode(Character.toString(code));
        }
    }

    @Override
    public String determineDisplayMarkings(Campaign campaign, EquippedPlane equippedPlane) throws PWCGException {
        Squadron squadron = PWCGContext.getInstance().getSquadronManager().getSquadron(equippedPlane.getSquadronId());

        if (squadron.getService() == BoSServiceManager.LUFTWAFFE)
        {
            if (squadron.determineDisplayName(campaign.getDate()).contains("JG") ||
                squadron.determineDisplayName(campaign.getDate()).contains("Sch.G") ||
                (squadron.determineDisplayName(campaign.getDate()).contains("SG") &&
                 squadron.determineUnitIdCode(campaign.getDate()) == null))
            {
                return equippedPlane.getAircraftIdCode() + "+" + squadron.determineSubUnitIdCode(campaign.getDate());
            } else {
                return squadron.determineUnitIdCode(campaign.getDate()) +
                        "+" +
                        equippedPlane.getAircraftIdCode() +
                        squadron.determineSubUnitIdCode(campaign.getDate());
            }
        }
        else if (squadron.getService() == BoSServiceManager.VVS ||
                 squadron.getService() == BoSServiceManager.NORMANDIE)
        {
            return equippedPlane.getAircraftIdCode();
        }
        else if (squadron.getService() == BoSServiceManager.REGIA_AERONAUTICA ||
                 squadron.getService() == BoSServiceManager.USAAF ||
                 squadron.getService() == BoSServiceManager.RAF ||
                 squadron.getService() == BoSServiceManager.FREE_FRENCH)
        {
            return squadron.determineUnitIdCode(campaign.getDate()) +
                    "-" +
                    equippedPlane.getAircraftIdCode();
        }

        return equippedPlane.getDisplaySerial();
    }

    @Override
    public void generatePlaneSerial(Date date, EquippedPlane plane, int service) throws PWCGException
    {
    	if (service == BoSServiceManager.RAF || service == BoSServiceManager.FREE_FRENCH || (service == BoSServiceManager.VVS && plane.getPrimaryUsedBy().indexOf(Country.BRITAIN) == 0))
    	{
    		double days = DateUtils.daysDifference(DateUtils.getDateYYYYMMDD("19000101"), date);
    		
    		// Assume 14-90 days delivery time
    		days -= 14;
    		days -= RandomNumberGenerator.getRandom((90-14) * 100) / 100.0;
    		
    		// Formula derived by plotting Spitfire production in Excel
    		int id = (int) (-3.83559265e-5*days*days*days + 1.806797f*days*days - 28157.9091*days + 145354255);
    		
    		if (id < RAF_OLD_SERIAL_LETTERS.length() * 9000)
    		{
    			plane.setServiceSerial(String.format("%c%04d",
    					                             RAF_OLD_SERIAL_LETTERS.charAt(id / 9000),
    					                             (id % 9000) + 1000));
    		} else {
    			int sub_id = id - RAF_OLD_SERIAL_LETTERS.length() * 9000;
    			int id_low = (sub_id % 900) + 100;
    			int id_mid = (sub_id / 900) % RAF_NEW_SERIAL_LETTERS.length();
    			int id_high = sub_id / (900 * RAF_NEW_SERIAL_LETTERS.length());

    			plane.setServiceSerial(String.format("%c%c%03d",
                        RAF_NEW_SERIAL_LETTERS.charAt(id_high),
                        RAF_NEW_SERIAL_LETTERS.charAt(id_mid),
                        id_low));
    		}
    	}
    	else if (service == BoSServiceManager.USAAF || (service == BoSServiceManager.VVS && plane.getPrimaryUsedBy().indexOf(Country.USA) == 0))
    	{
    		// Assume 6-12 months from order to delivery
    		Date orderDate = DateUtils.advanceTimeDays(date, -182 - RandomNumberGenerator.getRandom(183));
    		
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderDate);
    		float dayOffset = calendar.get(Calendar.DAY_OF_YEAR);
    		dayOffset += RandomNumberGenerator.getRandom(1000) / 1000f;
    		int sequence = (int) (dayOffset / 365f * USAAF_ORDERS_BY_YEAR.get(calendar.get(Calendar.YEAR)));
    		
    		plane.setServiceSerial(String.format("%02d-%03d",
    				                             calendar.get(Calendar.YEAR) % 100,
    				                             sequence));
    	}
    	else if (service == BoSServiceManager.VVS || plane.getPrimaryUsedBy().indexOf(Country.RUSSIA) == 0)
    	{
    		try {
    			// Generate a fake Zavod number - assume each arch type was made in one factory
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(plane.getArchType().getBytes());
				int zavod = ((256 + (int)md.digest()[0]) % 256) + 1;

	    		double days = DateUtils.daysDifference(DateUtils.getDateYYYYMMDD("19390101"), date);
	    		
	    		// Assume 14-90 days delivery time
	    		days -= 14;
	    		days -= RandomNumberGenerator.getRandom((90-14) * 100) / 100.0;
    		
	    		int serial = (int)(days * 40);
	    		
	    		plane.setServiceSerial(String.format("%d%05d", zavod, serial));
    		} catch (NoSuchAlgorithmException e) {
				PWCGLogger.logException(e);
			}
    		
    	}
    	else if (service == BoSServiceManager.LUFTWAFFE || plane.getPrimaryUsedBy().indexOf(Country.GERMANY) == 0)
    	{
    		try {
	    		double days = DateUtils.daysDifference(DateUtils.getDateYYYYMMDD("19390601"), date);
	    		
	    		// Assume 14-90 days delivery time
	    		days -= 14;
	    		days -= RandomNumberGenerator.getRandom((90-14) * 100) / 100.0;
	    		
	    		// Assume werknummern are allocated in blocks of 230
	    		int serial = (int) (days * 8);
	    		serial = ((serial / 230) * 230 * 47) + (serial % 230);
	    		
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.update(plane.getType().getBytes());
				serial += ((256 + (int)md.digest()[0]) % 47) * 230;
				
				plane.setServiceSerial(Integer.toString(serial));
    		} catch (NoSuchAlgorithmException e) {
				PWCGLogger.logException(e);
    		}
    	}
    }

    @Override
	public void generatePlaneSerialHistoric(Campaign campaign, EquippedPlane equippedPlane, int service) throws PWCGException {
		// Randomize a deployment date for the plane
		Date deployDate = DateUtils.advanceTimeDays(campaign.getDate(), -RandomNumberGenerator.getRandom(270));
		if (deployDate.before(equippedPlane.getIntroduction()))
			deployDate = equippedPlane.getIntroduction();
		generatePlaneSerial(deployDate, equippedPlane, service);
	}

}
