package com.iyr.sp


import android.R.attr.centerX
import android.R.attr.centerY
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iyr.sp.databinding.FragmentMainBinding
import com.iyr.sp.view.ui.main.adapters.RoundedInterestsAdapter
import kotlin.math.roundToLong


/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class main : Fragment() {
    private val hideHandler = Handler()
    private val recyclerInterests: RecyclerView? = null
    private var adapterMyInterests: RoundedInterestsAdapter? = null


    @Suppress("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        val flags =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.decorView?.systemUiVisibility = flags
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        fullscreenContentControls?.visibility = View.VISIBLE
    }
    private var visible: Boolean = false
    private val hideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val delayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    private var dummyButton: Button? = null
    private var fullscreenContent: View? = null
    private var fullscreenContentControls: View? = null

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
setupUI()
        visible = true

     //   dummyButton = binding.dummyButton
     //   fullscreenContent = binding.fullscreenContent
    //    fullscreenContentControls = binding.fullscreenContentControls
        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent?.setOnClickListener { toggle() }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        dummyButton?.setOnTouchListener(delayHideTouchListener)
    }

    private fun setupUI() {
        adapterMyInterests = RoundedInterestsAdapter()

        _binding!!.searchText.animate()
                  .translationX(_binding!!.interestImage.width.toFloat())
            .setDuration(200)
            .setInterpolator(AccelerateDecelerateInterpolator())

        _binding!!.interestImage.setOnClickListener {

            if (_binding!!.searchText.visibility ==View.GONE)
            {
           //     _binding!!.searchText.visibility =View.VISIBLE
                expandHorizontaly(_binding!!.searchText)
            }
            else
            {
                _binding!!.searchText.visibility =View.GONE
            }
        }

        _binding!!.recyclerInterests.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        _binding!!.recyclerInterests?.setAdapter(adapterMyInterests)
    }

    fun expandHorizontaly(view: View) {
        view.visibility = View.VISIBLE

        val matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            (view.getParent() as View).width,
            View.MeasureSpec.EXACTLY
        )
        val wrapContentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        var targetWidth = view.getMeasuredWidth();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        view.getLayoutParams().width = 1;
        view.setVisibility(View.VISIBLE);
        view.requestLayout()
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                view.getLayoutParams().width =
                    if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else ((targetWidth * interpolatedTime).toInt())
                view.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
a.setAnimationListener(object : Animation.AnimationListener{
    override fun onAnimationStart(animation: Animation?) {
        _binding!!.interestImage.setImageDrawable(requireContext().getDrawable(R.drawable.ic_baseline_search_24))


        val hypotenuse = Math.hypot(_binding!!.interestImage.getWidth().toDouble(),
            _binding!!.interestImage.getHeight().toDouble()
        ).toInt()

        val circularReveal =
            ViewAnimationUtils.createCircularReveal(_binding!!.interestImage, centerX, centerY, 0f,
                hypotenuse.toFloat()
            )
        circularReveal.duration = 2000
        //注意：这里显示 mPuppet 调用并没有在监听方法里，并且是在动画开始前调用。
        //注意：这里显示 mPuppet 调用并没有在监听方法里，并且是在动画开始前调用。
        _binding!!.interestImage.setVisibility(View.VISIBLE)
        circularReveal.start()

     //   _binding!!.interestImage.startAnimation(rotate)

    }

    override fun onAnimationEnd(animation: Animation?) {
    //    TODO("Not yet implemented")
    }

    override fun onAnimationRepeat(animation: Animation?) {
      //  TODO("Not yet implemented")
    }
})
        // Expansion speed of 1dp/ms

        // Expansion speed of 1dp/ms
        a.setDuration(
            ((targetWidth / view.getContext().getResources().getDisplayMetrics().density).roundToLong()*3)
        )
        view.startAnimation(a)


    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Clear the systemUiVisibility flag
        activity?.window?.decorView?.systemUiVisibility = 0
        show()
    }

    override fun onDestroy() {
        super.onDestroy()
        dummyButton = null
        fullscreenContent = null
        fullscreenContentControls = null
    }

    private fun toggle() {
        if (visible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        fullscreenContentControls?.visibility = View.GONE
        visible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @Suppress("InlinedApi")
    private fun show() {
        // Show the system bar
        fullscreenContent?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        visible = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}