package br.com.claus.sellvia.application.dto.request

import br.com.claus.sellvia.domain.exception.InvalidFieldException

data class CategoryRequestDTO(
    val id: Long? = null,
    val name: String? = null,
    val description: String? = null,
    val companyId: Long? = null,
){

    fun validate(){
        if(name.isNullOrBlank()) throw InvalidFieldException("O nome deve ser preenchido.")
        if(description.isNullOrBlank()) throw InvalidFieldException("A descrição deve ser preenchida.")
        if(companyId == null) throw InvalidFieldException("A empresa deve ser informada.")
    }
}