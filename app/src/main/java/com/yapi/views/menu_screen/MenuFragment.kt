package com.yapi.views.menu_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.yapi.R
import com.yapi.common.*
import com.yapi.databinding.FragmentMenuBinding
import com.yapi.pref.PreferenceFile
import com.yapi.views.profile.ProfileFragment
import com.yapi.views.search.SearchFragment
import com.yapi.views.signup_code.LoginUserData
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


@AndroidEntryPoint
class MenuFragment : Fragment() {
    private var rowHeight: Int? = 0
    private lateinit var binding: FragmentMenuBinding
    private var groupListClicked = true
    private var jobListClicked = true
    private var customerListClicked = true
    private var conversationListClicked = true
    private var teamListClicked = true
    private var leadListClicked = true
    private var settingListClicked = true
    private var adapterGroupsList: AdapterGroupsList? = null
    private var adapterJobsList: AdapterGroupsList? = null
    private var adapterCustomerList: AdapterCustomerList? = null
    private var adapterConversationList: AdapterCustomerList? = null
    private var adapterTeamList: AdapterCustomerList? = null
    private var adapterLeadsList: AdapterCustomerList? = null
    private var adapterSettingsList: AdapterSettingList? = null

    @Inject
    lateinit var preferenceFile: PreferenceFile

    val viewModel: MenuViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMenuBinding.inflate(LayoutInflater.from(requireActivity()))
        binding.mViewmodel = viewModel
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        viewModel.screenWidth = width

        Log.e("mflfldddff22==", preferenceFile.fetchStringValue(Constants.LOGIN_USER_ID))
        Log.e("mflfldddff555==", preferenceFile.fetchStringValue(Constants.USER_TOKEN))
        Log.e("mflfldddff8888==", NumberToWordsConverter.convert(47))

        if (Constants.API_CALL_DEMO) {
            viewModel.fetchGroupDataMethod().observe(requireActivity(), Observer {
                var data = it as AllData
                if (data != null) {
                    setGroupListAdapter(data.groups)
                }
            })
        }else{
            val list = ArrayList<GroupData>()
            val list2 = ArrayList<GroupInvitaion>()
           /* list.add(GroupData(1,
                "-1",
                "",
                "",
                "",
                "",
                "",
                list2,
                false,
                requireActivity().getString(R.string.createGroups_text),
                false,
                "",
                "",
                "",
                "",
                false))*/
            setGroupListAdapter(list)
        }
        /* val vto: ViewTreeObserver = binding.constraintsTop!!.getViewTreeObserver()
         vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                 if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                     binding.constraintsTop!!.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                 } else {
                     binding.constraintsTop!!.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                 }
                // val width: Int = binding.constraintsTop!!.getMeasuredWidth()
                 rowHeight = binding.constraintsTop!!.getMeasuredHeight()

             }
         })*/

      /*  var user_all_data = preferenceFile.fetchStringValue(Constants.USER_ALL_DATA)
        var loginUserData = Gson().fromJson(user_all_data, LoginUserData::class.java)*/
        // Log.e("efwekfefweff===",lstObject.toString())
        var loginUserData =preferenceFile.fetchUserData()
        viewModel.loginUserData = loginUserData

        if(loginUserData.profile_pic_url!="")
        {
            viewModel.noImageOnlyNameVisible.set(false)
            viewModel.userPhotoUrl.set(loginUserData.profile_pic_url)
        }else
        {
            viewModel.showTopNameTag.set(convertFromFullNameToTwoString(loginUserData.name!!))
            viewModel.noImageOnlyNameVisible.set(true)
        }

