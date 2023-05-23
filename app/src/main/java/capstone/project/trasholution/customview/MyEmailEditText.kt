package capstone.project.trasholution.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import com.google.android.material.textfield.TextInputEditText

class MyEmailEditText : TextInputEditText, View.OnTouchListener {

    var validation = Patterns.EMAIL_ADDRESS

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //do nothing
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (validation.matcher(s.toString()).matches()){

                }else{
                    error = "Please use a valid email address"
                }
            }

            override fun afterTextChanged(s: Editable) {
                //do nothing
            }
        })
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return false
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }
}