package capstone.project.trasholution.ui.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import capstone.project.trasholution.R
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

        arguments?.let {
            position = it.getInt(POSITION).toString()
        }
        if (this.position == "1") {
            binding?.onBoardLottie?.setAnimation(R.raw.nature_load)
            binding?.tvTitleOnboard?.text = getString(R.string.titleWelcome)
            binding?.tvContentOnboard?.text = getString(R.string.contentWelcome)
        }else if(this.position == "2"){
            binding?.onBoardLottie?.setAnimation(R.raw.take_photo)
            binding?.tvTitleOnboard?.text = getString(R.string.titleHow)
            binding?.tvContentOnboard?.text = getString(R.string.contentHow)
        }else {
            binding?.onBoardLottie?.setAnimation(R.raw.onboard1)
            binding?.tvTitleOnboard?.text = getString(R.string.titleCraftAndConnect)
            binding?.tvContentOnboard?.text = getString(R.string.contentCraftAndConnect)
        }
    }

    companion object {
        const val POSITION = "position"
    }
}
