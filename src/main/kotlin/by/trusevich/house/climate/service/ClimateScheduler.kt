package by.trusevich.house.climate.service

import by.trusevich.house.climate.repository.ClimateRepository
import by.trusevich.house.core.util.lazyLogger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
@Suppress("unused")
class ClimateScheduler(private val climateRepository: ClimateRepository, private val comPortService: ComPortService) {

    private val log by lazyLogger()

    @Scheduled(cron = "\${house.climate.cron}")
    fun runUpdateProducts() {
        log.info("Temperature synchronisation started.")

        climateRepository.findAll().forEach { climateZone ->
            val shouldOpen = climateZone.currentTemperature!! < climateZone.requiredTemperature!!

            comPortService.sendMessage(
                climateZone.collectorPortName!!,
                "${climateZone.collectorPinNumber}${if (shouldOpen) 1 else 0}"
            )

            climateRepository.save(climateZone.apply { isHeaterOn = shouldOpen })

            log.info("Temperature in zone ${climateZone.name} ${if (shouldOpen) "is low, switching heater on" else "is high, switching heater off"}")
        }
    }
}
