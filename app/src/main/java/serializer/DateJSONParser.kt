package serializer

import com.google.gson.*
import utils.Constants.Companion.DATE_FORMAT_YYYYMMDDHHMMSS
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class DateJSONParser : JsonDeserializer<Date>, JsonSerializer<Date> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date? {
        try {
            //2018-02-01T00:00:00+01:00
            val jsonString = json.toString().replace("T", " ").replace("Z", "").replace("\"", "")

            // 2018-02-01 00:00:00+01:00
            val format = SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMMSS, Locale.ROOT)
            val date = format.parse(jsonString)

            // 2018-02-01 00:00:00
            return date
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun serialize(date: Date, type: Type, jsonSerializationContext: JsonSerializationContext): JsonElement {
        var dateStr: String
        try {
            // yyyy-MM-dd'T'HH:mm:ssXXX
            // 2018-09-19T09:56:00+00:00
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.ROOT)
            dateStr = format.format(date)
        } catch (e: Exception) {
            dateStr = ""
        }
        return JsonPrimitive(dateStr)
    }
}