package com.csi.irite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.csi.irite.unuse.BomFormFragment
import com.csi.irite.unuse.FireFormFragment
import com.csi.irite.unuse.LifeFormFragment
import com.csi.irite.unuse.ReportEventFormFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateMenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateMenuFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_menu, container, false)
        val checklist = view.findViewById<View>(R.id.checklist)
        checklist.setOnClickListener(View.OnClickListener { v ->
            loadFragment(CheckListsFragment())

        })

        val event = view.findViewById<View>(R.id.report)
        event.setOnClickListener(View.OnClickListener { v ->
            loadFragment(ReportEventFormFragment())

        })

        val asset = view.findViewById<View>(R.id.asset)
        asset.setOnClickListener(View.OnClickListener { v ->
            loadFragment(AssetFormFragment())

        })

        val left = view.findViewById<View>(R.id.left)
        left.setOnClickListener(View.OnClickListener { v ->
            loadFragment(LifeFormFragment())

        })

        val bom = view.findViewById<View>(R.id.bom)
        bom.setOnClickListener(View.OnClickListener { v ->
            loadFragment(BomFormFragment())

        })


        val fire = view.findViewById<View>(R.id.fire)
        fire.setOnClickListener(View.OnClickListener { v ->
            loadFragment(FireFormFragment())

        })


        return view
    }

    private  fun loadFragment(fragment: Fragment){
        val fragmentManager = parentFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.nav_host_fragment,fragment)
        transaction.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateMenuFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateMenuFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}