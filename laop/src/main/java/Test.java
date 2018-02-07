import core.Component;
import core.RulesBase;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {

       AbstractSystem as= AbstractSystem.getInstance();
        as.addRule("ЗИ", "ПЗИ", 0.5, "additive");
        as.addRule("ПИ", "ПЗИ", 0.5, "additive");
        as.addRule("ИИ", "ПИ", 1, "pessimistic");
        as.addRule("ПЗ", "ПИ", 1, "pessimistic");
        as.addRule("ЗЗ", "ПЗ", 0.33, "additive");
        as.addRule("ИЗ", "ПЗ", 0.66, "additive");

        as.addFact("ИЗ", 0.5);
        as.addFact("ИИ", 1);
        as.addFact("ЗИ", 1);
        as.addFact("ЗЗ", 0.5);

        as.addRule("ЗЧТ", "ЗНП", 0.33, "additive");
        as.addRule("ЗЧР", "ЗНП", 0.67, "additive");
        as.addRule("Окно1", "ЗЧР", 0.5, "additive");
        as.addRule("Окно2", "ЗЧР", 0.5, "additive");

        as.addFact("Окно1", 0.5);
        as.addFact("Окно2", 0.5);

          as.exportRuleBase();
//        System.out.println();

    }
}
