package swapc.lib.search.util;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ReflectionHelper {

    public static boolean extendsClass(Class subject, Class desired) {
        List<Class> checked = new LinkedList<>();
        return extendsClassAndSubclasses(subject, desired, checked);
    }

    private static boolean extendsClassAndSubclasses(Class subject, Class desired, List<Class> checked) {
        if (subject.equals(desired)) { return true; }
        if (!checked.contains(subject)) {
            checked.add(subject);

            for (Class implemented : subject.getInterfaces()) {
                if (extendsClassAndSubclasses(implemented, desired, checked)) { return true; }
            }

            Class superClass = subject.getSuperclass();
            return superClass != null && extendsClassAndSubclasses(superClass, desired, checked);
        }
        return false;
    }


}
