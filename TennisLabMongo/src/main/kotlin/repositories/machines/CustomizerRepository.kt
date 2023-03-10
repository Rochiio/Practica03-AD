package repositories.machines

import model.machines.Customizer
import org.litote.kmongo.Id
import repositories.ICRUD

interface CustomizerRepository : ICRUD<Customizer, String> {
}