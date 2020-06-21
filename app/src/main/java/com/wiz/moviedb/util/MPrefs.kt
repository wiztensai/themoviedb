package wazma.punjabi.helper

import android.content.Context
import android.content.SharedPreferences
import com.wiz.moviedb.R

class MPrefs(val context: Context) {

    private val mode = Context.MODE_PRIVATE
    private var sharedPreferences:SharedPreferences

    init {
        val name = context.resources.getString(R.string.app_name) +"prefs"
        sharedPreferences = context.getSharedPreferences(name, mode)
    }

    fun getPrefs(): SharedPreferences {
        return sharedPreferences
    }

    /**
     * For update and create
     */
    fun setData(keyName:String, value:String): MPrefs {
        sharedPreferences.edit().putString(keyName, value).apply()
        return this
    }

    /**
     * For update and create
     */
    fun setData(keyName:String, value:Int): MPrefs {
        sharedPreferences.edit().putInt(keyName, value).apply()
        return this
    }

    /**
     * For update and create
     */
    fun setData(keyName:String, value:Boolean): MPrefs {
        sharedPreferences.edit().putBoolean(keyName, value).apply()
        return this
    }

    /**
     * For update and create
     */
    fun setData(ctx:Context, keyName:String, value:Int): MPrefs {
        sharedPreferences.edit().putInt(keyName, value).apply()
        return this
    }

    /**
     * For update and create
     */
    fun setData(keyName:String, value:Long): MPrefs {
        sharedPreferences.edit().putLong(keyName, value).apply()
        return this
    }

    fun deleteData(keyName:String): MPrefs {
        sharedPreferences.edit().remove(keyName).apply()
        return this
    }

    fun destroy(){
        sharedPreferences.edit().clear().apply()
    }

    fun isNull(keyName:String):Boolean{
        if (sharedPreferences.getString(keyName, null) == null ){
            return true
        } else {
            return false
        }
    }

    fun isNotNull(keyName:String):Boolean{
        if (sharedPreferences.getString(keyName, null) == null ){
            return false
        } else {
            return true
        }
    }
}