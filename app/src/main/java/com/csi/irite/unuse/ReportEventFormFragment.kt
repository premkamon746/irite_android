package com.csi.irite.unuse

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.csi.irite.BaseFragment
import com.csi.irite.R
import com.csi.irite.helper.EventReportHelper
import com.csi.irite.room.dao.EventReportDao
import com.csi.irite.room.data.EventReport


/**
 * A simple [Fragment] subclass.
 * Use the [ReportEventFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportEventFormFragment : BaseFragment() {
    // TODO: Rename and change types of parameters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_report_event_form, container, false)
        val eventReportHelper = EventReportHelper(requireContext(),childFragmentManager, parentFragmentManager)

        if(this.uid > 0){
            val eventReportDao: EventReportDao = this.db!!.eventReportDao()
            val eventReports:List<EventReport> = eventReportDao.getById(uid)
            if (eventReports.count() > 0) {
                val eventReport = eventReports.get(0)
                eventReportHelper.createForm(view, eventReport, R.id.mainReportEvent)
            }
        }else{
            eventReportHelper.createForm(view, EventReport(), R.id.mainReportEvent)
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportEventFormFragment().apply {
            }
    }
}