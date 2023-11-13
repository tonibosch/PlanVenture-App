package com.example.planventure.enumerations

/**
 * TRIP_STATE.kt
 * Enumeration that contains the different states a trip can be in
 * @property PLANNING
 * @property STARTED
 * @property FINISHED
 *
 * @constructor
 */
enum class TRIP_STATE(name: String) {
    PLANNING("PLANNING"),
    STARTED("STARTED"),
    FINISHED("FINISHED")
}