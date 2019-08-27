package com.pechenkin.travelmoney.diagram;

import androidx.annotation.NonNull;

import com.pechenkin.travelmoney.utils.Help;
import com.pechenkin.travelmoney.bd.local.table.NamespaceSettings;
import com.pechenkin.travelmoney.bd.local.table.TableSettings;
import com.pechenkin.travelmoney.transaction.processing.summary.Total;
import com.pechenkin.travelmoney.diagram.impl.BarDiagram;
import com.pechenkin.travelmoney.diagram.impl.DebitCreditDiagram;
import com.pechenkin.travelmoney.diagram.impl.LineDiagram;
import com.pechenkin.travelmoney.diagram.impl.TotalItemDiagram;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DefaultDiagram {


    @NonNull
    public static Diagram createDefaultDiagram(int sum, List<Total.MemberSum> total) {

        String defaultDiagramName = Objects.requireNonNull(TotalItemDiagram.class.getAnnotation(DiagramName.class)).name();
        defaultDiagramName = TableSettings.INSTANCE.get(NamespaceSettings.LIKE_DIAGRAM_NAME, defaultDiagramName);

        Class<? extends Diagram> dClass = getDiagramClassByName(defaultDiagramName);
        if (dClass != null) {
            try {
                Constructor<? extends Diagram> constructor = dClass.getConstructor(int.class, List.class);
                return constructor.newInstance(sum, total);
            } catch (NoSuchMethodException e) {
                Help.alertError("Не найден конструктор для создания диаграммы " + defaultDiagramName);
            } catch (Exception ex) {
                Help.alertError("Не удалось создать диаграмму " + defaultDiagramName);
            }
        }

        return new TotalItemDiagram(sum, total);

    }

    private static Map<String, Class<? extends Diagram>> classCache = new HashMap<>();

    private static Class<? extends Diagram> getDiagramClassByName(String diagramName) {

        if (classCache.containsKey(diagramName)) {
            return classCache.get(diagramName);
        }

        for (Class<? extends Diagram> dClass : getDiagramClasses()) {
            String dName = Objects.requireNonNull(dClass.getAnnotation(DiagramName.class)).name();
            if (dName.equals(diagramName)) {
                classCache.put(diagramName, dClass);
                return dClass;
            }
        }
        return null;
    }

    /**
     * Тут должны быть перечислены классы всех существующих диаграмм
     */
    @SuppressWarnings("unchecked")
    private static Class<? extends Diagram>[] getDiagramClasses() {
        return (Class<? extends Diagram>[]) new Class<?>[]{
                TotalItemDiagram.class,
                DebitCreditDiagram.class,
                LineDiagram.class,
                BarDiagram.class
        };
    }

}
