package net.kimleo.rescenario.model

interface IAsDefinition<T> {
    def tryDef(T object, Definition definition)
}