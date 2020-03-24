package net.kimleo.rescenario.execution.scenario

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@Target([ElementType.TYPE])
@Retention(RetentionPolicy.RUNTIME)
@interface ScenarioType {
    String value();
}