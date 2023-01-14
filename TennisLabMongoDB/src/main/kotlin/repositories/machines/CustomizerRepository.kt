package repositories.maquinas

import model.machines.Customizer
import org.litote.kmongo.Id
import repositories.ICRUD

interface CustomizerRepository : ICRUD<Customizer, Id<Customizer>> {
}