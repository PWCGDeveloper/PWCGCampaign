package pwcg.campaign.squadmember;

import java.text.Normalizer;
import java.util.LinkedHashMap;
import java.util.Map;

public class NameNormalizer {
    private static final Map<Character,String> replacements = new LinkedHashMap<>();
    static {
        // assume here capital chars indicate the beginning of a first/last name, hence only first capitalized character for transliterated string
        replacements.put('�', "oe");
        replacements.put('�', "Oe");
        replacements.put('�', "o");
        replacements.put('�', "O");
        replacements.put('�', "u");
        replacements.put('�', "U");
        replacements.put('�', "ae");
        replacements.put('�', "Ae");
        replacements.put('�', "ss");
        replacements.put('�', "Oe");
        replacements.put('�', "oe");
        replacements.put('�', "Ae");
        replacements.put('�', "ae");
        replacements.put('�', "Oe");
        replacements.put('�', "oe");
        replacements.put('�', "n");
        replacements.put('�', "N");
    }

    public static String normalize(String input, int maxLen) {
        StringBuilder sb = null; // init only when we actually need it
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c < 0x20 || c > 0x7e) { // check if char outside printable ascii range
                sb = sb != null ? sb : new StringBuilder(i > 0 ? input.substring(0,i) : "");
                String r = replacements.get(c);
                sb.append(r != null ? r : c);
            }
            else if (sb != null){
                sb.append(c);
            }
        }
        return truncate(sb != null ? Normalizer.normalize(sb, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "") : input, maxLen);
    }

    private static String truncate(String input, int maxLength) {
        return input.length() > maxLength ? input.substring(0, maxLength) : input;
    }

}
