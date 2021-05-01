import com.google.gson.Gson
import com.google.gson.GsonBuilder

internal fun jsonBuilder(prettyPrint: Boolean = true): Gson {
    val builder = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        .serializeNulls()

    if (prettyPrint) {
        builder.setPrettyPrinting()
    }

    return builder.create()
}