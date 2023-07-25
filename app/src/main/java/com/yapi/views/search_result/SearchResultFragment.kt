package com.yapi.views.search_result

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.yapi.R
import com.yapi.common.checkDeviceType
import com.yapi.common.hideKeyboard
import com.yapi.pref.PreferenceFile
import com.yapi.views.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultFragment : DialogFragment() {
    private lateinit var binding:com.yapi.databinding.FragmentSearchResultBinding
    private lateinit var adapterr:AdapterTabSearchResult
    private lateinit var tabList: ArrayList<PojoTabSearchResult>

    @Inject
    lateinit var preferenceFile: PreferenceFile

     val mViewModel:SearchResultViewModel by viewModels()

    companion object {
        fun newInstanceSearchResult(title: String): SearchResultFragment {
            val args = Bundle()
            args.putString("11", title)
            val fragment = SearchResultFragment()
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding= com.yapi.databinding.FragmentSearchResultBinding.inflate(LayoutInflater.from(requireActivity()))
        binding.mViewModel=mViewModel

        if(checkDeviceType())
        {
            mViewModel.cancelshowField.set(false)
        }else
        {
            mViewModel.cancelshowField.set(true)
        }

        dismissDialogScreenObserver()

        return binding.root
    }

    //For dismiss Dialog Fragment obsever
    fun dismissDialogScreenObserver()
    {
        mViewModel.dissmissDialogPopupData.observe(requireActivity(), Observer {
            var data=it as Boolean
            if(data)
            {
                dismiss()
            }
        })
    }
    fun setTouchListenereForNested()
    {
        binding.nestedScrollViewSearchResult.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                requireActivity().hideKeyboard()
                return false
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setBackgroundRectMethod()
        binding.apply {
            setTouchListenereForNested()
            txtTempResultSearch.setOnClickListener {
                findNavController().popBackStack()
            }
         /*   imgCancelSearch.setOnClickListener {
                findNavController().popBackStack()
            }
            imgCancelTabSearchResult.setOnClickListener {
                findNavController().popBackStack()
            }*/
            val check=arguments?.getBoolean("empty")?:false
            if(check==true){
                setTabLayout(true)
                rvSearchResult.visibility = View.GONE
                layoutNoDataSearchResult.visibility = View.VISIBLE
            }else {
                setTabLayout(false)
//                viewLineAllSearchResult.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.blueColor))
                layoutNoDataSearchResult.visibility = View.GONE
                rvSearchResult.visibility = View.VISIBLE
                val resentSearchList = ArrayList<PojoSearchScreenData>()
                resentSearchList.add(PojoSearchScreenData(3,
                    "Messeges",
                    "VIEW MESSAGES",
                    "Kaylynn",
                    null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.demo_photo),
                    "Human Resource",
                    "JANUARY 13, 2023",
                    "13:25",
                    "Consulting a Latin dictionary Job Page Design to a passage from Do Finibus Bonorum"
                ))
                resentSearchList.add(PojoSearchScreenData(3,
                    "Messeges",
                    "VIEW MESSAGES",
                    "Zaire",
                    null,
                    ContextCompat.getDrawable(requireContext(), R.drawable.demo_photo),
                    "DesignGig",
                    "JANUARY 05, 2023",
                    "01:05",
                    "Documentation is very important task. It is needed to be finished by this week."))

                resentSearchList.add(PojoSearchScreenData(1,
                    "Files",
                    "VIEW FILES",
                    "Documentation.pdf",
                    "0.4mb",
                    ContextCompat.getDrawable(requireContext(), R.drawable.file)))
                resentSearchList.add(PojoSearchScreenData(1,
                    "Files",
                    "VIEW FILES",
                    "Document_drafter.pdf",
                    "1.5mb",
                    ContextCompat.getDrawable(requireContext(), R.drawable.file)))
                searchList(resentSearchList)
                resentSearchList.add(PojoSearchScreenData(1,
                    "People",
                    "VIEW ALL",
                    "Donald Sutherland",
                    "Frontend Engineer",
                    ContextCompat.getDrawable(requireContext(), R.drawable.file)))
                resentSearchList.add(PojoSearchScreenData(1,
                    "People",
                    "VIEW ALL",
                    "Michelle Dochery",
                    "Customer",
                    ContextCompat.getDrawable(requireContext(), R.drawable.file)))
                searchList(resentSearchList)

            }   }
    }
    private fun searchList(list:ArrayList<PojoSearchScreenData>) {
        binding.rvSearchResult.adapter = AdapterSearch(requireContext(), list,
            object : ClickListener {
                override fun onClickLisener(position: Int) {

                }
            })

    }
    private fun setTabLayout(empty:Boolean) {
        tabList = ArrayList<PojoTabSearchResult>()
        if (empty) {
            tabList.add(PojoTabSearchResult("All", 0, true))
            tabList.add(PojoTabSearchResult("Messages", 0, false))
            tabList.add(PojoTabSearchResult("Files", 0, false))
            tabList.add(PojoTabSearchResult("People", 0, false))
            tabList.add(PojoTabSearchResult("Groups", 0, false))

        } else {
            tabList.add(PojoTabSearchResult("All", 12, true))
            tabList.add(PojoTabSearchResult("Messages", 2, false))
            tabList.add(PojoTabSearchResult("Files", 0, false))
            tabList.add(PojoTabSearchResult("People", 4, false))
            tabList.add(PojoTabSearchResult("Groups", 45, false))
            tabList.add(PojoTabSearchResult("Jobs", 0, false))
            tabList.add(PojoTabSearchResult("Jobs Type", 0, false))

        }
        adapterr = AdapterTabSearchResult(requireContext(),
            tabList,
            object : AdapterTabSearchResult.Click {
                override fun onClick(position: Int) {

                        for (i in 0 until tabList.size) {
                            if (i == position) {
                                tabList[i].selected = true
                            } else {
                                tabList[i].selected = false
                            }
                        }
                        adapterr.changeList(tabList)
                    }

            })
        binding.rvTabLayoutSearchResult.adapter = adapterr

        val arrayPeople= arrayListOf<String>("ALL","TEAM","CUSTOMERS","LEADS")
        val adpater=AdapterSpinnerSimple(requireContext(),arrayPeople)
        binding.spinnerAllSearchResult.adapter=adpater
//        val ad = ArrayAdapter(
//            requireActivity(), R.layout.custom_spinner_item,
//            arrayPeople)
//        adpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        binding.spinnerAllSearchResult.adapter=ad

        binding.spinnerAllSearchResult.onItemSelectedListener  = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val viewLine=view?.findViewById<View>(R.id.viewSpinnerSimple)
                viewLine?.visibility=View.GONE
//                Toast.makeText(requireContext(), "Clicked on ${arrayPeople[position]}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }


    //For set the background Rectangle
    private fun setBackgroundRectMethod()
    {
        binding.apply {
            var rightMarginTopLayout = 0
            if (checkDeviceType()) {
                rightMarginTopLayout =
                    requireActivity().resources.getDimension(com.intuit.sdp.R.dimen._18sdp).toInt()
                ivOutsideCloseSearchResult.visibility = View.VISIBLE
                imgCancelSearch.visibility = View.GONE
                layoutCreateSearch.setBackgroundResource(R.drawable.et_drawable)
            } else {
                layoutCreateSearch.setBackgroundResource(0)
                rightMarginTopLayout = 0
                ivOutsideCloseSearchResult.visibility = View.GONE
                imgCancelSearch.visibility = View.VISIBLE
            }

            val layoutParams = binding.layoutCreateSearch.layoutParams as LinearLayout.LayoutParams
            //  val newLayoutParams = toolbar.getLayoutParams()
            layoutParams.topMargin = 0
            layoutParams.leftMargin = 0
            layoutParams.rightMargin = rightMarginTopLayout
            binding.layoutCreateSearch.layoutParams = layoutParams
        }
    }
}