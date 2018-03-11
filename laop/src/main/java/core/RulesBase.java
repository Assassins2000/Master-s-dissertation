package core;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

/**
 * Класс служит для работы с базой правил
 * Реализует паттерн Singleton
 * @author Balanovsky Danil
 */
public class RulesBase {

    private static RulesBase ourInstance = new RulesBase();
    private RulesBase (){ this.rules= new Hashtable<>();}

    @Contract(pure = true)
    public static RulesBase getInstance() {
        return ourInstance;
    }


    /**
     * Поле- хранит базу фактов
     * Используется коллекция Hashtable
     */
    private Hashtable<Integer, Rule> rules;



    public void setRules(Hashtable<Integer, Rule> rules) {
        this.rules = rules;
    }


    /**
     * Метод для получения правила по ключу
     * @param key уникальный ключ
     * @return возвращает объект класса Rule
     */
    public Rule getRule(int key){
        return rules.get(key);
    }

    /**
     * @return- Метод возвращает множество всех ключей, имеющихся в коллекции rules
     */
    public Set<Integer> getKeys(){
        return rules.keySet();
    }


    /**
     * Метод добавляет факт в базу фактов
     * @param rule объект класса Rule
     * @return возвращает ключ добавленного правила
     */
    public int addRule(Rule rule) {
        int key = rules.size() + 1;
        rules.put(key, rule);
        return key;
    }

    /**
     * Метод находит главную систему в иерархии
     * @return возвращает объект класса Component (обозначение системы)
     */
    public Component getMainSystem(){
        boolean flag;
        for (int keyI: getKeys()){
            flag=false;
            for (int keyJ: getKeys())
            {
                /*
                    Если обозначение подсистемы в правеле keyI равно обозначению компонента в правеле keyJ
                 */
                if (rules.get(keyI).getSubsystem().equals(rules.get(keyJ).getComponent())){
                    flag=true;
                }
            }
            if(flag==false)
            {
                return new Component(rules.get(keyI).getSubsystem());
            }
        }
        return null;
    }


    /**
     * Метод находит правило, соответствующее факту
     * @param fact объект класса Fact
     * @return возвращает найденное правило
     */
    public Rule searchInSybsistems(Fact fact) {

        for (int key : getKeys()) {
            if (rules.get(key).getSubsystem().equals(fact.getComponent())) {
                return rules.get(key);
            }
        }
        return null;
    }


    /**
     * Метод заполняет коллекцию компонентами, которые входят в заданную подсистему
     * @param subsys обозначение заданной подсистемы
     * @param listComp коллекция, которую нужно заполнить
     * @return возвращает заполненную коллекцию
     */
    public List<Component> listComponent(Component subsys, List<Component> listComp) {

        //исследуем полученный массив компонентов на наличие подсистем
         List<Rule> ruless = new ArrayList<>();
        for (int keyR : getKeys()) {
            /*
                Если обозначение подсистемы в правеле равно обозначению заданной подсистемы
             */
            if (rules.get(keyR).getSubsystem().equals(subsys.getComponent())) {
                ruless.add(rules.get(keyR));
                listComp.add(new Component(rules.get(keyR).getComponent()));
            }
        }

        for (int i = 0; i < ruless.size(); i++) {
            /*
                Если найдено правило, соответствующее факту
             */
            if (searchInSybsistems(new Fact(ruless.get(i).getComponent(), 0)) != null) {
                return listComponent(new Component(ruless.get(i).getComponent()), listComp);
            }
        }

        return listComp;
    }


    public Hashtable<Integer, Rule> getRules() {
        return rules;
    }
}
