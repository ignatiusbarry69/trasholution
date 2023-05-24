package capstone.project.trasholution.ui.onboard


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.FragmentOnboardBinding
import capstone.project.trasholution.ui.login.LoginActivity
import com.google.android.material.tabs.TabLayout


class OnboardFragment : Fragment() {
    private var binding: FragmentOnboardBinding? = null
    private var position: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as AppCompatActivity
        arguments?.let {
            position = it.getInt(POSITION).toString()
        }
        if (this.position == "1") {
            binding?.btnLeft?.text = "SKIP"
            binding?.btnLeft?.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
            binding?.btnRight?.text = "NEXT"
            binding?.btnRight?.setOnClickListener {
                val tabLayout = activity.findViewById(R.id.tabs) as TabLayout
                val tab = tabLayout.getTabAt(1)
                tab!!.select()
            }
        }else if(this.position == "2"){
            binding?.btnLeft?.text = "BACK"
            binding?.btnLeft?.setOnClickListener {
                val tabLayout = activity.findViewById(R.id.tabs) as TabLayout
                val tab = tabLayout.getTabAt(0)
                tab!!.select()
            }
            binding?.btnRight?.text = "NEXT"
            binding?.btnRight?.setOnClickListener {
                val tabLayout = activity.findViewById(R.id.tabs) as TabLayout
                val tab = tabLayout.getTabAt(2)
                tab!!.select()
            }

        }else{
            binding?.btnLeft?.text = "BACK"
            binding?.btnLeft?.setOnClickListener {
                val tabLayout = activity.findViewById(R.id.tabs) as TabLayout
                val tab = tabLayout.getTabAt(1)
                tab!!.select()
            }
            binding?.btnRight?.text = "START"
            binding?.btnRight?.setOnClickListener {
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    companion object {
        const val POSITION = "position"
    }
}
