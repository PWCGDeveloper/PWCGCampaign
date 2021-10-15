package pwcg.gui.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pwcg.core.exception.PWCGException;

@ExtendWith(MockitoExtension.class)
public class PWCGStringValidatorTest
{

    @Test
    public void validDescriptorTest () throws PWCGException
    {
        assert(PWCGStringValidator.isValidDescriptor("ABC123") == true);
        assert(PWCGStringValidator.isValidDescriptor("ABC_.-123") == true);
    }

    @Test
    public void invalidDescriptorTest () throws PWCGException
    {
        assert(PWCGStringValidator.isValidDescriptor("ABC123ü") == false);
        assert(PWCGStringValidator.isValidDescriptor("ABC123&") == false);
    }

    @Test
    public void validNameTest () throws PWCGException
    {
        assert(PWCGStringValidator.isValidName("ABC") == true);
        assert(PWCGStringValidator.isValidName("ABCabc") == true);
        assert(PWCGStringValidator.isValidName("ABC.XYZ") == true);
        assert(PWCGStringValidator.isValidName("ABC-XYZ") == true);
        assert(PWCGStringValidator.isValidName("ABC XYZ") == true);
    }

    @Test
    public void invalidNameTest () throws PWCGException
    {
        assert(PWCGStringValidator.isValidName("ABC123&_.") == false);
        assert(PWCGStringValidator.isValidName("ABC1") == false);
        assert(PWCGStringValidator.isValidName("ABC&") == false);
    }
}
