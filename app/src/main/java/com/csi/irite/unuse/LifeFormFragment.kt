package com.csi.irite.unuse

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import com.csi.irite.BaseFragment
import com.csi.irite.R
import com.csi.irite.helper.PanelViewHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LifeFormFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LifeFormFragment : BaseFragment() {

    var nested_scroll_view: NestedScrollView? = null
    private val callback = OnMapReadyCallback { googleMap ->
        val zoomLevel = 12.0f // Desired zoom level
        //googleMap.moveCamera(CameraUpdateFactory.zoomTo(5.0f))

        val yala = LatLng(6.2006253477222435, 101.26524185416744)
        googleMap.addMarker(MarkerOptions().position(yala).title("Marker in Yala"))
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(yala))

        val cameraPosition = CameraPosition.Builder()
            .target(yala) // Set center
            .zoom(zoomLevel) // Set zoom level
            .build()

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_life_form, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        // input section

        // input section
        //bt_toggle_input = view.findViewById<View>(R.id.bt_toggle_input) as ImageButton
        //lyt_expand_input = view.findViewById<View>(R.id.lyt_expand_input) as View
        //lyt_expand_input!!.setVisibility(View.GONE)

        // nested scrollview
        nested_scroll_view = view.findViewById<View>(R.id.nested_scroll_view) as NestedScrollView

        //bt_toggle_input!!.setOnClickListener { toggleSectionInput(bt_toggle_input!!) }

        val pv = PanelViewHelper(requireContext(),nested_scroll_view!!)
        val linearLayoutTop = pv.createNestestChild()

        val cardview = pv.createPanel(R.layout.form_asset_event1,"1. การรับแจ้งเหตุ")
        val cardview2 = pv.createPanel(R.layout.form_common_place2,"2.สถานที่เกิดเหตุ")
        val cardview3 = pv.createPanel(R.layout.form_common_date3,"3. วันเวลาที่ทราบเหตุ/เกิดเหตุ")
        val cardview4 = pv.createPanel(R.layout.form_common_place4,"4. วันเวลาที่ตรวจเหตุ")
        val cardview5 = pv.createPanel(R.layout.form_fire_check5,"5.ผู้ตรวจสถานที่เกิดเหตุ")
        val cardview6 = pv.createPanel(R.layout.form_common_prop6,"6. ลักษณะสถานที่เกิดเหตุ")

        linearLayoutTop.addView(cardview)
        linearLayoutTop.addView(cardview2)
        linearLayoutTop.addView(cardview3)

        linearLayoutTop.addView(cardview4)
        linearLayoutTop.addView(cardview5)
        linearLayoutTop.addView(cardview6)

        return view
    }



    private fun showCustomDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_map)

        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        /*val bt_submit = dialog.findViewById<View>(R.id.bt_submit) as AppCompatButton
        (dialog.findViewById<View>(R.id.et_post) as EditText).addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                bt_submit.isEnabled = !s.toString().trim { it <= ' ' }.isEmpty()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        bt_submit.setOnClickListener {
            dialog.dismiss()
            Toast.makeText(requireContext(), "Post Submitted", Toast.LENGTH_SHORT).show()
        }
        (dialog.findViewById<View>(R.id.bt_photo) as ImageButton).setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Post Photo Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
        (dialog.findViewById<View>(R.id.bt_link) as ImageButton).setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Post Link Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
        (dialog.findViewById<View>(R.id.bt_file) as ImageButton).setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Post File Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
        (dialog.findViewById<View>(R.id.bt_setting) as ImageButton).setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Post Setting Clicked",
                Toast.LENGTH_SHORT
            ).show()
        }*/
        dialog.show()
        dialog.window!!.attributes = lp
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LifeFormFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LifeFormFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}