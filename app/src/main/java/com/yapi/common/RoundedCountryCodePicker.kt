/*
package com.yapi.common

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources.Theme
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.telephony.PhoneNumberFormattingTextWatcher
import android.telephony.TelephonyManager
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.rilixtech.widget.countrycodepicker.*
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber.PhoneNumber
import java.util.*

class RoundedCountryCodePicker : RelativeLayout() {
    val TAG = CountryCodePicker::class.java.simpleName

    val DEFAULT_COUNTRY = Locale.getDefault().country
    val DEFAULT_ISO_COUNTRY = "ID"
    val DEFAULT_TEXT_COLOR = 0
    val DEFAULT_BACKGROUND_COLOR = Color.TRANSPARENT

    var mBackgroundColor = DEFAULT_BACKGROUND_COLOR

    var mDefaultCountryCode = 0
    var mDefaultCountryNameCode: String? = null

    //Util
    var mPhoneUtil: PhoneNumberUtil? = null
    var mPhoneNumberWatcher: PhoneNumberWatcher? = null
    var mPhoneNumberInputValidityListener: PhoneNumberInputValidityListener? = null

    var mTvSelectedCountry: TextView? = null
    var mRegisteredPhoneNumberTextView: TextView? = null
    var mRlyHolder: RelativeLayout? = null
    var mImvArrow: ImageView? = null
    var mImvFlag: ImageView? = null
    var mLlyFlagHolder: LinearLayout? = null
    var mSelectedCountry: Country? = null
    var mDefaultCountry: Country? = null
    var mRlyClickConsumer: RelativeLayout? = null
    var mCountryCodeHolderClickListener: View.OnClickListener? = null

    var mHideNameCode = false
    var mShowFlag = true
    var mShowFullName = false
    val mUseFullName = false
    var mSelectionDialogShowSearch = true

    var mPreferredCountries: List<Country>? = null
    //this will be "AU,ID,US"
    var mCountryPreference: String? = null
    var mCustomMasterCountriesList: List<Country>? = null
    //this will be "AU,ID,US"
    var mCustomMasterCountries: String? = null
    var mKeyboardAutoPopOnSearch = true
    var mIsClickable = true
    var mCountryCodeDialog: CountryCodeDialog? = null

    var mHidePhoneCode = false

    var mTextColor = DEFAULT_TEXT_COLOR

    var mDialogTextColor = DEFAULT_TEXT_COLOR

    // Font typeface
    var mTypeFace: Typeface? = null

    var mIsHintEnabled = true
    var mIsEnablePhoneNumberWatcher = true

    var mSetCountryByTimeZone = true

    var mOnCountryChangeListener: OnCountryChangeListener? = null

    */
/**
     * interface to set change listener
     *//*

    interface OnCountryChangeListener {
        fun onCountrySelected(selectedCountry: Country?)
    }

    */
