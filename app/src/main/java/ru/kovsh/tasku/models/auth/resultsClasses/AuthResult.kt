package ru.kovsh.tasku.models.auth.resultsClasses

sealed class AuthResult<T> {
    class Authorized<T> : ru.kovsh.tasku.models.auth.resultsClasses.AuthResult<T>()
    class Unauthorized<T>: ru.kovsh.tasku.models.auth.resultsClasses.AuthResult<T>()
    class BadRequest<T> : ru.kovsh.tasku.models.auth.resultsClasses.AuthResult<T>() //invalid password, less than 6 symbols for example
    class Forbidden<T>   : ru.kovsh.tasku.models.auth.resultsClasses.AuthResult<T>() //incorrect password or email
    class InternalServerError<T> : ru.kovsh.tasku.models.auth.resultsClasses.AuthResult<T>()
    class Error<T> : ru.kovsh.tasku.models.auth.resultsClasses.AuthResult<T>()
}
