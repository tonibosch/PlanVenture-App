package com.example.planventure.database

import com.example.planventure.entity.Participant

interface PVRepository<T> {
    fun findAll(): List<T>
    fun getById(id: Long): T?
    fun deleteAll(): Boolean
    fun deleteById(id: Int): Boolean

}