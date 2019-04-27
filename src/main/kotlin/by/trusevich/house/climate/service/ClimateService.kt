package by.trusevich.house.climate.service

import by.trusevich.house.climate.model.ClimateZone
import by.trusevich.house.climate.repository.ClimateRepository
import by.trusevich.house.core.service.AbstractCrudService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class ClimateService(private val climateRepository: ClimateRepository, private val comPortService: ComPortService) :
    AbstractCrudService<ClimateZone>(climateRepository) {

    override fun create(model: ClimateZone): ClimateZone {
        comPortService.addListener(comPortService.connectToPort(model.thermometerPortName!!))

        return super.create(model)
    }

    fun setTemperature(id: Long, temperature: Double) {
        climateRepository.findByIdOrNull(id)?.let {
            climateRepository.save(it.apply { requiredTemperature = temperature })
        } ?: throw EntityNotFoundException()
    }

}
