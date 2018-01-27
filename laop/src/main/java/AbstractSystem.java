import core.*;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Класс служит для создания проивзольной системы
 * Реализует паттерн Singleton
 * @author Balanovsky Danil
 */
public class AbstractSystem {

    private static AbstractSystem ourInstance = new AbstractSystem();
    private AbstractSystem() {
        this.fb = FactsBase.getInstance();
        this.rb = RulesBase.getInstance();
    }
    @Contract(pure = true)
    public static AbstractSystem getInstance() {
        return ourInstance;
    }

    /**
     * Поле- обеспечивает доступ к базе фактов
     */
    private FactsBase fb;

    /**
     * Поле- обеспечивает доступ к базе правил
     */
    private RulesBase rb;


    /**
     * Метод создает и добавляет факт в базу фактов
     * @param component обозначение компонента
     * @param value падение эффективности
     * @return ключ созданного факта
     */
    public int addFact(String component, double value) {
        return fb.addFact(new Fact(component, value));
    }


    /**
     * Метод созлает и добавляет правило в базу правил
     * @param component обозначение комопнентп
     * @param subsystem обозначение подсистемы
     * @param value значение убыли эффективности компонента системы
     * @param ratio тип объединения при каждом компоненте подсистемы
     * @return возвращает ключ созданного правила
     */
    public int addRule(String component, String subsystem, double value, String ratio) {
        return rb.addRule(new Rule(component, subsystem, value, ratio));
    }

    /**
     * Процедура для очистки базы фактов и правил
     */
    public void deleteAll(){
        fb.setFacts(new Hashtable<>());
        rb.setRules(new Hashtable<>());
    }


    /**
     * Метод расчитывает эффективность работы системы с учетом всех потерь
     * @return возвращает эффективность работы системы
     */
    public double getEfficiency() {

        RulesBase rb = RulesBase.getInstance();
        FactsBase fb = FactsBase.getInstance();

        //объединение потерь
        fb.poolLosses();
        //удаление нулевых фактов
        fb.deleteZeroFact();

        //коллекция новых фактов
        Hashtable<Integer, Fact> newFacts = new Hashtable<>();

        for (int keyF : fb.getKeys()) {
            Fact mainSystem= null;
            for (int keyR : rb.getKeys()) {
                /*
                     Если обозначение компонента в факте keyF равно такому же обозначению компонента в правиле keyR
                 */
                if (fb.getFact(keyF).getComponent() == rb.getRule(keyR).getComponent()) {
                    //коллекция компонентов
                    List<Component> lComponent = new ArrayList<>();
                    //заполнение коллекции компонентами, которые входят в подсистему fb.getFact(keyF).getComponent()
                    lComponent = rb.listComponent(new Component(fb.getFact(keyF).getComponent()), lComponent);

                    /*
                        Если в коллекции компонентов есть компоненты, которые заданы в базе фактов и
                        не являются подсистемами
                     */
                    if (fb.haveComponent(lComponent, keyR) == false) {
                        newFacts.put(newFacts.size() + 1, fb.applyFact(keyF, rb.getRule(keyR)));
                    } else {
                        newFacts.put(newFacts.size() + 1, fb.getFact(keyF));
                    }
                } else {
                    /*
                        Если компонент, заданный в факте keyF является главной системой
                     */
                    if (fb.getFact(keyF).getComponent() == rb.getMainSystem().getComponent()) {
                        mainSystem= fb.getFact(keyF);
                        //newFacts.put(newFacts.size() + 1, fb.getFact(keyF));
                    }
                }

            }

            if(mainSystem!= null){
                newFacts.put(newFacts.size()+1, mainSystem);
            }

        }

        if (newFacts.size() != 1) {
            fb.setFacts(newFacts);
            return getEfficiency();

        } else {
            return 1-newFacts.get(1).getValue();

        }
    }


}
