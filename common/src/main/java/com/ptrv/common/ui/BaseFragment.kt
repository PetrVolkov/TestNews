package com.ptrv.common.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.ptrv.common.di.ComponentManager
import com.ptrv.common.di.DaggerComponent

abstract class BaseFragment : MvpAppCompatFragment() {
    companion object {
        private const val TAG_PROGRESS = "progress"
    }

    protected abstract val layoutRes: Int
    protected lateinit var componentBuilder: () -> DaggerComponent
    private var instanceStateSaved: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(layoutRes, container, false)

    protected inline fun <reified T : DaggerComponent> getComponent(): T =
            ComponentManager.getOrPutComponent(this.javaClass.simpleName, componentBuilder) as T

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (needClearComponent()) {
            ComponentManager.clearComponent(this.javaClass.simpleName)
        }
    }

    protected fun needClearComponent(): Boolean = when {
        activity?.isChangingConfigurations == true -> false
        activity?.isFinishing == true -> true
        else -> isRealRemoving()
    }

    private fun isRealRemoving(): Boolean = (isRemoving && !instanceStateSaved)
            || ((parentFragment as? BaseFragment)?.isRealRemoving() ?: false)

    open fun onBackPressed() {}
}