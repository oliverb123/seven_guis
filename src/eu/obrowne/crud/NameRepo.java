package eu.obrowne.crud;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class NameRepo {
    private int nextId;
    private Map<Integer, Entry> entries = new HashMap<Integer, Entry>();
    private DefaultListModel<Entry> model;
    private JList<Entry> ui;
    private Predicate<String> filter = (s) -> true;

    public NameRepo() {
        model = new DefaultListModel<>();
        ui = new JList<>(model);
        nextId = 0;
    }

    public synchronized void add(String s) {
        var e = new Entry();
        e.value = s;
        e.id = nextId++;
        entries.put(e.id, e);
        if(filter.test(s)){
            model.addElement(e);
        }
    }

    public JList<Entry> getUi() {
        return ui;
    }

    public synchronized void delete(int modelIndex) {
        var e = model.get(modelIndex);
        model.remove(modelIndex);
        entries.remove(e.id);
    }

    public synchronized void update(int modelIndex, String value) {
        var e = model.get(modelIndex);
        e.value = value;
        entries.put(e.id, e);
        model.set(modelIndex, e);
        applyFilter();
    }

    public synchronized void setFilter(Predicate<String> p) {
        filter = p;
        applyFilter();
    }

    private void applyFilter() {
        model.clear();
        entries.values().stream().filter(e -> filter.test(e.value)).forEach(model::addElement);
    }

    private static class Entry {
        public Integer id;
        public String value;

        public String toString() {
            return value;
        }
    }
}