/**
     * Interface for checking when phone number checker validity is finish.
     *//*

    interface PhoneNumberInputValidityListener {
        fun onFinish(ccp: CountryCodePicker?, isValid: Boolean)
    }

    fun CountryCodePicker(context: Context?) {
        super(context)
        //if (!isInEditMode())
        init(null)
    }

    fun CountryCodePicker(context: Context?, attrs: AttributeSet?) {
        super(context, attrs)
        //if (!isInEditMode())
        init(attrs)
    }

    override  fun CountryCodePicker(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) {
        super(context, attrs, defStyleAttr)
        //if (!isInEditMode())
        init(attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun CountryCodePicker(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) {
        super(context, attrs, defStyleAttr, defStyleRes)
        //if (!isInEditMode())
        init(attrs)
    }

    open fun init(attrs: AttributeSet) {
        View.inflate(getContext(), R.layout.country_code_picker_layout_code_picker, this)
        mTvSelectedCountry = findViewById<TextView>(R.id.selected_country_tv)
        mRlyHolder = findViewById<RelativeLayout>(R.id.country_code_holder_rly)
        mImvArrow = findViewById<ImageView>(R.id.arrow_imv)
        mImvFlag = findViewById<ImageView>(R.id.flag_imv)
        mLlyFlagHolder = findViewById<LinearLayout>(R.id.flag_holder_lly)
        mRlyClickConsumer = findViewById<RelativeLayout>(R.id.click_consumer_rly)
        applyCustomProperty(attrs)
        mCountryCodeHolderClickListener = View.OnClickListener {
            if (isClickable()) {
                showCountryCodePickerDialog()
            }
        }
        mRlyClickConsumer!!.setOnClickListener(mCountryCodeHolderClickListener)
    }

    open fun applyCustomProperty(attrs: AttributeSet) {
        mPhoneUtil = PhoneNumberUtil.createInstance(getContext())
        val theme: Theme = getContext().getTheme()
        val a = theme.obtainStyledAttributes(attrs, R.styleable.CountryCodePicker, 0, 0)
        try {
            mHidePhoneCode = a.getBoolean(R.styleable.CountryCodePicker_ccp_hidePhoneCode, false)
            mShowFullName = a.getBoolean(R.styleable.CountryCodePicker_ccp_showFullName, false)
            mHideNameCode = a.getBoolean(R.styleable.CountryCodePicker_ccp_hideNameCode, false)
            mIsHintEnabled = a.getBoolean(R.styleable.CountryCodePicker_ccp_enableHint, true)

            // enable auto formatter for phone number input
            mIsEnablePhoneNumberWatcher =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_enablePhoneAutoFormatter, true)
            setKeyboardAutoPopOnSearch(
                a.getBoolean(R.styleable.CountryCodePicker_ccp_keyboardAutoPopOnSearch, true))
            mCustomMasterCountries =
                a.getString(R.styleable.CountryCodePicker_ccp_customMasterCountries)
            refreshCustomMasterList()
            mCountryPreference = a.getString(R.styleable.CountryCodePicker_ccp_countryPreference)
            refreshPreferredCountries()
            applyCustomPropertyOfDefaultCountryNameCode(a)
            showFlag(a.getBoolean(R.styleable.CountryCodePicker_ccp_showFlag, true))
            applyCustomPropertyOfColor(a)

            // text font
            val fontPath = a.getString(R.styleable.CountryCodePicker_ccp_textFont)
            if (fontPath != null && !fontPath.isEmpty()) setTypeFace(fontPath)

            //text size
            val textSize = a.getDimensionPixelSize(R.styleable.CountryCodePicker_ccp_textSize, 0)
            if (textSize > 0) {
                mTvSelectedCountry!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
                setFlagSize(textSize)
                setArrowSize(textSize)
            } else { //no text size specified
                val dm: DisplayMetrics = getContext().getResources().getDisplayMetrics()
                val defaultSize = Math.round(18 * (dm.xdpi / DisplayMetrics.DENSITY_DEFAULT))
                setTextSize(defaultSize)
            }

            //if arrow arrow size is explicitly defined
            val arrowSize = a.getDimensionPixelSize(R.styleable.CountryCodePicker_ccp_arrowSize, 0)
            if (arrowSize > 0) setArrowSize(arrowSize)
            mSelectionDialogShowSearch =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_selectionDialogShowSearch, true)
            setClickable(a.getBoolean(R.styleable.CountryCodePicker_ccp_clickable, true))
            mSetCountryByTimeZone =
                a.getBoolean(R.styleable.CountryCodePicker_ccp_setCountryByTimeZone, true)

            // Set to default phone code if no country name code set in attribute.
            if (mDefaultCountryNameCode == null || mDefaultCountryNameCode!!.isEmpty()) {
                setDefaultCountryFlagAndCode()
            }
        } catch (e: Exception) {
            Log.d(TAG, "exception = $e")
            if (isInEditMode()) {
                mTvSelectedCountry.setText(
                    getContext().getString(R.string.phone_code,
                        getContext().getString(R.string.country_indonesia_number)))
            } else {
                mTvSelectedCountry!!.text = e.message
            }
        } finally {
            a.recycle()
        }
    }

    open fun applyCustomPropertyOfDefaultCountryNameCode(tar: TypedArray) {
        //default country
        mDefaultCountryNameCode = tar.getString(R.styleable.CountryCodePicker_ccp_defaultNameCode)
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "mDefaultCountryNameCode from attribute = $mDefaultCountryNameCode")
        }
        if (mDefaultCountryNameCode == null || mDefaultCountryNameCode!!.isEmpty()) return
        if (mDefaultCountryNameCode!!.trim { it <= ' ' }.isEmpty()) {
            mDefaultCountryNameCode = null
            return
        }
        setDefaultCountryUsingNameCode(mDefaultCountryNameCode)
        setSelectedCountry(mDefaultCountry)
    }

    open fun applyCustomPropertyOfColor(arr: TypedArray) {
        //text color
        val textColor: Int
        textColor = if (isInEditMode()) {
            arr.getColor(R.styleable.CountryCodePicker_ccp_textColor, DEFAULT_TEXT_COLOR)
        } else {
            arr.getColor(R.styleable.CountryCodePicker_ccp_textColor,
                getColor(getContext(), R.color.defaultTextColor))
        }
        if (textColor != 0) setTextColor(textColor)
        mDialogTextColor =
            arr.getColor(R.styleable.CountryCodePicker_ccp_dialogTextColor, DEFAULT_TEXT_COLOR)

        // background color of view.
        mBackgroundColor =
            arr.getColor(R.styleable.CountryCodePicker_ccp_backgroundColor, Color.TRANSPARENT)
        if (mBackgroundColor != Color.TRANSPARENT) mRlyHolder!!.setBackgroundColor(mBackgroundColor)
    }

    open fun getDefaultCountry(): Country? {
        return mDefaultCountry
    }

    open fun setDefaultCountry(defaultCountry: Country) {
        mDefaultCountry = defaultCountry
    }

    open fun getSelectedCountry(): Country? {
        return mSelectedCountry
    }

    fun setSelectedCountry(selectedCountry: Country?) {
        var selectedCountry = selectedCountry
        mSelectedCountry = selectedCountry
        val ctx: Context = getContext()

        //as soon as country is selected, textView should be updated
        if (selectedCountry == null) {
            selectedCountry = CountryUtils.getByCode(ctx, mPreferredCountries, mDefaultCountryCode)
        }
        if (mRegisteredPhoneNumberTextView != null) {
            val ISO = selectedCountry!!.iso.uppercase(Locale.getDefault())
            setPhoneNumberWatcherToTextView(mRegisteredPhoneNumberTextView, ISO)
        }
        if (mOnCountryChangeListener != null) {
            mOnCountryChangeListener.onCountrySelected(selectedCountry)
        }
        mImvFlag!!.setImageResource(CountryUtils.getFlagDrawableResId(selectedCountry))
        if (mIsHintEnabled) setPhoneNumberHint()
        setSelectedCountryText(ctx, selectedCountry)
    }

    open fun setSelectedCountryText(ctx: Context, selectedCountry: Country) {
        if (mHideNameCode && mHidePhoneCode && !mShowFullName) {
            mTvSelectedCountry!!.text = ""
            return
        }
        val phoneCode = selectedCountry.phoneCode
        if (mShowFullName) {
            val countryName = selectedCountry.name.uppercase(Locale.getDefault())
            if (mHidePhoneCode && mHideNameCode) {
                mTvSelectedCountry!!.text = countryName
                return
            }
            if (mHideNameCode) {
                mTvSelectedCountry!!.text = ctx.getString(R.string.country_full_name_and_phone_code,
                    countryName, phoneCode)
                return
            }
            val ISO = selectedCountry.iso.uppercase(Locale.getDefault())
            if (mHidePhoneCode) {
                mTvSelectedCountry!!.text = ctx.getString(R.string.country_full_name_and_name_code,
                    countryName, ISO)
                return
            }
            mTvSelectedCountry!!.text =
                ctx.getString(R.string.country_full_name_name_code_and_phone_code,
                    countryName, ISO, phoneCode)
            return
        }
        if (mHideNameCode && mHidePhoneCode) {
            val countryName = selectedCountry.name.uppercase(Locale.getDefault())
            mTvSelectedCountry!!.text = countryName
            return
        }
        if (mHideNameCode) {
            mTvSelectedCountry!!.text = ctx.getString(R.string.phone_code, phoneCode)
            return
        }
        if (mHidePhoneCode) {
            val iso = selectedCountry.iso.uppercase(Locale.getDefault())
            mTvSelectedCountry!!.text = iso
            return
        }
        val iso = selectedCountry.iso.uppercase(Locale.getDefault())
        mTvSelectedCountry!!.text =
            ctx.getString(R.string.country_code_and_phone_code, iso, phoneCode)
    }

    fun isKeyboardAutoPopOnSearch(): Boolean {
        return mKeyboardAutoPopOnSearch
    }

    */
