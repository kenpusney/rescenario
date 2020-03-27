package net.kimleo.rescenario.definition

import net.kimleo.rescenario.model.Definition

interface IAsDefinition<T> {
    def tryDef(T object, Definition definition)
}