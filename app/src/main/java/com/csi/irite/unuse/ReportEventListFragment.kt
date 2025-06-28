package com.csi.irite.unuse

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csi.irite.BaseFragment
import com.csi.irite.R
import com.csi.irite.helper.EventReportHelper
import com.csi.irite.room.dao.EventReportDao

/**
 * A fragment representing a list of Items.
 */
class EventReportListFragment : BaseFragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_evr_item_list, container, false)
        val recycleView = view.findViewById<RecyclerView>(R.id.event_report_list)

        val eventReportHelper = EventReportHelper(requireContext(),childFragmentManager, parentFragmentManager)

        val eventReportDao: EventReportDao = db!!.eventReportDao()
        val allEvent = eventReportDao.getAll()

        // Set the adapter
        if (recycleView is RecyclerView) {
            with(recycleView) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                val adapter = MyEventReportRecyclerViewAdapter(allEvent, parentFragmentManager)
                recycleView.adapter = adapter
                /*adapter.setOnItemClickListener(object : MyEventReportRecyclerViewAdapter.OnItemClickListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onItemClick(position: Int, item: EventReport) {
                       //Toast.makeText(context, "Item clicked: $item", Toast.LENGTH_SHORT).show()
                        //eventReportHelper.retrieveForm(item, true)
                        val type = item.report_type
                        Log.d("xxxxxxxxxxx", type.toString())
                        when (type) {
                            "ทรัพย์" -> loadFragment(AssetFormFragment(),item.uid)
                            "ชีวิต" -> loadFragment(LifeFormFragment(),item.uid)
                            "ระเบิด" -> loadFragment(BomFormFragment(),item.uid)
                            "เพลิงไหม้" -> loadFragment(FireFormFragment(),item.uid)
                        }
                    }
                })*/
            }
        }
        return view
    }

}

