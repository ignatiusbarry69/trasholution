package capstone.project.trasholution.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import capstone.project.trasholution.databinding.FragmentOnboardBinding


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
            //ki ngko nggo ngatur isine di statis ning kene wae
        }else if(this.position == "2"){

        }else {

        }
    }

    companion object {
        const val POSITION = "position"
    }
}
