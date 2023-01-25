
package util.mappers

import dto.customers.CustomerDTO
import model.users.Customer

fun CustomerDTO.fromDto(): Customer {
    return Customer(
        name = this.name.split(Regex.fromLiteral("[ -]"))[0],
        username = this.name,
        email = this.email,
        orderList = emptyList(),
        tennisRacketsList = emptyList(),
        available = true
    )
}