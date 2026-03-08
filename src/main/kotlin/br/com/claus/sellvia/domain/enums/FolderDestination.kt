package br.com.claus.sellvia.domain.enums

enum class FolderDestination(
    val key: String,
    val prefix: String = key,
) {
    PRODUCT("products", "product"),
    USER("users", "user"),
    COMPANY("companies", "company"),
    ;

    fun buildPath(
        companyId: Long,
        resourceId: Long,
    ): String {
        return "assets/comp_$companyId/${this.key}/${this.prefix}_$resourceId"
    }
}