package com.example.btliot.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.btliot.MyApp
import com.example.btliot.R
import com.example.btliot.databinding.FragmentBaseBinding

private typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(private val inflate: Inflate<VB>) : Fragment() {
    private var mViewBinding: VB? = null
    protected val viewBinding: VB
        get() = mViewBinding!!

    private var mParentBinding: FragmentBaseBinding? = null
    private val parentBinding: FragmentBaseBinding
        get() = mParentBinding!!

    open val backPressCallback = {
        findNavController().popBackStack()
    }

    private fun initFragment(inflater: LayoutInflater, container: ViewGroup?) {
        //init parent's view binding:
        mParentBinding = FragmentBaseBinding.inflate(inflater)

        //init child's view binding:
        mViewBinding = inflate.invoke(inflater, container, false)

        //add child's view into parent's view:
        parentBinding.baseContainer.apply {
            removeAllViews()
            addView(mViewBinding?.root)
        }
    }

    private fun handleSystemBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressCallback()
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initFragment(inflater, container)
        handleSystemBackPress()
        return mParentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAction()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewBinding = null
        mParentBinding = null
    }

    open fun initView() {}
    open fun initAction() {}
    open fun initData() {}

    fun setupToolbarVisible(isVisible: Boolean) {
        parentBinding.header.root.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setupNavigationButton(
        isVisible: Boolean = true,
        action: () -> Unit = { findNavController().popBackStack() }
    ) {
        parentBinding.header.btnNav.apply {
            visibility = if (isVisible) View.VISIBLE else View.GONE
            setOnClickListener { action() }
        }
    }

    fun setupTitle(
        isVisible: Boolean = true,
        title: String
    ) {
        parentBinding.header.title.apply {
            visibility = if (isVisible) View.VISIBLE else View.GONE
            text = title
        }
    }

    fun setStatusBarColorResource(@ColorRes colorId: Int) {
        val color = ContextCompat.getColor(
            context ?: MyApp.context(),
            colorId
        )

        setStatusBarColor(color)
    }

    fun setStatusBarColor(color: Int) {
        activity?.apply {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    fun <T> registerObserveDataBackFromForwardFragment(key: String, action: (T) -> Unit) {
        val navController = findNavController()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)
            ?.observe(viewLifecycleOwner) {
                action(it)
            }
    }

    fun navigate(
        @IdRes action: Int,
        vararg data: Pair<String, Any?>
    ) {
        navigate(action, null, true, *data)
    }

    fun navigate(
        @IdRes action: Int,
        builder: NavOptions.Builder? = null,
        haveDefaultAnim: Boolean = true,
        vararg data: Pair<String, Any?>
    ) {
        val navController = findNavController()
        val bundle = bundleOf(*data)

        val optionBuilder = builder ?: NavOptions.Builder()

        if (haveDefaultAnim)
            optionBuilder.apply {
                setExitAnim(R.anim.slide_out_to_left)
                setEnterAnim(R.anim.slide_in_from_right)
                setPopExitAnim(R.anim.slide_out_to_right)
                setPopEnterAnim(R.anim.slide_in_from_left)
            }

        navController.navigate(action, bundle, optionBuilder.build())
    }

    fun popBackStack(
        vararg data: Pair<String, Any?>
    ): Boolean {
        return popBackStack(null, false, *data)
    }

    fun popBackStack(
        @IdRes destinationId: Int? = null,
        inclusive: Boolean = false,
        vararg data: Pair<String, Any?>
    ): Boolean {
        val navController = findNavController()

        navController.previousBackStackEntry?.savedStateHandle?.apply {
            data.forEach {
                set(it.first, it.second)
            }
        }

        return if (destinationId == null)
            navController.popBackStack()
        else popBackStackTo(destinationId, inclusive)
    }

    private fun popBackStackTo(@IdRes destinationId: Int, inclusive: Boolean = false): Boolean {
        return findNavController().popBackStack(destinationId, inclusive)
    }
}