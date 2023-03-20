package com.elka.heofficeclub.other

import java.text.SimpleDateFormat
import java.util.*


val local: Locale by lazy { Locale.getDefault() }
val sdf: SimpleDateFormat by lazy { SimpleDateFormat("dd/MM/yyyy", local) }
val docSdf: SimpleDateFormat by lazy { SimpleDateFormat("dd.MM.yyyy", local) }

fun Date.format(): String = sdf.format(this)
fun Date.toDocFormat(): String = docSdf.format(this)
fun Date.toDays() = 0

fun Double.toRub(): String = this.toString().split(".")[0]


fun Double.toCent(): String = this.toString().split(".")[1]