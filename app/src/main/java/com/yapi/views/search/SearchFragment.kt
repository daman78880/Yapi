package com.yapi.views.search

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.yapi.R
import com.yapi.common.checkDeviceType
import com.yapi.common.hideKeyboard
import com.yapi.databinding.FragmentSearchBinding
import com.yapi.pref.PreferenceFile
import com.yapi.views.search_result.AdapterSearch
import com.yapi.views.search_result.ClickListener
import com.yapi.views.search_result.PojoSearchScreenData
import com.yapi.views.search_result.SearchResultFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : DialogFragment(){
    private lateinit var binding: FragmentSearchBinding
    private val viewmodel: SearchViewModel by viewModels()

    @Inject
    lateinit var preferenceFile: PreferenceFile

    companion object {
        fun newInstanceSearch(title: String): SearchFragment {
            val args = Bundle()
            args.putString("11", title)
            val fragment = SearchFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create a new dialog
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)

        // Get the window of the dialog
        val window: Window = dialog.getWindow()!!

        // Set the dialog to be shown at the bottom of the screen
        window.setGravity(Gravity.RIGHT)

        var second_frame_height= preferenceFile.fetchStringValue("second_frame_height")
        var second_frame_width=  preferenceFile.fetchStringValue("second_frame_width")
        Log.e("nefjkwnddfkewfwefe===",second_frame_height+"==="+second_frame_width)
        window.setLayout(second_frame_width.toInt(),second_frame_height.toInt())
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkDeviceType()) {
            System.out.println("phone========tablet")
            setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullScreenDialog)
        }
    }

 /*   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Create a new dialog
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)

        // Get the window of the dialog
        val window: Window = dialog.getWindow()!!

        // Set the dialog to be shown at the bottom of the screen
        window.setGravity(Gravity.BOTTOM)

        return dialog
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(LayoutInflater.from(requireContext()))
        binding.mViewModel = viewmodel

        if(checkDeviceType())
        {
        viewmodel.cancelshowField.set(false)
        }else
        {
            viewmodel.cancelshowField.set(true)
        }

        dismissDialogScreenObserver()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    //For dismiss Dialog Fragment obsever
    fun dismissDialogScreenObserver()
    {
        viewmodel.dissmissDialogPopupData.observe(requireActivity(), Observer {
            var data=it as Boolean
            if(data)
            {
                dismiss()
            }
        })
    }
    fun setTouchListenereForNested()
    {
        binding.nestedScrollViewSearch.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                requireActivity().hideKeyboard()
                return false
            }
        })
    }

    private fun init() {
        setBackgroundRectMethod()
        binding.apply {
            recentList()
            setTouchListenereForNested()

            etSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(
                    v: TextView?,
                    actionId: Int,
                    event: KeyEvent?,
                ): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        if (etSearch.text.toString().isEmpty()) {

                            if (findNavController().currentDestination?.id == R.id.searchFragment) {
                                val bundel = Bundle()
                                bundel.putBoolean("empty", true)
                                findNavController()
                                    .navigate(R.id.action_searchFragment_to_searchResultFragment,
                                        bundel)
                            }

                        } else {
                            if (findNavController().currentDestination?.id == R.id.searchFragment)
                                findNavController().navigate(R.id.action_searchFragment_to_searchResultFragment)

                        }
                    }
                    return true
                }
            })
            etSearch.doAfterTextChanged { t ->

                if (t.toString() == "do") {
                    txtTempViewHistorySearch.visibility = View.GONE
                    txtTempClearSearch.visibility = View.GONE
                    txtTempRecentSearch.visibility = View.GONE
                    val resentSearchList = ArrayList<PojoSearchScreenData>()
                    resentSearchList.add(PojoSearchScreenData(1,
                        "File",
                        "VIEW FILES",
                        "Documentation.pdf",
                        "0.4mb",
                        ContextCompat.getDrawable(requireContext(), R.drawable.person)))
                    resentSearchList.add(PojoSearchScreenData(1,
                        "File",
                        "VIEW FILES",
                        "Document_drafter.pdf",
                        "1.5mb",
                        ContextCompat.getDrawable(requireContext(), R.drawable.person)))
                    resentSearchList.add(PojoSearchScreenData(0,
                        "People",
                        "VIEW ALL",
                        "Donald Sutherland",
                        "Frontend Engineer",
                        ContextCompat.getDrawable(requireContext(), R.drawable.demo_photo)))
                    resentSearchList.add(PojoSearchScreenData(0,
                        "People",
                        "VIEW ALL",
                        "Michelle Dochery",
                        "Designer",
                        ContextCompat.getDrawable(requireContext(), R.drawable.demo_photo)))
                    searchList(resentSearchList)
                } else {

                    txtTempViewHistorySearch.visibility = View.VISIBLE
                    txtTempClearSearch.visibility = View.VISIBLE
                    txtTempRecentSearch.visibility = View.VISIBLE
                    recentList()

                }
            }
        }
    }

    private fun searchList(list: ArrayList<PojoSearchScreenData>) {
        binding.rvSearch.adapter = AdapterSearch(requireContext(), list, object : ClickListener {
            override fun onClickLisener(position: Int) {
                if(checkDeviceType())
                {
                    SearchResultFragment.newInstanceSearchResult("").showNow(requireActivity().supportFragmentManager,"SimpleDialog.TAG")
                }else
                {
                    if (findNavController().currentDestination?.id == R.id.searchFragment)
                        findNavController().navigate(R.id.action_searchFragment_to_searchResultFragment)
                }

            }
        })


    }

    private fun recentList() {
        val resentSearchList = ArrayList<PojoSearchScreenData>()
        resentSearchList.add(PojoSearchScreenData(1, null, null, "Dashboard designs", null,
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_replay_24)))
        resentSearchList.add(PojoSearchScreenData(0,
            null,
            null,
            "Omar Calzoni",
            "Frontend Engineer",
            ContextCompat.getDrawable(requireContext(), R.drawable.demo_photo)))
        resentSearchList.add(PojoSearchScreenData(1,
            null,
            null,
            "Meeting schedule.pdf",
            "0.4mb",
            ContextCompat.getDrawable(requireContext(), R.drawable.file), endViewLine = true))
        searchList(resentSearchList)
    }

    //For set the background Rectangle
    private fun setBackgroundRectMethod()
    {
        binding.apply {
            var rightMarginTopLayout = 0
            if (checkDeviceType()) {
                rightMarginTopLayout =
                    requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._18sdp).toInt()
                ivOutsideCloseSearch.visibility = View.VISIBLE
                imgCancelSearch.visibility = View.GONE
                layoutCreateSearch.setBackgroundResource(R.drawable.et_drawable)
            } else {
                layoutCreateSearch.setBackgroundResource(0)
                rightMarginTopLayout = 0
                ivOutsideCloseSearch.visibility = View.GONE
                imgCancelSearch.visibility = View.VISIBLE
            }

            // val ll = LinearLayout(this)
            //ll.setOrientation(LinearLayout.VERTICAL)

            //var layoutParams=binding.layoutCreateGroup.layoutParams

            val layoutParams = binding.layoutCreateSearch.layoutParams as LinearLayout.LayoutParams
            //  val newLayoutParams = toolbar.getLayoutParams()
            layoutParams.topMargin = 0
            layoutParams.leftMargin = 0
            layoutParams.rightMargin = rightMarginTopLayout
            binding.layoutCreateSearch.layoutParams = layoutParams
        }
    }

  /*  override fun onClickLisener(position: Int) {
        SearchFragment.newInstanceSearch("").showNow(requireActivity().supportFragmentManager,"SimpleDialog.TAG")

    }*/
}