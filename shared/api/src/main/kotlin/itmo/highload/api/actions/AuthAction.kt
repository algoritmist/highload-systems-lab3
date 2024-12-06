package itmo.highload.api.actions

class AuthAction (
    val login: String,
    val type: AuthActionType,
    val comment: String
){
}

enum class AuthActionType {
    BAN,
    UNBAN
}
