package by.trusevich.house.climate.model

import by.trusevich.house.climate.validation.PortValid
import by.trusevich.house.climate.validation.NameUnique
import by.trusevich.house.core.model.BaseEntity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY
import io.swagger.annotations.ApiModelProperty
import org.hibernate.envers.Audited
import org.hibernate.validator.constraints.Length
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Audited
@JsonInclude(NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(uniqueConstraints = [UniqueConstraint(name = "unique_name", columnNames = ["name"])])
data class ClimateZone(

    @get:NotBlank
    @get:NameUnique
    @get:Length(max = 255)
    @Column(nullable = false, updatable = false)
    var name: String? = null,

    @get:PortValid
    @get:NotBlank
    @get:Length(max = 255)
    @Column(nullable = false, updatable = false)
    var thermometerPortName: String? = null,

    @get:PortValid
    @get:NotBlank
    @get:Length(max = 255)
    @Column(nullable = false, updatable = false)
    var collectorPortName: String? = null,

    @get:NotNull
    @Column(nullable = false, updatable = false)
    var collectorPinNumber: Int? = null,

    @JsonProperty(access = READ_ONLY)
    @ApiModelProperty(readOnly = true)
    @get:NotNull
    @Column(nullable = false)
    var isHeaterOn: Boolean? = null,

    @get:NotNull
    @Column(nullable = false)
    var currentTemperature: Double? = null,

    @get:NotNull
    @Column(nullable = false)
    var requiredTemperature: Double? = null

) : BaseEntity() {

    companion object {

        private const val serialVersionUID = 79835162396908742L
    }
}
