package capstone.project.trasholution.ui.mainmenu.prediction

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import capstone.project.trasholution.R
import capstone.project.trasholution.databinding.ActivityPredictAndSolutionBinding

class PredictAndSolutionActivity : AppCompatActivity() {
    private var binding : ActivityPredictAndSolutionBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict_and_solution)

        binding?.btnClose?.setOnClickListener {
            this.finish()
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 200

    }
}