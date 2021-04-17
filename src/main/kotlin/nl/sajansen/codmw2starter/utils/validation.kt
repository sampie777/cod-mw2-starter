package nl.sajansen.codmw2starter.utils

import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("utils.validation")

class ValidationError(message: String? = "Value is not true") : Error(message)

fun validate(value: Boolean, failMessage: String) = validate(value) { failMessage }

fun validate(value: Boolean, failMessage: () -> String = { "Validation failed" }) {
    if (value) {
        return
    }

    throw ValidationError(failMessage())
}