/**
     * By default, keyboard is poped every time ccp is clicked and selection dialog is opened.
     *
     * @param keyboardAutoPopOnSearch true: to open keyboard automatically when selection dialog is
     * opened
     * false: to avoid auto pop of keyboard
     *//*

    fun setKeyboardAutoPopOnSearch(keyboardAutoPopOnSearch: Boolean) {
        mKeyboardAutoPopOnSearch = keyboardAutoPopOnSearch
    }

    */
/**
     * Get status of phone number formatter.
     *
     * @return enable or not.
     *//*

    fun isPhoneAutoFormatterEnabled(): Boolean {
        return mIsEnablePhoneNumberWatcher
    }

    */
/**
     * Enable or disable auto formatter for phone number inserted to TextView.
     * You need to set an EditText for phone number with `registerPhoneNumberTextView()`
     * to make use of this.
     *
     * @param isEnable return if phone auto formatter enabled or not.
     *//*

    fun enablePhoneAutoFormatter(isEnable: Boolean) {
        mIsEnablePhoneNumberWatcher = isEnable
        if (isEnable) {
            if (mPhoneNumberWatcher == null) {
                mPhoneNumberWatcher = PhoneNumberWatcher(getSelectedCountryNameCode())
            }
        } else {
            mPhoneNumberWatcher = null
        }
    }

    open fun getCountryCodeHolderClickListener(): View.OnClickListener? {
        return mCountryCodeHolderClickListener
    }

    */
/**
     * this will load mPreferredCountries based on mCountryPreference
     *//*

    fun refreshPreferredCountries() {
        if (mCountryPreference == null || mCountryPreference!!.length == 0) {
            mPreferredCountries = null
            return
        }
        val localCountryList: MutableList<Country> = ArrayList()
        for (nameCode in mCountryPreference!!.split(",").toTypedArray()) {
            val country =
                CountryUtils.getByNameCodeFromCustomCountries(getContext(),
                    mCustomMasterCountriesList,
                    nameCode) ?: continue
            //to avoid duplicate entry of country
            if (isAlreadyInList(country, localCountryList)) continue
            localCountryList.add(country)
        }
        mPreferredCountries = if (localCountryList.size == 0) {
            null
        } else {
            localCountryList
        }
    }

    */
/**
     * this will load mPreferredCountries based on mCountryPreference
     *//*

    fun refreshCustomMasterList() {
        if (mCustomMasterCountries == null || mCustomMasterCountries!!.length == 0) {
            mCustomMasterCountriesList = null
            return
        }
        val localCountries: MutableList<Country> = ArrayList()
        val split = mCustomMasterCountries!!.split(",").toTypedArray()
        for (i in split.indices) {
            val nameCode = split[i]
            val country = CountryUtils.getByNameCodeFromAllCountries(getContext(), nameCode)
                ?: continue
            //to avoid duplicate entry of country
            if (isAlreadyInList(country, localCountries)) continue
            localCountries.add(country)
        }
        mCustomMasterCountriesList = if (localCountries.size == 0) {
            null
        } else {
            localCountries
        }
    }

    fun getCustomCountries(): List<Country>? {
        return mCustomMasterCountriesList
    }

    */
/**
     * Get custom country by preference
     *
     * @param codePicker picker for the source of country
     * @return List of country
     *//*

    fun getCustomCountries(codePicker: CountryCodePicker): List<Country?>? {
        codePicker.refreshCustomMasterList()
        return if (codePicker.getCustomCountries() == null || codePicker.getCustomCountries().size <= 0) {
            CountryUtils.getAllCountries(codePicker.context)
        } else {
            codePicker.getCustomCountries()
        }
    }

    fun setCustomMasterCountriesList(customMasterCountriesList: List<Country>?) {
        mCustomMasterCountriesList = customMasterCountriesList
    }

    fun getCustomMasterCountries(): String? {
        return mCustomMasterCountries
    }

    fun getPreferredCountries(): List<Country>? {
        return mPreferredCountries
    }

    */
