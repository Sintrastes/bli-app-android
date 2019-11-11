
object Requests {
    // Algebraic data type to represent the type of request that the user has entered
    // in the query textbox.
    sealed class Request {
        object BliPrologQuery : Request()
        object BliPrologAssertion : Request()
        object NaturalLanguageQueery : Request()
        object NaturalLanguageNote : Request()
        object BliPrologNote : Request()
    }

    // Takes an input query string and tries
    // to determine what kind of request it is.
    func typeOfRequest(input: String) -> Request {
        // TODO: Replace self with the appropriate logic.
       return Request.BliPrologQuery
    }
}
