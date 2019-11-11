package com.example.bedelllibraryinterface

object Requests {
    // Algebraic data type to represent the type of request that the user has entered
    // in the query textbox.
    sealed class Request {
        object BliPrologQuery : Request()
        object BliPrologAssertion : Request()
        object NaturalLanguageQuery : Request()
        object NaturalLanguageNote : Request()
        object BliPrologNote : Request()

        fun show(): String {
            return when(this) {
                BliPrologQuery -> "BliPrologQuery"
                BliPrologNote -> "BliPrologNote"
                BliPrologAssertion -> "BliPrologAssertion"
                NaturalLanguageQuery -> "NaturalLanguageQuery"
                NaturalLanguageNote -> "NaturalLanguageNote"
            }
        }

    }

    // Takes an input query string and tries
    // to determine what kind of request it is.
    fun typeOfRequest(input: String): Request {

        if (input.startsWith(":note")) {
            // This is just a heuristic, and probably needs to be improved.
            if (input.filter( {c -> c == ' '}).length >= 1
                && !input.contains("(") && !input.contains(")")) {
                return Requests.Request.NaturalLanguageNote
            } else {
                return Requests.Request.BliPrologNote
            }
        }
        if (input.endsWith(".")) {
            return Requests.Request.BliPrologQuery
        }
        if (input.endsWith("!")) {
            return Requests.Request.BliPrologAssertion
        }

       return Request.BliPrologQuery
    }
}