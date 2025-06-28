package com.csi.irite.unuse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import com.csi.irite.BaseFragment
import com.csi.irite.R
import com.csi.irite.helper.PanelViewHelper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var nested_scroll_view: NestedScrollView? = null

/**
 * A simple [Fragment] subclass.
 * Use the [BomFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BomFormFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_bom_form, container, false)

        nested_scroll_view = view.findViewById<View>(R.id.nested_scroll_view) as NestedScrollView
        val pv = PanelViewHelper(requireContext(), nested_scroll_view!!)
        val linearLayoutTop = pv.createNestestChild()

        val cardview = pv.createPanel(R.layout.form_asset_event1,"1. การรับแจ้งเหตุ")
        val cardview2 = pv.createPanel(R.layout.form_common_place2,"2.สถานที่เกิดเหตุ")
        val cardview3 = pv.createPanel(R.layout.form_common_date3,"3. วันเวลาที่ทราบเหตุ/เกิดเหตุ")

        val cardview4 = pv.createPanel(R.layout.form_common_place4,"4. วันเวลาที่ตรวจเหตุ")
        val cardview5 = pv.createPanel(R.layout.form_fire_check5,"5.ผู้ตรวจสถานที่เกิดเหตุ")
        val cardview6 = pv.createPanel(R.layout.form_common_prop6,"6. ลักษณะสถานที่เกิดเหตุ")

        val cardview7 = pv.createPanel(R.layout.form_bom_result7,"7.ผลการตรวจสถานที่เกิดเหตุ")
        val cardview8 = pv.createPanel(R.layout.form_bom_sign8,"8.การส่งมอบคืนสถานที่เกิดเหตุ")

        linearLayoutTop.addView(cardview)
        linearLayoutTop.addView(cardview2)
        linearLayoutTop.addView(cardview3)

        linearLayoutTop.addView(cardview4)
        linearLayoutTop.addView(cardview5)
        linearLayoutTop.addView(cardview6)

        linearLayoutTop.addView(cardview7)
        linearLayoutTop.addView(cardview8)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BomFormFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BomFormFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}