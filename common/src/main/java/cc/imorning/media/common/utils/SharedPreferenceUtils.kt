package cc.imorning.media.common.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtils private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun save(key: String, value: Any) {
        when (value::class) {
            Int::class -> {
                sharedPreferences.edit().putInt(key, value.toString().toInt().or(0)).apply()
            }

            Long::class -> {
                sharedPreferences.edit().putLong(key, value.toString().toLong().or(0L)).apply()
            }

            Float::class -> {
                sharedPreferences.edit().putFloat(key, value.toString().toFloat()).apply()

            }

            String::class -> {
                sharedPreferences.edit().putString(key, value.toString()).apply()
            }

            Boolean::class -> {
                sharedPreferences.edit().putBoolean(key, value.toString().toBoolean().or(false))
                    .apply()
            }

            else -> {
                LogUtils.e(TAG, "error when save $key")
            }
        }
    }

    fun get(key: String, defaultValue: Any): Any {
        when (defaultValue::class) {
            Int::class -> {
                return sharedPreferences.getInt(key, defaultValue.toString().toInt())
            }

            Long::class -> {
                return sharedPreferences.getLong(key, defaultValue.toString().toLong())
            }

            Float::class -> {
                return sharedPreferences.getFloat(key, defaultValue.toString().toFloat())
            }

            String::class -> {
                return sharedPreferences.getString(key, defaultValue.toString()) ?: defaultValue
            }

            Boolean::class -> {
                return sharedPreferences.getBoolean(key, defaultValue.toString().toBoolean())
            }

            else -> {
                return defaultValue
            }
        }
    }


    companion object {
        private const val TAG = "SharedPreferenceUtils"

        private const val PREF_NAME = "app_settings"

        private var instance: SharedPreferenceUtils? = null

        fun getInstance(context: Context): SharedPreferenceUtils {
            if (instance == null) {
                synchronized(SharedPreferenceUtils::class.java) {
                    if (instance == null) {
                        instance = SharedPreferenceUtils(context.applicationContext)
                    }
                }
            }
            return instance!!
        }
    }
}
