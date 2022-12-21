package pwcg.campaign.squadmember;

import java.text.Normalizer;
import java.util.LinkedHashMap;
import java.util.Map;

public class NameNormalizer {
    private static final Map<Character,String> replacements = new LinkedHashMap<>();
    static {
        // assume here capital chars indicate the beginning of a first/last name, hence only first capitalized character for transliterated string

        /*
        Some common explicit mappings. Using unicode codes here for compatibility purposes .
        Adding explicit mappings here is slightly faster (and easier to reason about) than using java.text.Normalizer.
        */

        // German/Scandinavian/Estonian
        replacements.put('\u00F6', "oe");
        replacements.put('\u00D6', "Oe");
        replacements.put('\u00F5', "oe");
        replacements.put('\u00D5', "Oe");
        replacements.put('\u00FC', "ue");
        replacements.put('\u00DC', "Ue");
        replacements.put('\u00E4', "ae");
        replacements.put('\u00C4', "Ae");
        replacements.put('\u00DF', "ss");
        replacements.put('\u1E9E', "SS");
        replacements.put('\u00C6', "Ae");
        replacements.put('\u00E6', "ae");
        replacements.put('\u00D8', "Oe");
        replacements.put('\u00F8', "oe");

        // Spanish
        replacements.put('\u00F1', "n");
        replacements.put('\u00D1', "N");
        replacements.put('\u00C1', "A");
        replacements.put('\u00E1', "a");
        replacements.put('\u00C9', "E");
        replacements.put('\u00E9', "e");
        replacements.put('\u00CD', "I");
        replacements.put('\u00ED', "i");
        replacements.put('\u00D3', "O");
        replacements.put('\u00F3', "o");
        replacements.put('\u00DA', "U");
        replacements.put('\u00FA', "u");

        // Polish
        replacements.put('\u0141', "L");
        replacements.put('\u0142', "l");
        replacements.put('\u0142', "l");
        replacements.put('\u0142', "l");
        replacements.put('\u0104', "A");
        replacements.put('\u0105', "a");
        replacements.put('\u0106', "C");
        replacements.put('\u0107', "c");
        replacements.put('\u0118', "E");
        replacements.put('\u0119', "e");
        replacements.put('\u0143', "N");
        replacements.put('\u0144', "n");
        replacements.put('\u00D3', "O");
        replacements.put('\u00F3', "o");
        replacements.put('\u015A', "S");
        replacements.put('\u015B', "s");
        replacements.put('\u0179', "Z");
        replacements.put('\u017A', "z");
        replacements.put('\u017B', "Z");
        replacements.put('\u017C', "z");

        // French
        replacements.put('\u0152', "Oe");
        replacements.put('\u0153', "oe");
    }

    public static String normalize(String input, int maxLen) {
        StringBuilder sb = null; // init only when we actually need it
        boolean needAdditionalNormalization = false;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c < 0x20 || c > 0x7e) { // check if char outside printable ascii range
                sb = sb != null ? sb : new StringBuilder(i > 0 ? input.substring(0,i) : "");
                String r = replacements.get(c);
                if (r == null){
                    needAdditionalNormalization = true;
                    sb.append(c);
                }
                else {
                    sb.append(r);
                }
            }
            else if (sb != null){
                sb.append(c);
            }
        }
        if (sb == null) // all ascii name
            return truncate(input, maxLen);

        return truncate(needAdditionalNormalization ? Normalizer.normalize(sb, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "") : sb.toString(), maxLen);
    }

    private static String truncate(String input, int maxLength) {
        return input.length() > maxLength ? input.substring(0, maxLength) : input;
    }

}
