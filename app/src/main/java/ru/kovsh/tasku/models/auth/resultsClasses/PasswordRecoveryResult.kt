package ru.kovsh.tasku.models.auth.resultsClasses

sealed class PasswordRecoveryResult<T>(val data: T? = null) {
    class OK<T> : ru.kovsh.tasku.models.auth.resultsClasses.PasswordRecoveryResult<T>()
    class Bad_Request<T> : ru.kovsh.tasku.models.auth.resultsClasses.PasswordRecoveryResult<T>()
    class Forbidden<T> : ru.kovsh.tasku.models.auth.resultsClasses.PasswordRecoveryResult<T>()
    class Not_Found<T> : ru.kovsh.tasku.models.auth.resultsClasses.PasswordRecoveryResult<T>()
    class Internal_Server_Error<T> : ru.kovsh.tasku.models.auth.resultsClasses.PasswordRecoveryResult<T>()
    class Error<T> : ru.kovsh.tasku.models.auth.resultsClasses.PasswordRecoveryResult<T>()
}