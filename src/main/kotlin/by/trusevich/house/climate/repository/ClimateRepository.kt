package by.trusevich.house.climate.repository

import by.trusevich.house.climate.model.ClimateZone
import by.trusevich.house.core.repository.BaseRepository

interface ClimateRepository : BaseRepository<ClimateZone> {

    fun existsByName(name: String): Boolean

    fun findByThermometerPortNameAndCollectorPinNumber(thermometerPortName: String, collectorPinNumber: Int): ClimateZone
}