/**
     * To provide definite set of countries when selection dialog is opened.
     * Only custom master countries, if defined, will be there is selection dialog to select from.
     * To set any country in preference, it must be included in custom master countries, if defined
     * When not defined or null or blank is set, it will use library's default master list
     * Custom master list will only limit the visibility of irrelevant country from selection dialog.
     * But all other functions like setCountryForCodeName() or setFullNumber() will consider all the
     * countries.
     *
     * @param customMasterCountries is country name codes separated by comma. e.g. "us,in,nz"
     * if null or "" , will remove custom countries and library default will be used.
     *//*

    fun setCustomMasterCountries(customMasterCountries: String?) {
        mCustomMasterCountries = customMasterCountries
    }

    */
/**
     * This will match name code of all countries of list against the country's name code.
     *
     * @param countries list of countries against which country will be checked.
     * @return if country name code is found in list, returns true else return false
     *//*

    open fun isAlreadyInList(country: Country?, countries: List<Country>?): Boolean {
        if (country == null || countries == null) return false
        for (i in countries.indices) {
            if (countries[i].iso.equals(country.iso, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    */
/**
     * This function removes possible country code from fullNumber and set rest of the number as
     * carrier number.
     *
     * @param fullNumber combination of country code and carrier number.
     * @param country selected country in CCP to detect country code part.
     *//*

    open fun detectCarrierNumber(fullNumber: String?, country: Country?): String? {
        val carrierNumber: String?
        carrierNumber = if (country == null || fullNumber == null) {
            fullNumber
        } else {
            val indexOfCode = fullNumber.indexOf(country.phoneCode)
            if (indexOfCode == -1) {
                fullNumber
            } else {
                fullNumber.substring(indexOfCode + country.phoneCode.length)
            }
        }
        return carrierNumber
    }

    */
/**
     * This method is not encouraged because this might set some other country which have same country
     * code as of yours. e.g 1 is common for US and canada.
     * If you are trying to set US ( and mCountryPreference is not set) and you pass 1 as @param
     * mDefaultCountryCode, it will set canada (prior in list due to alphabetical order)
     * Rather use setDefaultCountryUsingNameCode("us"); or setDefaultCountryUsingNameCode("US");
     *
     *
     * Default country code defines your default country.
     * Whenever invalid / improper number is found in setCountryForPhoneCode() /  setFullNumber(), it
     * CCP will set to default country.
     * This function will not set default country as selected in CCP. To set default country in CCP
     * call resetToDefaultCountry() right after this call.
     * If invalid mDefaultCountryCode is applied, it won't be changed.
     *
     * @param defaultCountryCode code of your default country
     * if you want to set IN +91(India) as default country, mDefaultCountryCode =  91
     * if you want to set JP +81(Japan) as default country, mDefaultCountryCode =  81
     *//*

    @Deprecated("")
    fun setDefaultCountryUsingPhoneCode(defaultCountryCode: Int) {
        val defaultCountry =
            CountryUtils.getByCode(getContext(), mPreferredCountries, defaultCountryCode)
                ?: return

        //if correct country is found, set the country
        mDefaultCountryCode = defaultCountryCode
        setDefaultCountry(defaultCountry)
    }

    fun setDefaultCountryUsingPhoneCodeAndApply(defaultCountryCode: Int) {
        val defaultCountry =
            CountryUtils.getByCode(getContext(), mPreferredCountries, defaultCountryCode)
                ?: return

        //if correct country is found, set the country
        mDefaultCountryCode = defaultCountryCode
        setDefaultCountry(defaultCountry)
        resetToDefaultCountry()
    }

    */
/**
     * Default country name code defines your default country.
     * Whenever invalid / improper name code is found in setCountryForNameCode(), CCP will set to
     * default country.
     * This function will not set default country as selected in CCP. To set default country in CCP
     * call resetToDefaultCountry() right after this call.
     * If invalid countryIso is applied, it won't be changed.
     *
     * @param countryIso code of your default country
     * if you want to set IN +91(India) as default country, countryIso =  "IN" or "in"
     * if you want to set JP +81(Japan) as default country, countryIso =  "JP" or "jp"
     *//*

    fun setDefaultCountryUsingNameCode(countryIso: String) {
        val defaultCountry = CountryUtils.getByNameCodeFromAllCountries(getContext(), countryIso)
            ?: return

        //if correct country is found, set the country
        mDefaultCountryNameCode = defaultCountry.iso
        setDefaultCountry(defaultCountry)
    }

    */
/**
     * Set default country as selected in CountryCodePicker.
     *
     * There is no change applied if invalid countryIso is given.
     *
     * @param countryIso code of your default country
     * if you want to set IN +91(India) as default country, countryIso =  "IN" or "in"
     * if you want to set JP +81(Japan) as default country, countryIso =  "JP" or "jp"
     *//*

    fun setDefaultCountryUsingNameCodeAndApply(countryIso: String) {
        val defaultCountry = CountryUtils.getByNameCodeFromAllCountries(getContext(), countryIso)
            ?: return

        //if correct country is found, set the country
        mDefaultCountryNameCode = defaultCountry.iso
        setDefaultCountry(defaultCountry)

        //TODO: This part of code need to be optimized!!.
        setEmptyDefault(null)
    }

    */
/**
     * Get Country Code of default country
     * i.e if default country is IN +91(India)  returns: "91"
     * if default country is JP +81(Japan) returns: "81"
     *//*

    fun getDefaultCountryCode(): String {
        return mDefaultCountry!!.phoneCode
    }

    */
/**
     * * To get code of default country as Integer.
     *
     * @return integer value of default country code in CCP
     * i.e if default country is IN +91(India)  returns: 91
     * if default country is JP +81(Japan) returns: 81
     *//*

    fun getDefaultCountryCodeAsInt(): Int {
        var code = 0
        try {
            code = getDefaultCountryCode().toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return code
    }

    */
