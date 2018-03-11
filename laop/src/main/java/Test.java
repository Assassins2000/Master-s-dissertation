import core.Component;
import core.Rule;
import core.RulesBase;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Hashtable;

public class Test {
    public static void main(String[] args) throws IOException {

       AbstractSystem as= AbstractSystem.getInstance();
        as.addRule("ЗИ", "ПЗИ", 0.5, "additive");
        as.addRule("ПИ", "ПЗИ", 0.5, "additive");
        as.addRule("ИИ", "ПИ", 1, "pessimistic");
        as.addRule("ПЗ", "ПИ", 1, "pessimistic");
        as.addRule("ЗЗ", "ПЗ", 0.33, "additive");
        as.addRule("ИЗ", "ПЗ", 0.66, "additive");




       // as.addRule("ЗЧТ", "ЗНП", 0.33, "additive");
        //as.addRule("ЗЧР", "ЗНП", 0.67, "additive");
        //as.addRule("Окно1", "ЗЧР", 0.5, "additive");
        //as.addRule("Окно2", "ЗЧР", 0.5, "additive");

        //as.importRuleBase("Output.json");


        as.addFact("ИИ", 1);

       // as.addFact("ЗИ", 0);

        System.out.println(as.getEfficiency());
        System.out.println(new BigDecimal(as.getEfficiency()).setScale(2, BigDecimal.ROUND_UP).doubleValue());

    }
}
