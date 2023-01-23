package repositories.rackets

import model.Racket
import org.litote.kmongo.Id
import repositories.ICRUD

interface RacketRepository: ICRUD<Racket, Id<Racket>> {
}