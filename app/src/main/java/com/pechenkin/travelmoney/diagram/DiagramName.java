package com.pechenkin.travelmoney.diagram;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Данная аннотауция нужна для того, что бы можно было найти диаграмму по имени. Имя диаграммы может дублированть имя класса.
 * Не используется имя класса из-за того, что после работы минификатора от настоящего имени ничего не остается
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DiagramName {
    String name();
}
