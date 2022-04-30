package parsing;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class regexpr {

    public static void test1() {

        String s = "Высота ${xml_statementtc_connectobjname} объекта: ${xml_infmaxparam1}, этажность: ${xml_infmaxparam2}, протяженность сети: ${xml_infmaxparam3}, диаметр сети: ${xml_infmaxparam4}";

        Pattern pt = Pattern.compile("\\$\\{\\w+\\}");
        Matcher mt = pt.matcher(s);

        System.out.println(mt.find());

        mt = pt.matcher("sdsdffr {xml_statementtc_connectobjname} объекта");

        System.out.println(mt.find());

        System.out.println(s.indexOf("\\$\\{\\w+\\}"));
        s = s.replaceAll("\\$\\{\\w+\\}", "asd");
        System.out.println(s);

    }

}
