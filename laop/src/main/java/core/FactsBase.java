package core;

import com.sun.deploy.uitoolkit.impl.text.FXAppContext;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс служит для работы с базой фактов
 * Реализует паттерн Singleton
 *
 * @author Balanovsky Danil
 */
public class FactsBase {

    private static FactsBase ourInstance = new FactsBase();

    private FactsBase() {
        this.facts = new Hashtable<>();
    }

    @org.jetbrains.annotations.Contract(pure = true)
    public static FactsBase getInstance() {
        return ourInstance;
    }

    /**
     * Поле- хранит базу фактов
     * Используется коллекция Hashtable
     */
    private Hashtable<Integer, Fact> facts;


    public void setFacts(Hashtable<Integer, Fact> facts) {
        this.facts = facts;
    }

    public Hashtable<Integer, Fact> getFacts() {
        return facts;
    }

    /**
     * Метод для получения факта по ключу
     *
     * @param key уникальный ключ
     * @return возвращает объект класса Fact
     */
    public Fact getFact(int key) {
        return facts.get(key);
    }


    /**
     * Метод добавляет факт в базу фактов
     *
     * @param fact объект класса Fact
     * @return возвращает ключ добавленного факта
     */
    public int addFact(Fact fact) {
        int key = fact.hashCode();
        facts.put(key, fact);
        return key;
    }


    /**
     * @return- Метод возвращает множество всех ключей, имеющихся в коллекции facts
     */
    public Set<Integer> getKeys() {

        return facts.keySet();

    }

    /**
     * Метод удаляет все факты со значением поля value=0
     *
     * @return коллекцию нулевых фактов
     */
    public Hashtable<Integer, Fact> deleteZeroFact() {
        Hashtable<Integer, Fact> factsZero = new Hashtable<>();

        for (int key : getKeys()) {
            if (facts.get(key).getFallValue() != 0) {
                factsZero.put(key, facts.get(key));
            }
        }
        this.facts = factsZero;

        return facts;
    }


    /**
     * Метод применяет факт, исходя из правила
     *
     * @param keyFact ключ факта в базе фактов
     * @param rule    правило (объект класса Rule)
     * @return возвращает новое правило
     */
    public Fact applyFact(int keyFact, Rule rule) {

        double fall = 0;
        double value = 0;
        switch (rule.getFuncEffect()) {
            case 0: {
                if (rule.isContribution() == true) {
                    fall = facts.get(keyFact).getFallValue() * rule.getFallValue();
                    value = rule.getFallValue();
                }
                if (rule.isContribution() == false) {
                    fall = (1 - facts.get(keyFact).getFallValue()) * rule.getFallValue();
                }
                break;
            }
            case 1: {
                if (rule.isContribution() == true) {
                    if (facts.get(keyFact).getFallValue() <= rule.getPoint()) {
                        fall = facts.get(keyFact).getFallValue() * facts.get(keyFact).getFallValue() * rule.getFallValue();
                        value = rule.getFallValue();
                    } else {
                        fall = facts.get(keyFact).getFallValue() * rule.getFallValue();
                        value = rule.getFallValue();
                    }
                }
                break;
            }
        }

        Fact newFact = new Fact(rule.getSubsystem(), new BigDecimal(fall).setScale(10, BigDecimal.ROUND_UP).doubleValue());
        newFact.setRuleValue(value);
        System.out.println(newFact.getComponent() + ":" + newFact.getFallValue());
        return newFact;
    }


    /**
     * Метод проверяет наличие фактов в базе фактов, которые соотвествуют компонентам из списка
     *
     * @param comp    список компонентов
     * @param keyRule ключ правила в базе правил
     * @return возвращает флаг true/false
     */
    public boolean haveComponent(List<Component> comp, int keyRule) {
        boolean flag = false;
        for (int i = 0; i < comp.size(); i++) {
            for (int keyC : facts.keySet()) {
                /*
                   Если обозначение комопнента из списка равно такому же обозначению в базе фактов и
                   компонент не является главной подсистемой
                 */
                if (comp.get(i).getComponent().equals(facts.get(keyC).getComponent()) & comp.get(i).getComponent().equals(RulesBase.getInstance().getRule(keyRule).getComponent()) == false) {
                    flag = true;
                }
            }
        }
        return flag;
    }


