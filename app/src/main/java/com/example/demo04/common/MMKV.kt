@file:Suppress("ktlint:standard:filename")

package com.example.demo04.common

import com.tencent.mmkv.MMKV

object MUtil {
    var kv: MMKV = MMKV.defaultMMKV()
}
