package core;

import java.util.Hashtable;
import java.util.Set;
import java.util.List;

/**
 * Класс служит для работы с базой фактов
 * Реализует паттерн Singleton
 * @author Balanovsky Danil
 */
public class FactsBase {

    private static FactsBase ourInstance = new FactsBase();
    private FactsBase (){this.facts= new Hashtable<>();}

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
     * @param key уникальный ключ
     * @return возвращает объект класса Fact
     */
    public Fact getFact(int key){return facts.get(key);}


    /**
     * Метод добавляет факт в базу фактов
     * @param fact объект класса Fact
     * @return возвращает ключ добавленного факта
     */
    public int addFact(Fact fact) {
        int key = facts.size() + 1;
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
     * @return коллекцию нулевых фактов
     */
    public Hashtable<Integer, Fact> deleteZeroFact() {
        Hashtable<Integer, Fact> factsZero = new Hashtable<>();

        for (int key : getKeys()) {
            if (facts.get(key).getValue() != 0) {
                factsZero.put(key, facts.get(key));
            }
        }
        this.facts = factsZero;

        return facts;
    }


    /**
     * Метод применяет факт, исходя из правила
     * @param keyFact ключ факта в базе фактов
     * @param rule правило (объект класса Rule)
     * @return возвращает новое правило
     */
    public Fact applyFact(int keyFact, Rule rule) {

        double fall = facts.get(keyFact).getValue() * rule.getValue();
       // facts.put(facts.size() + 1, new Fact(rule.getSubsystem(), fall));

        return new Fact(rule.getSubsystem(), fall);
    }


    /**
     * Метод проверяет наличие фактов в базе фактов, которые соотвествуют компонентам из списка
     * @param comp список компонентов
     * @param keyRule ключ правила в базе правил
     * @return возвращает флаг true/false
     */
    public boolean haveComponent(List<Component> comp, int keyRule){
        boolean flag = false;
        for (int i = 0; i < comp.size(); i++) {
            for (int keyC : facts.keySet()) {
                /*
                   Если обозначение комопнента из списка равно такому же обозначению в базе фактов и
                   компонент не является главной подсистемой
                 */
                if (comp.get(i).getComponent() == facts.get(keyC).getComponent() & comp.get(i).getComponent() != RulesBase.getInstance().getRule(keyRule).getComponent()) {
                    flag = true;
                }
            }
        }
        return flag;
    }


    /**
     * Метод объединяет потери
     * @return возвращает базу фактов с объединенными потерями
     */
    public Hashtable<Integer, Fact> poolLosses() {

        //поиск фактов с одинаковыми обозначениями компонента
        for (int keyI : getKeys()) {
            for (int keyJ : getKeys()) {
                if (facts.get(keyI).getComponent() == facts.get(keyJ).getComponent() & keyI != keyJ) {
                    pool(keyI, keyJ);
                }
            }
        }
        return facts;
    }


    /**
     * Закрытая процедура
     * определяет тип функции объединение и производит само объединение по правлиам
     * @param keyI ключ первого факта
     * @param keyJ ключ второго факта
     */
    private void pool(int keyI, int keyJ){

        switch (RulesBase.getInstance()
                .searchInSybsistems((facts.get(keyJ)))
                .getRatio()) {
            case "additive": {
                facts.get(keyI).setValue(facts.get(keyI).getValue() + facts.get(keyJ).getValue());
                facts.get(keyJ).setValue(0);
                break;
            }
            case "pessimistic": {
                facts.get(keyI).setValue(Math.max(facts.get(keyI).getValue(), facts.get(keyJ).getValue()));
                facts.get(keyJ).setValue(0);
                break;
            }
        }
    }
}