    /**
     * Метод объединяет потери
     *
     * @return возвращает базу фактов с объединенными потерями
     */
    public Hashtable<Integer, Fact> poolLosses() {


        Hashtable<Integer, Fact> copyMass=  new Hashtable<>();
        copyMass.putAll(this.facts);
        List<Fact> factss= new ArrayList<>();
        //поиск фактов с одинаковыми обозначениями компонента
        for (int keyI :  getKeys()) {

            //List<Integer>keyMass =new ArrayList();

            for (int keyJ : getKeys()) {
                if (copyMass.get(keyI).getComponent().equals(copyMass.get(keyJ).getComponent()) & keyI != keyJ) {
                  // factss.add(copyMass.get(keyJ));
              //     keyMass.add(keyJ);
                 //  facts.remove(keyJ);
                    pool(keyI, keyJ);
                }

            }
//            if(factss.size()>1)
//            {
//                facts.put(keyI,pool(factss));
//                factss.clear();
//            }

        }
        return facts;
    }


    private Fact pool(List<Fact> facts) {
        Rule rule = RulesBase.getInstance().searchInSybsistems(facts.get(0));
        Double[] mass = new Double[facts.size()];
        Double[] mass1 = new Double[facts.size()];

        double value=0;

        for (int i = 0; i <= facts.size()-1; i++) {
            mass[i] = facts.get(i).getFallValue();
            mass1[i] = facts.get(i).getRuleValue();


        }

        switch (rule.getFuncRatio()) {

            case 1: {
                value= sum(mass);
                break;
            }
            case 2: {
                Arrays.sort(mass, Collections.reverseOrder());
                value= mass[0];
                break;
            }

            case 3:{
                value= sum(mass)-composition(mass);

                break;
            }
            case 4:{
                value= (sum(mass)-composition(mass))/(sum(mass1)- composition(mass1));
                break;
            }
        }
        Fact f = new Fact(facts.get(0).getComponent(), value);
        //this.facts.get(keyMass.get(1)).setFallValue(value);
        //deleteZero(keyMass);

        return f;
    }

    private void deleteZero(List<Integer> keyMass){
        for(int i=1; i<keyMass.size(); i++){
            facts.remove(i);
        }
    }

    private double sum(Double[] mass) {
        double sum = 0;

        for (int i = 0; i < mass.length; i++) {
                sum = sum + mass[i];
        }
        return sum;
    }
    private double composition(Double[] mass){
        double value = 1;

        for (int i = 0; i < mass.length; i++) {
            value = value * mass[i];
        }
        return value;
    }

    /**
     * Закрытая процедура
     * определяет тип функции объединение и производит само объединение по правлиам
     *
     * @param keyI ключ первого факта
     * @param keyJ ключ второго факта
     */
    private void pool(int keyI, int keyJ) {

        double x = facts.get(keyI).getFallValue();
        double y = facts.get(keyJ).getFallValue();
        double a = facts.get(keyI).getRuleValue();
        double b = facts.get(keyJ).getRuleValue();


        Rule rule = RulesBase.getInstance().searchInSybsistems((facts.get(keyI)));

        if (facts.get(keyI).getFallValue() != 0) {
            switch (rule.getFuncRatio()) {
                case 1: {
                    facts.get(keyI).setFallValue(x + y);
                    facts.get(keyJ).setFallValue(0);
                    break;
                }

                case 2: {
                    facts.get(keyI).setFallValue(Math.max(x, y));
                    facts.get(keyJ).setFallValue(0);
                    break;
                }
                case 3: {

                    if (facts.get(keyJ).isUnificationFlag()) {
                        x = x + a;
                        facts.get(keyI).setFallValue(x + y - a * y);
                        facts.get(keyI).setRuleValue(a * y);
                    } else {
                        facts.get(keyI).setFallValue(x + y - x * y);
                        facts.get(keyI).setRuleValue(x * y);
                    }
                    facts.get(keyI).setUnificationFlag(true);
                    facts.get(keyJ).setFallValue(0);
                    break;
                }
                case 4: {
                    facts.get(keyI).setFallValue((x + y - x * y) / (a + b - a * b));
                    facts.get(keyJ).setFallValue(0);
                }
                break;

                case 5: {

                    if (facts.get(keyI).isUnificationFlag()) {
                        x = x * a;
                    }

                    facts.get(keyI).setFallValue((1 - (1 - x) * (1 - y)) / (1 - (1 - a) * (1 - b)));
                    facts.get(keyI).setRuleValue((1 - (1 - a) * (1 - b)));
                    facts.get(keyI).setUnificationFlag(true);
                    facts.get(keyJ).setFallValue(0);
                    break;
                }
            }
        }


    }
}
