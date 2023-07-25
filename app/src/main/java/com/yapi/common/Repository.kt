package com.yapi.common

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.yapi.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule

class Repository @Inject constructor(val retrofit:RetrofitAPI,@ApplicationContext val context: Context) {

    //var aD: AlertDialog.Builder? = null
    fun <T : Any> makeCall(
        loader: Boolean,dismissDialog:Boolean?=true,
        requestProcessor: ApiProcessor<Response<T>>
    ) {
   //     val activity = MainActivity.context.get()!!

       /* if (!con.isNetworkAvailable()) {
           // CommonMethods.showToast(CommonMethods.context, "You are offline.")
            context.
            // activity.showNegativeAlerter(activity.getString(R.string.your_device_offline))
            return
        }*/
        if (loader) {
           // context.applicationContext.showProgress()
           showProgress()
        }
        val activity =   MainActivity.activity!!.get()!!
        //activity.showMessage("API HIT")
        val dataResponse: Flow<Response<Any>?> = flow {
            val response = requestProcessor.sendRequest(retrofit) as Response<Any>
            emit(response)
        }.flowOn(Dispatchers.IO)

        CoroutineScope(Dispatchers.Main).launch {
            dataResponse.catch { exception ->
                exception.printStackTrace()
                Log.d("exception", "errorException$exception")
                // activity.showProgress()
              //  if(dismissDialog!!) {
                    hideProgress()
              //  }
                showToastMessage(exception.message ?: "")
                //showErrorDialog()
            }.collect {
                    response ->
                Log.d("resCodeIs", "====${response?.code()}")
              //  Timer().schedule(1000) {
                if(response?.isSuccessful()== false || dismissDialog!!) {
                    hideProgress()
                }
                //}
                when {
                    response?.isSuccessful == true -> {
                        /**Success*/
                        Log.d("successBody", "====${response.body()}")

                        requestProcessor.onSuccess(response as Response<T>)
                    }

                    response?.code() in 100..199 -> {
                        /**Informational*/
                        requestProcessor.onError(
                          "Error" ?: ""
                        )
                       // CommonMethods.showToast(CommonMethods.context, "Oops! Something went wrong")

                        //    activity.showNegativeAlerter(activity.resources?.getString(R.string.some_error_occured) ?: "")

                    }



                    response?.code() in 300..399 -> {
                        /**Redirection*/
                      //  requestProcessor.onError(activity.resources?.getString(R.string.some_error_occured) ?: "")

                        // activity.showNegativeAlerter(activity.resources?.getString(R.string.some_error_occured) ?: "")

                    }
                    response?.code() == 401 -> {
                        /**UnAuthorized*/
                        Log.d("errorBody", "====${response.errorBody()?.string()}")
                        //CommonMethods.showToast(CommonMethods.context, "Oops! Something went wrong")
                       // requestProcessor.onError(activity.resources?.getString(R.string.some_error_occured) ?: "")
                      //  getRefreshToken()

                        //activity.sessionExpired()
                        requestProcessor.onError("unAuthorized")
                    }
                    response?.code() == 404 -> {
                        /**Page Not Found*/
                        requestProcessor.onError(
                           "Error" ?: ""
                        )
                 //       CommonMethods.showToast(CommonMethods.context, "Oops! Something went wrong")

                        // activity.showNegativeAlerter(activity.resources?.getString(R.string.some_error_occured) ?: "")
                    }
                    response?.code() in 500..599 -> {
                        /**ServerErrors*/
                        requestProcessor.onError(
                           "Server Error" ?: ""
                        )
//                        CommonMethods.showToast(CommonMethods.context, "Oops! Something went wrong")

                        // activity.showNegativeAlerter(activity.resources?.getString(R.string.some_error_occured) ?: "")
                    }
                    else -> {
                        /**ClientErrors*/
                        try {
                            val res = response?.errorBody()!!.string()
                            val jsonObject = JSONObject(res)
                            when {
                                jsonObject.has("message") -> {
                                    requestProcessor.onError(jsonObject.getString("message"))
                                    Log.e("Error_Messageee===",jsonObject.toString())
                                    if (!jsonObject.getString("message").equals("Data not found", true))
""
                                    //activity.showNegativeAlerter(jsonObject.getString("message"))
                                }
                                else -> {
                                    requestProcessor.onError(
                                      "Error"?: ""
                                    )
                                    // activity.showNegativeAlerter(activity.resources?.getString(R.string.some_error_occured) ?: "")
                                }
                            }
                        } catch (e:Exception){
                            print(e.message)
                        }
                        /* val res = response?.errorBody()!!.string()
                         val jsonObject = JSONObject(res)
                         when {
                             jsonObject.has("message") -> {
                                 requestProcessor.onError(jsonObject.getString("message"))
                                 if (!jsonObject.getString("message").equals("Data not found", true))
                                     Toast.makeText(
                                         MainActivity.context.get(),
                                         jsonObject.getString("message"),
                                         Toast.LENGTH_SHORT
                                     ).show()
                                 //activity.showNegativeAlerter(jsonObject.getString("message"))
                             }
                             else -> {
                                 requestProcessor.onError(
                                     activity.resources?.getString(R.string.some_error_occured) ?: ""
                                 )
                                 // activity.showNegativeAlerter(activity.resources?.getString(R.string.some_error_occured) ?: "")
                             }
                         }*/
                    }
                }
            }
        }
    }

}