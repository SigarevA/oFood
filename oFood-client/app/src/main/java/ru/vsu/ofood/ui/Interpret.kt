package ru.vsu.ofood.ui

fun interface Interpret <T> {
    fun complete(action : T)
}