package com.example.dhdesafio4.utils.behavior

import android.view.View
import android.view.ViewGroup.MarginLayoutParams


fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
    if (v.layoutParams is MarginLayoutParams) {
        val p = v.layoutParams as MarginLayoutParams
        p.setMargins(l, t, r, b)
        v.requestLayout()
    }
}