        addNextToScreenObserver()
        init()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("gmwslgwrgwrg===", "grwegrweg")
    }

    //for UI Intialization
    private fun init() {
        setGroupDataMethod(0)
        setJobDataMethod(0)
        setCusomerListMethod(0)
        setConversationMethod(0)
        setTeamMethod(0)
        setLeadMethod(0)
        setSettingMethod(0)

        binding.apply {
            layoutAddNewGroupsMenu.clipToOutline = true
            layoutAddNewCustomersMenu.clipToOutline = true
        }
        clickListener()
    }

    private fun clickListener() {

        binding.apply {
            imgTempDrawableMenu.setOnClickListener {
                lifecycleScope.launchWhenResumed {
                    if (findNavController().currentDestination?.id == R.id.menuFragment) {
                        // findNavController().popBackStack()
                    }
                }
            }
            layoutGroupsMenu.setOnClickListener {
                setGroupDataMethod(1)
            }
            layoutJobsMenu.setOnClickListener {
                setJobDataMethod(1)
            }
            layoutCustomersMenu.setOnClickListener {
                setCusomerListMethod(1)
            }
            layoutConversationMenu.setOnClickListener {
                setConversationMethod(1)
            }
            layoutTeamMenu.setOnClickListener {
                setTeamMethod(1)
            }
            layoutLeadMenu.setOnClickListener {
                setLeadMethod(1)
            }
            layoutSettingsMenu.setOnClickListener {
                setSettingMethod(1)
            }
            etSearchMenu.setOnClickListener {
                //  Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
            }
            layoutBoatMenu.setOnClickListener {
                //  Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
            }
            layoutAddNewCustomersMenu.setOnClickListener {
                //   Toast.makeText(requireContext(), "Add new member", Toast.LENGTH_SHORT).show()
            }
            layoutBookMarkMenu.setOnClickListener {
                if (checkDeviceType()) {
                    EventBus.getDefault()
                        .post(MyMessageEvent(15, Constants.SAVED_ITEMS_KEY)) //post event
                } else {
                    findNavController().navigate(R.id.action_menuFragment_to_savedItemsFragment)
                }
            }
        }
    }

    private fun setSettingMethod(type: Int) {
        if (type > 0) {
            settingListClicked = !settingListClicked
        }
        if (settingListClicked) {
            binding.imgArrowSettingsMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_up_24
                )
            )
            binding.rvSettingsListMenu.visibility = View.VISIBLE
            setSettingListAdapter()
        } else {
            binding.imgArrowSettingsMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
            )
            binding.rvSettingsListMenu.visibility = View.GONE
        }
    }

    private fun setLeadMethod(type: Int) {
        if (type > 0) {
            leadListClicked = !leadListClicked
        }
        if (leadListClicked) {
            binding.imgArrowLeadMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_up_24
                )
            )
            binding.rvLeadsListMenu.visibility = View.VISIBLE
            rvLeadsListMenu()
        } else {
            binding.imgArrowLeadMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
            )
            binding.rvLeadsListMenu.visibility = View.GONE
        }
    }

    private fun setTeamMethod(type: Int) {
        if (type > 0) {
            teamListClicked = !teamListClicked
        }
        if (teamListClicked) {
            binding.imgArrowTeamMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_up_24
                )
            )
            binding.rvTeamsListMenu.visibility = View.VISIBLE
            setTeamListAdapter()
        } else {
            binding.imgArrowTeamMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
            )
            binding.rvTeamsListMenu.visibility = View.GONE
        }
    }

    private fun setConversationMethod(type: Int) {
        if (type > 0) {
            conversationListClicked = !conversationListClicked
        }
        if (conversationListClicked) {
            binding.imgArrowConversationMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_up_24
                )
            )
            binding.rvConversationListMenu.visibility = View.VISIBLE
            setConversationListAdapter()
        } else {
            binding.imgArrowConversationMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
            )
            binding.rvConversationListMenu.visibility = View.GONE
        }
    }

    private fun setCusomerListMethod(type: Int) {
        if (type > 0) {
            customerListClicked = !customerListClicked
        }
        if (customerListClicked) {
            binding.imgArrowCustomersMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_up_24
                )
            )
            binding.rvCustomersListMenu.visibility = View.VISIBLE
            setCustomerListAdapter()
        } else {
            binding.imgArrowCustomersMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
            )
            binding.rvCustomersListMenu.visibility = View.GONE
        }
    }

    private fun setJobDataMethod(type: Int) {
        if (type > 0) {
            jobListClicked = !jobListClicked
        }
        if (jobListClicked) {
            binding.imgArrowJobsMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_up_24
                )
            )
            binding.rvJobsListMenu.visibility = View.VISIBLE
            //    setJobsListAdapter()
        } else {
            binding.imgArrowJobsMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
            )
            binding.rvJobsListMenu.visibility = View.GONE
        }
    }

    private fun setGroupDataMethod(type: Int) {
        if (type > 0) {
            groupListClicked = !groupListClicked
        }
        if (groupListClicked) {
            binding.imgArrowGroupMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_up_24
                )
            )
            binding.rvGroupListMenu.visibility = View.VISIBLE


        } else {
            binding.imgArrowGroupMenu.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_baseline_keyboard_arrow_right_24
                )
            )
            binding.rvGroupListMenu.visibility = View.GONE
        }
    }

    private fun setSettingListAdapter() {
        val list = ArrayList<PojoSettingList>()
        list.add(PojoSettingList("User Management", false))
        list.add(PojoSettingList("Workspaces Management", false))
        val fixImageHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._45sdp).toInt()
        val rvHeight = fixImageHeight * list.size
        binding.rvSettingsListMenu.layoutParams.height = rvHeight
        adapterSettingsList =
            AdapterSettingList(requireContext(), list, object : AdapterSettingList.Click {
                @SuppressLint("NotifyDataSetChanged")
                override fun onSelected(position: Int) {
                    for (i in 0 until adapterSettingsList?.getListt()?.size!!) {
                        adapterSettingsList?.getListt()?.get(i)?.selectedStatus = position == i
                    }
                    adapterSettingsList?.notifyDataSetChanged()

                    if (position == 0) {
                        if (checkDeviceType()) {
                            EventBus.getDefault()
                                .post(MyMessageEvent(2, Constants.USER_MANAGEMENT)) //post event
                        } else {
                            if (findNavController().currentDestination?.id == R.id.menuFragment)
                                findNavController().navigate(R.id.action_menuFragment_to_userListFragment)
                        }
                    } else {
                        if (checkDeviceType()) {
                            EventBus.getDefault()
                                .post(MyMessageEvent(11,
                                    Constants.WORKSPACE_MANAGEMENT)) //post event
                        } else {
                            if (findNavController().currentDestination?.id == R.id.menuFragment) {
                                findNavController().navigate(R.id.action_menuFragment_to_workspacelist)
                            }
                        }
                    }
                }
            })
        binding.rvSettingsListMenu.adapter = adapterSettingsList
    }

    private fun setCustomerListAdapter() {
        val list = ArrayList<PojoCustomerList>()
        val tempOnlineList = listOf(true, false)
        /* for (i in 0 until 5) {
             list.add(PojoCustomerList("ab", "Customer${i + 34}", tempOnlineList.random(), i))
         }*/

        list.add(PojoCustomerList("ab", "Omar Press", tempOnlineList.random(), 1))
        list.add(PojoCustomerList("ab", "Adison Septim...", tempOnlineList.random(), 1))
        list.add(PojoCustomerList("ab", "Talan George", tempOnlineList.random(), 0))
        list.add(PojoCustomerList("ab", "Madelyn Levin", tempOnlineList.random(), 1))
        list.add(PojoCustomerList("ab", "Zaire Stanton", tempOnlineList.random(), 0))
        list.add(PojoCustomerList("ab",
            requireActivity().getString(R.string.add_customers),
            tempOnlineList.random(),
            -1))

        val fixImageHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._45sdp).toInt()
        val rvHeight = fixImageHeight * list.size
        binding.rvCustomersListMenu.layoutParams.height = rvHeight

        adapterCustomerList =
            AdapterCustomerList(requireContext(), list, object : AdapterCustomerList.Click {
                @SuppressLint("NotifyDataSetChanged")
                override fun onSeletect(position: Int, userType: String) {
                    for (i in 0 until adapterCustomerList?.getListt()?.size!!) {
                        adapterCustomerList?.getListt()?.get(i)?.selectedStatus = position == i
                    }
                    adapterCustomerList?.notifyDataSetChanged()
                    if (findNavController().currentDestination?.id == R.id.menuFragment) {
                        var bundle = Bundle()
                        bundle.putString("userType", userType)
                        findNavController().navigate(R.id.action_menuFragment_to_chatMessageFragment,
                            bundle)
                    }
                }
            }, Constants.CUSTOMERS_KEY)
        binding.rvCustomersListMenu.adapter = adapterCustomerList
    }

    private fun setConversationListAdapter() {
        val list = ArrayList<PojoCustomerList>()
        val tempOnlineList = listOf(true, false)
        /* for (i in 0 until 5) {
             list.add(PojoCustomerList("ab", "Customer${i + 34}", tempOnlineList.random(), i))
         }*/

        list.add(PojoCustomerList("ab", "Daman", tempOnlineList.random(), 1))
        list.add(PojoCustomerList("ab", "Sahil sir", tempOnlineList.random(), 1))
        list.add(PojoCustomerList("ab", "Amit sir", tempOnlineList.random(), 0))
        list.add(PojoCustomerList("ab", "Arundeep sir", tempOnlineList.random(), 1))
        list.add(PojoCustomerList("ab", "Khem sir", tempOnlineList.random(), 0))
        list.add(PojoCustomerList("ab",
            requireActivity().getString(R.string.add_conversation),
            tempOnlineList.random(),
            -1))

        val fixImageHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._45sdp).toInt()
        val rvHeight = fixImageHeight * list.size
        binding.rvConversationListMenu.layoutParams.height = rvHeight

        adapterConversationList =
            AdapterCustomerList(requireContext(), list, object : AdapterCustomerList.Click {
                @SuppressLint("NotifyDataSetChanged")
                override fun onSeletect(position: Int, userType: String) {
                    for (i in 0 until adapterConversationList?.getListt()?.size!!) {
                        adapterConversationList?.getListt()?.get(i)?.selectedStatus = position == i
                    }
                    adapterConversationList?.notifyDataSetChanged()
                    if (findNavController().currentDestination?.id == R.id.menuFragment) {
                        var bundle = Bundle()
                        bundle.putString("userType", userType)
                        findNavController().navigate(R.id.action_menuFragment_to_chatMessageFragment,
                            bundle)
                    }
                }
            }, Constants.CONVERSATIONS_KEY)
        binding.rvConversationListMenu.adapter = adapterConversationList
    }

    private fun rvLeadsListMenu() {
        val list = ArrayList<PojoCustomerList>()
        val tempOnlineList = listOf(true, false)
        /* for (i in 0 until 5) {
             list.add(PojoCustomerList("ab", "Customer${i + 34}", tempOnlineList.random(), i))
         }*/

        list.add(PojoCustomerList("ab", "Bardi", tempOnlineList.random(), 1))
        list.add(PojoCustomerList("ab", "Lincoln Rosser", tempOnlineList.random(), 2))
        list.add(PojoCustomerList("ab", "Jakob Vetrovs", tempOnlineList.random(), 2))
        list.add(PojoCustomerList("ab", "John C Flood", tempOnlineList.random(), 0))
        list.add(PojoCustomerList("ab",
            requireActivity().getString(R.string.add_lead),
            tempOnlineList.random(),
            -1))

        val fixImageHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._45sdp).toInt()
        val rvHeight = fixImageHeight * list.size
        binding.rvLeadsListMenu.layoutParams.height = rvHeight

        adapterLeadsList =
            AdapterCustomerList(requireContext(), list, object : AdapterCustomerList.Click {
                @SuppressLint("NotifyDataSetChanged")
                override fun onSeletect(position: Int, userType: String) {
                    for (i in 0 until adapterLeadsList?.getListt()?.size!!) {
                        adapterLeadsList?.getListt()?.get(i)?.selectedStatus = position == i
                    }
                    adapterLeadsList?.notifyDataSetChanged()

                    if (findNavController().currentDestination?.id == R.id.menuFragment) {
                        var bundle = Bundle()
                        bundle.putString("userType", userType)
                        findNavController().navigate(R.id.action_menuFragment_to_chatMessageFragment,
                            bundle)
                    }
                }
            }, Constants.LEADS_KEY)
        binding.rvLeadsListMenu.adapter = adapterLeadsList
    }

    private fun setTeamListAdapter() {
        val list = ArrayList<PojoCustomerList>()
        val tempOnlineList = listOf(true, false)
        /*    for (i in 0 until 5) {
                list.add(PojoCustomerList("ab", "Customer${i + 34}", tempOnlineList.random(), i))
            }*/

        list.add(PojoCustomerList("ab", "Omar Press", tempOnlineList.random(), 1))
        list.add(PojoCustomerList("ab", "Adison Septim...", tempOnlineList.random(), 2))
        list.add(PojoCustomerList("ab", "Talan George", tempOnlineList.random(), 1))
        list.add(PojoCustomerList("ab", "Madelyn Levin", tempOnlineList.random(), 0))
        list.add(PojoCustomerList("ab", "Zaire Stanton", tempOnlineList.random(), 0))
        list.add(PojoCustomerList("ab",
            requireActivity().getString(R.string.add_teammates),
            tempOnlineList.random(),
            -1))

        val fixImageHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._45sdp).toInt()
        val rvHeight = fixImageHeight * list.size
        binding.rvTeamsListMenu.layoutParams.height = rvHeight

        adapterTeamList =
            AdapterCustomerList(requireContext(), list, object : AdapterCustomerList.Click {
                @SuppressLint("NotifyDataSetChanged")
                override fun onSeletect(position: Int, userType: String) {
                    for (i in 0 until adapterTeamList?.getListt()?.size!!) {
                        adapterTeamList?.getListt()?.get(i)?.selectedStatus = position == i
                    }
                    adapterTeamList?.notifyDataSetChanged()

                    if (findNavController().currentDestination?.id == R.id.menuFragment) {
                        var bundle = Bundle()
                        bundle.putString("userType", userType)
                        findNavController().navigate(R.id.action_menuFragment_to_chatMessageFragment,
                            bundle)
                    }
                }
            }, "")
        binding.rvTeamsListMenu.adapter = adapterTeamList
    }

    private fun setGroupListAdapter(groupsList: ArrayList<GroupData>) {
        val list = ArrayList<PojoGroupMembersList>()
        val list2 = ArrayList<GroupInvitaion>()
        var newGroupsList = ArrayList<GroupData>()
        newGroupsList.clear()
        if (groupsList.size > 0) {
            for (idx in 0 until groupsList.size) {
                groupsList[idx].selected = false
                newGroupsList.add(groupsList[idx])
            }
        }
        newGroupsList.add(GroupData(-1,
            "-1",
            "",
            "",
            "",
            "",
            "",
            list2,
            false,
            requireActivity().getString(R.string.createGroups_text),
            false,
            "",
            "",
            "",
            "",
            false))
        /*   if(groupsList.size>0)
           {
               for(idx in 0 until groupsList.size)
               {
                   list.add(PojoGroupMembersList(groupsList[idx].name,1,false))
               }
               list.add(PojoGroupMembersList(requireActivity().getString(R.string.createGroups_text),
                   -1,
                   false))
           }else {
               list.add(PojoGroupMembersList("Sales", 1, false))
               list.add(PojoGroupMembersList("Human Resource", 2, false))
               list.add(PojoGroupMembersList("Operations", 1, false))
               list.add(PojoGroupMembersList("Reports", 2, false))
               list.add(PojoGroupMembersList("Engineers", 2, false))
               list.add(PojoGroupMembersList(requireActivity().getString(R.string.createGroups_text),
                   -1,
                   false))
           }*/
        val fixImageHeight =
            requireContext().resources.getDimension(com.intuit.sdp.R.dimen._37sdp).toInt()
        val rvHeight = fixImageHeight * newGroupsList.size
        binding.rvGroupListMenu.layoutParams.height = rvHeight
        Log.e("fknkwefwsfwfwaf===", rowHeight.toString())
        adapterGroupsList =
            AdapterGroupsList(requireContext(),
                true,
                newGroupsList,
                object : AdapterGroupsList.Click {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onSeletect(position: Int, userType: String) {
                        if (adapterGroupsList?.getListt()?.get(position)!!.__v == -1) {
                            if (checkDeviceType()) {
                                System.out.println("phone========tablet")
                                EventBus.getDefault()
                                    .post(GroupEvent(2, Constants.CREATEGOUP_KEY)) //post event
                            } else {
                                findNavController().navigate(R.id.action_menuFragment_to_createGroupFragment)
                            }
                        } else {
                            for (i in 0 until adapterGroupsList?.getListt()?.size!!) {
                                adapterGroupsList?.getListt()?.get(i)?.selected = position == i
                            }
                            Log.i("asdfjanskdf",
                                "before notifiy list is\n${adapterGroupsList?.getListt()}")
                            adapterGroupsList?.notifyDataSetChanged()

                            if (checkDeviceType()) {
                                System.out.println("phone========tablet")
                                EventBus.getDefault()
                                    .post(MyMessageEvent(3,
                                        Constants.CHAT_MESSAGE_KEY,
                                        adapterGroupsList!!.getListt()[position])) //post event
                            } else {
                                if (findNavController().currentDestination?.id == R.id.menuFragment) {
                                    var bundle = Bundle()
                                    bundle.putString("userType", userType)
                                    bundle.putSerializable("group_data",
                                        adapterGroupsList!!.getListt()[position])
                                    findNavController()
                                        .navigate(R.id.action_menuFragment_to_chatMessageFragment,
                                            bundle)
                                }
                            }
                        }
                    }
                },
                Constants.GROUPS_KEY)
        binding.rvGroupListMenu.adapter = adapterGroupsList
    }

    /* private fun setJobsListAdapter() {
         val list = ArrayList<PojoGroupMembersList>()
         list.add(PojoGroupMembersList("Discount_SH1h73", 1, false))
         list.add(PojoGroupMembersList("Palosi_39875", 2, false))
         list.add(PojoGroupMembersList("Skiffington_h90", 1, false))
         // list.add(PojoGroupMembersList(requireActivity().getString(R.string.createGroups_text), -2, false))

         val fixImageHeight =
             requireContext().resources.getDimension(com.intuit.sdp.R.dimen._37sdp).toInt()
         val rvHeight = fixImageHeight * list.size
         binding.rvJobsListMenu.layoutParams.height = rvHeight

 //        val adapter = AdapterJobsList(requireContext(), list)
 //        binding.rvJobsListMenu.adapter = adapter

         adapterJobsList =
             AdapterGroupsList(requireContext(), false, list, object : AdapterGroupsList.Click {
                 @SuppressLint("NotifyDataSetChanged")
                 override fun onSeletect(position: Int, userType: String) {
                     for (i in 0 until adapterJobsList?.getListt()?.size!!) {
                         adapterJobsList?.getListt()?.get(i)?.selected = position == i
                     }
                     Log.i("asdfjanskdf", "before notifiy list is\n${adapterJobsList?.getListt()}")
                     adapterJobsList?.notifyDataSetChanged()

                     if (findNavController().currentDestination?.id == R.id.menuFragment) {
                         var bundle = Bundle()
                         bundle.putString("userType", userType)
                         findNavController().navigate(R.id.action_menuFragment_to_chatMessageFragment,
                             bundle)
                     }
                 }
             }, "")
         binding.rvJobsListMenu.adapter = adapterJobsList
     }*/

    private fun addNextToScreenObserver() {

        viewModel.openProfileScreenData.observe(requireActivity(), Observer {
            var data = it as Boolean
            if (data) {
                ProfileFragment.newInstanceProfileScreen("")
                    .showNow(requireActivity().supportFragmentManager, "")
            }
        })


        viewModel.openSearchScreenData.observe(requireActivity(), Observer {
            var data = it as Boolean
            if (data) {
                SearchFragment.newInstanceSearch("")
                    .showNow(requireActivity().supportFragmentManager, "SimpleDialog.TAG")
            }
        })
    }
}