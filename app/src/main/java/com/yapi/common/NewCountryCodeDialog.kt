/*
package com.yapi.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import com.rilixtech.widget.countrycodepicker.Country
import com.rilixtech.widget.countrycodepicker.CountryCodeArrayAdapter
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import com.yapi.R
import java.util.*


*/
/**
 * Dialog for selecting Country.
 *
 * Created by Joielechong on 11 May 2017.
 *//*

internal class CountryCodeDialog(private val mCountryCodePicker: CountryCodePicker) :
    Dialog(mCountryCodePicker.context) {
    private var mEdtSearch: EditText? = null
    private var mTvNoResult: TextView? = null
    private var mTvTitle: TextView? = null
    private var mLvCountryDialog: ListView? = null
    private var mRlyDialog: RelativeLayout? = null
    private var masterCountries: List<Country>? = null
    private var mFilteredCountries: List<Country?>? = null
    private var mInputMethodManager: InputMethodManager? = null
    private var mArrayAdapter: CountryCodeArrayAdapter? = null
    private var mTempCountries: MutableList<Country?>? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.country_code_picker_layout_picker_dialog)
        setupUI()
        setupData()
    }

    private fun setupUI() {
        mRlyDialog = findViewById(R.id.dialog_rly)
        mLvCountryDialog = findViewById(R.id.country_dialog_lv)
        mTvTitle = findViewById(R.id.title_tv)
        mEdtSearch = findViewById(R.id.search_edt)
        mTvNoResult = findViewById(R.id.no_result_tv)
    }

    private fun setupData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mLvCountryDialog!!.layoutDirection = mCountryCodePicker.layoutDirection
        }
        if (mCountryCodePicker.typeFace != null) {
            val typeface = mCountryCodePicker.typeFace
            mTvTitle!!.setTypeface(typeface)
            mEdtSearch!!.setTypeface(typeface)
            mTvNoResult!!.setTypeface(typeface)
        }
        if (mCountryCodePicker.backgroundColor != mCountryCodePicker.defaultBackgroundColor) {
            mRlyDialog!!.setBackgroundColor(mCountryCodePicker.backgroundColor)
        }
        if (mCountryCodePicker.dialogTextColor != mCountryCodePicker.defaultContentColor) {
            val color = mCountryCodePicker.dialogTextColor
            mTvTitle!!.setTextColor(color)
            mTvNoResult!!.setTextColor(color)
            mEdtSearch!!.setTextColor(color)
            mEdtSearch!!.setHintTextColor(adjustAlpha(color, 0.7f))
        }
        mCountryCodePicker.refreshCustomMasterList()
        mCountryCodePicker.refreshPreferredCountries()
        masterCountries = mCountryCodePicker.getCustomCountries(mCountryCodePicker)
        mFilteredCountries = filteredCountries
        setupListView(mLvCountryDialog)
        val ctx = mCountryCodePicker.context
        mInputMethodManager =
            ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        setSearchBar()
    }

    private fun setupListView(listView: ListView?) {
        mArrayAdapter = CountryCodeArrayAdapter(context, mFilteredCountries, mCountryCodePicker)
        if (!mCountryCodePicker.isSelectionDialogShowSearch) {
            val params = listView!!.layoutParams as RelativeLayout.LayoutParams
            params.height = ListView.LayoutParams.WRAP_CONTENT
            listView.layoutParams = params
        }
        val listener =
            OnItemClickListener { parent, view, position, id ->
                if (mFilteredCountries == null) {
                    Log.e(TAG,
                        "no filtered countries found! This should not be happened, Please report!")
                    return@OnItemClickListener
                }
                if (mFilteredCountries!!.size < position || position < 0) {
                    Log.e(TAG, "Something wrong with the ListView. Please report this!")
                    return@OnItemClickListener
                }
                val country = mFilteredCountries!![position] ?: return@OnItemClickListener
                */
/* view is only a separator, so the country is null and we ignore it.
                   see {@link #getFilteredCountries(String)} *//*

                mCountryCodePicker.setSelectedCountry(country)
                mInputMethodManager!!.hideSoftInputFromWindow(mEdtSearch!!.windowToken, 0)
                dismiss()
            }
        listView!!.onItemClickListener = listener
        listView.adapter = mArrayAdapter
    }

    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = Math.round(Color.alpha(color) * factor)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    private fun setSearchBar() {
        if (mCountryCodePicker.isSelectionDialogShowSearch) {
            setTextWatcher()
        } else {
            mEdtSearch!!.visibility = View.GONE
        }
    }

    */
/**
     * add textChangeListener, to apply new query each time editText get text changed.
     *//*

    private fun setTextWatcher() {
        if (mEdtSearch == null) return
        mEdtSearch!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                applyQuery(s.toString())
            }
        })
        if (mCountryCodePicker.isKeyboardAutoPopOnSearch()) {
            if (mInputMethodManager != null) {
                mInputMethodManager!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
        }
    }

    */
/**
     * Filter country list for given keyWord / query.
     * Lists all countries that contains @param query in country's name, name code or phone code.
     *
     * @param query : text to match against country name, name code or phone code
     *//*

    private fun applyQuery(query: String) {
        var query = query
        mTvNoResult!!.visibility = View.GONE
        query = query.lowercase(Locale.getDefault())

        //if query started from "+" ignore it
        if (query.length > 0 && query[0] == '+') {
            query = query.substring(1)
        }
        mFilteredCountries = getFilteredCountries(query)
        if (mFilteredCountries!!.size == 0) {
            mTvNoResult!!.visibility = View.VISIBLE
        }
        mArrayAdapter!!.notifyDataSetChanged()
    }

    private val filteredCountries: List<Country?>
        private get() = getFilteredCountries("")

    private fun getFilteredCountries(query: String): List<Country?> {
        if (mTempCountries == null) {
            mTempCountries = ArrayList()
        } else {
            mTempCountries!!.clear()
        }
        val preferredCountries = mCountryCodePicker.preferredCountries
        if (preferredCountries != null && preferredCountries.size > 0) {
            for (country in preferredCountries) {
                if (country.isEligibleForQuery(query)) {
                    mTempCountries!!.add(country)
                }
            }
            if (mTempCountries!!.size > 0) { //means at least one preferred country is added.
                mTempCountries!!.add(null) // this will add separator for preference countries.
            }
        }
        for (country in masterCountries!!) {
            if (country.isEligibleForQuery(query)) {
                mTempCountries!!.add(country)
            }
        }
        return mTempCountries!!
    }

    companion object {
        private const val TAG = "CountryCodeDialog"
    }
}
*/
