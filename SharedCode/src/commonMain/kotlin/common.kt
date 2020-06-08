package app.by.wildan.mobile


expect fun platformName():String

fun createApplicationScreenMessage() : String {
    return "Kotlin code base on ${platformName()}"
}

