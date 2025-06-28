package com.csi.irite.unuse

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.csi.irite.BaseFragment
import com.csi.irite.R


class TransferEvidenceFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transfer_evidence, container, false)
        /*val eventReportHelper = TransferEvidenceHelper(requireContext(),childFragmentManager, parentFragmentManager)

        if(this.uid > 0){
            val eventReportDao: EventReportDao = this.db!!.eventReportDao()
            val eventReports:List<EventReport> = eventReportDao.getById(uid)
            if (eventReports.count() > 0) {
                val eventReport = eventReports.get(0)
                eventReportHelper.createForm(view, eventReport, R.id.mainTranferEvidence)
            }

        }*/
        return view
    }


}