import core.Component;
import core.Rule;
import core.RulesBase;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

public class Test {
    public static void main(String[] args) throws IOException {

        AbstractSystem as = AbstractSystem.getInstance();//создание объекта класса AbstractSystem
        as.importRuleBase("laop/Test.json");
        // загрузка базы знаний
//        as.addRule("ЗИ", "ПЗИ", 0.5, 1);
//        as.addRule("ПИ", "ПЗИ", 0.5, 1);
//        as.addRule("ИИ", "ПИ", 1, 2);
//        as.addRule("ПЗ", "ПИ", 1, 2);
//        as.addRule("ЗЗ", "ПЗ", 0.33, 1);
//        as.addRule("ИЗ", "ПЗ", 0.66, 1);

        //добавление факта
       as.addFact("c11", 0.4);
       as.addFact("c12",0.4);
       as.addFact("c2",0.4);
       as.addFact("c31",1);
       as.addFact("c32",1);

        System.out.println(as.analaze());


       // System.out.println(new BigDecimal(as.analaze()).setScale(2, BigDecimal.ROUND_UP).doubleValue());



    }
}
