package com.medical.mytestapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.medical.mytestapp.R
import com.medical.mytestapp.ViewModel.SearchViewModel
import com.medical.mytestapp.ui.MainActivity


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment(), View.OnClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var imgback: ImageView
    lateinit var imgSelected: ImageView
    lateinit var titleHeader: TextView
    var intent: Intent? = null
    var toolbartitle: String? = null
    private var mainViewModel: SearchViewModel? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        mainViewModel = ViewModelProviders.of(requireActivity()).get(SearchViewModel::class.java)

        UIinitialize(view)


/*        if(savedInstanceState!=null){
        val savedTitle =  savedInstanceState.getString("title")
        titleHeader.text= savedTitle
            Log.i("the ","restored saved state"+ savedTitle)
        }
*/
        return view
    }

    private fun UIinitialize(view: View) {
        imgback = view.findViewById(R.id.img_backNavigate) as ImageView
        imgSelected = view.findViewById(R.id.detailImage) as ImageView
        titleHeader = view.findViewById(R.id.tv_ToolbarTitle) as TextView
        intent = requireActivity().getIntent()
        if (intent!!.hasExtra("title")&& intent!!.hasExtra("image")) {
            toolbartitle = intent?.getStringExtra("title")
            titleHeader.text = toolbartitle

        }
        imgback.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i("the", "activity created savedinstance used")
        savedInstanceState?.putString("title", toolbartitle);
        Log.i("the", "savedinstance" + toolbartitle)
    }

/*    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
      //  val myString = savedInstanceState?.getString("title")
    //   titleHeader.text= myString
    }
*/


    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putString("title", toolbartitle);
        Log.i("the", "savedinstance" + toolbartitle)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DetailFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onClick(v: View?) {
        var id = v?.id
        when (id) {
            R.id.img_backNavigate -> {
                navigateBack()
            }
            R.id.btnSave -> {

            }
            R.id.btnClear -> {

            }
        }
    }

    private fun navigateBack() {
        val intent = Intent(requireContext().applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK);
        requireContext().startActivity(intent)
        requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }


}