/**
     * To get code of default country with prefix "+".
     *
     * @return String value of default country code in CCP with prefix "+"
     * i.e if default country is IN +91(India)  returns: "+91"
     * if default country is JP +81(Japan) returns: "+81"
     *//*

    fun getDefaultCountryCodeWithPlus(): String? {
        return getContext().getString(R.string.phone_code, getDefaultCountryCode())
    }

    */
/**
     * To get name of default country.
     *
     * @return String value of country name, default in CCP
     * i.e if default country is IN +91(India)  returns: "India"
     * if default country is JP +81(Japan) returns: "Japan"
     *//*

    fun getDefaultCountryName(): String? {
        return mDefaultCountry!!.name
    }

    */
/**
     * To get name code of default country.
     *
     * @return String value of country name, default in CCP
     * i.e if default country is IN +91(India)  returns: "IN"
     * if default country is JP +81(Japan) returns: "JP"
     *//*

    fun getDefaultCountryNameCode(): String? {
        return mDefaultCountry!!.iso.uppercase(Locale.getDefault())
    }

    */
/**
     * reset the default country as selected country.
     *//*

    fun resetToDefaultCountry() {
        setEmptyDefault()
    }

    */
/**
     * To get code of selected country.
     *
     * @return String value of selected country code in CCP
     * i.e if selected country is IN +91(India)  returns: "91"
     * if selected country is JP +81(Japan) returns: "81"
     *//*

    fun getSelectedCountryCode(): String {
        return mSelectedCountry!!.phoneCode
    }

    */
/**
     * To get code of selected country with prefix "+".
     *
     * @return String value of selected country code in CCP with prefix "+"
     * i.e if selected country is IN +91(India)  returns: "+91"
     * if selected country is JP +81(Japan) returns: "+81"
     *//*

    fun getSelectedCountryCodeWithPlus(): String? {
        return getContext().getString(R.string.phone_code, getSelectedCountryCode())
    }

    */
/**
     * * To get code of selected country as Integer.
     *
     * @return integer value of selected country code in CCP
     * i.e if selected country is IN +91(India)  returns: 91
     * if selected country is JP +81(Japan) returns: 81
     *//*

    fun getSelectedCountryCodeAsInt(): Int {
        var code = 0
        try {
            code = getSelectedCountryCode().toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return code
    }

    */
/**
     * To get name of selected country.
     *
     * @return String value of country name, selected in CCP
     * i.e if selected country is IN +91(India)  returns: "India"
     * if selected country is JP +81(Japan) returns: "Japan"
     *//*

    fun getSelectedCountryName(): String? {
        return mSelectedCountry!!.name
    }

    */
/**
     * To get name code of selected country.
     *
     * @return String value of country name, selected in CCP
     * i.e if selected country is IN +91(India)  returns: "IN"
     * if selected country is JP +81(Japan) returns: "JP"
     *//*

    fun getSelectedCountryNameCode(): String? {
        return mSelectedCountry!!.iso.uppercase(Locale.getDefault())
    }

    */
/**
     * This will set country with @param countryCode as country code, in CCP
     *
     * @param countryCode a valid country code.
     * If you want to set IN +91(India), countryCode= 91
     * If you want to set JP +81(Japan), countryCode= 81
     *//*

    fun setCountryForPhoneCode(countryCode: Int) {
        val ctx: Context = getContext()
        val country = CountryUtils.getByCode(ctx, mPreferredCountries, countryCode)
        if (country == null) {
            if (mDefaultCountry == null) {
                mDefaultCountry =
                    CountryUtils.getByCode(ctx, mPreferredCountries, mDefaultCountryCode)
            }
            setSelectedCountry(mDefaultCountry)
        } else {
            setSelectedCountry(country)
        }
    }

    */
/**
     * This will set country with @param countryNameCode as country name code, in CCP
     *
     * @param countryNameCode a valid country name code.
     * If you want to set IN +91(India), countryCode= IN
     * If you want to set JP +81(Japan), countryCode= JP
     *//*

    fun setCountryForNameCode(countryNameCode: String) {
        val ctx: Context = getContext()
        val country = CountryUtils.getByNameCodeFromAllCountries(ctx, countryNameCode)
        if (country == null) {
            if (mDefaultCountry == null) {
                mDefaultCountry =
                    CountryUtils.getByCode(ctx, mPreferredCountries, mDefaultCountryCode)
            }
            setSelectedCountry(mDefaultCountry)
        } else {
            setSelectedCountry(country)
        }
    }

    */
/**
     * All functions that work with fullNumber need an editText to write and read carrier number of
     * full number.
     * An editText for carrier number must be registered in order to use functions like
     * setFullNumber() and getFullNumber().
     *
     * @param textView - an editText where user types carrier number ( the part of full
     * number other than country code).
     *//*

    fun registerPhoneNumberTextView(textView: TextView) {
        setRegisteredPhoneNumberTextView(textView)
        if (mIsHintEnabled) setPhoneNumberHint()
    }

    fun getRegisteredPhoneNumberTextView(): TextView? {
        return mRegisteredPhoneNumberTextView
    }

    fun setRegisteredPhoneNumberTextView(phoneNumberTextView: TextView) {
        mRegisteredPhoneNumberTextView = phoneNumberTextView
        if (mIsEnablePhoneNumberWatcher) {
            if (mPhoneNumberWatcher == null) {
                mPhoneNumberWatcher = PhoneNumberWatcher(getDefaultCountryNameCode())
            }
            mRegisteredPhoneNumberTextView!!.addTextChangedListener(mPhoneNumberWatcher)
        }
    }

    open fun setPhoneNumberWatcherToTextView(textView: TextView, countryNameCode: String) {
        if (!mIsEnablePhoneNumberWatcher) return
        if (mPhoneNumberWatcher == null) {
            mPhoneNumberWatcher = PhoneNumberWatcher(countryNameCode)
            textView.addTextChangedListener(mPhoneNumberWatcher)
        } else {
            if (!mPhoneNumberWatcher.getPreviousCountryCode()
                    .equals(countryNameCode, ignoreCase = true)
            ) {
                textView.removeTextChangedListener(mPhoneNumberWatcher)
                mPhoneNumberWatcher = PhoneNumberWatcher(countryNameCode)
                textView.addTextChangedListener(mPhoneNumberWatcher)
            }
        }
    }

    */
