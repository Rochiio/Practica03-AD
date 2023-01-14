package repositories.maquinas

import model.machines.Stringer
import org.litote.kmongo.Id
import repositories.ICRUD

interface StringerRepository: ICRUD<Stringer, Id<Stringer>> {
}