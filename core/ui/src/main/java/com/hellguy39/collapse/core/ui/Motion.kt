package com.hellguy39.collapse.core.ui
//
//import android.content.res.Resources
//import android.graphics.Color
//import android.transition.TransitionManager
//import android.util.TypedValue
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.google.android.material.transition.platform.*
//
//class Motion {
//
//    fun Fragment.setTransition(
//        fragmentTransition: FragmentTransition,
//        type: TransitionType,
//        animSpeed: Long = ANIM_SPEED_DEFAULT
//    ) {
////        when(fragmentTransition) {
////            is FragmentTransition.Enter -> {
////                enterTransition = getTransition(type, animSpeed)
////            }
////            is FragmentTransition.Exit -> {
////                exitTransition = getTransition(type, animSpeed)
////            }
////            is FragmentTransition.Reenter -> {
////                reenterTransition = getTransition(type, animSpeed)
////            }
////            is FragmentTransition.Return -> {
////                returnTransition = getTransition(type, animSpeed)
////            }
////        }
//    }
//
//    fun Fragment.setContainerTransformSharedElementTransition(
//        sharedElementTransition: SharedElementTransition,
//        animSpeed: Long = ANIM_SPEED_DEFAULT
//    ) {
//        when(sharedElementTransition) {
//            is SharedElementTransition.Enter -> {
//                sharedElementEnterTransition = MaterialContainerTransform().apply {
////                    drawingViewId = containerId
////                    scrimColor = Color.TRANSPARENT
////                    setAllContainerColors(
////                        requireActivity().theme.getColorByResId(
////                            com.google.android.material.R.attr.colorSurface
////                        )
////                    )
//                    duration = 3000L
//                    //setPathMotion(MaterialArcMotion())
//                }
//            }
//            is SharedElementTransition.Return -> {
//                sharedElementReturnTransition = MaterialContainerTransform().apply {
////                    drawingViewId = containerId
////                    scrimColor = Color.TRANSPARENT
////                    setAllContainerColors(
////                        requireActivity().theme.getColorByResId(
////                            com.google.android.material.R.attr.colorSurface
////                        )
////                    )
//                    duration = animSpeed
////                    pathMotion = MaterialArcMotion()
//                }
//            }
//        }
//    }
//
//    fun ViewGroup.viewTransitionOneWay(
//        view: View,
//        isVisible: Boolean,
//        type: TransitionType,
//        animSpeed: Long = ANIM_SPEED_DEFAULT
//    ) {
//        TransitionManager.beginDelayedTransition(
//            this,
//            MaterialFadeThrough()//getTransition(type, animSpeed)
//        )
//
//        view.visibility = if (isVisible) View.VISIBLE else View.GONE
//    }
//
//
////    fun ViewGroup.viewTransition(
////        outgoingView: View,
////        incomingView: View = outgoingView,
////        type: TransitionType,
////        animSpeed: Long = ANIM_SPEED_DEFAULT
////    ) {
////        TransitionManager.beginDelayedTransition(
////            this,
////            getTransition(type, animSpeed)
////        )
////
////        outgoingView.visibility = View.GONE
////        incomingView.visibility = View.VISIBLE
////    }
////
////    fun ViewGroup.transformViewTransition(
////        outgoingView: View,
////        incomingView: View,
////        animSpeed: Long = ANIM_SPEED_DEFAULT
////    ) {
////        val transform = MaterialContainerTransform().apply {
////            startView = outgoingView
////            endView = incomingView
////            addTarget(incomingView)
////            setPathMotion(MaterialArcMotion())
////            scrimColor = Color.TRANSPARENT
////            duration = animSpeed
////        }
////        TransitionManager.beginDelayedTransition(this, transform)
////        outgoingView.visibility = View.GONE
////        incomingView.visibility = View.VISIBLE
////    }
//
////    private fun getTransition(
////        type: TransitionType,
////        animSpeed: Long = ANIM_SPEED_DEFAULT
////    ): Transition {
////        return when(type) {
////            TransitionType.SharedAxisX -> {
////                MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
////                    duration = animSpeed
////                }
////            }
////            TransitionType.SharedAxisY -> {
////                MaterialSharedAxis(MaterialSharedAxis.Y, false).apply {
////                    duration = animSpeed
////                }
////            }
////            TransitionType.SharedAxisYForward -> {
////                MaterialSharedAxis(MaterialSharedAxis.Y, true).apply {
////                    duration = animSpeed
////                }
////            }
////            TransitionType.SharedAxisZ -> {
////                MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
////                    duration = animSpeed
////                }
////            }
////            TransitionType.FadeThought -> {
////                MaterialFadeThrough().apply {
////                    duration = animSpeed
////                }
////            }
////            TransitionType.Auto -> {
////                AutoTransition().apply {
////                    duration = animSpeed
////                }
////            }
////            TransitionType.Hold -> {
////                Hold().apply {
////                    duration = animSpeed
////                }
////            }
////        }
////    }
//
//    companion object {
//        const val ANIM_SPEED_SHORT = 250L
//        const val ANIM_SPEED_DEFAULT = 300L
//        const val ANIM_SPEED_MEDIUM = 350L
//        const val ANIM_SPEED_LONG = 450L
//    }
//}
//
//sealed interface TransitionType {
//    object SharedAxisX: TransitionType
//    object SharedAxisY: TransitionType
//    object SharedAxisYForward: TransitionType
//    object SharedAxisZ: TransitionType
//    object FadeThought: TransitionType
//    object Auto: TransitionType
//    object Hold: TransitionType
//}
//
//sealed interface FragmentTransition {
//    object Enter: FragmentTransition
//    object Exit: FragmentTransition
//    object Reenter: FragmentTransition
//    object Return: FragmentTransition
//}
//
//sealed interface SharedElementTransition {
//    object Enter: SharedElementTransition
//    object Return: SharedElementTransition
//}
//