/**
     * This function combines selected country code from CCP and carrier number from @param
     * editTextCarrierNumber
     *
     * @return Full number is countryCode + carrierNumber i.e countryCode= 91 and carrier number=
     * 8866667722, this will return "918866667722"
     *//*

    fun getFullNumber(): String? {
        var fullNumber = mSelectedCountry!!.phoneCode
        if (mRegisteredPhoneNumberTextView == null) {
            Log.w(TAG, getContext().getString(R.string.error_unregister_carrier_number))
        } else {
            fullNumber += mRegisteredPhoneNumberTextView!!.text.toString()
        }
        return fullNumber
    }

    */
/**
     * Separate out country code and carrier number from fullNumber.
     * Sets country of separated country code in CCP and carrier number as text of
     * editTextCarrierNumber
     * If no valid country code is found from full number, CCP will be set to default country code and
     * full number will be set as carrier number to editTextCarrierNumber.
     *
     * @param fullNumber is combination of country code and carrier number,
     * (country_code+carrier_number) for example if country is India (+91) and carrier/mobile number
     * is 8866667722 then full number will be 9188666667722 or +918866667722. "+" in starting of
     * number is optional.
     *//*

    fun setFullNumber(fullNumber: String) {
        val country = CountryUtils.getByNumber(getContext(), mPreferredCountries, fullNumber)
        setSelectedCountry(country)
        val carrierNumber = detectCarrierNumber(fullNumber, country)
        if (mRegisteredPhoneNumberTextView == null) {
            Log.w(TAG, getContext().getString(R.string.error_unregister_carrier_number))
        } else {
            mRegisteredPhoneNumberTextView!!.text = carrierNumber
        }
    }

    */
/**
     * This function combines selected country code from CCP and carrier number from @param
     * editTextCarrierNumber and prefix "+"
     *
     * @return Full number is countryCode + carrierNumber i.e countryCode= 91 and carrier number=
     * 8866667722, this will return "+918866667722"
     *//*

    fun getFullNumberWithPlus(): String? {
        return getContext().getString(R.string.phone_code, getFullNumber())
    }

    */
/**
     * @return content color of CCP's text and small downward arrow.
     *//*

    fun getTextColor(): Int {
        return mTextColor
    }

    fun getDefaultContentColor(): Int {
        return DEFAULT_TEXT_COLOR
    }

    */
/**
     * Sets text and small down arrow color of CCP.
     *
     * @param contentColor color to apply to text and down arrow
     *//*

    fun setTextColor(contentColor: Int) {
        mTextColor = contentColor
        mTvSelectedCountry!!.setTextColor(contentColor)
        mImvArrow!!.setColorFilter(contentColor, PorterDuff.Mode.SRC_IN)
    }

    fun getBackgroundColor(): Int {
        return mBackgroundColor
    }

    fun setBackgroundColor(backgroundColor: Int) {
        mBackgroundColor = backgroundColor
        mRlyHolder!!.setBackgroundColor(backgroundColor)
    }

    fun getDefaultBackgroundColor(): Int {
        return DEFAULT_BACKGROUND_COLOR
    }

    */
/**
     * Modifies size of text in side CCP view.
     *
     * @param textSize size of text in pixels
     *//*

    fun setTextSize(textSize: Int) {
        if (textSize > 0) {
            mTvSelectedCountry!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
            setArrowSize(textSize)
            setFlagSize(textSize)
        }
    }

    */
/**
     * Modifies size of downArrow in CCP view
     *
     * @param arrowSizeInDp size in dimension pixels
     *//*

    fun setArrowSize(arrowSizeInDp: Int) {
        if (arrowSizeInDp > 0) {
            val params = mImvArrow!!.layoutParams as RelativeLayout.LayoutParams
            params.width = arrowSizeInDp
            params.height = arrowSizeInDp
            mImvArrow!!.layoutParams = params
        }
    }

    */
/**
     * If nameCode of country in CCP view is not required use this to show/hide country name code of
     * ccp view.
     *
     * @param hide true will remove country name code from ccp view, it will result  " +91 "
     * false will show country name code in ccp view, it will result " (IN) +91 "
     *//*

    fun hideNameCode(hide: Boolean) {
        mHideNameCode = hide
        setSelectedCountry(mSelectedCountry)
    }

    */
/**
     * This will set preferred countries using their name code. Prior preferred countries will be
     * replaced by these countries.
     * Preferred countries will be at top of country selection box.
     * If more than one countries have same country code, country in preferred list will have higher
     * priory than others. e.g. Canada and US have +1 as their country code. If "us" is set as
     * preferred country then US will be selected whenever setCountryForPhoneCode(1); or
     * setFullNumber("+1xxxxxxxxx"); is called.
     *
     * @param countryPreference is country name codes separated by comma. e.g. "us,in,nz"
     *//*

    fun setCountryPreference(countryPreference: String) {
        mCountryPreference = countryPreference
    }

    */
/**
     * Set TypeFace for all the text in CCP
     *
     * @param typeFace TypeFace generated from assets.
     *//*

    fun setTypeFace(typeFace: Typeface) {
        mTypeFace = typeFace
        try {
            mTvSelectedCountry!!.setTypeface(typeFace)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    */
