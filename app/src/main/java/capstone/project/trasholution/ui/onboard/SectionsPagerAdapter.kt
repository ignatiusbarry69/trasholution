package capstone.project.trasholution.ui.onboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        val fragment = OnboardFragment()

        fragment.arguments = Bundle().apply {
            putInt(OnboardFragment.POSITION, position + 1)
        }
        return fragment
    }
    override fun getItemCount(): Int {
        return 3
    }
}