package com.csi.irite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.csi.irite.adapter.AdapterCheckListMain
import com.csi.irite.helper.UiViewHelper
import com.csi.irite.room.database.AppDatabase
import com.csi.irite.widget.LineItemDecoration
import com.google.android.material.snackbar.Snackbar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [CheckListMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckListMainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var parent_view: View? = null
    var recyclerView: RecyclerView? = null
    var mAdapter: AdapterCheckListMain? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_checklist_main, container, false)
        parent_view = view.findViewById<View>(android.R.id.content)
        initComponent(view)
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChecklistSaveFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckListMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initComponent(view: View) {
        //recyclerView = view.findViewById<View>(R.id.checklistMainList) as RecyclerView?
        val linearLayout = view.findViewById<View>(R.id.climeScene) as LinearLayout?
        val uiv = UiViewHelper(requireContext(), view, childFragmentManager)

        recyclerView = uiv.createRecyclerView("check_list_tool")
        linearLayout?.addView(recyclerView)

        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        recyclerView!!.addItemDecoration(
            LineItemDecoration(
                activity,
                LinearLayout.VERTICAL
            )
        )
        recyclerView!!.setHasFixedSize(true)

        val db = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()

        /*val ChecklistMainDao = db.ChecklistMainDao()
        val items: List<ChecklistMain?>? = ChecklistMainDao.getAll()

        //set data and list adapter
        mAdapter = AdapterCheckListMain(activity, items)
        recyclerView!!.adapter = mAdapter

        // on item list clicked
        mAdapter!!.setOnItemClickListener(object : AdapterView.OnItemClickListener,
            AdapterCheckListMain.OnItemClickListener {
             override fun onItemClick(view: View?, obj: ChecklistMain, position: Int) {
                Snackbar.make(parent_view!!, "Item " + obj.date + " clicked", Snackbar.LENGTH_SHORT)
                    .show()
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                TODO("Not yet implemented")
            }

        })
*/
    }
}