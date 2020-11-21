package br.com.medeve.Interfaces

import android.app.Activity

interface IIntent {
    fun intentWithFinish(activity: Activity?, clazz: Class<*>?)
    fun intentWithOutFinish(activity: Activity?, clazz: Class<*>?)
    fun intentWithFlags(activity: Activity?, clazz: Class<*>?)
}