/**
     * set TypeFace for all the text in CCP
     *
     * @param fontAssetPath font path in asset folder.
     *//*

    fun setTypeFace(fontAssetPath: String) {
        try {
            val typeFace = Typeface.createFromAsset(getContext().getAssets(), fontAssetPath)
            mTypeFace = typeFace
            mTvSelectedCountry!!.setTypeface(typeFace)
        } catch (e: Exception) {
            Log.d(TAG, "Invalid fontPath. $e")
        }
    }

    */
/**
     * To change font of ccp views along with style.
     *//*

    fun setTypeFace(typeFace: Typeface, style: Int) {
        try {
            mTvSelectedCountry!!.setTypeface(typeFace, style)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getTypeFace(): Typeface? {
        return mTypeFace
    }

    */
/**
     * To get call back on country selection a mOnCountryChangeListener must be registered.
     *//*

    fun setOnCountryChangeListener(onCountryChangeListener: OnCountryChangeListener) {
        mOnCountryChangeListener = onCountryChangeListener
    }

    */
/**
     * Modifies size of flag in CCP view
     *
     * @param flagSize size in pixels
     *//*

    fun setFlagSize(flagSize: Int) {
        mImvFlag!!.layoutParams.height = flagSize
        mImvFlag!!.requestLayout()
    }

    fun showFlag(showFlag: Boolean) {
        mShowFlag = showFlag
        mLlyFlagHolder!!.visibility = if (showFlag) View.VISIBLE else View.GONE
    }

    */
/**
     * Show full country name instead only iso name.
     *
     * @param show show or not.
     *//*

    fun showFullName(show: Boolean) {
        mShowFullName = show
        setSelectedCountry(mSelectedCountry)
    }

    */
/**
     * SelectionDialogSearch is the facility to search through the list of country while selecting.
     *
     * @return true if search is set allowed
     *//*

    fun isSelectionDialogShowSearch(): Boolean {
        return mSelectionDialogShowSearch
    }

    */
/**
     * SelectionDialogSearch is the facility to search through the list of country while selecting.
     *
     * @param selectionDialogShowSearch true will allow search and false will hide search box
     *//*

    fun setSelectionDialogShowSearch(selectionDialogShowSearch: Boolean) {
        mSelectionDialogShowSearch = selectionDialogShowSearch
    }

    fun isClickable(): Boolean {
        return mIsClickable
    }

    */
/**
     * Allow click and open dialog
     *//*

    fun setClickable(isClickable: Boolean) {
        mIsClickable = isClickable
        mRlyClickConsumer!!.setOnClickListener(if (isClickable) mCountryCodeHolderClickListener else null)
        mRlyClickConsumer!!.isClickable = isClickable
        mRlyClickConsumer!!.isEnabled = isClickable
    }

    fun isHidePhoneCode(): Boolean {
        return mHidePhoneCode
    }

    fun isHideNameCode(): Boolean {
        return mHideNameCode
    }

    */
/**
     * Check whether phone text sample hint is enabled or not.
     *
     * @return is hint enabled or not.
     *//*

    fun isHintEnabled(): Boolean {
        return mIsHintEnabled
    }

    */
/**
     * Enable hint for phone number sample in registered TextView with registerPhoneNumberTextView()
     *
     * @param hintEnabled disable or enable hint.
     *//*

    fun enableHint(hintEnabled: Boolean) {
        mIsHintEnabled = hintEnabled
        if (mIsHintEnabled) setPhoneNumberHint()
    }

    */
/**
     * Hide or show phone code
     *
     * @param hide show or not show the phone code.
     *//*

    fun hidePhoneCode(hide: Boolean) {
        mHidePhoneCode = hide
        setSelectedCountry(mSelectedCountry)
    }

    open fun setPhoneNumberHint() {
        // don't set phone number hint for null textView and country.
        if (mRegisteredPhoneNumberTextView == null || mSelectedCountry == null || mSelectedCountry!!.iso == null) {
            return
        }
        val iso = mSelectedCountry!!.iso.uppercase(Locale.getDefault())
        val mobile = PhoneNumberUtil.PhoneNumberType.MOBILE
        val phoneNumber = mPhoneUtil!!.getExampleNumberForType(iso, mobile)
        if (phoneNumber == null) {
            mRegisteredPhoneNumberTextView!!.hint = ""
            return
        }
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "setPhoneNumberHint called")
            Log.d(TAG, "mSelectedCountry.getIso() = " + mSelectedCountry!!.iso)
            Log.d(TAG,
                "hint = " + mPhoneUtil!!.format(phoneNumber,
                    PhoneNumberUtil.PhoneNumberFormat.NATIONAL))
        }
        val hint = mPhoneUtil!!.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)
        //if (mRegisteredPhoneNumberTextView.getHint() != null) {
        //  mRegisteredPhoneNumberTextView.setHint("");
        //}
        mRegisteredPhoneNumberTextView!!.hint = hint
    }

    class PhoneNumberWatcher : PhoneNumberFormattingTextWatcher {
        private var lastValidity = false
        var previousCountryCode = ""
            private set

        constructor() : super() {}

        //TODO solve it! support for android kitkat
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        constructor(countryCode: String) : super(countryCode) {
            previousCountryCode = countryCode
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            super.onTextChanged(s, start, before, count)
            try {
                var iso: String? = null
                if (mSelectedCountry != null) iso =
                    mSelectedCountry!!.phoneCode.uppercase(Locale.getDefault())
                val phoneNumber = mPhoneUtil!!.parse(s.toString(), iso)
                iso = mPhoneUtil!!.getRegionCodeForNumber(phoneNumber)
                if (iso != null) {
                    //int countryIdx = mCountries.indexOfIso(iso);
                    //mCountrySpinner.setSelection(countryIdx);
                }
            } catch (ignored: NumberParseException) {
            }
            if (mPhoneNumberInputValidityListener != null) {
                val validity: Boolean = isValid()
                if (validity != lastValidity) {
                    mPhoneNumberInputValidityListener.onFinish(this@CountryCodePicker, validity)
                }
                lastValidity = validity
            }
        }
    }

    */
