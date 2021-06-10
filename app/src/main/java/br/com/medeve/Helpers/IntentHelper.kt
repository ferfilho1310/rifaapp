package br.com.medeve.Helpers

import android.app.Activity
import android.content.Intent
import br.com.medeve.Interfaces.IIntent

object IntentHelper : IIntent {
    override fun intentWithFinish(activity: Activity?, clazz: Class<*>?) {
        val intent = Intent(activity, clazz)
        activity!!.startActivity(intent)
        activity.finish()
    }

    override fun intentWithOutFinish(activity: Activity?, clazz: Class<*>?) {
        val intent = Intent(activity, clazz)
        activity!!.startActivity(intent)
    }

    override fun intentWithFlags(activity: Activity?, clazz: Class<*>?) {
        val intent = Intent(activity, clazz)
        activity!!.startActivity(intent)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

}