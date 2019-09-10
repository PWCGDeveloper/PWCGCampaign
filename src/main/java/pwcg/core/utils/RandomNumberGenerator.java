package pwcg.core.utils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RandomNumberGenerator 
{
    private static SecureRandom generator;
    private static final int MAX_USES = 1001;
    private static int numnUses = 0;
    
	public static int getRandom(int num)
	{
	    buildGenerator();
	    checkReseed();
		int retVal = generateRandomNumber(num);
		return retVal;
	}

    private static void buildGenerator()
    {
        if (generator == null)
        {
            getSecureRandomSHA1PRNG();
            if (generator == null)
            {
                getSecureRandomDateSeed();
            }
        }
    }

    private static int generateRandomNumber(int num)
    {
        int retVal = 0;
		if (num > 0)
		{
			retVal = generator.nextInt(num);
		}
		
		++numnUses;
        return retVal;
    }

    private static void checkReseed()
    {
        if (numnUses > MAX_USES)
	    {
	        seedGenerator();
	        numnUses = 0;
	    }
    }

    private static void getSecureRandomSHA1PRNG()
    {
        try
        {
            generator = SecureRandom.getInstance("SHA1PRNG");
            seedGenerator();
        }
        catch (NoSuchAlgorithmException e)
        {
        }
    }

    private static void seedGenerator()
    {
        byte seed[] = generator.generateSeed(20);
        generator.setSeed(seed);
    }

    private static void getSecureRandomDateSeed()
    {
        generator = new SecureRandom();
    }
}
