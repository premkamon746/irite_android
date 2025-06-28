package com.csi.irite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.csi.irite.adapter.AdapterListExpand
import com.csi.irite.helper.PanelViewHelper
import com.csi.irite.helper.UiViewHelper
import com.csi.irite.room.database.AppDatabase
import com.csi.irite.widget.LineItemDecoration
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [CheckListsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CheckListsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var parent_view: View? = null
    var db : AppDatabase? = null
    //private var recyclerView: RecyclerView? = null
    //private var mAdapter: AdapterListExpand? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val activity = requireActivity() as MainActivity
        val toolbar = activity.getSupportActionBar()
        toolbar?.setTitle("Check List")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_check_lists, container, false)
        parent_view = view.findViewById<View>(android.R.id.content)
        initDB()
        initComponent(view)

        /*val nav_view = view.findViewById<View>(R.id.nav_view) as NavigationView
        nav_view.setNavigationItemSelectedListener { item ->
            //onItemNavigationClicked(item)
            true
        }*/

        return view
    }

    fun initDB(){
        db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckListsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    private fun initComponent(view: View) {

        val nested_scroll_view = view.findViewById<View>(R.id.checklist_scroll_view) as NestedScrollView

        val pv = PanelViewHelper(requireContext(),nested_scroll_view)
        val linearLayoutTop = pv.createNestestChild()

        val toolLayout = createRecycleviewData(view,"check_list_tool","1")
        val toolCardview = pv.createPanel(toolLayout,"แบบการตรวจสภาพเครื่องมือตรวจสถานที่เกิดเหตุ (Crime Scene Investigation Equipment Checklist)")
        linearLayoutTop.addView(toolCardview)

        val bagLayout = createRecycleviewData(view,"check_list_bag","2")
        val bagCardview = pv.createPanel(bagLayout,"แบบการตรวจกระเป๋าตรวจสถานที่เกิดเหตุทั่วไป (General Crime Scene Investigation Bag Checklist)")
        linearLayoutTop.addView(bagCardview)

        val camLayout = createRecycleviewData(view,"check_list_cam","3")
        val camCardview = pv.createPanel(camLayout,"แบบการตรวจสภาพกล้องถ่ายภาพแบบดิจิตอล (Digital Camera Checklist)")
        linearLayoutTop.addView(camCardview)

        val genLayout = createRecycleviewData(view,"check_list_gen","4")
        val genCardview = pv.createPanel(genLayout,"แบบการตรวจกระเป๋าตรวจสถานที่เกิดเหตุทั่วไป (General Crime Scene Investigation Bag Checklist)")
        linearLayoutTop.addView(genCardview)
    }

    private fun createRecycleviewData(view:View, _tag:String, groupFilter:String):RecyclerView{

        //val linearLayout = view.findViewById<View>(R.id.climeScene) as LinearLayout?
        val uiv = UiViewHelper(requireContext(), view, childFragmentManager)
        val recyclerView = uiv.createRecyclerView(_tag)//"check_list_tool"
        //linearLayout?.addView(recyclerView)

        //recyclerView = view.findViewById<View>(R.id.recyclerView) as RecyclerView?
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            LineItemDecoration(
                activity,
                LinearLayout.VERTICAL
            )
        )
        recyclerView.setHasFixedSize(true)

        /*val checklistDao = db?.checklistDao()
        val items: List<Checklist>? = checklistDao?.findGroup(groupFilter)

        val mAdapter = AdapterListExpand(activity, items)
        recyclerView.adapter = mAdapter

        // on item list clicked
        mAdapter.setOnItemClickListener(AdapterListExpand.OnItemClickListener(){ view: View, checklist: Checklist, i: Int ->
            Snackbar.make(view, "Item  clicked", Snackbar.LENGTH_SHORT).show()
        })
        mAdapter.setOnItemClickListener(object : AdapterView.OnItemClickListener,
            AdapterListExpand.OnItemClickListener {
                override fun onItemClick(view: View?, obj: Checklist, position: Int) {
                    Snackbar.make(parent_view!!, "Item " + obj.head + " clicked", Snackbar.LENGTH_SHORT)
                        .show()
                }

                override fun onItemClick(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    TODO("Not yet implemented")
                    Snackbar.make(parent_view!!, "Item " + position.toString() + " clicked", Snackbar.LENGTH_SHORT)
                        .show()/**/
                }

            })*/

        return recyclerView
    }

}