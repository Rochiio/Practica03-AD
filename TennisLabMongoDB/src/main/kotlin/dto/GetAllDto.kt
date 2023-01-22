package dto

data class GetAllDto<T>(
    val page : Int = 0,
    val per_page : Int? = 0,
    val data : List<T>,
    val support : SupportDto

)
