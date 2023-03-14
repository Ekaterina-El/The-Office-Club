package com.elka.heofficeclub.other

import java.text.SimpleDateFormat
import java.util.*


val local: Locale by lazy { Locale.getDefault() }
val sdf: SimpleDateFormat by lazy { SimpleDateFormat("dd/MM/yyyy", local) }

fun Date.format(): String = sdf.format(this)