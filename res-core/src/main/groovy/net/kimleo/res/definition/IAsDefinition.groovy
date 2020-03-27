package net.kimleo.res.definition

import net.kimleo.res.model.Definition

interface IAsDefinition<T> {
    def tryDef(T object, Definition definition)
}