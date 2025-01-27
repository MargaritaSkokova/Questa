package com.maran.questa.dependencyInjection

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesProvider @Inject constructor(val sharedPreferences: SharedPreferences)