package com.setyongr.simpleplayer.core

/**
 * Keyed Hash
 *
 * Provide key based hash code for object without hash code like onClickListener
 */

class KeyedHash<X>(private var value: X, private var key: String = "") {
    override fun hashCode() = if (value == null) -1 else key.hashCode()

    fun setValue(key: String = this.key, value: X) {
        this.key = key
        this.value = value
    }

    fun setValue(value: X) {
        this.value = value
    }

    fun getValue() = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KeyedHash<*>

        if (key != other.key) return false

        return true
    }

    operator fun invoke() = getValue()
}

fun <X> keyedHash(key: String = "", value: X) = KeyedHash(value, key)
fun <X> keyedHash(value: X) = KeyedHash(value, "")

typealias UnitListener = KeyedHash<(() -> Unit)?>

fun unitListener(key: String = "", value: (() -> Unit)?) = UnitListener(value, key)
fun emptyUnitListener() = UnitListener(null, "")