/**
     * Get number
     *
     * @return Phone number in E.164 format | null on error
     *//*

    fun getNumber(): String? {
        val phoneNumber: PhoneNumber = getPhoneNumber() ?: return null
        if (mRegisteredPhoneNumberTextView == null) {
            Log.w(TAG, getContext().getString(R.string.error_unregister_carrier_number))
            return null
        }
        return mPhoneUtil!!.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
    }

    */
/**
     * Get PhoneNumber object
     *
     * @return Phone Number | null on error
     *//*

    fun getPhoneNumber(): PhoneNumber? {
        return try {
            var iso: String? = null
            if (mSelectedCountry != null) iso =
                mSelectedCountry!!.iso.uppercase(Locale.getDefault())
            if (mRegisteredPhoneNumberTextView == null) {
                Log.w(TAG, getContext().getString(R.string.error_unregister_carrier_number))
                return null
            }
            mPhoneUtil!!.parse(mRegisteredPhoneNumberTextView!!.text.toString(), iso)
        } catch (ignored: NumberParseException) {
            null
        }
    }

    */
/**
     * Check if number is valid
     *
     * @return boolean
     *//*

    fun isValid(): Boolean {
        val phoneNumber = getPhoneNumber()
        return phoneNumber != null && mPhoneUtil!!.isValidNumber(phoneNumber)
    }

    fun setPhoneNumberInputValidityListener(listener: PhoneNumberInputValidityListener?) {
        mPhoneNumberInputValidityListener = listener
    }

    */
/**
     * Set default value
     * Will try to retrieve phone number from device
     *//*

    open fun setDefaultCountryFlagAndCode() {
        val ctx: Context = getContext()
        val manager = ctx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (manager == null) {
            Log.e(TAG, "Can't access TelephonyManager. Using default county code")
            setEmptyDefault(getDefaultCountryCode())
            return
        }
        try {
            val simCountryIso = manager.simCountryIso
            if (simCountryIso == null || simCountryIso.isEmpty()) {
                val iso = manager.networkCountryIso
                if (iso == null || iso.isEmpty()) {
                    enableSetCountryByTimeZone(true)
                } else {
                    setEmptyDefault(iso)
                    if (BuildConfig.DEBUG) Log.d(TAG,
                        "isoNetwork = $iso")
                }
            } else {
                setEmptyDefault(simCountryIso)
                if (BuildConfig.DEBUG) Log.d(TAG,
                    "simCountryIso = $simCountryIso")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error when getting sim country, error = $e")
            setEmptyDefault(getDefaultCountryCode())
        }
    }

    */
/**
     * Alias for setting empty string of default settings from the device (using locale)
     *//*

    open fun setEmptyDefault() {
        setEmptyDefault(null)
    }

    */
/**
     * Set default value with default locale
     *
     * @param countryCode ISO2 of country
     *//*

    open fun setEmptyDefault(countryCode: String) {
        var countryCode = countryCode
        if (countryCode == null || countryCode.isEmpty()) {
            countryCode =
                if (mDefaultCountryNameCode == null || mDefaultCountryNameCode!!.isEmpty()) {
                    if (DEFAULT_COUNTRY == null || DEFAULT_COUNTRY.isEmpty()) {
                        DEFAULT_ISO_COUNTRY
                    } else {
                        DEFAULT_COUNTRY
                    }
                } else {
                    mDefaultCountryNameCode
                }
        }
        if (mIsEnablePhoneNumberWatcher && mPhoneNumberWatcher == null) {
            mPhoneNumberWatcher = PhoneNumberWatcher(countryCode)
        }
        setDefaultCountryUsingNameCode(countryCode)
        setSelectedCountry(getDefaultCountry())
    }

    */
/**
     * Set checking for country from time zone. This is used to set country whenever CCP can't
     * detect country from phone setting.
     *
     * @param isEnabled set enable or not.
     *//*

    fun enableSetCountryByTimeZone(isEnabled: Boolean) {
        if (isEnabled) {
            if (mDefaultCountryNameCode != null && !mDefaultCountryNameCode!!.isEmpty()) return
            if (mRegisteredPhoneNumberTextView != null) return
            if (mSetCountryByTimeZone) {
                val tz = TimeZone.getDefault()
                if (BuildConfig.DEBUG) Log.d(TAG, "tz.getID() = " + tz.id)
                val countryIsos = CountryUtils.getCountryIsoByTimeZone(getContext(), tz.id)
                if (countryIsos == null) {
                    // If no iso country found, fallback to device locale.
                    setEmptyDefault()
                } else {
                    setDefaultCountryUsingNameCode(countryIsos[0])
                    setSelectedCountry(getDefaultCountry())
                }
            }
        }
        mSetCountryByTimeZone = isEnabled
    }

    fun getDialogTextColor(): Int {
        return mDialogTextColor
    }

    fun setDialogTextColor(dialogTextColor: Int) {
        mDialogTextColor = dialogTextColor
    }

    fun getColor(context: Context, id: Int): Int {
        val version = Build.VERSION.SDK_INT
        return if (version >= 23) {
            context.getColor(id)
        } else {
            context.resources.getColor(id)
        }
    }

    fun showCountryCodePickerDialog() {
        if (mCountryCodeDialog == null) mCountryCodeDialog = CountryCodeDialog(this)
        mCountryCodeDialog!!.show()
    }
}
*/
